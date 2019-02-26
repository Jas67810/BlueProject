//
//  UnBle.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef UnBle_h
#define UnBle_h

#import "EasyBlueToothManager.h"
#include "UnBleEasyPeripheral.h"

#define UUID_SERVICE @"Current Time"
#define UUID_CHARACTERISTIC @"Current Time"

@protocol UnBleDelegate <NSObject>
- (void) onScanEasyPeripheral:(EasyPeripheral*)peripheral andsearchFlagType:(searchFlagType) searchFlagtype;
- (void) onManagerStateChanged:(CBManagerState)state;
- (void) onEasyPeripheralReadyStateChange:(BOOL)state;
- (void) onConnectStateChange:(deviceConnectType) type;
@end

@interface UnBle : NSObject
@property(nonatomic,weak)id<UnBleDelegate> unBleDelegate;
@property(nonatomic,strong) EasyBlueToothManager * bleManager;
+ (instancetype) shareInstance;
@property(nonatomic,strong) UnBleEasyPeripheral * unBleEasyPeripheral;
@property (nonatomic,strong)EasyCenterManager * centerManager;
- (BOOL) sendUnBleKeyCode:(UnBleKeyCode)code;
@end

#endif /* UnBle_h */
