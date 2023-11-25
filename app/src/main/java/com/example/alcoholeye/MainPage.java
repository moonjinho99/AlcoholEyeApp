package com.example.alcoholeye;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        Button startBtn = (Button) findViewById(R.id.startBtn);

        Toast.makeText(getApplicationContext(),"중앙에 검사히기 버튼을 눌러주세요",Toast.LENGTH_SHORT).show();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, CheckUserFace.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {

            return true;
        }

        return false;
    }

}
