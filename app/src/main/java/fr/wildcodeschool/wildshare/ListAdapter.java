package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wilder on 26/03/18.
 */

public class ListAdapter extends BaseAdapter implements Filterable{

    private final Context mContext;
    public ArrayList<ItemModel> itemModels;
    private CustomFilter filter;
    private ArrayList<ItemModel> filterList;
    private ItemClickListerner listener;
    
    public ListAdapter(Context mContext, ArrayList<ItemModel> itemModels) {
        this.mContext = mContext;
        this.itemModels = itemModels;
        this.filterList = itemModels;
    }
    public ListAdapter(Context mContext, ArrayList<ItemModel> itemModels, ItemClickListerner listener) {
        this.mContext = mContext;
        this.itemModels = itemModels;
        this.listener = listener;
    }

    public interface ItemClickListerner {
        void onClick (ItemModel itemModel);
    }
    
    
    @Override
    public int getCount() {
        return itemModels.size();
    }

    @Override
    public Object getItem(int i) {
        return itemModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(item);
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }
}
