package com.example.toby.baimap;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

public class ParkActivity extends AppCompatActivity {
    private EditText EditParkid;
    private Button ButtonPark;
    public static Data app=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        EditParkid= (EditText) findViewById(R.id.EditParkid);
        ButtonPark= (Button) findViewById(R.id.ButtonPark);
        ButtonPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parking(v);
               // ParkP();
                jump();
            }
        });

    }

    private void jump() {
        Intent intent =new Intent();
        intent.setClass(ParkActivity.this,ParkingState.class);
        ParkActivity.this.startActivity(intent);
    }
    //链接数据库，插入停车表
    private void parking(View v) {
        final Data app= (Data) getApplication();
        final String user=app.getB();
        final String park=EditParkid.getText().toString();
        app.setC(park);
        if(TextUtils.isEmpty(park)){
            Toast.makeText(this, "请输入停车场编号", Toast.LENGTH_LONG).show();
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(ParkActivity.this);
       // Log.e("sda","sad");
        StringRequest stringRequest =new StringRequest(Request.Method.POST,"http://10.0.2.2/Project/parking.php",listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",user);
                map.put("parkid",park);
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
                double parkp = jsonObject.getDouble("price");
                final Data app= (Data) getApplication();
                app.setD(parkp);
                //String str = parkp+" ";
                Log.e("ue",parkp+"");
                Log.e("we",app.getD()+"");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", "sadsadasdsa");
                //mTextview.setText(str);
            }




        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };
    //记录停车位价格



}
