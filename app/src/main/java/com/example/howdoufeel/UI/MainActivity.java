package com.example.howdoufeel.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.howdoufeel.R;

public class MainActivity extends AppCompatActivity {
    private ImageButton btn_getEmotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_getEmotion = findViewById(R.id.btn_getEmotion);

        btn_getEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, playmusic.class);
                startActivity(intent);
            }
        });

    }


}