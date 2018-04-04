package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wilder on 03/04/18.
 */
public class FriendListAdapter extends ArrayAdapter<FriendModel> {

    FriendListAdapter(Context context, ArrayList<FriendModel> friend) {
    super(context, 0, friend);
}

    public View getView(int position, View convertView, ViewGroup parent) {

        FriendModel friend = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_item_list, parent, false);
        }

            TextView friendFirstname = convertView.findViewById(R.id.tv_firstname);
            TextView friendLastname = convertView.findViewById(R.id.tv_lastname);
            ImageView avatar = convertView.findViewById(R.id.iv_avatar);

        friendFirstname.setText(friend.getFirstname());
        friendLastname.setText(friend.getLastname());
        avatar.setImageDrawable(friend.getAvatar());
        return convertView;
    }
}