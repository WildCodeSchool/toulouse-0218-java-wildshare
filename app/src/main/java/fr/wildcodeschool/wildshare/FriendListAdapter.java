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
 * Created by wilder on 03/04/18.
 */
public class FriendListAdapter extends BaseAdapter {

    private final Context mContext;
    public ArrayList<FriendModel> friend;
    private ItemClickListerner listener;


    public FriendListAdapter(Context mContext, ArrayList<FriendModel> friend) {
        this.mContext = mContext;
        this.friend = friend;
    }

    public FriendListAdapter(Context mContext, ArrayList<FriendModel> friend, ItemClickListerner listener) {
        this.mContext = mContext;
        this.friend = friend;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return friend.size();
    }
    @Override
    public Object getItem(int position) {return friend.get(position);}
    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface ItemClickListerner {
        void onClick (FriendModel friend);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final FriendModel friend = (FriendModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_list, parent, false);
        }

            TextView friendFirstname = convertView.findViewById(R.id.tv_firstname);
            TextView friendLastname = convertView.findViewById(R.id.tv_lastname);
            ImageView avatar = convertView.findViewById(R.id.iv_avatar);

            friendFirstname.setText(friend.getFirstname());
            friendLastname.setText(friend.getLastname());
            avatar.setImageDrawable(friend.getAvatar());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(friend);
            }
        });
        return convertView;

    }
}
