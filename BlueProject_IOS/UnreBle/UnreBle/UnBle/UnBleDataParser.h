//
//  UnBleDataParser.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef UnBleDataParser_h
#define UnBleDataParser_h

typedef NS_ENUM(NSInteger, UnBleKeyCode)
{
    UnBle_KEYCODE_UNKNOW = 0,
    UnBle_KEYCODE_LEFT,
    UnBle_KEYCODE_RIGHT,
    UnBle_KEYCODE_UP,
    UnBle_KEYCODE_DOWN,
    UnBle_KEYCODE_MENU,
    UnBle_KEYCODE_POWER,
    UnBle_KEYCODE_VOLUMEUP,
    UnBle_KEYCODE_VOLUMEDOWN,
    UnBle_KEYCODE_ENTER,
    UnBle_KEYCODE_HOME,
    UnBle_KEYCODE_BACK
};

@interface UnBleDataParser : NSObject
//////////////////////////////////
+ (NSData *) tranformUnBleKeyCodeCommend:(UnBleKeyCode)code;
+ (Byte) tranformUnBleKeyCode:(UnBleKeyCode)code;
+ (NSString*) tranformUnBleKeyCodeToNSString:(UnBleKeyCode) code;
@end

#endif /* UnBleDataParser_h */
