import 'dart:async';

import 'package:flutter/services.dart';

class AndroidFlutterFries {
  static const MethodChannel _channel =
      const MethodChannel('android_flutter_fries');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<int> getSystemSetting(String setting) async =>
      await _channel.invokeMethod('getSystemSetting', {'setting': setting});

  static Future<int> getSecureSetting(String setting) async =>
      await _channel.invokeMethod('getSecureSetting', {'setting': setting});

  static Future<void> setSystemSetting(String setting, int value) async =>
      await _channel.invokeMethod('setSystemSetting', {'setting': setting, 'value': value});

  static Future<void> setSecureSetting(String setting, int value) async =>
      await _channel.invokeMethod('setSecureSetting', {'setting': setting, 'value': value});

  static Future<bool> getDarkModeValue() async =>
      await _channel.invokeMethod('getDarkModeValue');

  static Future<void> setDarkModeValue(bool value) async =>
      await _channel.invokeMethod('setDarkModeValue', {'value': value});

  static Future<void> changeOverlayStatus(String overlayPkg, bool enabled) async =>
      await _channel.invokeMethod('changeOverlayStatus', {'overlayPkg': overlayPkg, 'enabled': enabled});

  static Future<void> setOverlayEnabledExclusiveInCategory(String overlayPkg) async =>
      await _channel.invokeMethod('setOverlayEnabledExclusiveInCategory', {'overlayPkg': overlayPkg});

  static Future<int> getAccentColor() async =>
      await _channel.invokeMethod('getAccentColor');
}
