# Flutter Disable Screenshot Plugin

A Flutter plugin that provides functionality to disable screenshot and screen recording capabilities on both Android and iOS platforms. This plugin helps protect sensitive content in Flutter applications by preventing users from capturing screen content.

## 📚 Documentation

- **[Complete API Documentation](./API_DOCUMENTATION.md)** - Comprehensive guide covering all APIs, usage examples, and integration instructions
- **[Developer Guide](./DEVELOPER_GUIDE.md)** - Technical implementation details, architecture, and development best practices
- **[Troubleshooting Guide](./TROUBLESHOOTING.md)** - Common issues, debugging steps, and solutions

## 🚀 Quick Start

### Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  flutter_disable_screenshot: ^0.0.1
```

Then run:
```bash
flutter pub get
```

### Basic Usage

```dart
import 'package:flutter_disable_screenshot_example/disable_screen_shot.dart';

// Get the singleton instance
DisableScreenshot screenProtection = DisableScreenshot.getInstance();

// Disable screenshots
screenProtection.setDisabled(true);

// Enable screenshots
screenProtection.setDisabled(false);
```

### Complete Example

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
              'This content is protected from screenshots.',
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

## 🎯 Features

- ✅ **Android Support**: Uses `FLAG_SECURE` to prevent screenshots and screen recording
- ⚠️ **iOS Support**: Partial implementation (needs enhancement)
- 🔒 **Security Focus**: Protects sensitive content from unauthorized capture
- 🎛️ **Easy Control**: Simple enable/disable API
- 🔄 **State Management**: Maintains protection state across configuration changes
- 🧪 **Testable**: Includes comprehensive test support

## 📱 Platform Support

| Platform | Status | Implementation |
|----------|--------|----------------|
| Android | ✅ Fully Supported | Uses `WindowManager.FLAG_SECURE` |
| iOS | ⚠️ Partial | Returns platform version only (needs completion) |

## 🏗️ Architecture

The plugin follows the standard Flutter plugin architecture:

```
Flutter App
    ↓
DisableScreenshot (Dart)
    ↓
MethodChannel
    ↓
Platform Implementation
    ├── Android: FLAG_SECURE
    └── iOS: Not implemented
```

## 📖 API Reference

### Main Classes

#### `DisableScreenshot`

Singleton class that provides screenshot disabling functionality.

**Methods:**
- `getInstance()` → `DisableScreenshot` - Returns the singleton instance
- `setDisabled(bool isDisabled)` → `void` - Enables/disables screenshot protection

#### `FlutterDisableScreenshot`

Main plugin class for platform information.

**Methods:**
- `platformVersion` → `Future<String>` - Returns platform version

## ⚠️ Important Notes

### Current Limitations

1. **iOS Implementation Incomplete**: The iOS side currently only returns platform version
2. **Channel Name Inconsistency**: Different channel names used in different parts
3. **Code Organization**: Main functionality is in example folder rather than main library
4. **Limited Error Handling**: Missing comprehensive error handling

### Usage Recommendations

1. **Use the Working Implementation**: Import from the example folder for actual functionality
2. **Test on Physical Devices**: Emulators may not accurately reflect screenshot behavior
3. **Handle App Lifecycle**: Manage protection state during app backgrounding/foregrounding
4. **Android Focus**: Currently only reliable on Android devices

## 🔧 Development

### Prerequisites

- Flutter SDK ≥ 1.20.0
- Dart SDK ≥ 2.7.0
- Android Studio (for Android development)
- Xcode (for iOS development)

### Building the Plugin

```bash
# Clone the repository
git clone <repository-url>
cd flutter_disable_screenshot

# Get dependencies
flutter pub get

# Run the example
cd example
flutter pub get
flutter run
```

### Running Tests

```bash
flutter test
```

## 🐛 Troubleshooting

### Common Issues

1. **Screenshots Still Work**: Use the example implementation, not the main plugin class
2. **iOS Not Working**: iOS implementation is incomplete
3. **Channel Errors**: Ensure proper plugin installation and restart the app

For comprehensive troubleshooting, see the [Troubleshooting Guide](./TROUBLESHOOTING.md).

## 🤝 Contributing

Contributions are welcome! Please read our contributing guidelines and ensure:

1. Both Android and iOS implementations are updated
2. Tests are added for new functionality
3. Documentation is updated
4. Code follows Dart/Flutter style guidelines

### Priority Improvements Needed

1. **Complete iOS Implementation**: Implement actual screenshot prevention
2. **Fix Channel Names**: Use consistent naming throughout
3. **Move Core Code**: Move `DisableScreenshot` from example to main library
4. **Add Error Handling**: Implement comprehensive error handling
5. **Add Callbacks**: Support for screenshot attempt notifications

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🔗 Related Links

- [Flutter Plugin Development](https://flutter.dev/developing-packages/)
- [Android FLAG_SECURE Documentation](https://developer.android.com/reference/android/view/WindowManager.LayoutParams#FLAG_SECURE)
- [iOS Screenshot Detection](https://developer.apple.com/documentation/uikit/uiapplication/1623044-userdidtakescreenshotnotificatio)

## 📊 Version History

- **0.0.1**: Initial release with basic Android support and incomplete iOS implementation

---

**Note**: This plugin is in early development. The iOS implementation is incomplete, and there are several architectural improvements needed. Please see the documentation for current limitations and workarounds.

