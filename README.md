# cordova-baidu-navigator

百度导航的cordova插件

集成`android`百度导航`3.3.0`  `iOS`百度导航`2.0.5`

## 由于导航包比较大,下载时间较长请耐心等待
  
### Android安装插件

    cordova plugin add https://github.com/huangliop/cordova-baidu-navigator.git --variable API_KEY_IOS=your_ios-apiKey --variable API_KEY_ANDROID=your_android_apiKey --variable TTS_KEY_ANDROID=your_android_tts_appid

### iOS安装插件
执行过程中如有报错,请在命令行前面加 sudo

    $ cordova plugin add https://github.com/huangliop/cordova-baidu-navigator.git --variable API_KEY_IOS=your_ios-apiKey --variable API_KEY_ANDROID=your_android_apiKey --variable TTS_KEY_ANDROID=your_android_tts_appid
    $ chmod +x plugins/cordova-baidu-navigator/scripts/before_plugin_install.sh
    $ cordova platform add ios  //ios安装时,这个必须在添加插件之后进行.
  
### 使用方法

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
