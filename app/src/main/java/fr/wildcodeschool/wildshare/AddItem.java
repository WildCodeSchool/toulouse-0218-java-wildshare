package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AddItem extends AppCompatActivity {

    ImageView imgChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText nameItem = findViewById(R.id.et_newItemName);
        EditText nameDesc = findViewById(R.id.et_description);
        final EditText edLink = findViewById(R.id.et_url);
        ImageView btnCamera = findViewById(R.id.iv_camera);
        ImageView btnGallery = findViewById(R.id.iv_gallery);
        ImageView btnLink = findViewById(R.id.iv_url);
        ImageView addItem = findViewById(R.id.iv_valid);
        final ImageView btnValidUrl = findViewById(R.id.iv_validUrl);
        imgChoose = findViewById(R.id.imageView_choose);
        ImageView btnCancel = findViewById(R.id.iv_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItem.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
                startActivityForResult(galleryIntent, 1);
            }
        });


        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edLink.setVisibility(View.VISIBLE);
                btnValidUrl.setVisibility(View.VISIBLE);

            }
        });


        btnValidUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String link = edLink.getText().toString();
                Glide.with(AddItem.this).load(link).apply(RequestOptions.circleCropTransform()) .into(imgChoose);
                edLink.setVisibility(View.GONE);
                btnValidUrl.setVisibility(View.GONE);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    Glide.with(AddItem.this).load(bitmap).apply(RequestOptions.circleCropTransform()) .into(imgChoose);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgChoose.setImageURI(selectedImage);
                }
                break;
        }
    }

}