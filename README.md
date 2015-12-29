# cordova-baidu-navigator
百度导航的cordova插件
    集成android百度导航3.0,iOS百度导航2.0.5
  
###安装插件:
    cordova plugin add https://github.com/huangliop/cordova-baidu-navigator.git --variable API_KEY=your_apiKey
  
###使用方法: 
```javascript
    if(cordova){
        window.plugins.baiduNavigator.startNavi({
                        lat:32.432,  //起始点的纬度
                        lon:109.433,  //起始点的经度
                        des:"起点"
                    },{
                        lat:31.23,  //目的地的纬度
                        lon:107.32, //起始点的经度
                        des:"终点"
                    },function(){
                        //启动导航成功
                    },function(error){ //启动导航发生错误
                        console.log(error)
                    }) 
    } 
 ```
