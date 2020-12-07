import 'package:flutter/services.dart';

class DisableScreenshot {
  static DisableScreenshot _instance;

  MethodChannel _channel;

  static DisableScreenshot getInstance() {
    if (_instance == null) _instance = DisableScreenshot();
    return _instance;
  }

  DisableScreenshot() {
    _channel = MethodChannel("flutter_disable_screen_shot");
  }

  void setDisabled(bool isDisabled) {
    assert(_channel != null && isDisabled != null);
    _channel.invokeMethod("setDisabled", {"disabled": isDisabled});
  }
}
