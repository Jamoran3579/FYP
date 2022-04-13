package com.example.coinify;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

public class Results extends Fragment {

    File picture_file;
    public Double total_amount;
    public ImageView results_image;
    public TextView results_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.results_tab_layout, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("Contents of Camera.results: ", String.valueOf(Camera.results));

        total_amount = Camera.results;

        results_image = view.findViewById(R.id.Results_image);
        results_text = view.findViewById(R.id.Results_text_broken);

        if (total_amount != null) {
            set_results();
        }
    }

    @SuppressLint("SetTextI18n")
    public void set_results(){
        Bitmap myBitmap = BitmapFactory.decodeFile(Camera.Image.getAbsolutePath());

        results_image.setImageBitmap(myBitmap);
        results_text.setText("The total value found in the image was €" + Camera.results);
    }


}
