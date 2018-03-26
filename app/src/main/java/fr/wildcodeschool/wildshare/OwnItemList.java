package fr.wildcodeschool.wildshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class OwnItemList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_item_list);


        // Tableau de valeurs des objets de l'utilisateur
        final ArrayList<ItemModel> itemData = new ArrayList<>();
        itemData.add(new ItemModel("ObjetTest1", R.color.blue, "Description", "ownerFirstame", "ownerLastame", R.color.orange));
        itemData.add(new ItemModel("ObjetTest2", R.color.black, "Description", "ownerFirstame", "ownerLastame", R.color.red));
        itemData.add(new ItemModel("ObjetTest3", R.color.red, "Description", "ownerFirstame", "ownerLastame", R.color.yellow));
        itemData.add(new ItemModel("ObjetTest4", R.color.yellow, "Description", "ownerFirstame", "ownerLastame", R.color.blue));
        itemData.add(new ItemModel("ObjetTest5", R.color.blue, "Description", "ownerFirstame", "ownerLastame", R.color.orange));
        itemData.add(new ItemModel("ObjetTest6", R.color.black, "Description", "ownerFirstame", "ownerLastame", R.color.red));
        itemData.add(new ItemModel("ObjetTest7", R.color.orange, "Description", "ownerFirstame", "ownerLastame", R.color.yellow));

        final ListAdapter adapter = new ListAdapter(this, itemData);
        ListView ownItemList = findViewById(R.id.lv_ownItemList);
        ownItemList.setAdapter(adapter);
    }
}
