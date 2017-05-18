package com.example.toby.baimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private  EditText password;
    private Button login,register;
    private int retCode;
    public static Data app=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= (EditText) findViewById(R.id.username_edit);
        password= (EditText) findViewById(R.id.password_edit);
        login= (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        register= (Button) findViewById(R.id.res_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent2);
            }
        });
    }

    private void login(View v) {
        final String user=username.getText().toString();
        final Data app = (Data) getApplication();
        app.setB(user);
        final String psd=password.getText().toString();
        if(TextUtils.isEmpty(user)|| TextUtils.isEmpty(psd)){
            Toast.makeText(this, "用户和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest stringRequest =new StringRequest(Request.Method.POST,"http://10.0.2.2/Project/login.php",listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",user);
                map.put("password",psd);
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
                Toast.makeText(LoginActivity.this,"登入成功！",Toast.LENGTH_SHORT).show();
                jumpTo(MainActivity.class);
            }
            else {
                Toast.makeText(LoginActivity.this,"用户名或密码错误!",Toast.LENGTH_SHORT).show();
            }
        }

    };

    private void jumpTo(Class<MainActivity> m) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, m);
        LoginActivity.this.startActivity(intent);
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };

}

