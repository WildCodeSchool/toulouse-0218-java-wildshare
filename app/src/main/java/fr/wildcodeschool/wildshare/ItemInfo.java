package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        final ImageView itemImage = findViewById(R.id.iv_item_image);
        TextView itemName = findViewById(R.id.tv_item_name);
        final TextView itemDescription = findViewById(R.id.tv_item_description);
        final ImageView itemModif = findViewById(R.id.iv_modif);
        final ImageView itemeDelete = findViewById(R.id.iv_delete);

        final String itemNameValue = getIntent().getStringExtra("itemName");
        itemName.setText(itemNameValue);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference itemRef = database.getReference("Item");
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference myRef = database.getReference("User").child(userId);



        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot itemDataSnapshot : dataSnapshot.getChildren()) {

                    final String itemName = itemDataSnapshot.child("name").getValue(String.class);

                    if (itemName.equals(itemNameValue)) {

                        final ItemModel itemModel = itemDataSnapshot.getValue(ItemModel.class);
                        itemDescription.setText(itemModel.getDescription());
                        Glide.with(ItemInfo.this).load(itemModel.getImage()).apply(RequestOptions.circleCropTransform()).into(itemImage);

                        String userItemId = itemModel.getOwnerId();
                        if (userId.equals(userItemId)) {
                            itemModif.setVisibility(View.VISIBLE);
                            itemeDelete.setVisibility(View.VISIBLE);
                        }

                        itemeDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String itemKey = itemDataSnapshot.getKey();
                                itemRef.child(itemKey).removeValue();
                                myRef.child("Item").child(itemKey).removeValue();
                                Intent homeIntent = new Intent(ItemInfo.this, HomeActivity.class);
                                startActivity(homeIntent);
                            }
                        });

                        itemModif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent modifIntent = new Intent(ItemInfo.this, AddItem.class);
                                modifIntent.putExtra("itemName", itemModel.getName());
                                startActivity(modifIntent);
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ImageView returnhome = findViewById(R.id.iv_close_item);
        returnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        Intent intenthome = new Intent(ItemInfo.this, HomeActivity.class);
        startActivity(intenthome);
        finish();
        }
        });
    }
}
