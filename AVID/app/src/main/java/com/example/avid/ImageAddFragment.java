package com.example.avid;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAddFragment extends Fragment implements PictureSetAdapter.OnPictureSetListener {

    final int NUM_COLUMNS = 2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<PictureSetItem> pictureSets = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_add, container, false);

        List<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("Back Float",
                BitmapFactory.decodeResource(getResources(), R.drawable.back_float), "I want you to do a back float", "I want to do a back float"));
        pictures.add(new Picture("Dive",
                BitmapFactory.decodeResource(getResources(), R.drawable.dive), "I want you to dive", "I want to dive"));
        pictures.add(new Picture("Jump",
                BitmapFactory.decodeResource(getResources(), R.drawable.jump), "I want you to jump", "I want to jump"));

        pictureSets.add(new PictureSetItem(R.drawable.dive, "Swimming", "Created by Ben White", pictures));
        pictureSets.add(new PictureSetItem(R.drawable.backgrounddisplay, "School", "Created by Ben White", pictures));
        pictureSets.add(new PictureSetItem(R.drawable.caregiver, "People", "Created by Ben White", pictures));
        pictureSets.add(new PictureSetItem(R.drawable.floaties, "Swimming #2", "Created by Ben White", pictures));

        recyclerView = v.findViewById(R.id.img_add_recycler_view);
        layoutManager = new GridLayoutManager(getContext(), NUM_COLUMNS);
        adapter = new PictureSetAdapter(pictureSets, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void OnPictureSetClick(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        PictureSetFragment pictureSetFragment = new PictureSetFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("pictures", (ArrayList<? extends Parcelable>) pictureSets.get(position).getPictures());
        pictureSetFragment.setArguments(bundle);

        ft.add(R.id.fragment_container, pictureSetFragment);
        ft.show(pictureSetFragment);
        ft.commit();
    }
}
