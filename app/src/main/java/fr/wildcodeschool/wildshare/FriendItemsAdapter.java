package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by wilder on 09/04/18.
 */

public class FriendItemsAdapter extends ArrayAdapter<ItemModel> {

    private BorrowFriendItemListener mListener = null;

    FriendItemsAdapter(Context context, ArrayList<ItemModel> itemFriend, BorrowFriendItemListener listener) {
        super(context, 0, itemFriend);
        mListener = listener;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemModel friendItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_items_list, parent, false);
        }

        final ImageButton bAdd = convertView.findViewById(R.id.b_add);
        final TextView friendItemName = convertView.findViewById(R.id.tv_item_name);
        ImageView friendItemImage = convertView.findViewById(R.id.iv_item_image);

        friendItemName.setText(friendItem.getName());
        Glide.with(getContext()).load(friendItem.getImage()).apply(RequestOptions.circleCropTransform()).into(friendItemImage);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String ownerId = friendItem.getOwnerId();

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                // on marque l'objet de l'ami comme prêté à l'utilisateur
                database.getReference("User").child(ownerId).child("Item").child(friendItem.getItemId()).setValue(userId);

                // on ajoute l'objet de l'ami à sa liste d'emprunt
                database.getReference("User").child(userId).child("Borrowed").child(friendItem.getItemId()).setValue(ownerId);

                mListener.onBorrowed();
            }
        });

        return convertView;

    }

    public interface BorrowFriendItemListener {

        void onBorrowed();
    }
}

