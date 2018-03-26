package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.wildcodeschool.wildshare.R;

/**
 * Created by wilder on 26/03/18.
 */

public class ListAdapter extends ArrayAdapter<ItemModel> {

    ListAdapter(Context context, ArrayList<ItemModel> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ItemModel item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.tv_itemName);
        ImageView itemImage = convertView.findViewById(R.id.iv_itemImage);
        ImageButton ownerImage = convertView.findViewById(R.id.button_owner);

        itemName.setText(item.getName());
        itemImage.setImageResource(item.getImage());
        ownerImage.setImageResource(item.getOwnerImage());

        return convertView;
    }
}
