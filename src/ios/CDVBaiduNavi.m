//
//  CDVBaiduNavi.m
//  BaiduTest
//
//  Created by sope on 15/7/31.
//
//

#import "CDVBaiduNavi.h"
#import "BNRoutePlanModel.h"
#import "BaiduNaviViewController.h"

@implementation CDVBaiduNavi

-(void)pluginInitialize{
    NSString *bundlePath = [[NSBundle mainBundle] pathForResource:@"Info" ofType:@"plist"];
    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithContentsOfFile:bundlePath];
    NSString *APP_KEY = [dict objectForKey:@"APP_KEY"];
    [BNCoreServices_Instance initServices:APP_KEY];
    [BNCoreServices_Instance startServicesAsyn:nil fail:nil];
}
// 模拟导航
- (void)simulateNavi:(CDVInvokedUrlCommand*)command{
    // 将导航界面添加到主界面
    BaiduNaviViewController *childView = [BaiduNaviViewController new];
    [self.viewController addChildViewController:childView];
    [childView simulateNavi];
}

// 真实导航
- (void)startNavi:(CDVInvokedUrlCommand*)command{
    [self prepareForExec:command];
    NSDictionary *nvPoint = [self checkArgs:command];
    
    NSString *sStartPointX = nvPoint[@"startPointX"];
    NSString *sStartPointY = nvPoint[@"startPointY"];
    NSString *sEndPointX = nvPoint[@"endPointX"];
    NSString *sEndPointY = nvPoint[@"endPointY"];
    
    //起点 传入的是原始的经纬度坐标，若使用的是百度地图坐标，可以使用BNTools类进行坐标转化
    BNRoutePlanNode *startNode = [[BNRoutePlanNode alloc] init];
    startNode.pos = [[BNPosition alloc] init];
    startNode.pos.x = [sStartPointX floatValue];
    startNode.pos.y = [sStartPointY floatValue];
    startNode.pos.eType = BNCoordinate_BaiduMapSDK;
    
    //终点
    BNRoutePlanNode *endNode = [[BNRoutePlanNode alloc] init];
    endNode.pos = [[BNPosition alloc] init];
    endNode.pos.x = [sEndPointX floatValue];
    endNode.pos.y = [sEndPointY floatValue];
    endNode.pos.eType = BNCoordinate_BaiduMapSDK;
    
    // 将导航界面添加到主界面
    BaiduNaviViewController *childView = [BaiduNaviViewController new];
    [self.viewController addChildViewController:childView];
    [childView startNavi:startNode andEndNode:endNode];
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
    [self endForExec];
}


-(NSDictionary *)checkArgs:(CDVInvokedUrlCommand *) command{
    // check arguments
    NSDictionary *params = [command.arguments objectAtIndex:0];
    if (!params)
    {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"参数错误"] callbackId:command.callbackId];
        
        [self endForExec];
        return nil;
    }
    return params;
}

-(void) prepareForExec:(CDVInvokedUrlCommand *)command{
    self.currentCallbackId = command.callbackId;
}

-(void) endForExec{
    self.currentCallbackId = nil;
}


@end
