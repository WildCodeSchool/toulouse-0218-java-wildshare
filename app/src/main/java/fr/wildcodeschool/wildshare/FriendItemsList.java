package fr.wildcodeschool.wildshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_items_list);

        final ImageView avatar = findViewById(R.id.iv_avatar);
        TextView pseudo = findViewById(R.id.tv_pseudo);
        final ListView lvFriends = findViewById(R.id.lv_friend_items);
        final ArrayList<ItemModel> friendItemsData = new ArrayList<>();
        final FriendItemsAdapter friendItemsAdapter = new FriendItemsAdapter(FriendItemsList.this, friendItemsData);

        lvFriends.setAdapter(friendItemsAdapter);

        final String pseudoValue = getIntent().getStringExtra("pseudo");
        pseudo.setText(pseudoValue);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User");
        final DatabaseReference itemRef = database.getReference("Item");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference myFriendsRef = database.getReference("User").child(uid).child("Friends");

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
                                            friendItemsAdapter.notifyDataSetChanged();

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
