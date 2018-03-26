package fr.wcs.testprojet2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static String EXTRA_LOGIN = "EXTRA_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                    Toast.makeText(LoginActivity.this, "Please enter your Pseudo and Password", Toast.LENGTH_SHORT).show();;
                } else {
                    // enregistrer dans le cache de l'application
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", loginValue);
                    editor.commit();

                    // TODO : initialiser l'utilisateur
                    UserModel username = new UserModel(loginValue, passwordValue);

                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    intent.putExtra(EXTRA_LOGIN, username.getUsername());
                    LoginActivity.this.startActivity(intent);
                }

            }
        });
    }
}
