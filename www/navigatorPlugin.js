var exec = require('cordova/exec');
module.exports = {
    startNavi: function (start, end, success, error) {
        if(!start.lat||!start.lon||!end.lat||!end.lon){
            error("Missing parameter ");
            return;
        }
        
        exec(success, error, "plugins.baidNavigator", "startNavi"
             , [start.lat,start.lon,start.des?start.des:""
                ,end.lat,end.lon,end.des?end.des:""]);
    }
};