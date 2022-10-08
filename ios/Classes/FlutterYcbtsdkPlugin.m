#import "FlutterYcbtsdkPlugin.h"
#if __has_include(<flutter_ycbtsdk/flutter_ycbtsdk-Swift.h>)
#import <flutter_ycbtsdk/flutter_ycbtsdk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_ycbtsdk-Swift.h"
#endif

@implementation FlutterYcbtsdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterYcbtsdkPlugin registerWithRegistrar:registrar];
}
@end
