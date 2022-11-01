package com.example.videostream.ActivityClass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.videostream.AdapterClass.UrlAdapter;
import com.example.videostream.ModelData.urlModel;
import com.example.videostream.R;
import com.example.videostream.Interface.updateInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements updateInterface {
    RecyclerView url_recyclerView;
    TextInputLayout url_textLayout,username_textLayout;
    TextInputEditText url_textEditText,username_textEditText;
    Button url_save, url_search;
    String url;
    SharedPreferences sharedPreferences;
     ArrayList <urlModel> urlList;
     String saveUrl;
     String saveUsername;
     LinearLayout recyclerview_LL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url_textLayout = findViewById(R.id.url_textLayout);
        url_textEditText = findViewById(R.id.url_textEditText);

        username_textLayout=findViewById(R.id.username_textLayout);
        username_textEditText=findViewById(R.id.username_textEditText);

        url_recyclerView = findViewById(R.id.url_recyclerView);
        url_save = findViewById(R.id.url_save);
        url_search = findViewById(R.id.url_search);
        recyclerview_LL=findViewById(R.id.recyclerview_LL);

        urlList=new ArrayList<>();


        ButtonClicks();

    }

    private void loaddata() {
        SharedPreferences sharedPreferences=getSharedPreferences("link",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("linkdata", null);
        Type type = new TypeToken<ArrayList<urlModel>>() {}.getType();
        urlList=gson.fromJson(json, type);

        if(urlList.size()>0)
        {
            recyclerview_LL.setVisibility(View.VISIBLE);
            url_recyclerView.setLayoutManager(new LinearLayoutManager(this));
            UrlAdapter urlAdapter = new UrlAdapter(urlList, this,url_textEditText,this);
            url_recyclerView.setAdapter(urlAdapter);
        }
        else
        {
            recyclerview_LL.setVisibility(View.GONE);
        }

    }


    private void ButtonClicks() {
        url_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = url_textEditText.getText().toString().trim();
                if(url.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please add link in box", Toast.LENGTH_SHORT).show();
                }
               else
                {
                  nextPage(url);
                }
            }
        });

        url_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(urlList==null)
                {
                    urlList=new ArrayList<>();
                }
                else
                {

                }

                saveUsername=username_textEditText.getText().toString().trim();
                saveUrl = url_textEditText.getText().toString().trim();

                if(saveUsername.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please add Username before Save", Toast.LENGTH_SHORT).show();
                }
                else if(saveUrl.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please add link in Box", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   boolean Compair=CompairList();
                   if(Compair)
                   {
                       Toast.makeText(MainActivity.this, "Username or Link already exists", Toast.LENGTH_SHORT).show();
                   }
                    else
                    {
                        if(urlList.size()>4)
                        {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("You can Save upto Five Links \n Do you want to delete Oldest one ?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            urlList.remove(0);
                                            urlList.add(new urlModel(saveUsername,saveUrl));

                                            SharedPreferences sharedPreferences=getSharedPreferences("link",MODE_PRIVATE);
                                            SharedPreferences.Editor editor= sharedPreferences.edit();
                                            Gson gson = new Gson();
                                            String json = gson.toJson(urlList);
                                            editor.putString("linkdata", json);
                                            editor.apply();
                                            loaddata();
                                            nextPage(saveUrl);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                        else
                        {
                            storedata();
                        }
                    }
                }


            }
        });
    }

    private boolean CompairList() {
        String name = null;
        boolean getUsername=false;
        for(int i=0;i<urlList.size();i++)
        {
           name=urlList.get(i).getUsername();

           if(saveUsername.equals(name))
           {
               getUsername=true;
           }
        }
       return getUsername;
    }


    private void nextPage(String url) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void storedata() {
        urlList.add(new urlModel(saveUsername,saveUrl));
        SharedPreferences sharedPreferences=getSharedPreferences("link",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(urlList);
        editor.putString("linkdata", json);
        editor.apply();
        loaddata();
        nextPage(saveUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaddata();
    }

    @Override
    public void updateList(int n) {
        if(n>-1)
        {
            loaddata();
        }

    }
}