package com.myflashlab.air.extensions.pm
{
import flash.external.ExtensionContext;

/**
 *
 */
public class PackageInfo
{
	private var _context:ExtensionContext;
	private var _packageName:String;
	private var _isDetailsCalled:Boolean;
	
	private var _firstInstallTime:Date;
	private var _lastUpdateTime:Date;
	private var _label:String;
	private var _versionCode:int;
	private var _versionName:String;
	
	public function PackageInfo($context:ExtensionContext, $packageName:String):void
	{
		_context = $context;
		_packageName = $packageName;
	}
	
	private function getDetails():void
	{
		_isDetailsCalled = true;
		
		var str:String = _context.call("command", "getPackageInfo", _packageName) as String;
		var obj:Object = JSON.parse(str);
		
		_firstInstallTime = new Date(obj.firstInstallTime);
		_lastUpdateTime = new Date(obj.lastUpdateTime);
		_label = obj.label;
		_versionCode = obj.versionCode;
		_versionName = obj.versionName;
	}
	
	public function get packageName():String
	{
		return _packageName;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	
	public function get firstInstallTime():Date
	{
		if(!_isDetailsCalled) getDetails();
		
		return _firstInstallTime;
	}
	
	public function get lastUpdateTime():Date
	{
		if(!_isDetailsCalled) getDetails();
		
		return _lastUpdateTime;
	}
	
	public function get label():String
	{
		if(!_isDetailsCalled) getDetails();
		
		return _label;
	}
	
	public function get versionCode():int
	{
		if(!_isDetailsCalled) getDetails();
		
		return _versionCode;
	}
	
	public function get versionName():String
	{
		if(!_isDetailsCalled) getDetails();
		
		return _versionName;
	}
}
}