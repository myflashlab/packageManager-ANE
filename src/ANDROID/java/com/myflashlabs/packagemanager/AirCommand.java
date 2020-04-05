package com.myflashlabs.packagemanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.air.ActivityResultCallback;
import com.adobe.air.AndroidActivityWrapper;
import com.myflashlab.Conversions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * @author Hadi Tavakoli
 */
public class AirCommand implements FREFunction, ActivityResultCallback
{
	private boolean isDialogCalled = false;
	private boolean isDialogClicked = false;

	private AndroidActivityWrapper _aaw;

	private Activity _activity;
	private PackageManager _pm;

	public AirCommand()
	{
		_aaw = AndroidActivityWrapper.GetAndroidActivityWrapper();
		_aaw.addActivityResultListener(this);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ExConsts.REQUEST_CODE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
				{
					if (canRequestPackageInstalls())
					{
						MyExtension.AS3_CONTEXT.dispatchStatusEventAsync(ExConsts.PERMISSION_FOR_MANAGING_APPS, "true");
					}
					else
					{
						MyExtension.AS3_CONTEXT.dispatchStatusEventAsync(ExConsts.PERMISSION_FOR_MANAGING_APPS, "false");
					}
				}
			}
			else
			{
				MyExtension.AS3_CONTEXT.dispatchStatusEventAsync(ExConsts.PERMISSION_FOR_MANAGING_APPS, "false");
			}
		}
	}

	private enum commands
	{
		isTestVersion,
		initialize,
		getInstalledPackages,
		getPackageInfo,
		canRequestPackageInstalls,
		askUserPermissionForManagingUnknownApps,
		checkPermission,
		checkSignatures,
		isInstantApp,
		getAppIcon,
		installApplicationFromGooglePlay,
		installApplicationFromApk,
		runApplication,
		uninstallApplication,
	}

	private void showTestVersionDialog()
	{
		// If we know at least one ANE is DEMO, we don't need to show demo dialog again. It's already shown once.
		if(com.myflashlab.dependency.overrideAir.MyExtension.hasAnyDemoAne()) return;

		// Check if this ANE is registered?
		if(com.myflashlab.dependency.overrideAir.MyExtension.isAneRegistered(ExConsts.ANE_NAME)) return;

		// Otherwise, show the demo dialog.

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
		dialogBuilder.setTitle("DEMO ANE!");
		dialogBuilder.setMessage("The library '"+ExConsts.ANE_NAME+"' is not registered!");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
				isDialogClicked = true;
			}
		});

		AlertDialog myAlert = dialogBuilder.create();
		myAlert.show();

		isDialogCalled = true;
	}

	public FREObject call(FREContext $context, FREObject[] $params)
	{
		String command = Conversions.AirToJava_String($params[0]);
		FREObject result = null;

		if (_activity == null)
		{
			_activity = $context.getActivity();
		}

		switch (commands.valueOf(command))
		{
			case isTestVersion:

				showTestVersionDialog();

				break;
			case initialize:

				toTrace("initialize BEGIN...");
				_pm = _activity.getPackageManager();
				toTrace("initialize ...END");

				break;
			case getInstalledPackages:

				result = Conversions.JavaToAir_String(getInstalledPackages(
						_activity,
						Conversions.AirToJava_Boolean($params[1])
				).toString());

				break;
			case getPackageInfo:

				result = Conversions.JavaToAir_String(
						getPackageInfo(Conversions.AirToJava_String($params[1])).toString()
				);

				break;
			case canRequestPackageInstalls:

				result = Conversions.JavaToAir_Boolean(
						canRequestPackageInstalls()
				);

				break;
			case askUserPermissionForManagingUnknownApps:

				askUserPermissionForManagingUnknownApps(_activity);

				break;
			case checkPermission:

				result = Conversions.JavaToAir_Integer(
						checkPermission(
								Conversions.AirToJava_String($params[1]), // $permissionName
								Conversions.AirToJava_String($params[2]) // $packageName
						)
				);

				break;
			case checkSignatures:

				result = Conversions.JavaToAir_Integer(
						checkSignatures(
								Conversions.AirToJava_String($params[1]), // $package1
								Conversions.AirToJava_String($params[2]) // $package2
						)
				);

				break;
			case isInstantApp:

				result = Conversions.JavaToAir_Boolean(
						isInstantApp(Conversions.AirToJava_String($params[1]))
				);

				 break;
			case getAppIcon:

				result = Conversions.JavaToAir_Bitmap(
						getAppIcon(Conversions.AirToJava_String($params[1]))
				);

				break;
			case installApplicationFromGooglePlay:

				installApplicationFromGooglePlay(Conversions.AirToJava_String($params[1]));

				break;
			case installApplicationFromApk:

				installApplicationFromApk(
						Conversions.AirToJava_String($params[1]), // $apkPath
						Conversions.AirToJava_String($params[2]), // $appId
						Conversions.AirToJava_Boolean($params[3]) // $useAirPrefix
				);

				break;
			case runApplication:

				runApplication(
						Conversions.AirToJava_String($params[1]), // $packageName
						Conversions.AirToJava_String($params[2]) // $extras: could be empty String ""
				);

				break;
			case uninstallApplication:

				uninstallApplication(Conversions.AirToJava_String($params[1]));

				break;
		}

		return result;
	}

	private void installApplicationFromApk(String $apkPath, String $appId, boolean $useAirPrefix)
	{
		try
		{
			File f = new File($apkPath);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
			{
				if($useAirPrefix)
				{
					$appId = "air." + $appId;
				}

				// add the .provider suffix
				$appId = $appId + ".provider";

				Uri uri = FileProvider.getUriForFile(_activity.getApplicationContext(), $appId, f);
				Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
				intent.setData(uri);
				intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				_activity.startActivity(intent);
			}
			else
			{
				Uri uri = Uri.fromFile(f);

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				_activity.startActivity(intent);
			}
		}
		catch (Exception $exception)
		{
			toTrace($exception.getMessage());
		}
	}

	private void uninstallApplication(String $packageName)
	{
		try
		{
			Uri packageUri = Uri.parse("package:"+$packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
			_activity.startActivity(uninstallIntent);
		}
		catch (Exception e)
		{
			toTrace(e.getMessage());
		}
	}

	private void runApplication(String $packageName, String $extrasStr)
	{
		Intent LaunchIntent = _pm.getLaunchIntentForPackage($packageName);

		if($extrasStr.length() > 0)
		{
			try
			{
				JSONArray arr = new JSONArray($extrasStr);

				for (int i=0; i < arr.length(); i++)
				{
					JSONObject obj = arr.getJSONObject(i);
					LaunchIntent.putExtra(obj.getString("key"), obj.getString("value"));
				}
			}
			catch (JSONException e)
			{
				toTrace(e.getMessage());
			}
		}

		_activity.startActivity(LaunchIntent);
	}

	private void installApplicationFromGooglePlay(String $packageName)
	{
		try
		{
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + $packageName));
			marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			_activity.startActivity(marketIntent);
		}
		catch (Exception $exception)
		{
			toTrace($exception.getMessage());
		}
	}

	private Bitmap getAppIcon(String $packageName)
	{
		Bitmap bitmapIcon = null;

		try
		{
			Drawable icon = _pm.getApplicationIcon($packageName);
			bitmapIcon = ((BitmapDrawable)icon).getBitmap();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			toTrace(e.getMessage());
		}

		return bitmapIcon;
	}

	private boolean isInstantApp(String $packageName)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			return _pm.isInstantApp($packageName);
		}

		return false;
	}

	private int checkSignatures(String $package1, String $package2)
	{
		return _pm.checkSignatures($package1, $package2);
	}

	private int checkPermission(String $permissionName, String $packageName)
	{
		return _pm.checkPermission($permissionName, $packageName);
	}

	private void askUserPermissionForManagingUnknownApps(Activity $activity)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			$activity.startActivityForResult(
					new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
							.setData(Uri.parse(String.format("package:%s", $activity.getPackageName()))),
					ExConsts.REQUEST_CODE
			);
		}
	}

	private boolean canRequestPackageInstalls()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			return _pm.canRequestPackageInstalls();
		}

		return true;
	}

	private JSONObject getPackageInfo(String $packageName)
	{
		JSONObject obj = new JSONObject();

		try
		{
			PackageInfo info = _pm.getPackageInfo($packageName, 0);
			obj = convertPackageInfoToJson(info);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			toTrace(e.getMessage());
		}

		return obj;
	}

	private JSONArray getInstalledPackages(Activity $activity, Boolean $systemApps)
	{
		JSONArray arr = null;
		try
		{
			arr = new JSONArray();

			List<PackageInfo> allApps = _pm.getInstalledPackages(0);

			for (PackageInfo app : allApps)
			{
				if ($systemApps)
				{
					if (checkIsSystemPackage(app))
					{
						arr.put(app.packageName);
					}

				}
				else
				{
					if (!checkIsSystemPackage(app))
					{
						arr.put(app.packageName);
					}
				}
			}
		}
		catch (Exception e)
		{
			toTrace(e.getMessage());
		}

		return arr;
	}

	private boolean checkIsSystemPackage(PackageInfo pkgInfo)
	{
		return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
	}

	private JSONObject convertPackageInfoToJson(PackageInfo $info)
	{
		JSONObject obj = new JSONObject();

		try
		{
			obj.put("packageName", $info.packageName);
			//obj.put("permissions", $app.permissions); //This is only filled in if the flag PackageManager.GET_PERMISSIONS was set.
			obj.put("firstInstallTime", $info.firstInstallTime);
			obj.put("lastUpdateTime", $info.lastUpdateTime);
			obj.put("label", $info.applicationInfo.loadLabel(_pm).toString());
			obj.put("versionCode", $info.versionCode);
			obj.put("versionName", $info.versionName);
//			obj.put("sharedUserId", $info.sharedUserId);
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) obj.put("installLocation", $info.installLocation);
		}
		catch (JSONException e)
		{
			toTrace(e.getMessage());
		}

		return obj;
	}

	private void toTrace(String $msg)
	{
		com.myflashlab.dependency.overrideAir.MyExtension.toTrace(
				ExConsts.ANE_NAME,
				this.getClass().getSimpleName(),
				$msg);
	}
}