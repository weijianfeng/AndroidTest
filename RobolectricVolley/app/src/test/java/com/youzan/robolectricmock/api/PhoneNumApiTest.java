package com.youzan.robolectricmock.api;

import android.app.Application;
import android.content.Context;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.youzan.robolectricmock.BuildConfig;
import com.youzan.robolectricvolley.MyApplication;
import com.youzan.robolectricvolley.api.BaseApi;
import com.youzan.robolectricvolley.api.BaseJsonObjectRequest;
import com.youzan.robolectricvolley.api.PhoneNumApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by weijianfeng on 16/3/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
// http://apistore.baidu.com/apiworks/servicedetail/794.html  测试一个查找手机归属地请求
public class PhoneNumApiTest {

    private Application mApplication;
    private RequestQueue mRequestQueue;
    private int errNum;

    @Before
    public void setUp() throws Exception {
        errNum = 0;
        //mApplication = RuntimeEnvironment.application;
        mApplication = MyApplication.getAppContext();
        mRequestQueue = getTestQequestQueue(mApplication);
        //FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testWeatherInfo1() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        //故意填写错误参数，是返回值为 －1
        BaseApi api = new PhoneNumApi().numerInfo("");
        mRequestQueue.add(new BaseJsonObjectRequest(
                mApplication,
                api,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            errNum = jsonObject.getInt("errNum");
                            latch.countDown();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        latch.countDown();
                    }
                }
        ));
        //  设置CountDownLatch 超时的时间
        latch.await(30, TimeUnit.SECONDS);
        assertEquals(errNum, -1);
    }

    @Test
    // 填入正确的号码，返回值为0
    public void testWeatherInfo2() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        BaseApi api = new PhoneNumApi().numerInfo("18502519079");
        mRequestQueue.add(new BaseJsonObjectRequest(
                mApplication,
                api,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            errNum = jsonObject.getInt("errNum");
                            latch.countDown();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        latch.countDown();
                    }
                }
        ));
        latch.await(30, TimeUnit.SECONDS);
        assertEquals(errNum, 0);
    }

    private RequestQueue getTestQequestQueue(Context context){
        HttpStack stack = new HurlStack();
//        HttpStack stack = new HttpClientStack(new DefaultHttpClient());
        Network network = new BasicNetwork(stack);

        ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());

        RequestQueue queue = new RequestQueue(new NoCache(), network, 4, responseDelivery);

        queue.start();
        return queue;
    }
}