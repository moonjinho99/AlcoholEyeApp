package com.example.alcoholeye;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InputAlcohol extends AppCompatActivity {

    EditText inputAlcohol;
    Button measureBtn;

    private Retrofit retrofit;
    private ApiService service;

    private JSONObject alcoholData = new JSONObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_alcohol);

        inputAlcohol = (EditText) findViewById(R.id.inputAlcohol);
        measureBtn = (Button) findViewById(R.id.measureBtn);

        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    alcoholData.put("alcohol",inputAlcohol.getText().toString());

                    Call<ResponseBody> call_input_alcohol = service.inputAlcohol(alcoholData);

                    call_input_alcohol.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {

                                //알코올 측정값에 따라 나오는 메시지를 서버에서 result로 받아 Toast로 출력한다.
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

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

    }
}
