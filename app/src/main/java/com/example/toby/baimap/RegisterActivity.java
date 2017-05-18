package com.example.toby.baimap;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private int retCode;
    private EditText username,password,carp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register= (Button) findViewById(R.id.reg_div_button);
        username= (EditText) findViewById(R.id.rusername_edit);
        password= (EditText) findViewById(R.id.password_redit);
        carp= (EditText) findViewById(R.id.carp);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {

        final String user=username.getText().toString();
        final String psd=password.getText().toString();
        final String car=carp.getText().toString();
        if(TextUtils.isEmpty(user)|| TextUtils.isEmpty(psd)||TextUtils.isEmpty(car)){
            Toast.makeText(this, "内容不全", Toast.LENGTH_LONG).show();
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest stringRequest =new StringRequest(Request.Method.POST,"http://10.0.2.2/Project/register.php",listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",user);
                map.put("password",psd);
                map.put("usercol",car);
                return  map;
            }
        };
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> listener = new Response.Listener<String>(){
        @Override
        public void onResponse(String s) {
            Log.e("TAG", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                retCode = jsonObject.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (retCode == 1) {
                Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent();
                intent2.setClass(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.startActivity(intent2);

            }
            else {
                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }

    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };
}
