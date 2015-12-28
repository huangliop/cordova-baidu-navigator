//
//  BaiduNaviViewController.h
//  BaiduTest
//
//  Created by sope on 15/7/31.
//
//

#import <UIKit/UIKit.h>
#import "BNRoutePlanModel.h"
#import "BNCoreServices.h"

@interface BaiduNaviViewController : UIViewController<BNNaviUIManagerDelegate,BNNaviRoutePlanDelegate>


//导航类型，分为模拟导航和真实导航
@property (assign, nonatomic) BN_NaviType naviType;

- (void)simulateNavi;
- (void)realNavi;
-(void)startNavi:(BNRoutePlanNode *)startNode andEndNode:(BNRoutePlanNode *)endNode;

@end
