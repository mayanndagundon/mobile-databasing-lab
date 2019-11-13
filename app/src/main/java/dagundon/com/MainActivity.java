package dagundon.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvCreateAccount;
    int formsuccess;
    String username, password;

    SharedPreferences shared;

    DbHepler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHepler(this);

        shared = getSharedPreferences("meh", Context.MODE_PRIVATE);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        btnLogin.setOnClickListener(this);
        tvCreateAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                formsuccess = 2;
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.equals("")){
                    etUsername.setError("This field is required!");
                    formsuccess--;
                }
                if (password.equals("")){
                    etPassword.setError("This field is required");
                    formsuccess--;
                }
                if (formsuccess==2){
                    HashMap<String, String> map_user = new HashMap<>();
                    map_user.put(db.TBL_USER_USERNAME, username);
                    map_user.put(db.TBL_USER_PASSWORD, password);

                    if(db.checkUser(map_user)>0){
                        startActivity(new Intent(this, DisplayActivity.class));
                    }
                    else{
                        etUsername.setError("Invalid Credentials");
                    }
                }
                break;
            case R.id.tvCreateAccount:
               startActivity(new Intent(this, CreateAccountActivity.class));
               break;
        }
    }

    @Override
    protected void onResume() {
        if (shared.contains(db.TBL_USER_ID)){
            this.finish();
            startActivity(new Intent(this, DisplayActivity.class));
        }
        super.onResume();
    }
}
