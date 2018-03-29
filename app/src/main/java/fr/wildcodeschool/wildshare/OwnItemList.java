package fr.wildcodeschool.wildshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class OwnItemList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tabbed);
        if (getIntent().getParcelableArrayExtra("new item") == null) {

            // Database objets de l'utilisateur
            final ArrayList<ItemModel> itemData = new ArrayList<>();
            itemData.add(new ItemModel("ObjetTest1", null, "Description", "ownerFirstame", "ownerLastame", R.color.orange));
            itemData.add(new ItemModel("ObjetTest2", null, "Description", "ownerFirstame", "ownerLastame", R.color.red));
            itemData.add(new ItemModel("ObjetTest3", null, "Description", "ownerFirstame", "ownerLastame", R.color.yellow));

            final ListAdapter adapter = new ListAdapter(this, itemData);
            ListView ownItemList = findViewById(R.id.lv_own_item_list);
            ownItemList.setAdapter(adapter);

        } else {

            ItemModel newItem = getIntent().getParcelableExtra("new item");

            // Database objets de l'utilisateur
            final ArrayList<ItemModel> itemData = new ArrayList<>();
            itemData.add(new ItemModel("ObjetTest1", null, "Description", "ownerFirstame", "ownerLastame", R.color.orange));
            itemData.add(new ItemModel("ObjetTest2", null, "Description", "ownerFirstame", "ownerLastame", R.color.red));
            itemData.add(new ItemModel("ObjetTest3", null, "Description", "ownerFirstame", "ownerLastame", R.color.yellow));

            //Récupération des données depuis AddItem
            itemData.add(newItem);

            final ListAdapter adapter = new ListAdapter(this, itemData);
            ListView ownItemList = findViewById(R.id.lv_own_item_list);
            ownItemList.setAdapter(adapter);

        }



    }
}
