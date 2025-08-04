# Flutter Disable Screenshot Plugin - API Documentation

## Overview

The `flutter_disable_screenshot` plugin provides functionality to disable screenshot and screen recording capabilities on both Android and iOS platforms. This plugin helps protect sensitive content in Flutter applications by preventing users from capturing screen content.

## Table of Contents

1. [Installation](#installation)
2. [Platform Support](#platform-support)
3. [Public APIs](#public-apis)
4. [Classes and Components](#classes-and-components)
5. [Platform Implementations](#platform-implementations)
6. [Usage Examples](#usage-examples)
7. [Integration Guide](#integration-guide)
8. [Troubleshooting](#troubleshooting)
9. [Known Issues](#known-issues)

## Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  flutter_disable_screenshot: ^0.0.1
```

Then run:

```bash
flutter pub get
```

## Platform Support

| Platform | Supported | Notes |
|----------|-----------|--------|
| Android | ✅ | Uses `FLAG_SECURE` window flag |
| iOS | ⚠️ | Partial implementation (needs enhancement) |

## Public APIs

### FlutterDisableScreenshot Class

The main plugin class that provides platform version information.

#### Static Methods

##### `platformVersion`

```dart
static Future<String> get platformVersion
```

**Description:** Returns the platform version string.

**Returns:** `Future<String>` - A future that resolves to the platform version.

**Example:**
```dart
import 'package:flutter_disable_screenshot/flutter_disable_screenshot.dart';

String version = await FlutterDisableScreenshot.platformVersion;
print('Platform version: $version');
```

### DisableScreenshot Class

The main functional class that provides screenshot disabling capabilities.

#### Static Methods

##### `getInstance()`

```dart
static DisableScreenshot getInstance()
```

**Description:** Returns a singleton instance of the DisableScreenshot class.

**Returns:** `DisableScreenshot` - The singleton instance.

**Example:**
```dart
DisableScreenshot screenProtection = DisableScreenshot.getInstance();
```

#### Instance Methods

##### `setDisabled(bool isDisabled)`

```dart
void setDisabled(bool isDisabled)
```

**Description:** Enables or disables screenshot and screen recording protection.

**Parameters:**
- `isDisabled` (bool): `true` to disable screenshots, `false` to enable them.

**Platform Behavior:**
- **Android:** Sets or clears the `FLAG_SECURE` window flag
- **iOS:** Currently returns platform version (needs proper implementation)

**Example:**
```dart
DisableScreenshot screenProtection = DisableScreenshot.getInstance();

// Disable screenshots
screenProtection.setDisabled(true);

// Enable screenshots
screenProtection.setDisabled(false);
```

## Classes and Components

### 1. FlutterDisableScreenshot

**Location:** `lib/flutter_disable_screenshot.dart`

**Purpose:** Main plugin class that handles platform communication.

**Properties:**
- `_channel` (MethodChannel): Static method channel for platform communication

**Methods:**
- `platformVersion`: Returns platform version information

### 2. DisableScreenshot

**Location:** `example/lib/disable_screen_shot.dart`

**Purpose:** Singleton class that provides the main screenshot disabling functionality.

**Properties:**
- `_instance` (static): Singleton instance holder
- `_channel` (MethodChannel): Method channel for platform communication

**Methods:**
- `getInstance()`: Returns singleton instance
- `setDisabled(bool)`: Controls screenshot prevention

## Platform Implementations

### Android Implementation

**File:** `android/src/main/java/com/kangleon/flutter_disable_screenshot/FlutterDisableScreenshotPlugin.java`

#### Key Features:
- Implements `FlutterPlugin`, `MethodChannel.MethodCallHandler`, and `ActivityAware`
- Uses Android's `FLAG_SECURE` window flag to prevent screenshots
- Maintains state across activity configuration changes

#### Methods:

##### `setDisabled(boolean isDisabled, MethodChannel.Result result)`

**Description:** Sets or clears the secure flag on the activity window.

**Implementation:**
```java
private void setDisabled(boolean isDisabled, MethodChannel.Result result) {
    if (activity != null) {
        if (isDisabled)
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, 
                                        WindowManager.LayoutParams.FLAG_SECURE);
        else
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        if (result != null)
            result.success(true);
    } else {
        if (result != null)
            result.notImplemented();
    }
}
```

##### `onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result)`

**Description:** Handles method calls from Flutter.

**Supported Methods:**
- `setDisabled`: Toggles screenshot protection

### iOS Implementation

**Files:** 
- `ios/Classes/FlutterDisableScreenshotPlugin.m`
- `ios/Classes/SwiftFlutterDisableScreenshotPlugin.swift`

#### Current Status:
⚠️ **Incomplete Implementation** - Currently only returns platform version.

#### Required Enhancement:
The iOS implementation needs to be enhanced to actually disable screenshots. Recommended approach:

```swift
// Recommended iOS implementation
public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "setDisabled":
        if let args = call.arguments as? [String: Any],
           let disabled = args["disabled"] as? Bool {
            setScreenshotDisabled(disabled)
            result(true)
        } else {
            result(FlutterError(code: "INVALID_ARGUMENTS", 
                              message: "Invalid arguments", 
                              details: nil))
        }
    default:
        result(FlutterMethodNotImplemented)
    }
}

private func setScreenshotDisabled(_ disabled: Bool) {
    // Implementation would involve:
    // 1. Listening for UIApplicationUserDidTakeScreenshotNotification
    // 2. Using UIView's isUserInteractionEnabled or similar
    // 3. Potentially using UITextField's isSecureTextEntry for sensitive views
}
```

## Usage Examples

### Basic Usage

```dart
import 'package:flutter/material.dart';
import 'package:flutter_disable_screenshot_example/disable_screen_shot.dart';

class ProtectedScreen extends StatefulWidget {
  @override
  _ProtectedScreenState createState() => _ProtectedScreenState();
}

class _ProtectedScreenState extends State<ProtectedScreen> {
  DisableScreenshot _screenProtection = DisableScreenshot.getInstance();
  bool _isProtected = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Protected Content'),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            'Screenshot Protection: ${_isProtected ? "ON" : "OFF"}',
            style: TextStyle(fontSize: 20),
          ),
          SizedBox(height: 20),
          ElevatedButton(
            onPressed: _toggleProtection,
            child: Text(_isProtected ? 'Disable Protection' : 'Enable Protection'),
          ),
          SizedBox(height: 40),
          Container(
            padding: EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: _isProtected ? Colors.green.shade100 : Colors.red.shade100,
              borderRadius: BorderRadius.circular(10),
            ),
            child: Text(
              'This is sensitive content that should be protected from screenshots.',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 16),
            ),
          ),
        ],
      ),
    );
  }

  void _toggleProtection() {
    setState(() {
      _isProtected = !_isProtected;
      _screenProtection.setDisabled(_isProtected);
    });
  }

  @override
  void dispose() {
    // Always re-enable screenshots when leaving the screen
    _screenProtection.setDisabled(false);
    super.dispose();
  }
}
```

### Advanced Usage with State Management

```dart
import 'package:flutter/material.dart';
import 'package:flutter_disable_screenshot_example/disable_screen_shot.dart';

