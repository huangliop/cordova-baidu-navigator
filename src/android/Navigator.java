package cn.baidu.navigator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuangLi on 2015/12/16.
 */
public class Navigator extends CordovaPlugin {
    private  static final String TAG="BaiduNavigator";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private static final String APP_FOLDER_NAME = "BaiduNavigator";
    private String mSDCardPath;
    //是否正在计算线路
    private boolean isPlanRoute=false;
    private final String authBaseArr[] =
            { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION };
    private final String authComArr[] = { Manifest.permission.READ_PHONE_STATE };
    private final int authBaseRequestCode = 1;
    private final int authComRequestCode = 2;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        if(initDirs()){
            initNavi();
        }
    }

    private boolean hasBasePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = cordova.getActivity().getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, cordova.getActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi(){

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.cordova.getActivity().requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(cordova.getActivity(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int i, String s) {
                String m = "";
                if (0 == i) {
                    m = "key验证成功";
                } else {
                    m = "key验证失败" + s;
                }
                Log.d(TAG, m);
            }

            @Override
            public void initStart() {
                Log.d(TAG, "Init Start.....");
            }

            @Override
            public void initSuccess() {
                Log.d(TAG, "Init Success");
                initSetting();
            }

            @Override
            public void initFailed() {
                Log.d(TAG, "Init Failed");
            }
        }, null,myHandler,ttsPlayStateListener);

    }

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();

        try
        {
            ApplicationInfo appInfo = this.cordova.getActivity().getPackageManager().getApplicationInfo(this.cordova.getActivity().getPackageName(), PackageManager.GET_META_DATA);
            String ttsAppId=appInfo.metaData.getString("TTS_KEY_ANDROID");
            if(ttsAppId != null)
            {
                // 必须设置APPID，否则会静音
                bundle.putString(BNCommonSettingParam.TTS_APP_ID, ttsAppId);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    private final MyHandler myHandler=new MyHandler();
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if(action.equals("startNavi")) {

            double startLat, startLon, endLat, endLon;
            String startAddress, endAddress;
            try{
                startLat = args.getDouble(0);
                startLon = args.getDouble(1);
                startAddress = args.getString(2);
                endLat = args.getDouble(3);
                endLon = args.getDouble(4);
                endAddress = args.getString(5);
            }catch (Exception e){
                callbackContext.error("Parameters type error");
                return true;
            }
            Toast.makeText(cordova.getActivity(),"线路规划中...",Toast.LENGTH_SHORT).show();
            if(isPlanRoute)return true;
            isPlanRoute=true;
            if (startAddress.equals("null")) startAddress = "";
            if (endAddress.equals("null")) endAddress = "";

            final List<BNRoutePlanNode> list=new ArrayList<BNRoutePlanNode>();
            final BNRoutePlanNode sNode=new BNRoutePlanNode(startLon,startLat,startAddress,startAddress ,BNRoutePlanNode.CoordinateType.BD09LL);
            list.add(sNode);
            list.add(new BNRoutePlanNode(endLon, endLat, endAddress, endAddress, BNRoutePlanNode.CoordinateType.BD09LL));
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaiduNaviManager.getInstance().launchNavigator(cordova.getActivity(), list, 1, true, new MyRoutePlanListener(sNode));
                }
            });
            callbackContext.success();
            return true;
        }
        return super.execute(action, args, callbackContext);
    }
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private static class MyHandler extends android.os.Handler{

    }

    public class MyRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public MyRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            isPlanRoute=false;
            Toast.makeText(cordova.getActivity(),"规划成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(cordova.getActivity(), GuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE,mBNRoutePlanNode);
            intent.putExtras(bundle);
            cordova.getActivity().startActivity(intent);
        }
        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            isPlanRoute=false;
            Toast.makeText(cordova.getActivity(),"线路规划失败",Toast.LENGTH_SHORT).show();
        }
    }
}
