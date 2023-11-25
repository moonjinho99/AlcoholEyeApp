package com.example.alcoholeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainActivityLog";
    public static final String URL = "http://3.36.106.170:8090/";

    private Retrofit retrofit;
    private ApiService service;

    Button login_btn,join_btn;

    EditText userid,userpw;

    private JSONObject data = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstInit();


        login_btn.setOnClickListener(this);
        join_btn.setOnClickListener(this);
    }


    public void firstInit() {
       login_btn = (Button) findViewById(R.id.login_btn);
       join_btn = (Button) findViewById(R.id.join_btn);

       userid = (EditText) findViewById(R.id.userid);
       userpw = (EditText) findViewById(R.id.userpw);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                try{
                    data.put("id",userid.getText().toString());
                    data.put("pw",userpw.getText().toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                Call<ResponseBody> call_get = service.signIn(data);
                call_get.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.v(TAG, "result = " + result);

                                if(result.equals("OK"))
                                {
                                    GlobalId globalId = (GlobalId) getApplication();
                                    globalId.setUserId(userid.getText().toString());
                                    Toast.makeText(getApplicationContext(),"안녕하세요! 알코올아이입니다.",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,MainPage.class);
                                    startActivity(intent);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.v(TAG, "error = " + String.valueOf(response.code()));
                            Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v(TAG, "Fail");
                        Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.join_btn:
                Intent intent = new Intent(MainActivity.this, JoinPage.class);
                startActivity(intent);
                break;


        }
    }

}