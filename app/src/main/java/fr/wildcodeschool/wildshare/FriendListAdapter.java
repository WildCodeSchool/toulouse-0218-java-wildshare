package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wilder on 03/04/18.
 */
public class FriendListAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    public ArrayList<FriendModel> friendModel;
    private FriendClickListerner listener;
    private  CustomFilterFriend filter;
    private ArrayList<FriendModel> filterList;

    public FriendListAdapter(Context mContext, ArrayList<FriendModel> friendModel) {
        this.mContext = mContext;
        this.friendModel = friendModel;
        this.filterList = friendModel;
    }

    public FriendListAdapter(Context mContext, ArrayList<FriendModel> friend, FriendClickListerner listener) {
        this.mContext = mContext;
        this.friendModel = friend;
        this.listener = listener;
    }

    public FriendListAdapter(Context mContext, ArrayList<FriendModel> friend, CustomFilterFriend filter, ArrayList<FriendModel> filterList) {
        this.mContext = mContext;
        this.friendModel = friend;
        this.filter = filter;
        this.filterList = filterList;
    }

    @Override
    public int getCount() {return friendModel.size();}
    @Override
    public Object getItem(int position) {return friendModel.get(position);}
    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface FriendClickListerner {
        void onClick(FriendModel friend);
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
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilterFriend(filterList, this);
        }
        return filter;
    }
}
