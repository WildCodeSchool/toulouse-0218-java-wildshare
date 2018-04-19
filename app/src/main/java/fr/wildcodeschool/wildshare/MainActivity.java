package fr.wildcodeschool.wildshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static String CACHE_USERNAME = "username";
    public static String EXTRA_LOGIN = "EXTRA_LOGIN";

    private EditText mEditEmail;
    private EditText mEditPassword;
    private EditText mNewEmail;
    private EditText mNewPassword;
    private Button mButtonLogin;
    Button mButtonSignUp;
    Button mBtnNewAccount;
    ProgressBar mProgressBar;
    Button mButtonReturn;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressbar);
        mEditEmail = findViewById(R.id.edit_email);
        mEditPassword = findViewById(R.id.edit_password);
        mButtonLogin = findViewById(R.id.b_singin);
        mButtonSignUp = findViewById(R.id.b_signup);

        //initialiser les sharedPreferences
        //final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //récupérer le username du cache s'il existe
        //String usernameCache = sharedPref.getString(CACHE_USERNAME, "");
        //editLogin.setText(usernameCache);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginValue = mEditEmail.getText().toString();
                String passwordValue = mEditPassword.getText().toString();
                mProgressBar.setVisibility(view.VISIBLE);
                if (loginValue.isEmpty() || passwordValue.isEmpty()) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, R.string.enter_pseudo_password, Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(loginValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressBar.setVisibility(View.GONE);
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




        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewEmail = findViewById(R.id.new_mail);
                mNewPassword = findViewById(R.id.new_password);
                mBtnNewAccount = findViewById(R.id.btn_new_account);
                mButtonReturn = findViewById(R.id.b_return);
                mButtonSignUp.setVisibility(View.GONE);
                mButtonReturn.setVisibility(View.VISIBLE);
                mNewPassword.setVisibility(View.VISIBLE);
                mNewEmail.setVisibility(View.VISIBLE);
                mBtnNewAccount.setVisibility(View.VISIBLE);
                mEditEmail.setVisibility(View.GONE);
                mEditPassword.setVisibility(View.GONE);
                mButtonLogin.setVisibility(View.GONE);

                mBtnNewAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailN = mNewEmail.getText().toString();
                        String passwordN = mNewPassword.getText().toString();
                        if (TextUtils.isEmpty(emailN) || (TextUtils.isEmpty(passwordN))){
                            Toast.makeText(MainActivity.this, R.string.enter_both_values, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mAuth.createUserWithEmailAndPassword(emailN, passwordN).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, R.string.account_create, Toast.LENGTH_SHORT).show();
                                        Intent intentProfil = new Intent(MainActivity.this, ProfilActivity.class);
                                        startActivity(intentProfil);
                                    }
                                }
                            });
                        }
                    }
                });


                mButtonReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNewPassword.setVisibility(View.GONE);
                        mNewEmail.setVisibility(View.GONE);
                        mBtnNewAccount.setVisibility(View.GONE);
                        mButtonSignUp.setVisibility(View.VISIBLE);
                        mButtonReturn.setVisibility(View.GONE);
                        mEditEmail.setVisibility(View.VISIBLE);
                        mEditPassword.setVisibility(View.VISIBLE);
                        mButtonLogin.setVisibility(View.VISIBLE);



                    }
                });
            }
        });



        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
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
        mAuth.addAuthStateListener(mAuthStateListener);
    }

}