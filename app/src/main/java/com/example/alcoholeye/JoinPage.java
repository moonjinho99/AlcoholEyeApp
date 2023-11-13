package com.example.alcoholeye;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinPage extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "JoinPageLog";
    private Retrofit retrofit;
    private ApiService service;

    EditText join_id, join_pw,join_username;
    Button join_btn,id_check_btn;
    TextView id_check_msg;
    ImageView userImg;
    Spinner addressSpin;

    int idcheckres = 0;

    Uri uri;
    String img_path;
    String address;
    String id;
    String pw;
    String username;

    private JSONObject data = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        firstInit();


        id_check_btn.setOnClickListener(this);
        join_btn.setOnClickListener(this);

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        addressSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                address = addressSpin.getSelectedItem().toString();
                Log.e("주소",addressSpin.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();
                        userImg.setImageURI(uri);
                        img_path = getRealPathFromURI(uri);
                        Log.e("이미지 경로: ",img_path);
                    }
                }
                break;
        }
    }

    //실제 경로
    private String getRealPathFromURI(Uri contentURI) {

        String result;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * Init
     */
    public void firstInit() {
        join_id = (EditText) findViewById(R.id.join_id);
        join_pw = (EditText) findViewById(R.id.join_pw);
        join_btn = (Button) findViewById(R.id.join_end_btn);
        addressSpin = (Spinner) findViewById(R.id.join_address_spin);
        userImg = (ImageView) findViewById(R.id.join_user_img);
        id_check_btn = (Button) findViewById(R.id.join_id_check_btn);
        id_check_msg = (TextView) findViewById(R.id.id_check_msg);
        join_username = (EditText) findViewById(R.id.join_username);

        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //아이디 중복체크
            case R.id.join_id_check_btn:
                try{
                    data.accumulate("checkid",join_id.getText().toString());

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

                                //서버에서 값이 OK로 넘어와야 함(아이디가 중복되지 않을 때)
                                if(result.equals("OK"))
                                {
                                    idcheckres = 1; // 중복체크가 되면 1아니면 초기값 0
                                    id_check_msg.setText("이 아이디는 사용 가능합니다.");
                                    id_check_msg.setTextColor(Color.GREEN);
                                    id_check_msg.setVisibility(View.VISIBLE);
                                    id_check_btn.setEnabled(false);
                                }
                                else {
                                    id_check_msg.setText("아이디가 존재합니다.");
                                    id_check_msg.setTextColor(Color.RED);
                                    id_check_msg.setVisibility(View.VISIBLE);
                                }

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

            case R.id.join_end_btn:

                try{
                    id = join_id.getText().toString();
                    pw = join_pw.getText().toString();
                    username = join_username.getText().toString();


                    String hashpw = BCrypt.withDefaults().hashToString(12, pw.toCharArray());

                    data.accumulate("join_id",id);
                    data.accumulate("join_pw",hashpw);
                    data.accumulate("join_username",username);
                    data.accumulate("join_address",address);
                    data.accumulate("join_img",img_path);

//                    RequestBody idRequestBody = RequestBody.create(MediaType.parse("text/plain"), id);
//                    RequestBody pwRequestBody = RequestBody.create(MediaType.parse("text/plain"), pw);
//                    RequestBody usernameRequestBody = RequestBody.create(MediaType.parse("text/plain"), username);
//                    RequestBody addressRequestBody = RequestBody.create(MediaType.parse("text/plain"), address);
//
//                    MultipartBody.Part imagePart = prepareFilePart("join_img", img_path);
//
//                    Call<ResponseBody> call = service.uploadImage(idRequestBody, pwRequestBody, usernameRequestBody, addressRequestBody, imagePart);
//
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            if (response.isSuccessful()) {
//                                try {
//                                    String result = response.body().string();
//                                    Log.v(TAG, "result = " + result);
//                                    Toast.makeText(getApplicationContext(), "회원 가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(JoinPage.this,MainActivity.class);
//                                    startActivity(intent);
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                Log.v(TAG, "error = " + String.valueOf(response.code()));
//                                Toast.makeText(getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.v(TAG, "Fail");
//                            Toast.makeText(getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(idcheckres == 1)
                {
                    Call<ResponseBody> call_get_join = service.getFunc(data);
                    call_get_join.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String result = response.body().string();
                                    Log.v(TAG, "result = " + result);
                                    Toast.makeText(getApplicationContext(), "회원 가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(JoinPage.this,MainActivity.class);
                                    startActivity(intent);

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
                }
                else{
                    Toast.makeText(JoinPage.this,"아이디 중복체크를 해주세요",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private MultipartBody.Part prepareFilePart(String partName, String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}