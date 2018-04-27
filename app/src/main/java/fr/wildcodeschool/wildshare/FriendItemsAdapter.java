package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wilder on 09/04/18.
 */

public class FriendItemsAdapter extends ArrayAdapter<ItemModel> {

    private String itemId;

    FriendItemsAdapter(Context context, ArrayList<ItemModel> itemFriend) {
        super(context, 0, itemFriend);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemModel friendItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_items_list, parent, false);
        }


        final ImageButton bAdd = convertView.findViewById(R.id.button_give_back);
        final TextView friendItemName = convertView.findViewById(R.id.tv_item_name);
        ImageView friendItemImage = convertView.findViewById(R.id.iv_item_image);


        friendItemName.setText(friendItem.getName());
        Glide.with(getContext()).load(friendItem.getImage()).apply(RequestOptions.circleCropTransform()).into(friendItemImage);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference itemRef = database.getReference("Item");
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String ownerId = friendItem.getOwnerId();
        final String itemName = friendItem.getName();
        final DatabaseReference ownerItemRef = database.getReference("User").child(ownerId).child("Item");

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                            ItemModel itemModel = itemSnapshot.getValue(ItemModel.class);
                            String itemNameCompare = itemModel.getName();

                            if (itemNameCompare.equals(itemName) && itemModel.getOwnerId().equals(friendItem.getOwnerId())) {

                                itemId = itemSnapshot.getKey();
                                ownerItemRef.child(itemId).setValue(userId);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return convertView;

    }

}