class ScreenProtectionManager {
  static final ScreenProtectionManager _instance = ScreenProtectionManager._internal();
  factory ScreenProtectionManager() => _instance;
  ScreenProtectionManager._internal();

  final DisableScreenshot _screenProtection = DisableScreenshot.getInstance();
  bool _isGloballyProtected = false;

  bool get isProtected => _isGloballyProtected;

  void enableProtection() {
    if (!_isGloballyProtected) {
      _screenProtection.setDisabled(true);
      _isGloballyProtected = true;
      print('Screenshot protection enabled');
    }
  }

  void disableProtection() {
    if (_isGloballyProtected) {
      _screenProtection.setDisabled(false);
      _isGloballyProtected = false;
      print('Screenshot protection disabled');
    }
  }
}

// Usage in your app
class MyApp extends StatelessWidget {
  final ScreenProtectionManager _protectionManager = ScreenProtectionManager();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomePage(),
      navigatorObservers: [
        ScreenProtectionObserver(_protectionManager),
      ],
    );
  }
}

class ScreenProtectionObserver extends NavigatorObserver {
  final ScreenProtectionManager _protectionManager;

  ScreenProtectionObserver(this._protectionManager);

  @override
  void didPush(Route<dynamic> route, Route<dynamic>? previousRoute) {
    super.didPush(route, previousRoute);
    
    // Enable protection for specific routes
    if (route.settings.name?.contains('protected') == true) {
      _protectionManager.enableProtection();
    }
  }

  @override
  void didPop(Route<dynamic> route, Route<dynamic>? previousRoute) {
    super.didPop(route, previousRoute);
    
    // Disable protection when leaving protected routes
    if (route.settings.name?.contains('protected') == true) {
      _protectionManager.disableProtection();
    }
  }
}
```

### Testing the Plugin

```dart
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/services.dart';
import 'package:flutter_disable_screenshot_example/disable_screen_shot.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('DisableScreenshot Tests', () {
    const MethodChannel channel = MethodChannel('flutter_disable_screen_shot');
    List<MethodCall> methodCalls = [];

    setUp(() {
      methodCalls.clear();
      channel.setMockMethodCallHandler((MethodCall methodCall) async {
        methodCalls.add(methodCall);
        return true;
      });
    });

    tearDown(() {
      channel.setMockMethodCallHandler(null);
    });

    test('should call setDisabled with correct parameters', () {
      final disableScreenshot = DisableScreenshot.getInstance();
      
      disableScreenshot.setDisabled(true);
      
      expect(methodCalls.length, 1);
      expect(methodCalls.first.method, 'setDisabled');
      expect(methodCalls.first.arguments['disabled'], true);
    });

    test('should handle multiple calls correctly', () {
      final disableScreenshot = DisableScreenshot.getInstance();
      
      disableScreenshot.setDisabled(true);
      disableScreenshot.setDisabled(false);
      
      expect(methodCalls.length, 2);
      expect(methodCalls[0].arguments['disabled'], true);
      expect(methodCalls[1].arguments['disabled'], false);
    });
  });
}
```

## Integration Guide

### Step 1: Add Dependency

Add the plugin to your `pubspec.yaml`:

```yaml
dependencies:
  flutter:
    sdk: flutter
  flutter_disable_screenshot: ^0.0.1
