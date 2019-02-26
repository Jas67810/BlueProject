//
//  UnBleScanViewController.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleScanViewController.h"
#import "QDCustomToastAnimator.h"
#include "UnBleScanUITableView.h"

@interface UnBleScanViewController()
@property (nonatomic,strong) NSMutableArray *dataArray;
@property (nonatomic,strong) UnBleScanUITableView * unBleScanUITableView;
@end

@implementation UnBleScanViewController

- (void)setupNavigationItems
{
    [super setupNavigationItems];
    self.title = @"搜索";
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self commitUnBleScanViewControllerInit];
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self scanUnBle:YES];
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self scanUnBle:NO];
}
- (void) commitUnBleScanViewControllerInit
{
    _unBleScanUITableView = [[UnBleScanUITableView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [_unBleScanUITableView setCurrentEasyCenterManager:self.unBle.centerManager];
    _unBleScanUITableView.dataScanSource = self.dataArray;
    [self.view addSubview:_unBleScanUITableView];
}
///////////////////////////////////////////////////
- (NSMutableArray *)dataArray
{
    if (nil == _dataArray) {
        _dataArray  =[NSMutableArray arrayWithCapacity:10];
    }
    return _dataArray ;
}
//////////////////////////////////////////////////
- (void) scanUnBle:(BOOL) enable
{
    NSLog(@"scanUnBle:%@", (enable?@"YES":@"NO"));
    if(enable){
        [self.unBle.centerManager startScanDevice];
    } else {
        [self.unBle.centerManager stopScanDevice];
    }
}
- (void)onScanEasyPeripheral:(EasyPeripheral *)peripheral andsearchFlagType:(searchFlagType)searchType
{
    [super onScanEasyPeripheral:peripheral andsearchFlagType:searchType];
    //NSLog(@"onScanEasyPeripheral:%zi", searchType);
    if (peripheral) {
        kWeakSelf(self)
        if (searchType&searchFlagTypeChanged) {
            NSInteger perpheralIndex = [weakself.dataArray indexOfObject:peripheral];
            [weakself.dataArray replaceObjectAtIndex:perpheralIndex withObject:peripheral];
        }
        else if(searchType&searchFlagTypeAdded){
            [weakself.dataArray addObject:peripheral];
        }
        else if (searchType&searchFlagTypeDisconnect || searchType&searchFlagTypeDelete){
            [weakself.dataArray removeObject:peripheral];
        }
        if(searchType == searchFlagTypeChanged
           || searchType == searchFlagTypeAdded
           || searchType == searchFlagTypeDisconnect
           || searchType == searchFlagTypeDelete){
            queueMainStart
            [weakself.unBleScanUITableView reloadData];
            queueEnd
        }
    }
}
- (void)onManagerStateChanged:(CBManagerState)state
{
    [super onManagerStateChanged:state];
    switch (state) {
        case CBManagerStatePoweredOn:
            [self.unBle.centerManager startScanDevice];
            break;
        default:
            break;
    }
}

@end
