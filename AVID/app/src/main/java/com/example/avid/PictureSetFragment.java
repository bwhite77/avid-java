package com.example.avid;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PictureSetFragment extends Fragment {

    public interface PictureSetFragmentCallback {
        void changeToolbar(int id);
    }

    @Override
    public void onAttach(Context context) {
        callback = (PictureSetFragment.PictureSetFragmentCallback) context;
        super.onAttach(context);
    }

    PictureSetFragmentCallback callback;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Picture> picturesFromSet = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_set, container, false);

        callback.changeToolbar(1);

        picturesFromSet = getArguments().getParcelableArrayList("pictures");

        recyclerView = v.findViewById(R.id.picture_set_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new PictureAdapter(picturesFromSet);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
