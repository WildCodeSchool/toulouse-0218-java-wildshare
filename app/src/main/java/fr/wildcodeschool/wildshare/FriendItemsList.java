package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendItemsList extends AppCompatActivity {

    private static FriendItemsAdapter mFriendItemsAdapter;
    private static ArrayList<ItemModel> friendItemsData;
    private static String pseudoValue;
    private static String ownerPic;
    private static ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_items_list);

        TextView pseudo = findViewById(R.id.tv_pseudo);
        pseudoValue = getIntent().getStringExtra("pseudo");
        ownerPic = getIntent().getStringExtra("ownerPic");
        pseudo.setText(pseudoValue);
        avatar = findViewById(R.id.iv_avatar);

        Glide.with(getApplicationContext()).load(ownerPic).apply(RequestOptions.circleCropTransform()).into(avatar);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User");
        final DatabaseReference itemRef = database.getReference("Item");
        friendItemsData = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {

                    String friendId = userDataSnapshot.getKey();
                    String friendPseudo = userDataSnapshot.child("Profil").child("pseudo").getValue(String.class);

                    if (friendPseudo.equals(pseudoValue)) {

                        final DatabaseReference friendItemRef = database.getReference("User").child(friendId).child("Item");
                        friendItemRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                loadFriendItemList();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final ListView lvFriendItems = findViewById(R.id.lv_friend_items);

        mFriendItemsAdapter = new FriendItemsAdapter(FriendItemsList.this, friendItemsData);

        lvFriendItems.setAdapter(mFriendItemsAdapter);
        lvFriendItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String itemName = friendItemsData.get(position).getName();
                Intent intent = new Intent(FriendItemsList.this, ItemInfo.class);
                intent.putExtra("itemName", itemName);
                FriendItemsList.this.startActivity(intent);
            }
        });


        // TODO simplifier tout ça

        ImageView returnhome = findViewById(R.id.iv_close_friend);
        returnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intenthome = new Intent(FriendItemsList.this, HomeActivity.class);
                startActivity(intenthome);
                finish();
            }
        });


    }


    public static void loadFriendItemList() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User");
        final DatabaseReference itemRef = database.getReference("Item");
        friendItemsData.clear();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {

                    String friendId = userDataSnapshot.getKey();
                    String friendPseudo = userDataSnapshot.child("Profil").child("pseudo").getValue(String.class);

                    if (friendPseudo.equals(pseudoValue)) {

                        final DatabaseReference friendItemRef = database.getReference("User").child(friendId).child("Item");
                        friendItemRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (final DataSnapshot friendItemsDataSnapshot : dataSnapshot.getChildren()) {

                                    final String itemId = friendItemsDataSnapshot.getKey();
                                    String itemDisponibility = friendItemsDataSnapshot.getValue(String.class);

                                    if (itemDisponibility.equals("0")) {

                                        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                ItemModel item = dataSnapshot.child(itemId).getValue(ItemModel.class);
                                                item.setItemId(itemId);
                                                friendItemsData.add(item);
                                                mFriendItemsAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
