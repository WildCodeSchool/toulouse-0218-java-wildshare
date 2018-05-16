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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendItemsList extends AppCompatActivity {

    private static FriendItemsAdapter mFriendItemsAdapter;
    private String mPseudo = null;
    private String mOwnerPic = null;
    final ArrayList<ItemModel> mFriendItemsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_items_list);

        final ImageView avatar = findViewById(R.id.iv_avatar);
        TextView pseudo = findViewById(R.id.tv_pseudo);
        final ListView lvFriendItems = findViewById(R.id.lv_friend_items);

        mFriendItemsAdapter = new FriendItemsAdapter(FriendItemsList.this, mFriendItemsData,
                new FriendItemsAdapter.BorrowFriendItemListener() {
                    @Override
                    public void onBorrowed() {
                        setFriendItemList();
                    }
                });

        lvFriendItems.setAdapter(mFriendItemsAdapter);
        lvFriendItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String itemName = mFriendItemsData.get(position).getName();
                Intent intent = new Intent(FriendItemsList.this, ItemInfo.class);
                intent.putExtra("itemName", itemName);
                FriendItemsList.this.startActivity(intent);
            }
        });

        mPseudo = getIntent().getStringExtra("pseudo");
        mOwnerPic = getIntent().getStringExtra("ownerPic");
        pseudo.setText(mPseudo);
        Glide.with(this).load(mOwnerPic).apply(RequestOptions.circleCropTransform()).into(avatar);

        setFriendItemList();

        ImageView returnhome = findViewById(R.id.iv_close_friend);
        returnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenthome = new Intent(FriendItemsList.this, HomeActivity.class);
                startActivity(intenthome);
                finish();
            }
        });

        ImageView deleteFriend = findViewById(R.id.iv_delete_friend);
        deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setFriendItemList() {
        mFriendItemsData.clear();
        mFriendItemsAdapter.notifyDataSetChanged();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("User")
                .orderByChild("Profil/pseudo").equalTo(mPseudo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot friendSS : dataSnapshot.getChildren()) {
                            for (DataSnapshot itemSS : friendSS.child("Item").getChildren()) {
                                if (itemSS.getValue(String.class).equals("0")) {
                                    final String itemId = itemSS.getKey();
                                    database.getReference("Item")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    ItemModel item = dataSnapshot.child(itemId)
                                                            .getValue(ItemModel.class);
                                                    item.setItemId(itemId);
                                                    mFriendItemsData.add(item);
                                                    mFriendItemsAdapter.notifyDataSetChanged();
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}

