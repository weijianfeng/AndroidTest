package com.youzan.robolectricvolley.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.youzan.robolectricvolley.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by weijianfeng on 16/3/15.
 */
public class BaseJsonObjectRequest extends Request<JSONObject> {

    public static final String TAG = "BaseJsonObjectRequest";

    protected Context mContext;
    protected BaseApi mApi;
    protected Response.Listener<JSONObject> mListener;

    public BaseJsonObjectRequest(Context context, BaseApi api,
                                 Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener) {
        super(api.method, api.uri, errorListener);
        mContext = context;
        mApi = api;
        mListener = listener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Response<JSONObject> success = Response.success(new JSONObject(
                    jsonString), HttpHeaderParser.parseCacheHeaders(response));

            return success;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        } catch (Exception ex) {
            Log.e(TAG, "process error!", ex);
            return Response.error(new VolleyError(response));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (mListener == null) {
            Log.d(TAG, "finish http request without response on main-thread!");
        } else {
            mListener.onResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mApi.authRequired ? MyApplication.getAuthHeaders() : super
                .getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mApi.params == null ? super.getParams() : mApi.params;
    }
}
