#! /bin/bash
rm -rf ./plugins/cordova-baidu-navigator/src/ios/libs/BaiduNaviSDK/__MACOSX
rm ./plugins/cordova-baidu-navigator/src/ios/libs/BaiduNaviSDK/libbaiduNaviSDK.a
unzip ./plugins/cordova-baidu-navigator/src/ios/libs/BaiduNaviSDK/libbaiduNaviSDK.a.zip -d ./plugins/cordova-baidu-navigator/src/ios/libs/BaiduNaviSDK
