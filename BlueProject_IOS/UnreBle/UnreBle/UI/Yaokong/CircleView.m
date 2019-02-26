//
//  CircleView.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CircleView.h"
#define ImageViewWidth 30

@interface CircleView ()
@property(nonatomic,assign) CGFloat startAngel,endAngel,bigRadius,smallRadius;
@property(nonatomic,strong) UIColor *fillColor;
@property(nonatomic,assign) BOOL bActive;
@property(nonatomic,assign) UIImageOrientation arrOrientation;
@end
@implementation CircleView

-(instancetype)initWithFrame:(CGRect)frame startAngel:(CGFloat)startAngel endAngel:(CGFloat)endAngel bigRadius:(CGFloat)bigRadius smallRadius:(CGFloat)smallRadius andUIImageOrientation:(UIImageOrientation)ori
{
    self = [super initWithFrame:frame];
    if (self) {
        self.fillColor = [UIColor whiteColor];
        self.startAngel = startAngel;
        self.endAngel = endAngel;
        self.bigRadius = bigRadius;
        self.smallRadius = smallRadius;
        self.arrOrientation = ori;
        self.backgroundColor = UIColor.clearColor;
        [self addSubview:self.imageView];
    }
    return self;
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    CGPoint point = self.center;
    CGFloat rValue = (self.bigRadius - self.smallRadius) / 2 + self.smallRadius;
    CGFloat radian = (self.endAngel + self.startAngel) / 2;
    _imageView.center = CGPointMake(point.x + rValue * cosf(radian), point.y + rValue * sinf(radian));
}
-(UIImageView *)imageView
{
    if (_imageView == nil) {
        _imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, ImageViewWidth , ImageViewWidth)];
        // _imageView.backgroundColor = [UIColor blackColor];
        // _imageView.userInteractionEnabled = YES;
        _imageView.image = [[UIImage imageNamed:@"arrer"] qmui_imageWithOrientation:self.arrOrientation];
    }
    return _imageView;
}

-(void)setCircleColor:(UIColor *)fillColor
{
    self.fillColor = fillColor;
    [self setNeedsDisplay];
}

-(void)drawRect:(CGRect)rect
{
    CGPoint center = CGPointMake(self.frame.size.width / 2, self.frame.size.height / 2);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetStrokeColorWithColor(context, APP_MAINCOLOR.CGColor);
    CGContextSetLineWidth(context, 1);
    CGContextSetFillColorWithColor(context, self.fillColor.CGColor);
    CGContextAddArc(context, self.frame.size.width / 2, self.frame.size.height / 2, self.bigRadius, self.startAngel, self.endAngel, 0);
    CGContextAddLineToPoint(context, self.smallRadius * cosf(self.endAngel) + center.x, center.y + self.smallRadius * sinf(self.endAngel));
    CGContextAddArc(context, self.frame.size.width / 2, self.frame.size.height / 2, self.smallRadius, self.endAngel, self.startAngel, 1);
    CGContextAddLineToPoint(context, center.x + self.bigRadius * cosf(self.startAngel), center.y + self.bigRadius * sinf(self.startAngel));
    CGContextDrawPath(context, kCGPathFillStroke);
}

#pragma mark - touch
//- (void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touchesBegan");
//    [super touchesBegan:touches withEvent:event];
//    //self.bActive = YES;
//}
//- (void) touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touchesMoved");
//    [super touchesMoved:touches withEvent:event];
//}
//- (void) touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touchesEnded");
//    [super touchesEnded:touches withEvent:event];
//    //self.bActive = NO;
//}
//- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    //NSLog(@"touchesCancelled");
//    [super touchesCancelled:touches withEvent:event];
//    //self.bActive = NO;
//}

- (void)setBActive:(BOOL)bActive
{
    //NSLog(@"setBActive:%d", bActive);
    if(_bActive != bActive){
        _bActive = bActive;
        [self setCircleColor:(_bActive?APP_MAINCOLOR:UIColor.whiteColor)];
    }
}

@end
