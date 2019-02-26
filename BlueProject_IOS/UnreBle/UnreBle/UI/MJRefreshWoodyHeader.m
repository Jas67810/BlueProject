//
//  MJRefreshWoodyHeader.m
//  UnretekFace3D
//
//  Created by JasWorkSpace on 2018/10/30.
//  Copyright © 2018 Unre（Shanghai）Information Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "MJRefreshWoodyHeader.h"


@interface MJRefreshWoodyHeader()
@end

@implementation MJRefreshWoodyHeader

- (void)prepare
{
    [super prepare];
    // 设置控件的高度
    self.mj_h = 50;
    [self.layer addSublayer:_kafkaReplicatorLayer];
}

-(void)setFrame:(CGRect)frame
{
    [super setFrame:frame];
    self.kafkaReplicatorLayer.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
}

- (KafkaReplicatorLayer *)kafkaReplicatorLayer
{
    if(!_kafkaReplicatorLayer){
        _kafkaReplicatorLayer = [KafkaReplicatorLayer new];
        _kafkaReplicatorLayer.animationStyle = KafkaReplicatorLayerAnimationStyleWoody;
    }
    return _kafkaReplicatorLayer;
}

#pragma mark 监听scrollView的contentOffset改变
- (void)scrollViewContentOffsetDidChange:(NSDictionary *)change
{
    [super scrollViewContentOffsetDidChange:change];
    
}

#pragma mark 监听scrollView的contentSize改变
- (void)scrollViewContentSizeDidChange:(NSDictionary *)change
{
    [super scrollViewContentSizeDidChange:change];
    
}

#pragma mark 监听scrollView的拖拽状态改变
- (void)scrollViewPanStateDidChange:(NSDictionary *)change
{
    [super scrollViewPanStateDidChange:change];
    
}

#pragma mark 监听控件的刷新状态
- (void)setState:(MJRefreshState)state
{
    MJRefreshCheckState;
    
    switch (state) {
        case MJRefreshStateIdle:
            [self.kafkaReplicatorLayer stopAnimating];
            break;
        case MJRefreshStatePulling:
            [self.kafkaReplicatorLayer stopAnimating];
            break;
        case MJRefreshStateRefreshing:
            [self.kafkaReplicatorLayer startAnimating];
            break;
        default:
            break;
    }
}

#pragma mark 监听拖拽比例（控件被拖出来的比例）
- (void)setPullingPercent:(CGFloat)pullingPercent
{
    [super setPullingPercent:pullingPercent];
    // 1.0 0.5 0.0
    // 0.5 0.0 0.5
    CGFloat red = 1.0 - pullingPercent * 0.5;
    CGFloat green = 0.5 - 0.5 * pullingPercent;
    CGFloat blue = 0.5 * pullingPercent;
    //self.label.textColor = [UIColor colorWithRed:red green:green blue:blue alpha:1.0];
}

@end
