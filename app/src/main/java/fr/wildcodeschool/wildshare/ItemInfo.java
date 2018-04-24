package fr.wildcodeschool.wildshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        ImageView itemImage = findViewById(R.id.iv_item_image);
        TextView itemName = findViewById(R.id.tv_item_name);
        TextView itemDescription = findViewById(R.id.tv_item_description);

    }
}
