package dagundon.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class EditUserActivity extends AppCompatActivity {


    EditText etUsername, etFullname;
    String username, fullname;
    int formsuccess, userid;
    DbHepler db;

    ArrayList<HashMap<String, String>> select_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new DbHepler(this);

        etUsername = findViewById(R.id.etUsername);
        etFullname = findViewById(R.id.etFullname);

        Intent intent = getIntent();
        userid = intent.getIntExtra(db.TBL_USER_ID, 0);

        select_user = db.getSelectUser(userid);

        etUsername.setText(select_user.get(0).get(db.TBL_USER_USERNAME));
        etFullname.setText(select_user.get(0).get(db.TBL_USER_FULLNAME));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSave:
                formsuccess = 2;
                username = etUsername.getText().toString();
                fullname = etUsername.getText().toString();

                if (username.equals("")){
                    etUsername.setError("This field is required!");
                    formsuccess--;
                }
                if (username.equals("")){
                    etFullname.setError("This field is required!");
                    formsuccess--;
                }
                if (formsuccess==2){
                    HashMap<String, String> map_user = new HashMap();

                    map_user.put(db.TBL_USER_ID,String.valueOf(userid));
                    map_user.put(db.TBL_USER_USERNAME,username);
                    map_user.put(db.TBL_USER_FULLNAME,fullname);

                    db.updateUser(map_user);
                    Toast.makeText(this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                    this.finish();
                }

                break;
            case R.id.btnCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
