package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wilder on 26/03/18.
 */

public class ListAdapter extends BaseAdapter {

    private final Context mContext;
    public ArrayList<ItemModel> userItem;
    private ItemClickListerner listener;

    public ListAdapter(Context mContext, ArrayList<ItemModel> userItem, ItemClickListerner listener) {
        this.mContext = mContext;
        this.userItem = userItem;
        this.listener = listener;
    }

    public ListAdapter(Context mContext, ArrayList<ItemModel> userItem) {
        this.mContext = mContext;
        this.userItem = userItem;
    }

    public interface ItemClickListerner {
        void onClick (ItemModel itemModel);
    }

    @Override
    public int getCount() {
        return userItem.size();
    }

    @Override
    public Object getItem(int position) {
        return userItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemModel item = (ItemModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.tv_item_name);
        ImageView itemImage = convertView.findViewById(R.id.iv_item_image);
        ImageButton ownerImage = convertView.findViewById(R.id.button_owner);

        itemName.setText(item.getName());
        itemImage.setImageDrawable(item.getImage());
        ownerImage.setImageResource(item.getOwnerImage());

        return convertView;
    }
}
