package fr.wildcodeschool.wildshare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wilder on 24/04/18.
 */

public class PopUpAddFriends extends AppCompatDialogFragment {
    private EditText etpseudo;
    private AddFriendListener mListener = null;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_search_friend, null);

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
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String userId = user.getUid();

                        // recherche un utilisateur avec ce pseudo
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference("User")
                                .orderByChild("Profil/pseudo").equalTo(pseudoAdd)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot friendDS : dataSnapshot.getChildren()) {
                                                String friendId = friendDS.getKey();

                                                database.getReference("User").child(userId)
                                                        .child("Friends").child(friendId)
                                                        .setValue(true);

                                                // ajout mutuel
                                                database.getReference("User").child(friendId)
                                                        .child("Friends").child(userId)
                                                        .setValue(true);

                                                mListener.onSuccess();
                                            }
                                        } else if (mListener != null) {
                                            // affichage du message d'erreur
                                            mListener.onError(pseudoAdd);
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

    public void setListener(AddFriendListener listener) {
        mListener = listener;
    }

    public interface AddFriendListener {

        void onError(String pseudo);
        void onSuccess();
    }
}