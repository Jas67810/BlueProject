//
//  UnBleBaseViewController.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleBaseViewController.h"
#include "QDCustomToastAnimator.h"

@interface UnBleBaseViewController()<UnBleDelegate>
@end

@implementation UnBleBaseViewController

- (instancetype) init
{
    if(self = [super init]){
        _mUIViewControllerState = STATE_VIEW_UNKNOW;
    }
    return self;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    //[self configCenterManager:YES];
}
- (void)viewWillAppear:(BOOL)animated
{
    //LogDebug(@"viewWillAppear base");
    [super viewWillAppear:animated];
    [UIViewController attemptRotationToDeviceOrientation];
    _mUIViewControllerState = STATE_VIEW_WILLAPPEAR;
}
- (void)viewDidAppear:(BOOL)animated
{
    //LogDebug(@"viewDidAppear base");
    [super viewDidAppear:animated];
    _mUIViewControllerState = STATE_VIEW_DIDAPPEAR;
    //[self autoClearControllerParams];
    NSLog(@"BaseViewCOntroller viewDidAppear");
    self.unBle.unBleDelegate = self;
}
-(void)viewWillDisappear:(BOOL)animated
{
    //LogDebug(@"viewWillDisappear base");
    [super viewWillDisappear:animated];
    _mUIViewControllerState = STATE_VIEW_WILLDISAPPEAR;
}
- (void) viewDidDisappear:(BOOL)animated
{
    //LogDebug(@"viewDidDisappear base");
    [super viewDidDisappear:animated];
    _mUIViewControllerState = STATE_VIEW_DIDDISAPPEAR;
}
- (BOOL) isActiveUIViewControllerState
{
    return (_mUIViewControllerState == STATE_VIEW_DIDLOAD
            || _mUIViewControllerState == STATE_VIEW_DIDAPPEAR
            || _mUIViewControllerState == STATE_VIEW_WILLAPPEAR);
}
///////////////////////////////////////////////////
- (UnBle *)unBle
{
    if(_unBle == nil){
        NSLog(@"UnBle shareInstance");
        _unBle = [UnBle shareInstance];
    }
    return _unBle;
}
//////////////////////////////////////////////////////////////////////
- (void) onScanEasyPeripheral:(EasyPeripheral*)peripheral andsearchFlagType:(searchFlagType) searchFlagtype
{
    //NSLog(@"onScanEasyPeripheral:%zi, %@", searchFlagtype, peripheral);
}
- (void) onManagerStateChanged:(CBManagerState)state
{
    NSLog(@"onManagerStateChanged:%zi", state);
    queueMainStart
    if (state == CBManagerStatePoweredOn) {
        UIViewController *vc = [EasyUtils topViewController];
        if ([vc isKindOfClass:[self class]]) {
            //[self.centerManager startScanDevice];
        }
    } else if (state == CBManagerStatePoweredOff){
        QMUITips *tips = [QMUITips createTipsToView:self.view];
        QDCustomToastAnimator *customAnimator = [[QDCustomToastAnimator alloc] initWithToastView:tips];
        tips.toastAnimator = customAnimator;
        [tips showInfo:@"" detailText:@"系统蓝牙已关闭，请打开系统蓝牙" hideAfterDelay:2];
    } else if (state == CBManagerStateUnsupported){
        QMUITips *tips = [QMUITips createTipsToView:self.view];
        QDCustomToastAnimator *customAnimator = [[QDCustomToastAnimator alloc] initWithToastView:tips];
        tips.toastAnimator = customAnimator;
        [tips showError:@"" detailText:@"系统蓝牙不支持，请检查硬件设备" hideAfterDelay:2];
    } else if (state == CBManagerStateUnauthorized){
        QMUITips *tips = [QMUITips createTipsToView:self.view];
        QDCustomToastAnimator *customAnimator = [[QDCustomToastAnimator alloc] initWithToastView:tips];
        tips.toastAnimator = customAnimator;
        [tips showInfo:@"" detailText:@"需要系统蓝牙权限" hideAfterDelay:2];
    }
    queueEnd
}
- (void) onEasyPeripheralReadyStateChange:(BOOL)state
{
    
}
- (void) onConnectStateChange:(deviceConnectType) type
{
    
}
/////////////////////////////////////////
- (BOOL) isShowSelf
{
    UIViewController *vc = [EasyUtils topViewController];
    return [vc isKindOfClass:[self class]];
}
///////////////////////////////////////////////////////////
@end
