//
//  YaoKongPanel.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "YaoKongPanel.h"
#include "YaoKong.h"

@interface YaoKongPanel()<YaoKongDelegate>
@property(nonatomic,strong) FlexFrameView * rootView;
@property(nonatomic,strong) UIImageView * power;
@property(nonatomic,strong) UIImageView * volueup;
@property(nonatomic,strong) UIImageView * voluedown;
@property(nonatomic,strong) UIImageView * menu;
@property(nonatomic,strong) UIImageView * home;
@property(nonatomic,strong) UIImageView * back;
@property(nonatomic,strong) YaoKong * yaokong;
@end

@implementation YaoKongPanel

- (instancetype)initWithFrame:(CGRect)frame
{
    if(self = [super initWithFrame:frame]){
        [self commitYaoKongPanelInit];
    }
    return self;
}

- (void) commitYaoKongPanelInit
{
    NSLog(@"commitYaoKongPanelInit:%@", NSStringFromCGRect(self.frame));
    _rootView = [[FlexFrameView alloc] initWithFlex:@"yaokongqi" Frame:self.frame Owner:self];
    [self addSubview:_rootView];
    [_rootView layoutIfNeeded];
    _power.image = [_power.image qmui_imageWithTintColor:APP_MAINCOLOR];
    _voluedown.image = [_voluedown.image qmui_imageWithTintColor:APP_MAINCOLOR];
    _volueup.image = [_volueup.image qmui_imageWithTintColor:APP_MAINCOLOR];
    _menu.image = [_menu.image qmui_imageWithTintColor:APP_MAINCOLOR];
    _home.image = [_home.image qmui_imageWithTintColor:APP_MAINCOLOR];
    _back.image = [_back.image qmui_imageWithTintColor:APP_MAINCOLOR];
    _yaokong.yaoKongDelegate = self;
}
/////////////////////////////////////////////////////////////////////////
- (void) onPower
{
    [self notifyYAOKONGKeyDown:UnBle_KEYCODE_POWER];
}
- (void) onVolueUp
{
    [self notifyYAOKONGKeyDown:UnBle_KEYCODE_VOLUMEUP];
}
- (void) onVolueDown
{
    [self notifyYAOKONGKeyDown:UnBle_KEYCODE_VOLUMEDOWN];
}
- (void) onMenu
{
    [self notifyYAOKONGKeyDown:UnBle_KEYCODE_MENU];
}
- (void) onHome
{
    [self notifyYAOKONGKeyDown:UnBle_KEYCODE_HOME];
}
- (void) onBack
{
    [self notifyYAOKONGKeyDown:UnBle_KEYCODE_BACK];
}

- (void) onYaoKongKeyDown:(UnBleKeyCode)code
{
    [self notifyYAOKONGKeyDown:code];
}

- (void) notifyYAOKONGKeyDown:(UnBleKeyCode) code
{
    if(_yaoKongPanelDelegate){
        [_yaoKongPanelDelegate onYaoKongKeyDown:code];
    }
}

@end
