//
//  UnBleUITabView.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleUITabView.h"

@interface UnBleUITabView()
@end

@implementation UnBleUITabView

- (instancetype)initWithFrame:(CGRect)frame
{
    if(self = [super initWithFrame:frame]){
        [self commitUnBleUITabViewInit];
    }
    return self;
}

- (void) commitUnBleUITabViewInit
{
    self.backgroundColor = UIColor.clearColor;
    self.tableFooterView = [UIView new];
    self.showEmptyViewIfNeed = YES;
    self.emptyDataSetSource = self;
    self.emptyDataSetDelegate = self;
}


//////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - DZNEmptyDataSetSource Methods

- (UIColor *)backgroundColorForEmptyDataSet:(UIScrollView *)scrollView
{
    return [UIColor clearColor];
}

- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView
{
    return -64.0;
}

#pragma mark - DZNEmptyDataSetDelegate Methods

- (BOOL)emptyDataSetShouldDisplay:(UIScrollView *)scrollView
{
    return _showEmptyViewIfNeed;
}

- (BOOL)emptyDataSetShouldAllowTouch:(UIScrollView *)scrollView
{
    return YES;
}

- (BOOL)emptyDataSetShouldAllowScroll:(UIScrollView *)scrollView
{
    return NO;
}

- (nullable UIImage *)imageForEmptyDataSet:(UIScrollView *)scrollView
{
    return [UIImage imageNamed:@"history_empty"];
}

- (void)emptyDataSet:(UIScrollView *)scrollView didTapView:(UIView *)view
{
    NSLog(@"%s",__FUNCTION__);
}

- (void)emptyDataSet:(UIScrollView *)scrollView didTapButton:(UIButton *)button
{
    NSLog(@"emptyDataSet didTapButton");
}

- (void)emptyDataSetWillAppear:(UIScrollView *)scrollView
{
    NSLog(@"emptyDataSetWillAppear");
}

- (void)emptyDataSetDidAppear:(UIScrollView *)scrollView
{
    NSLog(@"emptyDataSetDidAppear");
}

- (void)emptyDataSetWillDisappear:(UIScrollView *)scrollView
{
    NSLog(@"emptyDataSetWillDisappear");
}

- (void)emptyDataSetDidDisappear:(UIScrollView *)scrollView
{
    NSLog(@"emptyDataSetDidDisappear");
}


@end
