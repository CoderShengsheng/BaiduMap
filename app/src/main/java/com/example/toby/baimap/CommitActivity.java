package com.example.toby.baimap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class CommitActivity extends AppCompatActivity {
    private int num;
    private Button comup;
    private EditText com_edit;
    public static Data park=null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        com_edit= (EditText) findViewById(R.id.com_edit);
        comup= (Button) findViewById(R.id.com_up);
        comup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                find();
                Intent intent=new Intent();
                intent.setClass(CommitActivity.this,ParkingState.class);
                CommitActivity.this.startActivity(intent);
            }
        });

    }
    //交互函数
    private void find() {
        park= (Data) getApplication();
        final String commit=com_edit.getText().toString();
        RequestQueue requestQueue1 = Volley.newRequestQueue(CommitActivity.this);

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://10.0.2.2/Project/commit.php", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username",park.getB());
                map.put("parkid",park.getC() );
                map.put("commit",commit);
                Log.e(park.getB(),park.getC());
                Log.e("dsad",commit);
                return map;
            }
        };
        requestQueue1.add(stringRequest1);
        //return npark;

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
           if(num==0)
               Log.e("sd","sd");
            else Log.e("sd","yes");
            //Toast.makeText(context,"评论成功",Toast.LENGTH_SHORT).show();
            //count = (Data) getApplication();
            //count.setD(str);


        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };
}
