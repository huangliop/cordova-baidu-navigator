//
//  CDVBaiduNavi.h
//  BaiduTest
//
//  Created by sope on 15/7/31.
//
//
#import <Foundation/Foundation.h>

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>

@interface CDVBaiduNavi : CDVPlugin

@property(nonatomic,strong)NSString *currentCallbackId;
@property(nonatomic,strong)NSString *appKey;

- (void)startNavi:(CDVInvokedUrlCommand*)command;
- (void)simulateNavi:(CDVInvokedUrlCommand*)command;

@end
