package com.example.toby.baimap;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class ParkingState extends AppCompatActivity {
    private TextView pView;
    private Button parkEnd,commit,select;
    private Chronometer timer;
    private String username,parkid;
    private int num;

    private static double price;
    private String priceStr;
    private Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            pView.setText(priceStr);
        }};
    private Handler handler= null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_state);
        pView= (TextView) findViewById(R.id.pView);
        parkEnd= (Button) findViewById(R.id.parkEnd);
        timer= (Chronometer) findViewById(R.id.timer);
        timeStart();
        //ParkP();
        Timer mTimer =new Timer();
        //int temp = 0;
        handler = new Handler();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final Data app= (Data) getApplication();

                double parkp2=app.getD();//停车场单价
                priceCount(parkp2);
                handler.post(uiRunnable);

            }
        },1000,5000);
        parkEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stop();
                ParkEnd(v);
                jump();

            }
        });
        commit= (Button) findViewById(R.id.comment);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(ParkingState.this,CommitActivity.class);
                ParkingState.this.startActivity(intent2);
            }
        });
        /*select= (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(ParkingState.this,SelectActivity.class);
                ParkingState.this.startActivity(intent2);

            }
        });*/

    }

    private void timeStart() {
        timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0"+String.valueOf(hour)+":%s");
        timer.start();
    }
    private void priceCount(double b){
        int temp0=Integer.parseInt(timer.getText().toString().split(":")[0]);
        int temp1=Integer.parseInt(timer.getText().toString().split(":")[1]);
        //int temp2=Integer.parseInt(timer.getText().toString().split(":")[2]);
        int stopTime=temp0*60+temp1;

        double pri=b/4;
        //Log.e("e",stopTime+"");
        if (stopTime<0) price=0;
        else {
            price=(stopTime+1)*pri;
        }
        //Log.e("tag" ,price+"");
        priceStr=price+"元";
//        pView.setText(str);
        //pView.setText(price+"");
        //timer.stop();
    }
    private void ParkEnd(View v) {
        RequestQueue requestQueue = Volley.newRequestQueue(ParkingState.this);
        final Data app= (Data) getApplication();
        final String pstr=price+"";
        username=app.getB();
        parkid=app.getC();

        StringRequest stringRequest =new StringRequest(Request.Method.POST,"http://10.0.2.2/Project/charge.php",listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",username);
                map.put("parkid",parkid);
                map.put("money",pstr);
                return  map;
            }
        };
        requestQueue.add(stringRequest);

        jump();
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
            Log.e("we",str);

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

    private void jump() {
        Intent intent =new Intent();
        intent.setClass(ParkingState.this,MainActivity.class);
        ParkingState.this.startActivity(intent);
    }

}
