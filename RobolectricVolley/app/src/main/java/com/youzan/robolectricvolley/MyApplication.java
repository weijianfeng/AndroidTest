package com.youzan.robolectricvolley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by weijianfeng on 16/3/15.
 */
public class MyApplication extends Application {
    private static MyApplication mAppContext;
    private static Map<String, String> sAuthHeaders;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mAppContext = this;
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static MyApplication getAppContext() {
        return mAppContext;
    }

    public static Map<String, String> getAuthHeaders() {
        // 如果没有授权，返回null
        // 之前持久化的数据，需要在之前的登陆流程中进行持久化
        String accessToken = "test";
        if (accessToken == null) {
            return Collections.emptyMap();
        }

        if (sAuthHeaders == null) {
            sAuthHeaders = new HashMap<String, String>(1);
            sAuthHeaders.put("apikey", "9931049916380b549f63681753144433");
        }
        return sAuthHeaders;

    }
}
