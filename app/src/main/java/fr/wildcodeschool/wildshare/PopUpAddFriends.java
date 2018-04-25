package fr.wildcodeschool.wildshare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wilder on 24/04/18.
 */

public class PopUpAddFriends extends AppCompatDialogFragment {
    private EditText etpseudo;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_search_friend,null);

        builder.setView(view)
                .setTitle(R.string.add_friends)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etpseudo = view.findViewById(R.id.et_pseudo_search);
                        final String pseudoAdd = etpseudo.getText().toString();
                        final DatabaseReference pathUser = FirebaseDatabase.getInstance().getReference("User");
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        pathUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot itemDataSnapshot : dataSnapshot.getChildren()) {
                                    String pseudoComp = itemDataSnapshot.child("Profil").child("pseudo").getValue().toString();
                                    if (pseudoAdd.equals(pseudoComp)) {
                                        String stringKey = itemDataSnapshot.getKey().toString();
                                        pathUser.child(user.getUid()).child("Friends").child(stringKey).setValue("true");
                                        return;
                                    }
                                    //else {
                                        //Toast.makeText(getContext(), R.string.pseudo_do_not_exist, Toast.LENGTH_SHORT).show();
                                    //}
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });






                    }
                });
        return builder.create();
    }

}