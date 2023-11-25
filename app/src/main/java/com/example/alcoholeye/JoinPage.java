package com.example.alcoholeye;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinPage extends AppCompatActivity implements View.OnClickListener {

    private static  int REQUEST_IMAGE_CAPTURE = 1;
    private String imageFilePath;

    private Uri photoUri;


    private final String TAG = "JoinPageLog";
    private Retrofit retrofit;
    private ApiService service;
    EditText join_id, join_pw,join_username;
    Button join_btn,id_check_btn;
    TextView id_check_msg;
    ImageView userImg;
    Spinner addressSpin;

    RadioGroup gender_radio;

    DatePicker birth_datepicker;

    int idcheckres = 1;
    String address;
    String id;
    String pw;
    String hashpw;
    String username;

    String birth;

    String gender;

    private JSONObject check_data = new JSONObject();
    private JSONObject signUpData = new JSONObject();
    MultipartBody.Part photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        firstInit();


        id_check_btn.setOnClickListener(this);
        join_btn.setOnClickListener(this);

        gender_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male_radio:
                        gender="남";
                        break;

                    case R.id.female_radio:
                        gender="여";
                        break;
                }
            }
        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);*/

                sendTakePhotoIntent();
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

    public void firstInit() {
        join_id = (EditText) findViewById(R.id.join_id);
        join_pw = (EditText) findViewById(R.id.join_pw);
        join_btn = (Button) findViewById(R.id.join_end_btn);
        addressSpin = (Spinner) findViewById(R.id.join_address_spin);
        userImg = (ImageView) findViewById(R.id.join_user_img);
        id_check_btn = (Button) findViewById(R.id.join_id_check_btn);
        id_check_msg = (TextView) findViewById(R.id.id_check_msg);
        join_username = (EditText) findViewById(R.id.join_username);
        gender_radio = (RadioGroup) findViewById(R.id.geder_raio);
        birth_datepicker = (DatePicker) findViewById(R.id.birth_datepicker);


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
                    check_data.put("id",join_id.getText().toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                Call<ResponseBody> call_get = service.idCheck(check_data);
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
                    hashpw = BCrypt.withDefaults().hashToString(12, pw.toCharArray());
                    username = join_username.getText().toString();
                    birth = Integer.toString(birth_datepicker.getYear())+Integer.toString(birth_datepicker.getMonth()+1)+Integer.toString(birth_datepicker.getDayOfMonth());
                    Log.e("birth",birth);

                    File photoFile = new File(imageFilePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), photoFile);
                    photo = MultipartBody.Part.createFormData("join_img", photoFile.getName(), requestFile);


                    signUpData.put("id",id);
                    signUpData.put("pw",hashpw);
                    signUpData.put("name",username);
                    signUpData.put("address",address);
                    signUpData.put("birth",birth);
                    signUpData.put("gender",gender);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(idcheckres == 1)
                {
                    //ApiService에 저장된 함수 실행
                    Call<ResponseBody> call_get_join = service.signUp( signUpData, photo );
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

    private void sendTakePhotoIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ((ImageView) findViewById(R.id.join_user_img)).setImageURI(photoUri);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

}