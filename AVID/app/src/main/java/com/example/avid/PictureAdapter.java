package com.example.avid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private List<Picture> pictures;

    public static class PictureViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgView;
        public TextView key;
        public TextView caregiverMsg;
        public TextView childMsg;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.picture_img);
            key = itemView.findViewById(R.id.picture_key);
            caregiverMsg = itemView.findViewById(R.id.picture_caregiver_msg);
            childMsg = itemView.findViewById(R.id.picture_child_msg);
        }
    }

    public PictureAdapter(List<Picture> pictures)
    {
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        PictureViewHolder pictureViewHolder = new PictureViewHolder(v);
        return pictureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Picture currentItem = pictures.get(position);

        Log.e("NULL!!!!!!!!!!!!!", "IMG VIEW NULL");

        holder.imgView.setImageBitmap(currentItem.getImageBitmap());
        holder.key.setText(currentItem.getKey());
        holder.caregiverMsg.setText(currentItem.getCaregiverMessage());
        holder.childMsg.setText(currentItem.getmChildMessage());
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

}
