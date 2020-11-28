package com.example.howdoufeel.UI;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.howdoufeel.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private ImageButton btn_getEmotion;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_getEmotion = findViewById(R.id.btn_getEmotion);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        btn_getEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Cannot get the camera", Toast.LENGTH_LONG).show();
                }


//                Intent intent = new Intent(MainActivity.this, playmusic.class);
//                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap == null) {
                Log.e("Camera:", "not found");
            } else {
                new GetEmotion(imageBitmap).execute();
            }
        }
    }

    class GetEmotion extends AsyncTask<Void, Void, String> {

        private Bitmap bitmap;
        private final String BASE_URL="http://9b423b4f5915.ngrok.io";

        public GetEmotion(Bitmap bitmap)
        {
            this.bitmap = bitmap;
        }
        @Override
        protected String doInBackground(Void... voids) {
            File file ;
            try {
                file = new File(Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis()+".png");
                file.createNewFile();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
                return "Cant save file"; // it will return null
            }
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    file))
                    .build();
            Request request = new Request.Builder()
                    .url(BASE_URL+"/analysis")
                    .method("POST", body)
                    .build();
            try {
                ResponseBody response = client.newCall(request).execute().body();
                return response.string();
            } catch (IOException e) {
                Log.e("bug", "this is bu here");
                e.printStackTrace();
            }
            file.delete();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, playmusic.class);
            intent.putExtra("mood", s);
            startActivity(intent);

        }

    }

}