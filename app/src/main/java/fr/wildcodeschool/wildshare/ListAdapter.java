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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wilder on 26/03/18.
 */

public class ListAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    public ArrayList<ItemModel> itemModels;
    private String itemId;
    private String from;

    private CustomFilter filter;
    private ArrayList<ItemModel> filterList;
    private ItemClickListerner listener;

    public ListAdapter(Context mContext, ArrayList<ItemModel> itemModels) {
        this.mContext = mContext;
        this.itemModels = itemModels;
        this.filterList = itemModels;
    }

    public ListAdapter(Context mContext, ArrayList<ItemModel> itemModels, String from, ItemClickListerner listener) {
        this.mContext = mContext;
        this.itemModels = itemModels;
        this.from = from;
        this.listener = listener;
        this.filterList = itemModels;
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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User");
        final DatabaseReference itemRef = database.getReference("Item");
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String itemNameM = item.getName();
        final String ownerIdM = item.getOwnerId();
        final DatabaseReference ownerItemRef = database.getReference("User").child(ownerIdM).child("Item");
        final DatabaseReference userBorrowedRef = database.getReference("User").child(userId).child("Borrowed");

        TextView itemName = convertView.findViewById(R.id.tv_item_name);
        ImageView itemImage = convertView.findViewById(R.id.iv_item_image);
        final ImageView ownerImage = convertView.findViewById(R.id.iv_owner);
        final ImageButton actionButton = convertView.findViewById(R.id.b_add);

        itemName.setText(item.getName());
        Glide.with(mContext).load(item.getImage()).apply(RequestOptions.circleCropTransform()).into(itemImage);
        Glide.with(mContext).load(item.getOwnerProfilPic()).apply(RequestOptions.circleCropTransform()).into(ownerImage);

        if (from.equals("freeItem")) {


            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                                final ItemModel itemModelValue = itemSnapshot.getValue(ItemModel.class);

                                if (itemModelValue.getName().equals(itemNameM) && itemModelValue.getOwnerId().equals(ownerIdM)) {
                                    itemId = itemSnapshot.getKey();
                                    ownerItemRef.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String itemValue = dataSnapshot.getValue().toString();
                                            if (itemValue.equals("0")) {
                                                ownerItemRef.child(itemId).setValue(userId);
                                                userBorrowedRef.child(itemId).setValue(itemModelValue.getOwnerId());
                                            }
                                            else {
                                                Toast.makeText(mContext, R.string.item_already_borrowed, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    
                                }
                            }
                            listener.onUpdate();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });
            //return convertView;
        } else if (from.equals("myBorrowed")) {
            actionButton.setBackgroundResource(R.drawable.rendre_min);

            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                                ItemModel itemModelValue = itemSnapshot.getValue(ItemModel.class);

                                if (itemModelValue.getName().equals(itemNameM) && itemModelValue.getOwnerId().equals(ownerIdM)) {
                                    itemId = itemSnapshot.getKey();
                                    ownerItemRef.child(itemId).setValue("0");
                                    userBorrowedRef.child(itemId).removeValue();
                                }
                            }
                            listener.onUpdate();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            });
        } else {

            actionButton.setBackgroundResource(R.drawable.rendre_min);
            ownerImage.setVisibility(View.GONE);
            actionButton.setVisibility(View.GONE);

            final DatabaseReference myItemRef = database.getReference("User")
                    .child(userId).child("Item").child(item.getItemId());
            myItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.getValue(String.class).equals("0")) {
                                final String itemId = dataSnapshot.getKey();
                                final String friendId = dataSnapshot.getValue(String.class);

                                userRef.child(friendId).child("Profil").child("profilPic")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String friendPhoto = dataSnapshot.getValue().toString();
                                                Glide.with(mContext).load(friendPhoto)
                                                        .apply(RequestOptions.circleCropTransform()).into(ownerImage);
                                                ownerImage.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });





                                actionButton.setVisibility(View.VISIBLE);
                                actionButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        userRef.child(friendId).child("Borrowed").child(itemId).removeValue();
                                        myItemRef.setValue("0");
                                        listener.onUpdate();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }

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

    public interface ItemClickListerner {
        void onClick(ItemModel itemModel);

        void onUpdate();
    }

}
