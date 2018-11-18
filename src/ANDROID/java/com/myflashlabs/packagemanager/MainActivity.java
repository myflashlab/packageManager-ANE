package com.myflashlabs.packagemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO;
import static android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY;
import static android.content.pm.PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL;
import static android.content.pm.PackageManager.GET_META_DATA;
import static android.content.pm.PackageManager.SIGNATURE_FIRST_NOT_SIGNED;
import static android.content.pm.PackageManager.SIGNATURE_MATCH;
import static android.content.pm.PackageManager.SIGNATURE_NEITHER_SIGNED;
import static android.content.pm.PackageManager.SIGNATURE_NO_MATCH;
import static android.content.pm.PackageManager.SIGNATURE_SECOND_NOT_SIGNED;
import static android.content.pm.PackageManager.SIGNATURE_UNKNOWN_PACKAGE;

public class MainActivity extends AppCompatActivity
{
	private String TAG = "packageManager";


	private Activity _activity;
	private PackageManager _pm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		_activity = this;
		_pm = _activity.getPackageManager();

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				/*JSONArray arr = getInstalledPackages(_activity, false);
				Log.i(TAG, arr.toString());*/

				//----------------------------------------------------------------------

				/*if (!canRequestPackageInstalls())
				{
					askUserPermissionForManagingUnknownApps();
				}
				else
				{
					callInstallProcess();
				}*/

				//----------------------------------------------------------------------

				/*int result = checkPermission(Manifest.permission.CAMERA, "air.com.doitflash.exBarcode");
				Log.i(TAG, "checkPermission result: " + result);*/

				//----------------------------------------------------------------------

				/*int result = checkSignatures("air.com.doitflash.exBarcode", "air.com.doitflash.exFileBrowser");
				Log.i(TAG, "checkSignatures result: " + result);*/

				//----------------------------------------------------------------------

				/*Boolean result = isInstantApp("air.com.doitflash.exBarcode");
				Log.i(TAG, "isInstantApp air.com.doitflash.exBarcode: " + result);*/

				//----------------------------------------------------------------------

				/*List<PermissionGroupInfo> list = getAllPermissionGroups(GET_META_DATA);
				for (int i=0; i < list.size(); i++)
				{
					PermissionGroupInfo info = list.get(i);
					Log.i(TAG, "PermissionGroupInfo: " + info.name);
				}*/

				//----------------------------------------------------------------------

				/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH)
				{
					try
					{
						Drawable icon = _pm.getApplicationIcon("air.com.doitflash.exBarcode");
						Bitmap bitmapIcon = ((BitmapDrawable)icon).getBitmap();

						Log.i(TAG, "bitmapIcon: "+bitmapIcon.getWidth() + "x" + bitmapIcon.getHeight());
					}
					catch (PackageManager.NameNotFoundException e)
					{
						e.printStackTrace();
					}
				}*/

				//----------------------------------------------------------------------

				/*JSONObject obj = getPackageInfo("air.com.doitflash.exBarcode");
				Log.i(TAG, obj.toString());*/

				//----------------------------------------------------------------------

				/*String installer = _pm.getInstallerPackageName("air.com.doitflash.exBarcode");
				Log.i(TAG, "installer: "+installer);*/

				//----------------------------------------------------------------------

				/*JSONArray arr = getSystemAvailableFeatures();
				Log.i(TAG, arr.toString());*/

				//----------------------------------------------------------------------

//				Log.i(TAG, "isSafeMode: " + _pm.isSafeMode());

				//----------------------------------------------------------------------

//				installApplicationFromGooglePlay(_activity, "air.com.myflashlabs.app");

				//----------------------------------------------------------------------

				/*try
				{
					JSONArray arr = new JSONArray();

					JSONObject obj1 = new JSONObject();
					obj1.put("key", "key1");
					obj1.put("value", "value1");
					arr.put(obj1);

					JSONObject obj2 = new JSONObject();
					obj2.put("key", "key2");
					obj2.put("value", "value2");
					arr.put(obj2);

					runApplication(_activity, "air.com.doitflash.exBarcode", arr);
				}
				catch (Exception e)
				{
					Log.e(TAG, e.getMessage());
				}*/

				//----------------------------------------------------------------------

				uninstallApplication(_activity,"air.com.doitflash.exFileBrowser");

				//----------------------------------------------------------------------

