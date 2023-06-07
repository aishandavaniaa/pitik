package com.example.user.navigationdrawersample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.user.navigationdrawersample.Auth.ApiServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener {
    Button buttonLogin;
    private EditText et_email,et_password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = (EditText) findViewById(R.id.etl_email);
        et_password = (EditText) findViewById(R.id.etl_password);
        buttonLogin = (Button) findViewById(R.id.btn_login);

        buttonLogin.setOnClickListener(this);
        SharedPreferences preferences = getSharedPreferences("PHITIX", MODE_PRIVATE);
        boolean isLogin = preferences.getBoolean("isLogin", false);

        if (isLogin) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
       if(view == buttonLogin) {
           String email = et_email.getText().toString().trim();
           String pass = et_password.getText().toString().trim();

           if (!email.isEmpty() && !pass.isEmpty()){
              ApiServices.login(getApplicationContext(), email, pass, new ApiServices.LoginResponseListener() {
                  @Override
                  public void onSuccess(String response) {
                      Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();

                      Intent intent = new Intent(login.this, MainActivity.class);
                      startActivity(intent);
                      finish();
                  }

                  @Override
                  public void onError(String message) {

                      Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                  }
              });
           }else {
               et_email.setError("Masukkan Email!");
               et_password.setError("Masukkan Password!");
           }
       }
    }
}
