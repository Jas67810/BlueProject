//
//  UnBleUITabView.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef UnBleUITabView_h
#define UnBleUITabView_h

#include "UIScrollView+EmptyDataSet.h"

@interface UnBleUITabView : UITableView<DZNEmptyDataSetSource, DZNEmptyDataSetDelegate>
@property(nonatomic,assign) BOOL showEmptyViewIfNeed;
@end

#endif /* UnBleUITabView_h */
