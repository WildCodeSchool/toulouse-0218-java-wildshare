package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wilder on 09/04/18.
 */

public class FriendItemsAdapter extends ArrayAdapter<ItemModel> {

    FriendItemsAdapter(Context context, ArrayList<ItemModel> monsters) {
        super(context, 0, monsters);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemModel friendItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_items_list, parent, false);
        }

        TextView friendItemName = convertView.findViewById(R.id.tv_name);
        ImageView friendItemImage = convertView.findViewById(R.id.iv_image);

        friendItemName.setText(friendItem.getName());
        friendItemImage.setImageDrawable(friendItem.getImage());

        return convertView;
        }

    }

