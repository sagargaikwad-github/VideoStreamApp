package com.example.videostream.AdapterClass;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    SharedPreferences sharedPreferences ;
    com.example.videostream.Interface.updateInterface updateInterface;


    public UrlAdapter(ArrayList<urlModel> arrayList, Context context, TextInputEditText url_textEditText, updateInterface updateInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.url_textEditText = url_textEditText;
        this.updateInterface=updateInterface;
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

        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                url_textEditText.setText(arrayList.get(position).getUrl());
//                String url=url_textEditText.getText().toString().trim();
                String url=arrayList.get(position).getUrl();

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=arrayList.get(position).getUrl();

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView url;
        TextView username;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            url=itemView.findViewById(R.id.url_item_url);
            username=itemView.findViewById(R.id.url_item_nickname);
            delete=itemView.findViewById(R.id.url_item_deleteItem);
        }
    }
}
