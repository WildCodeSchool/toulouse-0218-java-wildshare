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
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            filteredItem = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                    filteredItem.add(filterList.get(i));
                }
            }

            results.count = filteredItem.size();
            results.values = filteredItem;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.itemModels = (ArrayList<ItemModel>) results.values;
        adapter.notifyDataSetChanged();
    }


}
