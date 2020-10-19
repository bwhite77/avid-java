package com.example.avid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureSetAdapter extends RecyclerView.Adapter<PictureSetAdapter.PictureSetViewHolder> {

    private List<PictureSetItem> pictureSetItemList;
    private OnPictureSetListener onPictureSetListener;

    public static class PictureSetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgView;
        public TextView title;
        public TextView desc;
        OnPictureSetListener onPictureSetListener;

        public PictureSetViewHolder(@NonNull View itemView, OnPictureSetListener onPictureSetListener) {
            super(itemView);
            imgView = itemView.findViewById(R.id.pictureset_image);
            title = itemView.findViewById(R.id.pictureset_title);
            desc = itemView.findViewById(R.id.pictureset_desc);
            this.onPictureSetListener = onPictureSetListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPictureSetListener.OnPictureSetClick(getAdapterPosition());
        }
    }

    public PictureSetAdapter(List<PictureSetItem> pictureSetItemList, OnPictureSetListener onPictureSetListener)
    {
        this.pictureSetItemList = pictureSetItemList;
        this.onPictureSetListener = onPictureSetListener;
    }

    @NonNull
    @Override
    public PictureSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_set_item, parent, false);
        PictureSetViewHolder pictureSetViewHolder = new PictureSetViewHolder(v, onPictureSetListener);
        return pictureSetViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureSetViewHolder holder, int position) {
        PictureSetItem currentItem = pictureSetItemList.get(position);

        holder.imgView.setImageResource(currentItem.getImgRes());
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return pictureSetItemList.size();
    }

    public interface OnPictureSetListener
    {
        void OnPictureSetClick(int position);
    }

}
