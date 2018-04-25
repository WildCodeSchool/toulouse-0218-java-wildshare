package fr.wildcodeschool.wildshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendItemsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_items_list);

        String pseudoValue = getIntent().getStringExtra("pseudo");

        ImageView avatar = findViewById(R.id.iv_avatar);
        TextView pseudo = findViewById(R.id.tv_pseudo);

        pseudo.setText(pseudoValue);

        ListView lvFriends = findViewById(R.id.lv_friend_items);
        final ArrayList<ItemModel> friendItemsData = new ArrayList<>();
        friendItemsData.add(new ItemModel("NameTest1"));
        friendItemsData.add(new ItemModel("NameTest2"));
        friendItemsData.add(new ItemModel("NameTest3"));
        friendItemsData.add(new ItemModel("NameTest4"));
        final FriendItemsAdapter adapter = new FriendItemsAdapter(FriendItemsList.this, friendItemsData);
        lvFriends.setAdapter(adapter);

    }
}
