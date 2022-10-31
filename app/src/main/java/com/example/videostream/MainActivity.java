package com.example.videostream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView url_recyclerView;
    TextInputLayout url_textLayout;
    TextInputEditText url_textEditText;
    Button url_save, url_search;
    String url;
    SharedPreferences SaveLinks;
    ArrayList<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url_textLayout = findViewById(R.id.url_textLayout);
        url_textEditText = findViewById(R.id.url_textEditText);
        url_recyclerView = findViewById(R.id.url_recyclerView);
        url_save = findViewById(R.id.url_save);
        url_search = findViewById(R.id.url_search);

        //SaveLinks=getSharedPreferences("Links",MODE_PRIVATE);
        //  Gson gson = new Gson();
        ButtonClicks();


       //urlList=sharefPref.readSharedPref(getApplicationContext());

        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        else {
            url_recyclerView.setLayoutManager(new LinearLayoutManager(this));
            UrlAdapter urlAdapter = new UrlAdapter(urlList, this);
            url_recyclerView.setAdapter(urlAdapter);
        }


    }

    private void ButtonClicks() {

        url_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = url_textEditText.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        url_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              SharedPreferences.Editor editor= SaveLinks.edit();
                String saveUrl = url_textEditText.getText().toString().trim();
                urlList.add(saveUrl);
                //boolean j = sharefPref.writeSharedPref(getApplicationContext(), urlList);

//                if (j == true) {
//                    Toast.makeText(MainActivity.this, "Saved Sucessfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
//                }
//              editor.putString("url",saveUrl);
//              editor.apply();
            }
        });
    }
}