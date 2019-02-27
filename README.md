# packageManager-ANE for Adobe AIR apps

This ANE gives you access to Android's PackageManager API to do the following tasks:

1. Know what other apps are installed on user's device.
    * Check their permissions
    * Load their app icon
    * Check/compare their signatures
    * Read their packageInfo
        * Read name
        * Read version
        * Read last update time
        * Read first install time
2. Un/Install .apk files from GooglePlay or directly from a File Object
3. Run other apps from your AIR app


* [Click here for ASDOC](http://myflashlab.github.io/asdoc/com/myflashlab/air/extensions/pm/package-detail.html)
* [See the ANE setup requirements](https://github.com/myflashlab/packageManager-ANE/blob/master/src/ANE/extension.xml)

**IMPORTANT:** Implementing ANEs in your AIR projects means you may be required to add some [dependencies](https://github.com/myflashlab/common-dependencies-ANE) or copy some frameworks or editing your app's manifest file. Our ANE setup instruction is designed in a human-readable format but you may still need to familiarize yourself with this format. [Read this post for more information](https://www.myflashlabs.com/understanding-ane-setup-instruction/)

If you think manually setting up ANEs in your projects is confusing or time-consuming, you better check the [ANELAB Software](https://github.com/myflashlab/ANE-LAB/).

[![The ANE-LAB Software](https://www.myflashlabs.com/wp-content/uploads/2017/12/myflashlabs-ANE-LAB_features.jpg)](https://github.com/myflashlab/ANE-LAB/)

# Tech Support #
If you need our professional support to help you with implementing and using the ANE in your project, you can join [MyFlashLabs Club](https://www.myflashlabs.com/product/myflashlabs-club-membership/) or buy a [premium support package](https://www.myflashlabs.com/product/myflashlabs-support/). Otherwise, you may create new issues at this repository and the community might help you.

# AIR Usage #
```actionscript
import com.myflashlab.air.extensions.pm.*;

// init the ANE before calling any other methods
PackageManager.init();
```
list the installed apps on user's device
```actionscript
var arr:Array = PackageManager.getInstalledPackages(false);
for(var i:int=0; i < arr.length; i++)
{
	var packageInfo:PackageInfo = arr[i];
	trace("packageName: " + packageInfo.packageName);
}
```

read package information of any app!
```actionscript
var info:PackageInfo = PackageManager.getPackageInfo("[PACKAGE_NAME]]");
trace("label: " + 		info.label);
trace("firstInstallTime: " + 	info.firstInstallTime.toLocaleString());
trace("lastUpdateTime: " + 	info.lastUpdateTime.toLocaleString());
trace("versionName: " + 	info.versionName);
```
check if you have permission for requesting package installs?
```actionscript
if(PackageManager.canRequestPackageInstalls())
{
	trace("canRequestPackageInstalls() "+PackageManager.canRequestPackageInstalls());
}
else
{
	PackageManager.requestPackageInstalls(function ($result:Boolean):void
	{
		trace("requestPackageInstalls result: " + $result);
	})
}
```
check if an app has a specefic permission or not
```actionscript
switch(PackageManager.checkPermission("android.permission.CAMERA", "[PACKAGE_NAME]"))
{
	case PackageManager.PERMISSION_GRANTED:
					
		trace("PERMISSION_GRANTED");
		
	break;
	case PackageManager.PERMISSION_DENIED:
		
		trace("PERMISSION_DENIED");
		
	break;
}
```
get an app icon BitmapData
```actionscript
var bmd:BitmapData = PackageManager.getAppIcon("[PACKAGE_NAME]");
```
install an .apk file. apk file must be inside ```File.cacheDirectory``` for the ANE to install it.
```actionscript
var apk:File = File.cacheDirectory.resolvePath("app.apk");
PackageManager.installApplicationFromApk(apk);


```
```actionscript
PackageManager.installApplicationFromGooglePlay("[PACKAGE_NAME]");
```


```actionscript
PackageManager.uninstallApplication("[PACKAGE_NAME]");
```

```actionscript
PackageManager.runApplication("[PACKAGE_NAME]");
```

Are you using this ANE in your project? Maybe you'd like to buy us a beer :beer:?

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=payments@myflashlabs.com&lc=US&item_name=Donation+to+PackageManager+ANE&no_note=0&cn=&currency_code=USD&bn=PP-DonationsBF:btn_donateCC_LG.gif:NonHosted)

Add your name to the below list? Donate anything more than $100 and it will be.

## Sponsored by... ##
<table align="left">
    <tr>
        <td align="left"><img src="https://myflashlab.github.io/sponsors/yeticgi.com.jpg" width="60" height="60"></td>
        <td align="left"><a href="http://yeticgi.com">yeticgi.com</a><br>We’ve been solving problems on the frontier of emerging tech for a decade.</td>
    </tr>
</table>