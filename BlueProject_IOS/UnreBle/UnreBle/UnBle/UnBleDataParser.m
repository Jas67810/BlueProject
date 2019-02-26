//
//  UnBleDataParser.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleDataParser.h"

//byte[] 需要小于 20 个字节。
 // 第一个字节为标示码，固定 0XFF
 // 第二个字节为操作码，0X01 代表按键值
 // 后续为操作参数
 ////////////////////////////////////////////
/*按键需要3个字节，第三个
*/
#define BLE_HEAD                0xFF
#define BLE_KEYCODE_UNKNOW      0x00
#define BLE_KEY_KEYCODE         0x01
#define BLE_KEYCODE_LEFT        0x01
#define BLE_KEYCODE_RIGHT       0x02
#define BLE_KEYCODE_UP          0x03
#define BLE_KEYCODE_DOWN        0x04
#define BLE_KEYCODE_MENU        0x05
#define BLE_KEYCODE_POWER       0x06
#define BLE_KEYCODE_VOLUMEUP    0x07
#define BLE_KEYCODE_VOLUMEDOWN  0x08
#define BLE_KEYCODE_ENTER       0x09
#define BLE_KEYCODE_HOME        0x0A
#define BLE_KEYCODE_BACK        0x0B

@interface UnBleDataParser()
@end

@implementation UnBleDataParser

+ (NSData *) tranformUnBleKeyCodeCommend:(UnBleKeyCode)code
{
    Byte data = [UnBleDataParser tranformUnBleKeyCode:code];
    if(data != BLE_KEYCODE_UNKNOW){
        Byte commend[] = {BLE_HEAD,BLE_KEY_KEYCODE,data};
        return [[NSData alloc] initWithBytes:commend length:3];
    }
    return nil;
}

+ (Byte) tranformUnBleKeyCode:(UnBleKeyCode)code
{
    switch (code) {
        case UnBle_KEYCODE_UNKNOW:return BLE_KEYCODE_UNKNOW;
        case UnBle_KEYCODE_UP:return BLE_KEYCODE_UP;
        case UnBle_KEYCODE_BACK:return BLE_KEYCODE_BACK;
        case UnBle_KEYCODE_DOWN:return BLE_KEYCODE_DOWN;
        case UnBle_KEYCODE_HOME:return BLE_KEYCODE_HOME;
        case UnBle_KEYCODE_LEFT:return BLE_KEYCODE_LEFT;
        case UnBle_KEYCODE_MENU:return BLE_KEYCODE_MENU;
        case UnBle_KEYCODE_ENTER:return BLE_KEYCODE_ENTER;
        case UnBle_KEYCODE_POWER:return BLE_KEYCODE_POWER;
        case UnBle_KEYCODE_RIGHT:return BLE_KEYCODE_RIGHT;
        case UnBle_KEYCODE_VOLUMEUP:return BLE_KEYCODE_VOLUMEUP;
        case UnBle_KEYCODE_VOLUMEDOWN:return BLE_KEYCODE_VOLUMEDOWN;
        default:
            return BLE_KEYCODE_UNKNOW;
    }
}

+ (NSString*) tranformUnBleKeyCodeToNSString:(UnBleKeyCode) code
{
    switch (code) {
        case UnBle_KEYCODE_UNKNOW:return @"UnBle_KEYCODE_UNKNOW";
        case UnBle_KEYCODE_UP:return @"UnBle_KEYCODE_UP";
        case UnBle_KEYCODE_BACK:return @"UnBle_KEYCODE_BACK";
        case UnBle_KEYCODE_DOWN:return @"UnBle_KEYCODE_DOWN";
        case UnBle_KEYCODE_HOME:return @"UnBle_KEYCODE_HOME";
        case UnBle_KEYCODE_LEFT:return @"UnBle_KEYCODE_LEFT";
        case UnBle_KEYCODE_MENU:return @"UnBle_KEYCODE_MENU";
        case UnBle_KEYCODE_ENTER:return @"UnBle_KEYCODE_ENTER";
        case UnBle_KEYCODE_POWER:return @"UnBle_KEYCODE_POWER";
        case UnBle_KEYCODE_RIGHT:return @"UnBle_KEYCODE_RIGHT";
        case UnBle_KEYCODE_VOLUMEUP:return @"UnBle_KEYCODE_VOLUMEUP";
        case UnBle_KEYCODE_VOLUMEDOWN:return @"UnBle_KEYCODE_VOLUMEDOWN";
        default:
            return [NSString stringWithFormat:@"UnBle_KEYCODE_%zi", code];
    }
}

@end
