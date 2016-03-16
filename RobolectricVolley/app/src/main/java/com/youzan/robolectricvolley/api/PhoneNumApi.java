package com.youzan.robolectricvolley.api;

import com.android.volley.Request;

/**
 * Created by weijianfeng on 16/3/15.
 */
public class PhoneNumApi {

    private static final String BASE_URI = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber";

    public static BaseApi numerInfo(String num) {
        return new BaseApi(Request.Method.GET, BASE_URI + "?" + "phone=" + num, true, null);
    }
}
