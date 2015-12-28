//
//  BaiduNaviViewController.m
//  BaiduTest
//
//  Created by sope on 15/7/31.
//
//

#import "BaiduNaviViewController.h"

@implementation BaiduNaviViewController

- (void)loadView{
    [super loadView];
    
    self.view.backgroundColor = [UIColor blackColor];
}

-(BOOL)checkServicesInited{
    if(![BNCoreServices_Instance isServicesInited])
    {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                            message:@"引擎尚未初始化完成，请稍后再试"
                                                           delegate:nil
                                                  cancelButtonTitle:@"我知道了"
                                                  otherButtonTitles:nil];
        [alertView show];
        return NO;
    }
    return YES;
}

//模拟导航
- (void)simulateNavi
{
    if (![self checkServicesInited]) return;
    _naviType = BN_NaviTypeSimulator;
    [self startNavi];
}

//真实GPS导航
- (void)realNavi
{
    if (![self checkServicesInited]) return;
    _naviType = BN_NaviTypeReal;
    [self startNavi];
    //[BNCoreServices_UI showNaviUI:BN_NaviTypeReal delegete:self isNeedLandscape:YES];
}

- (void)digitDog:(UIButton*)button
{
    if (![self checkServicesInited]) return;
    [BNCoreServices_UI showDigitDogUI:YES delegete:self];
}

-(void)startNavi:(BNRoutePlanNode *)startNode andEndNode:(BNRoutePlanNode *)endNode{
    
    if (![self checkServicesInited]) return;
    
    NSMutableArray *nodesArray = [[NSMutableArray alloc]initWithCapacity:2];
    [nodesArray addObject:startNode];
    [nodesArray addObject:endNode];
    
    [BNCoreServices_RoutePlan startNaviRoutePlan:BNRoutePlanMode_Highway naviNodes:nodesArray time:nil delegete:self userInfo:nil];
}


- (void)startNavi
{
    
    NSMutableArray *nodesArray = [[NSMutableArray alloc]initWithCapacity:2];
    //起点 传入的是原始的经纬度坐标，若使用的是百度地图坐标，可以使用BNTools类进行坐标转化
    BNRoutePlanNode *startNode = [[BNRoutePlanNode alloc] init];
    startNode.pos = [[BNPosition alloc] init];
    startNode.pos.x = 116.426174;
    startNode.pos.y = 40.068405;
    startNode.pos.eType = BNCoordinate_BaiduMapSDK;
    [nodesArray addObject:startNode];
    
    //也可以在此加入1到3个的途经点
    
    //终点
    BNRoutePlanNode *endNode = [[BNRoutePlanNode alloc] init];
    endNode.pos = [[BNPosition alloc] init];
    endNode.pos.x = 116.444934;
    endNode.pos.y = 40.05663;
    endNode.pos.eType = BNCoordinate_BaiduMapSDK;
    [nodesArray addObject:endNode];
    
    [BNCoreServices_RoutePlan startNaviRoutePlan:BNRoutePlanMode_Highway naviNodes:nodesArray time:nil delegete:self userInfo:nil];
}


#pragma mark - BNNaviRoutePlanDelegate
//算路成功回调
-(void)routePlanDidFinished:(NSDictionary *)userInfo
{
    NSLog(@"算路成功");
    //路径规划成功，开始导航
    [BNCoreServices_UI showNaviUI:_naviType delegete:self isNeedLandscape:YES];
}

//算路失败回调
- (void)routePlanDidFailedWithError:(NSError *)error andUserInfo:(NSDictionary *)userInfo
{
    NSLog(@"算路失败");
    if ([error code] == BNRoutePlanError_LocationFailed) {
        NSLog(@"获取地理位置失败");
    }
    else if ([error code] == BNRoutePlanError_LocationServiceClosed)
    {
        NSLog(@"定位服务未开启");
    }
}

//算路取消回调
-(void)routePlanDidUserCanceled:(NSDictionary*)userInfo {
    NSLog(@"算路取消");
}

#pragma mark - BNNaviUIManagerDelegate

//退出导航回调
-(void)onExitNaviUI:(NSDictionary*)extraInfo
{
    NSLog(@"退出导航");
}

//退出导航声明页面回调
- (void)onExitDeclarationUI:(NSDictionary*)extraInfo
{
    NSLog(@"退出导航声明页面");
}

-(void)onExitDigitDogUI:(NSDictionary*)extraInfo
{
    NSLog(@"退出电子狗页面");
}

- (NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}
@end
