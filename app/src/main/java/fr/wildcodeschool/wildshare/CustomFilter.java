package fr.wildcodeschool.wildshare;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by wilder on 08/04/18.
 */

public class CustomFilter extends Filter{

    private ArrayList<ItemModel> filterList;
    private ListAdapter adapter;


    public CustomFilter(ArrayList<ItemModel> filterList, ListAdapter adapter) {

        this.filterList = filterList;
        this.adapter = adapter;
    }

    /** Création de la searchview qui va nous permettre de chercher un monstre par son nom
     * La searchview créait une nouvelle liste de monstres en les filtrant
     */
    public ArrayList<ItemModel> filteredItem;

    public CustomFilter(ArrayList<FriendModel> filterList, FriendListAdapter friendListAdapter) {
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.itemModels = (ArrayList<ItemModel>) results.values;
        adapter.notifyDataSetChanged();
    }


}
