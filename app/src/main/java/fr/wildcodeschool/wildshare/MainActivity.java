package fr.wildcodeschool.wildshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_LOGIN = "EXTRA_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//TODO rentrer bon layout
        final EditText editLogin = findViewById(R.id.edit_pseudo);
        // initialiser les sharedPreferences
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // récupérer le username du cache s'il existe
       String usernameCache = sharedPref.getString("username", "");
        editLogin.setText(usernameCache);

        ImageView imageLogin = findViewById(R.id.image_log);
        imageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editPassword = findViewById(R.id.edit_password);
                String passwordValue = editPassword.getText().toString();
                String loginValue = editLogin.getText().toString();
                if (loginValue.isEmpty() || passwordValue.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your Pseudo and Password", Toast.LENGTH_SHORT).show();
                } else {
                    // enregistrer dans le cache de l'application
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", loginValue);
                    editor.commit();

                    // TODO : initialiser l'utilisateur
                    UserModel username = new UserModel(loginValue, passwordValue);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra(EXTRA_LOGIN, username.getUsername());
                    MainActivity.this.startActivity(intent);
                }

            }
        });
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
}