import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_disable_screenshot/flutter_disable_screenshot.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_disable_screenshot');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterDisableScreenshot.platformVersion, '42');
  });
}
