package com.myflashlab.air.extensions.pm
{
import flash.events.Event;

/**
 * @private
 * @author Hadi Tavakoli - 10/1/2018 6:11 PM
 */
public class PackageManagerEvent extends Event
{
	/** @private */
	public static const PERMISSION_FOR_MANAGING_APPS:String = "onPermissionForManagingApps";
	
	private var _allowed:Boolean;
	
	/** @private */
	public function PackageManagerEvent($type:String, $allowed:Boolean=false):void
	{
		_allowed = $allowed;
		
		super($type, false, false);
	}
	
	/** @private */
	public function get allowed():Boolean
	{
		return _allowed;
	}
}
}