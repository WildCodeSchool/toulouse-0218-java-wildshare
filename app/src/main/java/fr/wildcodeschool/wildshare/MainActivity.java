package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static String CACHE_USERNAME = "username";
    public static String EXTRA_LOGIN = "EXTRA_LOGIN";

    private EditText editLogin;
    private EditText editPassword;
    private ImageView imageLogin;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//TODO rentrer bon layout
        editLogin = findViewById(R.id.edit_pseudo);
        mAuth = FirebaseAuth.getInstance();
        // initialiser les sharedPreferences
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //récupérer le username du cache s'il existe
        String usernameCache = sharedPref.getString(CACHE_USERNAME, "");
        editLogin.setText(usernameCache);

        imageLogin = findViewById(R.id.image_log);
        imageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword = findViewById(R.id.edit_password);
                String passwordValue = editPassword.getText().toString();
                String loginValue = editLogin.getText().toString();
                if (loginValue.isEmpty() || passwordValue.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.enter_pseudo_password, Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(loginValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


                    // TODO : initialiser l'utilisateur
                    //UserModel username = new UserModel(loginValue, passwordValue);
                    //Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    //intent.putExtra(EXTRA_LOGIN, username.getUsername());
                    //MainActivity.this.startActivity(intent);

            }

        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }
        };

        ImageView imageSignUp = findViewById(R.id.image_signup);
        imageSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editconfirm = findViewById(R.id.edit_confirm);
                final EditText editemail = findViewById(R.id.edit_mail);
                final ImageView buttonSignIn = findViewById(R.id.image_log);
                final CheckBox checkRemember = findViewById(R.id.checkBox_remember);
                ImageView logo = findViewById(R.id.image_logo_fond);
                editconfirm.setVisibility(View.VISIBLE);
                editemail.setVisibility(View.VISIBLE);
                checkRemember.setVisibility(View.GONE);
                buttonSignIn.setVisibility(View.GONE);

                ImageView imageSignUp = findViewById(R.id.image_signup);
                imageSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editconfirm.setVisibility(View.GONE);
                        editemail.setVisibility(View.GONE);
                        checkRemember.setVisibility(View.VISIBLE);
                        buttonSignIn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

}