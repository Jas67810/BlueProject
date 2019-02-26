//
//  YaoKongPanel.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/25.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef YaoKongPanel_h
#define YaoKongPanel_h

#include "UnBleDataParser.h"

@protocol YaoKongPanelDelegate<NSObject>
- (void) onYaoKongKeyDown:(UnBleKeyCode)code;
@end
@interface YaoKongPanel : UIView
@property(nonatomic,weak)id<YaoKongPanelDelegate> yaoKongPanelDelegate;
@end

#endif /* YaoKongPanel_h */
