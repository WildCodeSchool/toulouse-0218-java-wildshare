package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfilActivity extends AppCompatActivity {

    EditText mEditPseudo;
    private Uri mUri = null;
    ImageView mImgProfilPic;
    String link;
    String urlSave;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);



        Button btnCamera;
        Button btnGallery;
        Button btnLink;
        final Button btnOK;
        final EditText edLink;
        Button btnValidModif;
        final TextView tvPseudo;
        String uid;

        edLink = findViewById(R.id.et_link);
        btnCamera = findViewById(R.id.btn_camera);
        btnGallery = findViewById(R.id.btn_gallery);
        btnLink = findViewById(R.id.btn_link);
        btnOK = findViewById(R.id.btn_ok);
        mImgProfilPic = findViewById(R.id.iv_profil_pic);
        mEditPseudo = findViewById(R.id.et_enter_pseudo);
        btnValidModif = findViewById(R.id.btn_valid_modif);
        tvPseudo = findViewById(R.id.tv_pseudo);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference pathID = mDatabase.getReference("User").child(uid);

        pathID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("profilPic").getValue() != null)) {
                    urlSave = dataSnapshot.child("profilPic").getValue(String.class);
                    Glide.with(ProfilActivity.this).load(urlSave).apply(RequestOptions.circleCropTransform()).into(mImgProfilPic);
                }

                if ((dataSnapshot.child("pseudo").getValue() != null)) {
                    String pseudo = dataSnapshot.child("pseudo").getValue(String.class);
                    tvPseudo.setText(pseudo);
                    mEditPseudo.setText(pseudo);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mUri = CameraUtils.getOutputMediaFileUri(ProfilActivity.this);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
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

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link = edLink.getText().toString();
                Glide.with(ProfilActivity.this).load(link) .into(mImgProfilPic);
                edLink.setVisibility(View.GONE);
                btnOK.setVisibility(View.GONE);
            }
        });

        btnValidModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = mEditPseudo.getText().toString();
                if (firstName.isEmpty()) {
                    Toast.makeText(ProfilActivity.this, R.string.pseudo_missed, Toast.LENGTH_SHORT).show();
                }
                else {
                    saveUserModel();

                    Intent intentHome = new Intent(ProfilActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                }
            }
        });
    }

    private void saveUserModel() {
        final String pseudo = mEditPseudo.getText().toString();

        if (mUri == null){

            if (link != null){
                String profilPic = link;
                UserModel userModel = new UserModel(pseudo, profilPic);
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabaseReference = mDatabase.getReference("User");
                mDatabaseReference.child(user.getUid()).setValue(userModel);
            }
            else{
                String profilPic = urlSave;
                UserModel userModel = new UserModel(pseudo, profilPic);
                FirebaseUser user = mAuth.getCurrentUser();
                mDatabaseReference = mDatabase.getReference("User");
                mDatabaseReference.child(user.getUid()).setValue(userModel);
            }


        }
        else {
            StorageReference filePath = mStorageReference.child("profilPicture").child(mUri.getLastPathSegment());
            filePath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String profilPic = downloadUrl.toString();
                    UserModel userModel = new UserModel(pseudo, profilPic);
                    FirebaseUser user = mAuth.getCurrentUser();
                    mDatabaseReference = mDatabase.getReference("User");
                    mDatabaseReference.child(user.getUid()).setValue(userModel);
                }
            });
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    //Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    mImgProfilPic.setImageURI(mUri);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    mUri = imageReturnedIntent.getData();
                    mImgProfilPic.setImageURI(mUri);
                }
                break;
        }
    }

}
