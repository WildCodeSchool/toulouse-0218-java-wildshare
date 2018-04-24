package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static fr.wildcodeschool.wildshare.FriendItemsAdapter.*;

public class FriendItemsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_items_list);

        ImageView avatar = findViewById(R.id.iv_avatar);
        TextView fullName = findViewById(R.id.tv_firstname_lastname);

        ListView lvFriends = findViewById(R.id.lv_friend_items);
        final ArrayList<ItemModel> friendItemsData = new ArrayList<>();
        friendItemsData.add(new ItemModel("NameTest1", null));
        friendItemsData.add(new ItemModel("NameTest2", null));
        friendItemsData.add(new ItemModel("NameTest3", null));
        friendItemsData.add(new ItemModel("NameTest4", null));
        final FriendItemsAdapter adapter = new FriendItemsAdapter(FriendItemsList.this, friendItemsData);
        lvFriends.setAdapter(adapter);

    }
}
