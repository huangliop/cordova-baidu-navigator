package cn.baidu.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
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

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        if(initDirs()){
            initNavi();
        }
    }
    private void initNavi(){

        BaiduNaviManager.getInstance().init(cordova.getActivity(), mSDCardPath, null, new BaiduNaviManager.NaviInitListener() {
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
            }

            @Override
            public void initFailed() {
                Log.d(TAG, "Init Failed");
            }
        }, null,myHandler,null);

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
