//
//  UnBleScanUITableView.h
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#ifndef UnBleScanUITableView_h
#define UnBleScanUITableView_h

#include "UnBleUITabView.h"

@interface UnBleScanUITableView : UnBleUITabView<QMUITableViewDataSource, QMUITableViewDelegate>
@property (nonatomic, strong) NSMutableArray * dataScanSource;
///////////////////////////////////////////////////////////////////
- (void) setCurrentEasyCenterManager:(EasyCenterManager*)manager;
@end

#endif /* UnBleScanUITableView_h */