```

### Step 2: Import Required Files

```dart
import 'package:flutter_disable_screenshot_example/disable_screen_shot.dart';
```

**Note:** Currently, the main functionality is in the example folder. This should be moved to the main plugin library.

### Step 3: Initialize in Your App

```dart
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> with WidgetsBindingObserver {
  DisableScreenshot _screenProtection = DisableScreenshot.getInstance();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    // Ensure screenshots are re-enabled when app is disposed
    _screenProtection.setDisabled(false);
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    
    // Optional: Disable screenshots when app goes to background
    switch (state) {
      case AppLifecycleState.paused:
        _screenProtection.setDisabled(true);
        break;
      case AppLifecycleState.resumed:
        _screenProtection.setDisabled(false);
        break;
      default:
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Protected App',
      home: MainScreen(),
    );
  }
}
```

### Step 4: Platform-Specific Setup

#### Android

No additional setup required. The plugin automatically integrates with the Android lifecycle.

#### iOS

Currently requires manual implementation enhancement. The existing iOS code only returns platform version.

## Troubleshooting

### Common Issues

#### 1. Channel Name Mismatch

**Problem:** The plugin uses inconsistent channel names.
- Main plugin: `'flutter_disable_screenshot'`
- Working implementation: `'flutter_disable_screen_shot'`

**Solution:** Use the working channel name `'flutter_disable_screen_shot'` until this is fixed.

#### 2. iOS Not Working

**Problem:** Screenshot protection doesn't work on iOS.

**Cause:** The iOS implementation is incomplete and only returns platform version.

**Solution:** The iOS implementation needs to be enhanced to actually disable screenshots.

#### 3. State Persistence Issues

**Problem:** Screenshot protection state is lost during configuration changes.

**Solution:** The Android implementation handles this correctly by maintaining state in `onReattachedToActivityForConfigChanges`.

#### 4. Testing on Emulators

**Problem:** Screenshot behavior may differ on emulators vs. real devices.

**Solution:** Always test on physical devices for accurate results.

### Debugging

Enable debug logging to track method calls:

```dart
import 'package:flutter/services.dart';

class DebugDisableScreenshot extends DisableScreenshot {
  @override
  void setDisabled(bool isDisabled) {
    print('Setting screenshot disabled: $isDisabled');
    super.setDisabled(isDisabled);
  }
}
```

## Known Issues

### 1. Channel Name Inconsistency

The plugin has inconsistent method channel names:
- `FlutterDisableScreenshot` class uses: `'flutter_disable_screenshot'`
- `DisableScreenshot` class uses: `'flutter_disable_screen_shot'`
- Android implementation expects: `'flutter_disable_screen_shot'`

### 2. Incomplete iOS Implementation

The iOS implementation currently only returns platform version and doesn't actually disable screenshots.

### 3. Code Organization

The main functional code (`DisableScreenshot`) is in the example folder rather than the main plugin library.

### 4. Missing Error Handling

The current implementation lacks proper error handling for platform-specific failures.

### 5. No Callback Support

The plugin doesn't provide callbacks to notify when screenshot attempts are made or blocked.

## Recommended Improvements

1. **Consolidate Channel Names:** Use a single, consistent channel name throughout the plugin.

2. **Move Core Functionality:** Move `DisableScreenshot` class from example to main plugin library.

3. **Complete iOS Implementation:** Implement actual screenshot prevention for iOS.

4. **Add Error Handling:** Implement proper error handling and result callbacks.

5. **Add Documentation:** Include inline documentation for all public APIs.

6. **Add Callback Support:** Provide callbacks for screenshot attempt notifications.

7. **Add Configuration Options:** Allow fine-grained control over what content is protected.

## Contributing

When contributing to this plugin, please ensure:

1. Both Android and iOS implementations are updated
2. Tests are added for new functionality
3. Documentation is updated
4. Channel names are consistent
5. Error handling is implemented

## Version History

- **0.0.1**: Initial release with basic Android support and incomplete iOS implementation