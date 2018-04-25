package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_items_list);

        final ImageView avatar = findViewById(R.id.iv_avatar);
        TextView pseudo = findViewById(R.id.tv_pseudo);
        final ListView lvFriendItems = findViewById(R.id.lv_friend_items);
        final ArrayList<ItemModel> friendItemsData = new ArrayList<>();

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

        final String pseudoValue = getIntent().getStringExtra("pseudo");
        pseudo.setText(pseudoValue);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User");
        final DatabaseReference itemRef = database.getReference("Item");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {

                    String friendId = userDataSnapshot.getKey();
                    String friendPseudo = userDataSnapshot.child("Profil").child("pseudo").getValue(String.class);


                    if (friendPseudo.equals(pseudoValue)) {

                        String profilPic = userDataSnapshot.child("Profil").child("profilPic").getValue(String.class);
                        Glide.with(FriendItemsList.this).load(profilPic).apply(RequestOptions.circleCropTransform()).into(avatar);

                        final DatabaseReference friendItemRef = database.getReference("User").child(friendId).child("Item");
                        friendItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (final DataSnapshot friendItemsDataSnapshot : dataSnapshot.getChildren()) {

                                    final String itemId = friendItemsDataSnapshot.getKey();

                                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            ItemModel item = dataSnapshot.child(itemId).getValue(ItemModel.class);
                                            friendItemsData.add(item);
                                            mFriendItemsAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
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
