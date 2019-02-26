//
//  UnBleEasyPeripheral.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleEasyPeripheral.h"
#include "EFShowView.h"

@interface UnBleEasyPeripheral()
@property(nonatomic,assign)BOOL teardownFlag;
@property(nonatomic,assign)BOOL isReadyFlag;
@property(nonatomic,assign) EasyCharacteristic * characteristic;
@end

@implementation UnBleEasyPeripheral

- (instancetype)initWithEasyPeripheral:(EasyPeripheral*)easy
{
    self = [super init];
    if (self) {
        _peripheral = easy;
        _teardownFlag = false;
        _isReadyFlag = false;
    }
    return self;
}
- (void)dealloc
{
    //[super dealloc];
    NSLog(@"dealloc so we teardown it !!!");
    [self tearDown];
}
- (NSString *)description
{
    return [NSString stringWithFormat:@"{peripheral:%@, showDialog:%@, teardownFlag:%@, isReadyFlag:%@}",
            _peripheral.name,
            (_showDialog?@"YES":@"NO"),
            (_teardownFlag?@"YES":@"NO"),
            (_isReadyFlag?@"YES":@"NO")];
}
/////////////////////////////////////////
- (BOOL) startConnect
{
    NSLog(@"startConnect: %@", self);
    if(_teardownFlag){
        [self tearDown];
        return NO;
    }
    if(_peripheral.state==CBPeripheralStateConnected ){
        [self hideHUB];
        return YES;
    }
    if(_showDialog){
        [self showHUB:@"正在连接设备..."];
    }
    //__weak UnBleEasyPeripheral * weakself = self;
    kWeakSelf(self)
    [_peripheral connectDeviceWithCallback:^(EasyPeripheral *perpheral, NSError *error, deviceConnectType deviceConnectType) {
        NSLog(@"connectDeviceWithCallback:%zi", deviceConnectType);
        if (deviceConnectType == deviceConnectTypeDisConnect
            || deviceConnectType == deviceConnectTypeFaild
            || deviceConnectType == deviceConnectTypeFaildTimeout) {
            [self deviceDisconnect:self.peripheral error:error];
        } else if(deviceConnectType == deviceConnectTypeSuccess){
            [self deviceConnect:self.peripheral error:error];
        }
        if(weakself.unBleEasyPeripheralDelegate){
            [weakself.unBleEasyPeripheralDelegate onConnectStateChange:deviceConnectType];
        }
    }];
    return YES;
}

- (void)tearDown
{
    NSLog(@"---> tearDown");
    [_peripheral disconnectDevice];
    [self hideHUB];
    _isReadyFlag = NO;
    _teardownFlag = YES;
}
///////////////////////////////////////
- (void)deviceDisconnect:(EasyPeripheral *)peripheral error:(NSError *)error
{
    NSLog(@"---> deviceDisconnect %@", self);
    if(!_teardownFlag){
        [self setNOReady];
        if(_showDialog){
            [self hideHUB];
            //[_peripheral reconnectDevice];
        } else {
            [_peripheral reconnectDevice];
        }
    }
}

- (void)deviceConnect:(EasyPeripheral *)peripheral error:(NSError *)error
{
    NSLog(@"--->deviceConnect %@", self);
    if(!_teardownFlag){
        [self checkService];
    }
}

