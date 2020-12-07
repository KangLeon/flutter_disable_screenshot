
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterDisableScreenshot {
  static const MethodChannel _channel =
      const MethodChannel('flutter_disable_screenshot');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
