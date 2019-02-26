//
//  YaoKong.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "YaoKong.h"
#include "CircleView.h"
#include "CenterCircleView.h"

@interface YaoKong()
@property(nonatomic,strong) CircleView * leftCircleView;
@property(nonatomic,strong) CircleView * rightCircleView;
@property(nonatomic,strong) CircleView * upCircleView;
@property(nonatomic,strong) CircleView * downCircleView;
@property(nonatomic,strong) CenterCircleView * enterUIImageView;
///////////////////////////
@property(nonatomic,assign) CGFloat radius_MAX;
@property(nonatomic,assign) CGFloat radius_MIN;
@end

@implementation YaoKong

- (instancetype)initWithFrame:(CGRect)frame
{
    if(self = [super initWithFrame:frame]){
        _radius_MAX = 150;
        _radius_MIN = 70;
        [self commitYaoKongInit];
    }
    return self;
}


- (void) commitYaoKongInit
{
    NSLog(@"commitYaoKongInit:%@", NSStringFromCGRect(self.frame));
    CGRect rect = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
    _upCircleView = [[CircleView alloc]initWithFrame:rect startAngel:- 0.75 * M_PI endAngel:-0.25 * M_PI bigRadius:_radius_MAX smallRadius:_radius_MIN andUIImageOrientation:UIImageOrientationUp];
    _rightCircleView = [[CircleView alloc]initWithFrame:rect startAngel:- 0.25 * M_PI endAngel:0.25 * M_PI bigRadius:_radius_MAX smallRadius:_radius_MIN andUIImageOrientation:UIImageOrientationRight];
    _downCircleView = [[CircleView alloc]initWithFrame:rect startAngel:0.25 * M_PI endAngel:0.75 * M_PI bigRadius:_radius_MAX smallRadius:_radius_MIN andUIImageOrientation:UIImageOrientationDown];
    _leftCircleView = [[CircleView alloc]initWithFrame:rect startAngel:0.75 * M_PI endAngel:1.25 * M_PI bigRadius:_radius_MAX smallRadius:_radius_MIN andUIImageOrientation:UIImageOrientationLeft];
    _enterUIImageView = [[CenterCircleView alloc] initWithFrame:CGRectMake(0, 0, _radius_MIN*2, _radius_MIN*2)];
    /////////////////////////////////////////////////////////////////////////
    _upCircleView.tag = 100;
    _rightCircleView.tag = 101;
    _downCircleView.tag = 102;
    _leftCircleView.tag = 103;
    _enterUIImageView.tag = 104;
    [self addSubview:_enterUIImageView];
    [self addSubview:_rightCircleView];
    [self addSubview:_downCircleView];
    [self addSubview:_leftCircleView];
    [self addSubview:_upCircleView];
}

- (void)layoutSubviews
{
    CGRect rect = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
    CGSize center = CGSizeMake(self.frame.size.height/2, self.frame.size.height/2);
    _enterUIImageView.center = CGPointMake(center.width, center.height);
    _upCircleView.frame = rect;
    _leftCircleView.frame = rect;
    _downCircleView.frame = rect;
    _rightCircleView.frame = rect;
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesBegan:touches withEvent:event];
    CGPoint point = [touches.allObjects.firstObject locationInView:self];
    if([self getArcByClickPoint:point] < _radius_MIN){
        [self notifyunBleKeyCode:UnBle_KEYCODE_ENTER];
    } else {
        for (CircleView *view in self.subviews) {
            float color = [self getColorWithView:view location:point];
            if(view.tag >= 100 && [view isKindOfClass:[CircleView class]]){
                [(CircleView *)view setBActive:color];
                if(color>=1){
                    UnBleKeyCode code = UnBle_KEYCODE_UNKNOW;
                    switch (view.tag) {
                        case 100:code = UnBle_KEYCODE_UP;break;
                        case 101:code = UnBle_KEYCODE_RIGHT;break;
                        case 102:code = UnBle_KEYCODE_DOWN;break;
                        case 103:code = UnBle_KEYCODE_LEFT;break;
                        default:
                            break;
                    }
                    if(code != UnBle_KEYCODE_UNKNOW){
                        [self notifyunBleKeyCode:code];
                    }
                }
            }
        }
    }
}

- (float) getArcByClickPoint:(CGPoint) point
{
    CGPoint center = self.enterUIImageView.center;
    return sqrtf(powf(point.x-center.x, 2) + powf(point.y-center.y, 2));
}

- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesEnded:touches withEvent:event];
    [self clearColor];
}

- (void)touchesCancelled:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesCancelled:touches withEvent:event];
    [self clearColor];
}

- (void) clearColor
{
    [_leftCircleView setBActive:NO];
    [_rightCircleView setBActive:NO];
    [_downCircleView setBActive:NO];
    [_upCircleView setBActive:NO];
}
//获取颜色
-(CGFloat)getColorWithView:(UIView *)view location:(CGPoint)point
{
    unsigned char pixel[4] = {0};
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef context = CGBitmapContextCreate(pixel, 1, 1, 8, 4, colorSpace, (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    CGContextTranslateCTM(context, -point.x, -point.y);
    [view.layer renderInContext:context];
    CGContextRelease(context);
    CGColorSpaceRelease(colorSpace);
    return (CGFloat)(pixel[0] / 255.0);
}
////////////////////////////////////////////////////////////////
- (void) notifyunBleKeyCode:(UnBleKeyCode) code
{
    if(_yaoKongDelegate){
        [_yaoKongDelegate onYaoKongKeyDown:code];
    }
}

@end