- (void) checkService
{
    NSLog(@"checkService %@", self);
    [_peripheral discoverAllDeviceServiceWithCallback:^(EasyPeripheral *peripheral, NSArray<EasyService *> *serviceArray, NSError *error) {
        for (EasyService *tempS in serviceArray) {
            if(![UUID_SERVICE isEqualToString:[NSString stringWithFormat:@"%@", tempS.UUID]]){
                [self setNOReady];
                return;
            }
            [tempS discoverCharacteristicWithCallback:^(NSArray<EasyCharacteristic *> *characteristics, NSError *error) {
                NSLog(@"discoverCharacteristicWithCallback: %zi", characteristics.count );
                for (EasyCharacteristic *tempC in characteristics) {
                    NSLog(@"%@  = %@", tempC.name , tempC.UUID );
                    if([UUID_CHARACTERISTIC isEqualToString:[NSString stringWithFormat:@"%@",tempC.UUID]]){
                        [tempC discoverDescriptorWithCallback:^(NSArray<EasyDescriptor *> *descriptorArray, NSError *error) {
                            NSLog(@" descriptorArray Size:%zi, %@", descriptorArray.count, descriptorArray)  ;
//                            if (descriptorArray.count > 0) {
//                                for (EasyDescriptor *d in descriptorArray) {
//                                    NSLog(@"%@ - %@ %@ ", d,d.UUID ,d.value);
//                                }
//                            }
//                            for (EasyDescriptor *desc in descriptorArray) {
//                                [desc readValueWithCallback:^(EasyDescriptor *descriptor, NSError *error) {
//                                    NSLog(@"读取descriptor的值：%@ ,%@ ",descriptor.value,error);
//                                    [self setEasyCharacteristic:tempC isReady:YES];
//                                }];
//                            }
                            [self readTest];//for test read data
                            [self setEasyCharacteristic:tempC isReady:YES];
                        }];
                        return;
                    }
                }
                [self setNOReady];
            }];
        }
    }];
}
- (void) readTest
{
    NSLog(@"read Test start");
    [self.characteristic readValueWithCallback:^(EasyCharacteristic *characteristic, NSData *data, NSError *error) {
        NSLog(@"read finish:%@, error:%@", data, error);
    }];
}
- (BOOL) writeTest
{
    Byte byte[] = {1, 3, 5};
    return [self writeByteData:byte withSize:3];
}

- (BOOL) writeByteData:(Byte[])data withSize:(NSInteger) size
{
    return [self writeData:[[NSData alloc] initWithBytes:data length:size]];
}

- (BOOL) writeData:(NSData *)data
{
    NSLog(@"writeData -->%@, %@" , data, self);
    if(data != nil){
        if([self isReadyToSendData]){
            NSLog(@"writeData start -->%@, %@" , data, self);
            [_characteristic writeValueWithData:data callback:^(EasyCharacteristic *characteristic, NSData *data, NSError *error) {
                NSLog(@"writeData:%@ finish with:%@", data, error);
            }];
        }
    }
    return NO;
}
- (BOOL) isReadyToSendData
{
    if(_characteristic == nil
       || _characteristic.peripheral == nil){
        return NO;
    }
    return [_characteristic.peripheral isConnected];
}

- (BOOL) isPeripheralConnect
{
    if(_peripheral == nil){
        return NO;
    }
    return [_peripheral isConnected];
}

- (BOOL) sendUnBleKeyCode:(UnBleKeyCode)code
{
    return [self writeData:[UnBleDataParser tranformUnBleKeyCodeCommend:code]];
}
////////////////////////////////////////////////////////////////////
- (void) setNOReady
{
    [self setEasyCharacteristic:nil isReady:NO];
}

- (void) setEasyCharacteristic:(EasyCharacteristic*)characteristic isReady:(BOOL) ready
{
    NSLog(@"setReady:%@, %@", (ready?@"YES":@"NO"), self);
    _characteristic = characteristic;
    //if(_isReadyFlag != ready){
        _isReadyFlag = ready;
        [self hideHUB];
        [self notifyReadyStateChange:_isReadyFlag];
    //}
}
//////////////////////////////////////
- (void) showHUB:(NSString*)message
{
    if(_showDialog){
        queueMainStart
        [EFShowView showHUDMsg:message];
        queueEnd
    }
}
- (void) hideHUB
{
    queueMainStart
    [EFShowView HideHud];
    queueEnd
}
//////////////////////////////////////////
- (void) notifyReadyStateChange:(BOOL)flag
{
    if(_unBleEasyPeripheralDelegate){
        [_unBleEasyPeripheralDelegate onEasyUnBleEasyPeripheral:self readyStateChange:flag];
    }
}

@end
