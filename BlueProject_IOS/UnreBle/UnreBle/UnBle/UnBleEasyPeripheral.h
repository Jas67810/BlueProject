//
//  UnBleEasyPeripheral.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef UnBleEasyPeripheral_h
#define UnBleEasyPeripheral_h

#include "EasyPeripheral.h"
#include "UnBleDataParser.h"

@class UnBleEasyPeripheral;
@protocol UnBleEasyPeripheralDelegate;

@protocol UnBleEasyPeripheralDelegate<NSObject>
- (void) onEasyUnBleEasyPeripheral:(UnBleEasyPeripheral*) peripheral readyStateChange:(BOOL) state;
- (void) onConnectStateChange:(deviceConnectType) type;
@end

@interface UnBleEasyPeripheral : NSObject
@property(nonatomic,weak)id<UnBleEasyPeripheralDelegate> unBleEasyPeripheralDelegate;
@property (nonatomic,strong)EasyPeripheral *peripheral ;
@property(nonatomic,assign)BOOL showDialog;
- (instancetype)initWithEasyPeripheral:(EasyPeripheral*)easy;
- (BOOL) startConnect;
- (void) tearDown;
////////////
- (BOOL) sendUnBleKeyCode:(UnBleKeyCode)code;
- (void) readTest;
- (BOOL) writeTest;
- (BOOL) writeByteData:(Byte[])data withSize:(NSInteger) size;
- (BOOL) writeData:(NSData *)data;
////////////////
- (BOOL) isReadyToSendData;
- (BOOL) isPeripheralConnect;
@end

#endif /* UnBleEasyPeripheral_h */
