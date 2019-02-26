//
//  CenterCircleView.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "CenterCircleView.h"

#define ImageViewWidth 30

@interface CenterCircleView()
@property(nonatomic,strong)UIImageView * okimageView;
@end

@implementation CenterCircleView

- (instancetype)initWithFrame:(CGRect)frame
{
    if(self = [super initWithFrame:frame]){
        [self commitCenterCircleViewInit];
    }
    return self;
}

- (void)layoutSubviews
{
    NSLog(@"CenterCircleView layoutSubviews");
    [self rebuildArcImage];
    self.okimageView.center = self.center;
    NSLog(@"%@, %@", NSStringFromCGRect(self.okimageView.frame), NSStringFromCGRect(self.frame));
}

- (void) rebuildArcImage
{
    float size = (self.frame.size.width>self.frame.size.height?self.frame.size.height:self.frame.size.width);
    NSLog(@"rebuildArcImage:%f, frame:%@", size, NSStringFromCGRect(self.frame));
    self.image = [self imageWithArc:size/2];
}

- (void) commitCenterCircleViewInit
{
    NSLog(@"commitCenterCircleViewInits");
    self.backgroundColor = UIColor.clearColor;
    [self addSubview:self.okimageView];
}

- (UIImage *)imageWithArc:(float)arc
{
    UIGraphicsBeginImageContextWithOptions(CGSizeMake(arc, arc), NO, 0);
    //2、画大圆
    UIBezierPath *path = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(0, 0, arc, arc)];
    [APP_MAINCOLOR set];
    [path fill];
    UIImage *clipImage = UIGraphicsGetImageFromCurrentImageContext();
    //3、关闭上下文
    UIGraphicsEndImageContext();
    return clipImage;
}

-(UIImageView *)okimageView
{
    if (_okimageView == nil) {
        NSLog(@"okimageView okimageView");
        _okimageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, ImageViewWidth , ImageViewWidth)];
        _okimageView.backgroundColor = [UIColor blackColor];
        // _imageView.userInteractionEnabled = YES;
        _okimageView.image = [UIImage imageNamed:@"icon_ok"];
    }
    return _okimageView;
}

@end