//				installApplicationFromApk();
			}
		});
	}

	private void uninstallApplication(Activity $activity, String $package)
	{
		try
		{
			Uri packageUri = Uri.parse("package:"+$package);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
			$activity.startActivity(uninstallIntent);
		}
		catch (Exception e)
		{
			Log.i(TAG, e.getMessage());
		}
	}

	private void runApplication(Activity $activity, String $package, JSONArray $extras)
	{
		try
		{
			Intent LaunchIntent = _pm.getLaunchIntentForPackage($package);

			for (int i=0; i < $extras.length(); i++)
			{
				JSONObject obj = $extras.getJSONObject(i);
				LaunchIntent.putExtra(obj.getString("key"), obj.getString("value"));
			}

			$activity.startActivity(LaunchIntent);
		}
		catch (Exception e)
		{
			Log.i(TAG, e.getMessage());
		}
	}

	private void installApplicationFromGooglePlay(Activity $activity, String $package)
	{
		try
		{
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + $package));
			marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			$activity.startActivity(marketIntent);
		}
		catch (Exception $exception)
		{
			Log.e(TAG, $exception.getMessage());
		}
	}

	private JSONObject getPackageInfo(String $packageName)
	{
		JSONObject obj = new JSONObject();


		try
		{
			PackageInfo info = _pm.getPackageInfo($packageName, 0);

			obj.put("label", info.applicationInfo.loadLabel(_pm).toString());
			obj.put("lastUpdateTime", info.lastUpdateTime);
			obj.put("firstInstallTime", info.firstInstallTime);
			obj.put("versionCode", info.versionCode);
			obj.put("versionName", info.versionName);
			obj.put("sharedUserId", info.sharedUserId);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) obj.put("installLocation", info.installLocation);

		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return obj;
	}

	private JSONArray getSystemAvailableFeatures()
	{
		JSONArray arr = new JSONArray();
		FeatureInfo[] list = _pm.getSystemAvailableFeatures();
		for (FeatureInfo feature : list)
		{
			JSONObject obj = new JSONObject();

			try
			{
				obj.put("name", feature.name);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
				{
					obj.put("version", feature.version);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

			arr.put(obj);
		}

		return arr;
	}

	private List<PermissionGroupInfo> getAllPermissionGroups(int $flags)
	{
		return _pm.getAllPermissionGroups($flags);
	}

	private Boolean isInstantApp(String $packageName)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			return _pm.isInstantApp($packageName);
		}

		return false;
	}

	private int checkSignatures(String $package1, String $packag2)
	{
		/*
			Value is SIGNATURE_MATCH, SIGNATURE_NEITHER_SIGNED, SIGNATURE_FIRST_NOT_SIGNED,
			SIGNATURE_SECOND_NOT_SIGNED, SIGNATURE_NO_MATCH or SIGNATURE_UNKNOWN_PACKAGE.
		*/

		return _pm.checkSignatures($package1, $packag2);
	}

	private int checkPermission(String $permissionName, String $packageName)
	{
		return _pm.checkPermission($permissionName, $packageName);
	}

	private void askUserPermissionForManagingUnknownApps()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			startActivityForResult(
					new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))),
					1234
			);
		}
	}

	private Boolean canRequestPackageInstalls()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			return _pm.canRequestPackageInstalls();
		}

		return true;
	}

	private void callInstallProcess()
	{
		Log.i(TAG, "callInstallProcess :)");
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
						arr.put(addToJsonObject(app));
					}

				}
				else
				{
					if (!checkIsSystemPackage(app))
					{
						arr.put(addToJsonObject(app));
					}
				}
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
		}

		return arr;
	}

	private JSONObject addToJsonObject(PackageInfo $app)
	{
		JSONObject obj = new JSONObject();

		try
		{
			obj.put("packageName", $app.packageName);
			obj.put("permissions", $app.permissions);
			obj.put("firstInstallTime", $app.firstInstallTime);
			obj.put("lastUpdateTime", $app.lastUpdateTime);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return obj;
	}

	private boolean checkIsSystemPackage(PackageInfo pkgInfo)
	{
		return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1234)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
				{
					if (canRequestPackageInstalls())
					{
						callInstallProcess();
					}
					else
					{
						Log.i(TAG, "permission NOT given!");
					}
				}
			}
			else
			{
				Log.i(TAG, "permission NOT given!");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
