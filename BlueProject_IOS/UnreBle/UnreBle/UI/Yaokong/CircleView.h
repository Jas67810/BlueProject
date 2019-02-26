//
//  CircleView.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef CircleView_h
#define CircleView_h

@interface CircleView : UIView

-(instancetype)initWithFrame:(CGRect)frame startAngel:(CGFloat)startAngel endAngel:(CGFloat)endAngel bigRadius:(CGFloat )bigRadius smallRadius:(CGFloat)smallRadius andUIImageOrientation:(UIImageOrientation)ori;
- (void)setCircleColor:(UIColor *)fillColor;
- (void)setBActive:(BOOL)bActive;
@property(nonatomic,strong)UIImageView *imageView;
@property(nonatomic,copy)void(^handleTap)(NSInteger tag);
@end

#endif /* CircleView_h */
