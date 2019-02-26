//
//  YaoKong.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef YaoKong_h
#define YaoKong_h
#include "UnBleDataParser.h"

@protocol YaoKongDelegate<NSObject>
- (void) onYaoKongKeyDown:(UnBleKeyCode)code;
@end
@interface YaoKong : UIView
@property(nonatomic,weak)id<YaoKongDelegate> yaoKongDelegate;
@end

#endif /* YaoKong_h */
