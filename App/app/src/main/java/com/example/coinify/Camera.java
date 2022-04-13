package com.example.coinify;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Camera extends Fragment{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static Double results;
    public static File Image;
    private Uri imageUri;
    String currentPhotoPath;
    public ImageView camera_image;
    public TextView results_text;
    public File photoFile = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.camera_tab_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton camera_button = view.findViewById(R.id.camera_button);
        camera_image = view.findViewById(R.id.Camera_image);
        results_text = view.findViewById(R.id.Results_text);

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }


    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Fragment yourFragment = this;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.i("Inside catch","CreateImageFile() not working");
        }
        // Continue only if the File was successfully created
        Log.i("PhotoFile", String.valueOf(photoFile));
        if (photoFile != null) {
            Uri photoUri = FileProvider.getUriForFile(getActivity(),
                    "com.example.coinify",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            camera_image.setImageBitmap(imageBitmap);
        }
    }
    */



    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            /*
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.i("Data", String.valueOf(data));
            Log.i("Data", String.valueOf(data.getExtras()));

            Uri selectedImage = data.getData();
            //Log.i("Data", String.valueOf(selectedImage));

            if (selectedImage == null){
                Log.i("Selected Image","Empty");
            }
            else {
                Log.i("Selected Image", "Not Empty");
            }



            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            Log.i("Indirect value of imageUri", String.valueOf(columnIndex));
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            galleryAddPic();
            camera_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            */

            Log.i("PhotoFile", String.valueOf(photoFile));

            if(photoFile.exists()) {
                Double total_amount;
                Analyzer analyzer = new Analyzer();
                analyzer.picture_file = photoFile;
                total_amount = analyzer.calculate_amount();
                results = total_amount;
                Image = photoFile;

                //Results results = new Results();
                //results.total_amount = total_amount;
                //results.picture_file = photoFile;

                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                results_text.setText("The total value found in the image was €" + total_amount/100);
                camera_image.setImageBitmap(myBitmap);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = camera_image.getWidth();
        int targetH = camera_image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        camera_image.setImageBitmap(bitmap);
    }

}
