//
//  UnBle.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBle.h"

@interface UnBle()<UnBleEasyPeripheralDelegate>
@end

@implementation UnBle

+ (instancetype)shareInstance
{
    static UnBle *share = nil ;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        share = [[UnBle alloc]init];
    });
    return share;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        [self configUnBle];
    }
    return self;
}

- (void) configUnBle
{
    NSLog(@"-------- configUnBle ---------");
    dispatch_queue_t queue = dispatch_queue_create("com.unreBluetooth.queue", 0);
    NSDictionary *managerDict = @{CBCentralManagerOptionShowPowerAlertKey:@YES};
    NSDictionary *scanDict = @{CBCentralManagerScanOptionAllowDuplicatesKey: @YES };
    NSDictionary *connectDict = @{CBConnectPeripheralOptionNotifyOnConnectionKey:@YES,CBConnectPeripheralOptionNotifyOnDisconnectionKey:@YES,CBConnectPeripheralOptionNotifyOnNotificationKey:@YES};
    
    EasyManagerOptions *options = [[EasyManagerOptions alloc]initWithManagerQueue:queue managerDictionary:managerDict scanOptions:scanDict scanServiceArray:nil connectOptions:connectDict];
    options.scanTimeOut = 6 ;
    options.connectTimeOut = 5 ;
    options.autoConnectAfterDisconnect = YES ;
    
    [EasyBlueToothManager shareInstance].managerOptions = options ;
    ////////////
    kWeakSelf(self)
    [self.centerManager scanDeviceWithTimeInterval:NSIntegerMax services:nil options:@{ CBCentralManagerScanOptionAllowDuplicatesKey: @YES }  callBack:^(EasyPeripheral *peripheral, searchFlagType searchType) {
        if(weakself.unBleDelegate){
            [weakself.unBleDelegate onScanEasyPeripheral:peripheral andsearchFlagType:searchType];
        }
    }];
    self.centerManager.stateChangeCallback = ^(EasyCenterManager *manager, CBManagerState state) {
        if(weakself.unBleDelegate){
            [weakself.unBleDelegate onManagerStateChanged:state];
        }
    };
}
//////////////////////
- (void)setUnBleEasyPeripheral:(UnBleEasyPeripheral *)unBleEasyPeripheral
{
    NSLog(@"UnBle setUnBleEasyPeripheral:%@", unBleEasyPeripheral);
    if(_unBleEasyPeripheral != unBleEasyPeripheral){
        if(nil != _unBleEasyPeripheral){
            NSLog(@"_unBleEasyPeripheral:%@", _unBleEasyPeripheral);
            _unBleEasyPeripheral.unBleEasyPeripheralDelegate = nil;
            [_unBleEasyPeripheral tearDown];
        }
        _unBleEasyPeripheral = unBleEasyPeripheral;
        _unBleEasyPeripheral.unBleEasyPeripheralDelegate = self;
    }
}
///////////////
- (void) onEasyUnBleEasyPeripheral:(UnBleEasyPeripheral*) peripheral readyStateChange:(BOOL) state
{
    if(_unBleDelegate){
        [_unBleDelegate onEasyPeripheralReadyStateChange:state];
    }
}

- (void) onConnectStateChange:(deviceConnectType) type
{
    if(_unBleDelegate){
        [_unBleDelegate onConnectStateChange:type];
    }
}
- (BOOL) sendUnBleKeyCode:(UnBleKeyCode)code
{
    if(_unBleEasyPeripheral != nil){
        return [_unBleEasyPeripheral sendUnBleKeyCode:code];
    }
    return NO;
}
/////////////////////////////////////////////
- (EasyCenterManager *)centerManager
{
    if (nil == _centerManager) {
        dispatch_queue_t queue = dispatch_queue_create("com.unreBluetootth.demo", 0);
        _centerManager = [[EasyCenterManager alloc]initWithQueue:queue options:nil];
    }
    return _centerManager ;
}

- (EasyBlueToothManager *) bleManager
{
    if(_bleManager == nil){
        _bleManager = [EasyBlueToothManager shareInstance];
    }
    return _bleManager;
}
////////////////////////////////////////////////////
@end
