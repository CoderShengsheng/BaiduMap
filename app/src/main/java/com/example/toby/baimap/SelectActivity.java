package com.example.toby.baimap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class SelectActivity extends AppCompatActivity {
    private String parkid,parkn,user;
    //private int num;
    public static Data app=null;
    private TextView select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String str=bundle.getString("str");
        SelectComment(str);
    }
    private void SelectComment(String s) {
        RequestQueue requestQueue = Volley.newRequestQueue(SelectActivity.this);
        //final Data app= (Data) getApplication();
        parkid=s;

        StringRequest stringRequest =new StringRequest(Request.Method.POST,"http://10.0.2.2/Project/selectComment.php",listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("parkid",parkid);
                //map.put("parkid","001");
                return  map;
            }
        };
        requestQueue.add(stringRequest);

    }

    Response.Listener<String> listener = new Response.Listener<String>(){
        @Override
        public void onResponse(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                 parkn= jsonObject.getString("show1");
                user=jsonObject.getString("show2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("we",user);
            String str=parkn+"     来自用户："+user;
            select= (TextView) findViewById(R.id.show1);
            select.setText(str);

            //Log.e("TAG", s);
            //mTextview.setText(str);
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };
}
