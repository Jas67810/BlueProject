//
//  UnBleMainViewController.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleMainViewController.h"
#include "UnBleScanViewController.h"
#include "YaoKongPanel.h"

@interface UnBleMainViewController()<YaoKongPanelDelegate>
@property(nonatomic,strong) YaoKongPanel * yaoKongPanel;
@end


@implementation UnBleMainViewController

- (void)setupNavigationItems
{
    [super setupNavigationItems];
    self.title = @"遥控";
    self.navigationItem.rightBarButtonItem = [UIBarButtonItem qmui_itemWithTitle:@"切换" target:self action:@selector(handleNavigationItemSwitchEvent)];
}

- (void) handleNavigationItemSwitchEvent
{
    NSLog(@"handleNavigationItemSwitchEvent");
    [self.navigationController pushViewController:[UnBleScanViewController new] animated:YES];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupUI];
}
/////////////////////////
- (void) setupUI
{
    NSLog(@"setupUI");
    _yaoKongPanel = [[YaoKongPanel alloc] initWithFrame:self.view.frame];
    _yaoKongPanel.yaoKongPanelDelegate = self;
    [self.view addSubview:_yaoKongPanel];
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    //[self checkBleState];
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self checkBleState];
}
////////////////////////////////
- (void) onYaoKongKeyDown:(UnBleKeyCode)code
{
    BOOL result = [self.unBle sendUnBleKeyCode:code];
    NSLog(@"onYaoKongKeyDown:%@, %@", [UnBleDataParser tranformUnBleKeyCodeToNSString:code], (result?@"YES":@"NO"));
}

- (void) checkBleState
{
    [self updateYaoKongPanel];
}

- (void) updateYaoKongPanel
{
    BOOL enable = NO;
    if(self.unBle.unBleEasyPeripheral != nil){
        if([self.unBle.unBleEasyPeripheral isReadyToSendData]){
            enable = YES;
        }
    }
    self.yaoKongPanel.userInteractionEnabled = enable;
}


@end
