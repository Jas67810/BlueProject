//
//  UnBleScanUITableView.m
//  UnreBle
//
//  Created by JasWorkSpace on 2019/2/22.
//  Copyright © 2019 JasWorkSpace. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "UnBleScanUITableView.h"
#include "MJRefreshWoodyHeader.h"
#include "UnBleScanCell.h"
#include "UnBleEasyPeripheral.h"
#include "UnBleScanViewController.h"

@interface UnBleScanUITableView()<UnBleEasyPeripheralDelegate>
@property (nonatomic,strong)  EasyCenterManager * centerManager;
@end

@implementation UnBleScanUITableView

- (instancetype)initWithFrame:(CGRect)frame
{
    if(self = [super initWithFrame:frame]){
        [self commitUnBleScanUITableViewInit];
    }
    return self;
}

- (void) commitUnBleScanUITableViewInit
{
    self.dataSource = self;
    self.delegate = self;
    //self.qmui_cacheCellHeightByKeyAutomatically = YES;
    [self registerClass:[UnBleScanCell class] forCellReuseIdentifier:@"cellID"];
    [self setupUI];
}


- (void)setCenterManager:(EasyCenterManager *)centerManager
{
    
}

- (void) setupUI
{
    self.showEmptyViewIfNeed = NO;
    MJRefreshWoodyHeader * header = [MJRefreshWoodyHeader headerWithRefreshingBlock:^{
        
    }];
    [header.kafkaReplicatorLayer setTintColor:APP_MAINCOLOR];
    self.mj_header = nil;
    //[self.mj_header beginRefreshing];
}
///////////////////////////////////////////////////////////////////
- (void) setCurrentEasyCenterManager:(EasyCenterManager*)manager
{
    _centerManager = manager;
}
/////////////////////////////////////////////////////////////////////
//- (id<NSCopying>)qmui_tableView:(UITableView *)tableView cacheKeyForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    // 返回一个用于标记当前 cell 高度的 key，只要 key 不变，高度就不会重新计算，所以建议将有可能影响 cell 高度的数据字段作为 key 的一部分（例如 username、content.md5 等），这样当数据发生变化时，只要触发 cell 的渲染，高度就会自动更新
//    return [self.dataScanSource objectAtIndex:indexPath];
//}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataScanSource.count;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self qmui_clearsSelection];
    [self connectEasyPeripheral:[_dataScanSource objectAtIndex:indexPath.row]];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UnBleScanCell *cell = (UnBleScanCell *)[tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (!cell) {
        cell = [[UnBleScanCell alloc] initForTableView:tableView withReuseIdentifier:@"cellID"];
    }
    [cell bindEasyPeripheral:[_dataScanSource objectAtIndex:indexPath.row]];
    //cell.separatorInset = UIEdgeInsetsZero;
    //[cell updateCellAppearanceWithIndexPath:indexPath];
    return cell;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 100.f;
}
//////////////////////////////////////////////////////////////////////////
- (void) connectEasyPeripheral:(EasyPeripheral*)easyPeripheral
{
    UnBleEasyPeripheral * per = [[UnBleEasyPeripheral alloc] initWithEasyPeripheral:easyPeripheral];
    per.unBleEasyPeripheralDelegate = self;
    per.showDialog = YES;
    [per startConnect];
}
////////////////////////////////////////////////////
- (void) onEasyUnBleEasyPeripheral:(UnBleEasyPeripheral*) peripheral readyStateChange:(BOOL) state
{
    NSLog(@"TabView onEasyUnBleEasyPeripheral:%@, state:%@", peripheral, (state?@"YES":@"NO"));
    peripheral.unBleEasyPeripheralDelegate = nil;
    if(peripheral.showDialog){
        peripheral.showDialog = NO;
        if(state){
            [UnBle shareInstance].unBleEasyPeripheral = peripheral;
            queueMainStart
//            [peripheral writeTest];
//            [peripheral sendUnBleKeyCode:UnBle_KEYCODE_POWER];
//            [peripheral sendUnBleKeyCode:UnBle_KEYCODE_DOWN];
//            [self fortest];
            UIViewController *vc = [EasyUtils topViewController];
            if([vc isKindOfClass:[UnBleScanViewController class]]){
                [vc.navigationController popViewControllerAnimated:YES];
            }
            queueEnd
            return;
        }
    }
    [peripheral tearDown];
}
- (void) onConnectStateChange:(deviceConnectType)type
{
    
}
- (void) fortest
{
    //[[[NSThread alloc] initWithTarget:self selector:@selector(startsyncTranform) object:nil] start];
    [NSTimer scheduledTimerWithTimeInterval:7 target:self selector:@selector(startsyncTranform) userInfo:nil repeats:YES];
}

- (void) startsyncTranform
{
    UnBleEasyPeripheral * peripheral = [UnBle shareInstance].unBleEasyPeripheral;
    if(peripheral != nil){
        NSLog(@"startsyncTranform start");
        [peripheral writeTest];
        [peripheral sendUnBleKeyCode:UnBle_KEYCODE_POWER];
        [peripheral sendUnBleKeyCode:UnBle_KEYCODE_DOWN];
    } else {
        NSLog(@"startsyncTranform fail");
    }
}
@end
