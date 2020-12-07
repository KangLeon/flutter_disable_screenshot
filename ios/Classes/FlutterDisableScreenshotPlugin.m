#import "FlutterDisableScreenshotPlugin.h"
#if __has_include(<flutter_disable_screenshot/flutter_disable_screenshot-Swift.h>)
#import <flutter_disable_screenshot/flutter_disable_screenshot-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_disable_screenshot-Swift.h"
#endif

@implementation FlutterDisableScreenshotPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterDisableScreenshotPlugin registerWithRegistrar:registrar];
}
@end
