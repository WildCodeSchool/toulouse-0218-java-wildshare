package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wilder on 09/04/18.
 */

public class FriendItemsAdapter extends BaseAdapter {

    private final Context mContext;
    public ArrayList<ItemModel> friendItem;
    private FriendItemClickListerner listener;

    public FriendItemsAdapter(Context mContext, ArrayList<ItemModel> friendItem) {
        this.mContext = mContext;
        this.friendItem = friendItem;
    }

    public FriendItemsAdapter(Context mContext, ArrayList<ItemModel> friendItem, FriendItemClickListerner listener) {
        this.mContext = mContext;
        this.friendItem = friendItem;
        this.listener = listener;
    }

    public interface FriendItemClickListerner {
         void onClick (ItemModel itemModel);
    }

    @Override
    public int getCount() {
        return friendItem.size();
    }

    @Override
    public ItemModel getItem(int position) {
        return friendItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemModel friendItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_items_item_list, parent, false);
        }

        TextView friendItemName = convertView.findViewById(R.id.tv_name);
        ImageView friendItemImage = convertView.findViewById(R.id.iv_image);

        friendItemName.setText(friendItem.getName());
        friendItemImage.setImageDrawable(friendItem.getImage());




        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(friendItem);
            }
        });
        return convertView;
    }
}
