package fr.wildcodeschool.wildshare;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by wilder on 08/04/18.
 */

public class CustomFilter extends Filter{

    private ArrayList<ItemModel> filterList;
    private ListAdapter listAdapter;


    public CustomFilter(ArrayList<ItemModel> filterList, ListAdapter listAdapter) {

        this.filterList = filterList;
        this.listAdapter = listAdapter;
    }


    public ArrayList<ItemModel> filteredItem;

    public CustomFilter(ArrayList<FriendModel> filterList, FriendListAdapter friendListAdapter) {
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        listAdapter.itemModels = (ArrayList<ItemModel>) results.values;
        listAdapter.notifyDataSetChanged();
    }


}
