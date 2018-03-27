package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ImageView avatar = findViewById(R.id.iv_itemImage);
        EditText itemName = findViewById(R.id.et_newItemName);
        EditText description = findViewById(R.id.et_description);
        Drawable avatarContent = avatar.getDrawable();
        String nameValue = itemName.getText().toString();
        String descriptionValue = description.getText().toString();

        final Parcelable newItem = new ItemModel(nameValue, avatarContent, descriptionValue, null, null, R.color.pink);

        final Intent intent = new Intent(AddItem.this, OwnItemList.class);

        Button add = findViewById(R.id.b_addToData);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.putExtra("new item", newItem);
                startActivity(intent);

            }
        });
    }
}
