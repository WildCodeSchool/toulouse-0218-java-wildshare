package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

public class AddItem extends AppCompatActivity {

    ImageView imgChoose;
    ItemModel newItem = new ItemModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final EditText nameItem = findViewById(R.id.et_newItemName);
        final EditText itemDesc = findViewById(R.id.et_description);
        final EditText edLink = findViewById(R.id.editText_link);
        Button btnCamera = findViewById(R.id.button_camera);
        Button btnGallery = findViewById(R.id.button_gallery);
        Button btnLink = findViewById(R.id.button_link);
        Button addItem = findViewById(R.id.b_addToData);
        final Button btnOK = findViewById(R.id.button_ok);
        imgChoose = findViewById(R.id.imageView_choose);

        String newItemName = nameItem.getText().toString();
        final String newItemDesc = itemDesc.getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase items = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final DatabaseReference itemsReference = items.getReference("items");
       // databaseReference.child(user.getUid()).getDatabase().getReference();

        newItem.setName(newItemName);
        newItem.setOwnerID(user.getUid());

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String picture = MediaStore.ACTION_IMAGE_CAPTURE;
                newItem.setImageURL(picture);
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
                btnOK.setVisibility(View.VISIBLE);
            }
        });

        final String url = "https://wildcodeschool.fr/wp-content/uploads/2017/01/logo_orange_pastille.png";

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String link = edLink.getText().toString();
                newItem.setImageURL(link);
                Glide.with(AddItem.this).load(link) .into(imgChoose);

            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newItemDesc.equals("")) {
                    newItem.setDescription(newItemDesc);
                }
                itemsReference.setValue(newItem);
                Intent intent = new Intent(AddItem.this, HomeActivity.class);
                startActivity(intent);
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
                    newItem.setImageBit(bitmap);
                    imgChoose.setImageBitmap(bitmap);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    newItem.setImageURI(selectedImage);
                    imgChoose.setImageURI(selectedImage);
                }
                break;
        }
    }

}