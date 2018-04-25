package fr.wildcodeschool.wildshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

        final String itemNameValue = getIntent().getStringExtra("itemName");
        itemName.setText(itemNameValue);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference itemRef = database.getReference("Item");

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemDataSnapshot : dataSnapshot.getChildren()) {

                    String itemName = itemDataSnapshot.child("name").getValue(String.class);

                    if (itemName.equals(itemNameValue)) {

                        ItemModel itemModel = itemDataSnapshot.getValue(ItemModel.class);

                        itemDescription.setText(itemModel.getDescription());

                        Glide.with(ItemInfo.this).load(itemModel.getImage()).apply(RequestOptions.circleCropTransform()).into(itemImage);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
