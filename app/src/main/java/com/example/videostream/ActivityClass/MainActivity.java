package com.example.videostream.ActivityClass;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videostream.AdapterClass.UrlAdapter;
import com.example.videostream.Interface.updateInterface;
import com.example.videostream.ModelData.urlModel;
import com.example.videostream.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements updateInterface {
    RecyclerView url_recyclerView;
    TextInputLayout url_textLayout, username_textLayout;
    TextInputEditText url_textEditText, username_textEditText;
    Button url_save, url_search;
    String url;
    SharedPreferences sharedPreferences;
    ArrayList<urlModel> urlList;
    String saveUrl;
    String saveUsername;
    LinearLayout recyclerview_LL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url_textLayout = findViewById(R.id.url_textLayout);
        url_textEditText = findViewById(R.id.url_textEditText);

        username_textLayout = findViewById(R.id.username_textLayout);
        username_textEditText = findViewById(R.id.username_textEditText);

        url_recyclerView = findViewById(R.id.url_recyclerView);
        url_save = findViewById(R.id.url_save);
        url_search = findViewById(R.id.url_search);
        recyclerview_LL = findViewById(R.id.recyclerview_LL);

        urlList = new ArrayList<>();

        ButtonClicks();

        //THIS TEXTWATCHER USED FOR IF DISMISSING ERROR WHEN ENTERED TEXT IN USERNAME EDITTEXT
        username_textEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username_textLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //THIS TEXTWATCHER USED FOR IF DISMISSING ERROR WHEN ENTERED TEXT IN URL EDITTEXT
        url_textEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                url_textLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void ButtonClicks() {
        url_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = url_textEditText.getText().toString().trim();
                if (url.isEmpty()) {
                    url_textLayout.setError("Cannot be Empty");
                } else {
                    nextPage(url);
                }
            }
        });

        url_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urlList == null) {
                    urlList = new ArrayList<>();
                } else {

                }

                saveUsername = username_textEditText.getText().toString().trim();
                saveUrl = url_textEditText.getText().toString().trim();

                if (saveUsername.isEmpty()) {
                    username_textLayout.setError("Username Cannot be Empty");
                } else if (saveUrl.isEmpty()) {
                    url_textLayout.setError("Enter url before Save");
                } else {
                    boolean CompairUsername = CompairUsername();
                    if (CompairUsername) {
                        Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean CompaurLink = CompairLink();
                        if (CompaurLink) {
                            Toast.makeText(MainActivity.this, "This Link already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            if (urlList.size() > 4) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("You can Save upto Five Links \n Do you want to delete Oldest one ?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                urlList.remove(0);
                                                urlList.add(new urlModel(saveUsername, saveUrl));

                                                SharedPreferences sharedPreferences = getSharedPreferences("link", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
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
                            } else {
                                storedata();
                            }
                        }

                    }
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //LOADING DATA FROM Sharedpreference AND ASSIGN TO A RECYCLERVIEW :
        loaddata();
    }

    //THIS FUNCTION USE TO COMPARE USERNAME
    //USER WANTS SAVE A USERNAME AND URL IN Sharedpreferences BUT IF SAME USERNAME ALREADY SAVED IN Sharedpreferences IT WILL GET ERROR THAT USERNAME ARE ALREADY SAVED
    private boolean CompairUsername() {
        String getName = null;
        boolean getUsername = false;
        for (int i = 0; i < urlList.size(); i++) {
            getName = urlList.get(i).getUsername();

            if (saveUsername.equals(getName)) {
                getUsername = true;
            }
        }
        return getUsername;
    }

    //THIS FUNCTION USE TO COMPARE URL LIKE USERNAME
    private boolean CompairLink() {
        String url_check = null;
        boolean getUrl = false;
        for (int i = 0; i < urlList.size(); i++) {
            url_check = urlList.get(i).getUrl();

            if (saveUrl.equals(url_check)) {
                getUrl = true;
            }
        }
        return getUrl;
    }

    //GOING NEXT PAGE BY CALLING THIS FUNCTION
    private void nextPage(String url) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    //THIS FUNCTION USED TO LOAD A DATA FROM Sharedpreference
    private void loaddata() {

        url_textLayout.setError(null);
        username_textLayout.setError(null);

        SharedPreferences sharedPreferences = getSharedPreferences("link", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("linkdata", null);
        Type type = new TypeToken<ArrayList<urlModel>>() {
        }.getType();
        urlList = gson.fromJson(json, type);

        try {
            if (urlList.size() > 0) {
                recyclerview_LL.setVisibility(View.VISIBLE);
                url_recyclerView.setLayoutManager(new LinearLayoutManager(this));
                UrlAdapter urlAdapter = new UrlAdapter(urlList, this, url_textEditText, username_textEditText, this);
                url_recyclerView.setAdapter(urlAdapter);
            } else {
                recyclerview_LL.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

    }

    //THIS FUNCTION USED TO STORE A DATA IN Sharedpreference :
    private void storedata() {
        urlList.add(new urlModel(saveUsername, saveUrl));
        SharedPreferences sharedPreferences = getSharedPreferences("link", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(urlList);
        editor.putString("linkdata", json);
        editor.apply();
        loaddata();
        nextPage(saveUrl);
    }


    //updateList IS A INTERFACE GETTING DATA FROM A UrlAdapter ADAPTER TO CHECK WHICH DATA WILL BE DELETED AND UPDATE A UI.
    @Override
    public void updateList(int n) {
        if (n > -1) {
            Toast.makeText(this, "Record Deleted Sucessfully", Toast.LENGTH_SHORT).show();
            loaddata();
        }

    }
}