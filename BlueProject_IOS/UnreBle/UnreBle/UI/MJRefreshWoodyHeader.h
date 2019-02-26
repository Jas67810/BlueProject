//
//  MJRefreshWoodyHeader.h
//  UnretekFace3D
//
//  Created by JasWorkSpace on 2018/10/30.
//  Copyright © 2018 Unre（Shanghai）Information Technology Co., Ltd. All rights reserved.
//

#ifndef MJRefreshWoodyHeader_h
#define MJRefreshWoodyHeader_h

#import "MJRefresh.h"
#import "KafkaReplicatorLayer.h"

@interface MJRefreshWoodyHeader : MJRefreshHeader
@property(nonatomic,strong) KafkaReplicatorLayer * kafkaReplicatorLayer;
@end

#endif /* MJRefreshWoodyHeader_h */
