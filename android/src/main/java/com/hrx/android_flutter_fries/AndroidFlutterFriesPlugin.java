package com.hrx.android_flutter_fries;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** AndroidFlutterFriesPlugin */
@SuppressWarnings("Convert2Lambda")
public class AndroidFlutterFriesPlugin {
  private final Activity mActivity;
  private Context mContext;
  private UiModeManager mUiModeManager;
  private static final String TAG = "FlutterFriesPlugin";
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "android_flutter_fries");
    new AndroidFlutterFriesPlugin(channel, registrar.activity());
  }

  private AndroidFlutterFriesPlugin(MethodChannel channel, Activity activity) {
    this.mActivity = activity;
    this.mContext = activity.getApplicationContext();
    this.mUiModeManager = mContext.getSystemService(UiModeManager.class);

    channel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
      @Override
      public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
          case "getPlatformVersion": {
            resultSuccess(result, "Android " + android.os.Build.VERSION.RELEASE);
            break;
          }
          case "getSystemSetting": {
            final String setting = call.argument("setting");
            resultSuccess(result, getSystemSetting(setting));
          }
          case "setSystemSetting": {
            final String setting = call.argument("setting");
            final int value = call.argument("value");

            setSystemSetting(setting, value);
            resultSuccess(result, null);
          }
          case "getSecureSetting": {
            final String setting = call.argument("setting");
            resultSuccess(result, getSecureSetting(setting));
          }
          case "setSecureSetting": {
            final String setting = call.argument("setting");
            final int value = call.argument("value");

            setSecureSetting(setting, value);
            resultSuccess(result, null);
          }
          case "getDarkModeValue": {
            resultSuccess(result, getDarkModeValue());
          }
          case "setDarkModeValue": {
            final boolean value = call.argument("value");

            setDarkModeValue(value);
            resultSuccess(result, getDarkModeValue());
          }
          case "getAccentColor": {
            resultSuccess(result, getAccentColor());
          }
          default:
            result.notImplemented();
            break;
        }
      }
    });
  }

  private int getSystemSetting(String setting) {
    int returnInt = 0;
    try {
      returnInt = Settings.System.getInt(mContext.getContentResolver(), setting);
    } catch(Settings.SettingNotFoundException e) {
      Log.e(TAG, "Setting not found: " + setting);
    }

    return returnInt;
  }

  private void setSystemSetting(String setting, int newValue) {
    Settings.System.putInt(mContext.getContentResolver(), setting, newValue);
  }

  private int getSecureSetting(String setting) {
    int returnInt = 0;
    try {
      returnInt = Settings.Secure.getInt(mContext.getContentResolver(), setting);
    } catch(Settings.SettingNotFoundException e) {
      Log.e(TAG, "Setting not found: " + setting);
    }

    return returnInt;
  }

  private void setSecureSetting(String setting, int newValue) {
    Settings.Secure.putInt(mContext.getContentResolver(), setting, newValue);
  }

  private boolean getDarkModeValue() {
    return (mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) ==
        Configuration.UI_MODE_NIGHT_YES;
  }

  private void setDarkModeValue(boolean value) {
    mUiModeManager.setNightMode(value ? UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO);
  }

  private int getAccentColor() {
    String colResName = "accent_device_default_dark";
    Resources res = null;
    try {
      res = mActivity.getPackageManager().getResourcesForApplication("android");
      int resId = res.getIdentifier("android:color/" + colResName, null, null);
      return res.getColor(resId);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return 0;
  }

  private void resultSuccess(MethodChannel.Result result, Object object) {
    final Result resultFinal = result;
    final Object objectFinal = object;
    if (mActivity == null) return;
    mActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        resultFinal.success(objectFinal);
      }
    });
  }
}
