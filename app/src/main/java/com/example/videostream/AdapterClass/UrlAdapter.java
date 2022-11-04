package com.example.videostream.AdapterClass;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videostream.ModelData.urlModel;
import com.example.videostream.R;
import com.example.videostream.ActivityClass.VideoActivity;
import com.example.videostream.Interface.updateInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UrlAdapter extends RecyclerView.Adapter<UrlAdapter.ViewHolder>  {
    ArrayList<urlModel>arrayList;
    Context context;
    TextInputEditText url_textEditText;
    TextInputEditText username_textEditText;
    SharedPreferences sharedPreferences ;
    com.example.videostream.Interface.updateInterface updateInterface;


    public UrlAdapter(ArrayList<urlModel> arrayList, Context context, TextInputEditText url_textEditText, TextInputEditText username_textEditText, updateInterface updateInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.url_textEditText = url_textEditText;
        this.username_textEditText = username_textEditText;
        this.sharedPreferences = sharedPreferences;
        this.updateInterface = updateInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.url_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.url.setText(arrayList.get(position).getUrl());
        holder.username.setText(arrayList.get(position).getUsername());


        holder.url_itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url_textEditText.setText(arrayList.get(position).getUrl());
                username_textEditText.setText(arrayList.get(position).getUsername());

                String url=arrayList.get(position).getUrl();

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });


//        holder.url.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                url_textEditText.setText(arrayList.get(position).getUrl());
//                username_textEditText.setText(arrayList.get(position).getUsername());
////                String url=url_textEditText.getText().toString().trim();
//                String url=arrayList.get(position).getUrl();
//
//                Intent intent = new Intent(context, VideoActivity.class);
//                intent.putExtra("url", url);
//                context.startActivity(intent);
//            }
//        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            new AlertDialog.Builder(context)
                    .setMessage("Do you want to Delete "+arrayList.get(position).getUsername()+" ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    arrayList.remove(position);
                                    notifyDataSetChanged();

                                    sharedPreferences= context.getSharedPreferences("link", MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(arrayList);
                                    editor.putString("linkdata", json);
                                    editor.apply();

                                    updateInterface.updateList(position);
                                }
                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                            .show();




            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView url;
        TextView username;
        LinearLayout url_itemLL;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            url=itemView.findViewById(R.id.url_item_url);
            username=itemView.findViewById(R.id.url_item_nickname);

            url_itemLL=itemView.findViewById(R.id.url_itemLL);
            delete=itemView.findViewById(R.id.url_item_deleteItem);
        }
    }
}
