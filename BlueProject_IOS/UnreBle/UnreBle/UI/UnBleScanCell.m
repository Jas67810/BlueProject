//
//  UnBleScanCell.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleScanCell.h"

@interface UnBleScanCell()
@property(nonatomic,strong) FlexFrameView * rootCellView;
@property(nonatomic, strong) UIImageView *icon;
@property(nonatomic, strong) UILabel *names;
@property(nonatomic, strong) UILabel *mac;
@property(nonatomic, strong) UILabel *other;
@property(nonatomic, strong) UILabel *rssi;
@end

@implementation UnBleScanCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if(self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]){
        [self commitUnBleScanCellInit];
    }
    return self;
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if(self = [super initWithFrame:frame]){
        [self commitUnBleScanCellInit];
    }
    return self;
}

- (void) commitUnBleScanCellInit
{
    self.backgroundColor = UIColor.clearColor;
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    [self setupRootView];
    [self addSubview:self.rootCellView];
}
////////////////////////////////////////////////////////
- (void) setupRootView
{
    self.rootCellView = [[FlexFrameView alloc] initWithFlex:@"scancell"
                                                      Frame:CGRectMake(0, 0, ScreenWidth, 100)
                                                      Owner:self];
    [self.rootCellView layoutIfNeeded];
    //UIImage *afterImage = [icon.image qmui_imageWithTintColor:[QDCommonUI randomThemeColor]];
    [_icon setImage:[_icon.image qmui_imageWithTintColor:APP_MAINCOLOR]];
}

- (void) bindEasyPeripheral:(EasyPeripheral*)easyPeripheral
{
    _names.text = easyPeripheral.name;
    _mac.text = easyPeripheral.identifierString;
    _rssi.text =  easyPeripheral.RSSI.stringValue;
}
@end
