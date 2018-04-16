package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static String CACHE_USERNAME = "username";
    public static String EXTRA_LOGIN = "EXTRA_LOGIN";

    private EditText editEmail;
    private EditText editPassword;
    private EditText newEmail;
    private EditText newPassword;
    private ImageView imageLogin;
    ImageView imageSignUp;
    Button btnNewAccount;
    ProgressBar progressBar;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        imageLogin = findViewById(R.id.image_log);
        imageSignUp = findViewById(R.id.image_signup);

        //initialiser les sharedPreferences
        //final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //récupérer le username du cache s'il existe
        //String usernameCache = sharedPref.getString(CACHE_USERNAME, "");
        //editLogin.setText(usernameCache);

        imageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginValue = editEmail.getText().toString();
                String passwordValue = editPassword.getText().toString();
                progressBar.setVisibility(view.VISIBLE);
                if (loginValue.isEmpty() || passwordValue.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, R.string.enter_pseudo_password, Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(loginValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
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




        imageSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEmail = findViewById(R.id.new_mail);
                newPassword = findViewById(R.id.new_password);
                btnNewAccount = findViewById(R.id.btnNewAccount);
                final ImageView buttonSignIn = findViewById(R.id.image_log);

                ImageView logo = findViewById(R.id.image_logo_fond);
                newPassword.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.VISIBLE);
                btnNewAccount.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.GONE);
                editPassword.setVisibility(View.GONE);
                buttonSignIn.setVisibility(View.GONE);

                btnNewAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailN = newEmail.getText().toString();
                        String passwordN = newPassword.getText().toString();
                        if (TextUtils.isEmpty(emailN) || (TextUtils.isEmpty(passwordN))){
                            Toast.makeText(MainActivity.this, "Enter both values", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mAuth.createUserWithEmailAndPassword(emailN, passwordN).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Account create", Toast.LENGTH_SHORT).show();
                                        Intent intentProfil = new Intent(MainActivity.this, ProfilActivity.class);
                                        startActivity(intentProfil);
                                    }
                                }
                            });
                        }
                    }
                });

                imageSignUp = findViewById(R.id.image_signup);
                imageSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPassword.setVisibility(View.GONE);
                        newEmail.setVisibility(View.GONE);
                        btnNewAccount.setVisibility(View.GONE);
                        buttonSignIn.setVisibility(View.VISIBLE);
                        editEmail.setVisibility(View.VISIBLE);
                        editPassword.setVisibility(View.VISIBLE);


                    }
                });
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

}