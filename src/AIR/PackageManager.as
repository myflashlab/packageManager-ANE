package com.myflashlab.air.extensions.pm
{
import flash.display.BitmapData;
import flash.display.BitmapDataChannel;
import flash.external.ExtensionContext;
import flash.events.StatusEvent;
import flash.filesystem.File;
import flash.utils.getDefinitionByName;
import flash.utils.getQualifiedClassName;
import flash.geom.Rectangle;
import flash.geom.Point;
import flash.desktop.NativeApplication;

/**
 *
 */
public class PackageManager
{
	/** @private */
	internal static const DEMO_ANE:Boolean = true;
	
	public static const PERMISSION_GRANTED:int = 0;
	public static const PERMISSION_DENIED:int = -1;
	
	public static const SIGNATURE_MATCH:int = 0;
	public static const SIGNATURE_NEITHER_SIGNED:int = 1;
	public static const SIGNATURE_FIRST_NOT_SIGNED:int = -1;
	public static const SIGNATURE_SECOND_NOT_SIGNED:int = -2;
	public static const SIGNATURE_NO_MATCH:int = -3;
	public static const SIGNATURE_UNKNOWN_PACKAGE:int = -4;
	
	public static const EXTENSION_ID:String = "com.myflashlab.air.extensions.packageManager";
	public static const VERSION:String = "3.0.0";
	
	private static var _ex:PackageManager;
	private var _requestCallback:Function;
	
	private var OverrideClass:Class;
	private var _context:ExtensionContext;
	
	public static var USE_AIR_PREFIX_FOR_ANDROID:Boolean = true;
	
	public function PackageManager():void
	{
		OverrideClass = getDefinitionByName("com.myflashlab.air.extensions.dependency.OverrideAir") as Class;
		
		// Tell Override ANE to read the ANE-LAB ID from the manifest. This must happen on Android and iOS.
		// Pass id/version of this ANE to Override ANE so it can check its validity.
		OverrideClass["applyToAneLab"](getQualifiedClassName(this));
		
		if(OverrideClass["os"] == "desktop") return;
		
		// initialize the context
		_context = ExtensionContext.createExtensionContext(EXTENSION_ID, null);
		_context.addEventListener(StatusEvent.STATUS, onStatus);
		
		_context.call("command", "initialize");
		
		if(DEMO_ANE) _context.call("command", "isTestVersion");
	}
	
	private function onStatus(e:StatusEvent):void
	{
		switch (e.code)
		{
			case PackageManagerEvent.PERMISSION_FOR_MANAGING_APPS:
				
				if(_requestCallback != null)
				{
					_requestCallback((e.level == "true"));
					_requestCallback = null;
				}
				
				break;
		}
	}
	
	private function convertBitmapChannels($src:BitmapData):BitmapData
	{
		var bmd1:BitmapData;
		var bmd2:BitmapData;
		
		bmd1 = $src.clone();
		bmd2 = $src.clone();
		
		var rect:Rectangle = new Rectangle(0, 0, bmd1.width, bmd1.height);
		var pt:Point = new Point(0, 0);
		
		bmd1.copyChannel(bmd2, rect, pt, BitmapDataChannel.RED, BitmapDataChannel.BLUE);
		bmd1.copyChannel(bmd2, rect, pt, BitmapDataChannel.BLUE, BitmapDataChannel.RED);
		
		bmd2.dispose();
		bmd2 = null;
		
		return bmd1;
		
	}
	
	// ----------------------------------------------------------------------------------------------------
	
	public static function init():void
	{
		if(!_ex) _ex = new PackageManager();
	}
	
	public static function getInstalledPackages($systemApps:Boolean):Array
	{
		var str:String = _ex._context.call("command", "getInstalledPackages", $systemApps) as String;
		var arr:Array = JSON.parse(str) as Array;
		
		for(var i:int=0; i < arr.length; i++)
		{
			var packageInfo:PackageInfo = new PackageInfo(_ex._context, arr[i]);
			arr.splice(i, 1, packageInfo);
		}
		
		return arr;
	}
	
	public static function getPackageInfo($packageName:String):PackageInfo
	{
		return new PackageInfo(_ex._context, $packageName);
	}
	
	public static function canRequestPackageInstalls():Boolean
	{
		return _ex._context.call("command", "canRequestPackageInstalls") as Boolean;
	}
	
	public static function requestPackageInstalls($callback:Function):void
	{
		_ex._requestCallback = $callback;
		_ex._context.call("command", "askUserPermissionForManagingUnknownApps");
	}
	
	public static function checkPermission($permissionName:String, $packageName:String):int
	{
		return _ex._context.call("command", "checkPermission", $permissionName, $packageName) as int;
	}
	
	public static function checkSignatures($package1:String, $package2:String):int
	{
		return _ex._context.call("command", "checkSignatures", $package1, $package2) as int;
	}
	
	public static function isInstantApp($packageName:String):Boolean
	{
		return _ex._context.call("command", "isInstantApp", $packageName) as Boolean;
	}
	
	public static function getAppIcon($packageName:String):BitmapData
	{
		var bmd:BitmapData = _ex._context.call("command", "getAppIcon", $packageName) as BitmapData;
		
		return _ex.convertBitmapChannels(bmd);
	}
	
	public static function installApplicationFromGooglePlay($packageName:String):void
	{
		_ex._context.call("command", "installApplicationFromGooglePlay", $packageName);
	}
	
	public static function installApplicationFromApk($apk:File):void
	{
		_ex._context.call("command", "installApplicationFromApk",
				$apk.nativePath,
				NativeApplication.nativeApplication.applicationID,
				USE_AIR_PREFIX_FOR_ANDROID
		);
	}
	
	public static function runApplication($packageName:String, $extras:Array=null):void
	{
		var extrasStr:String = "";
		if($extras) extrasStr = JSON.stringify($extras);
		
		_ex._context.call("command", "runApplication", $packageName, extrasStr);
	}
	
	public static function uninstallApplication($packageName:String):void
	{
		_ex._context.call("command", "uninstallApplication", $packageName);
	}
}
}
