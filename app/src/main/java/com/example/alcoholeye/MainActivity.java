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
    public static final String URL = "http://172.16.24.141:3000/";

    private Retrofit retrofit;
    private ApiService service;

    //private Button btn_get, btn_post, btn_delete, btn_update;

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

    /**
     * Init
     */
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

    /**
     * View.OnLongClickListener override method
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                try{
                    data.accumulate("id",userid.getText().toString());
                    data.accumulate("pw",userpw.getText().toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                Call<ResponseBody> call_get = service.getFunc(data);
                call_get.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                Log.v(TAG, "result = " + result);
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
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