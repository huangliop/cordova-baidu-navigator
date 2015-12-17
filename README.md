# cordova-baidu-navigator
百度导航的cordova插件
  这是集成百度导航3.0的Cordova插件
  
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
                        console.log("success")
                    },function(e){
                        console.log(e)
                    }) 
    } 
 ```
