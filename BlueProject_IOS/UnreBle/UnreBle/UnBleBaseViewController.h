//
//  UnBleBaseViewController.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef UnBleBaseViewController_h
#define UnBleBaseViewController_h

#include "QDCommonViewController.h"
#include "UnBle.h"

typedef NS_ENUM(NSInteger, UIViewControllerState)
{
    STATE_VIEW_UNKNOW = 0,
    STATE_VIEW_DIDLOAD,
    STATE_VIEW_WILLAPPEAR,
    STATE_VIEW_DIDAPPEAR,
    STATE_VIEW_WILLDISAPPEAR,
    STATE_VIEW_DIDDISAPPEAR
};

@interface UnBleBaseViewController : QDCommonViewController
@property(nonatomic,assign) UIViewControllerState mUIViewControllerState;
- (BOOL) isActiveUIViewControllerState;
@property(nonatomic,strong) UnBle * unBle;
//@property (nonatomic,strong)EasyCenterManager * centerManager;
//////////////////////
- (BOOL) isShowSelf;
/////////////////////////////////////
- (void) onManagerStateChanged:(CBManagerState)state;
- (void) onScanEasyPeripheral:(EasyPeripheral*)peripheral andsearchFlagType:(searchFlagType) searchFlagtype;
@end

#endif /* UnBleBaseViewController_h */
