package com.example.toby.dao;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.toby.baimap.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toby on 2017/5/12.
 */

public class ParkCount extends Activity {
    public static Data cou = null;
    private int num;

    public String parkCount(final String str){
        RequestQueue requestQueue = Volley.newRequestQueue(ParkCount.this);
        StringRequest stringRequest =new StringRequest(Request.Method.POST,"http://10.0.2.2/Project/ParkCount.php",listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                //map.put("username",user);
                map.put("parkid",str);
                return  map;
            }
        };
        requestQueue.add(stringRequest);
        return cou.getC();
    }
    Response.Listener<String> listener = new Response.Listener<String>(){
        @Override
        public void onResponse(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                num = jsonObject.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String str = num+" ";
            cou = (Data)getApplication();
            cou.setC(str);
            Log.e("we",str);

        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };

}
