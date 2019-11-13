package dagundon.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {

    ListView lvUsers;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> all_users;
    DbHepler db;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        lvUsers = findViewById(R.id.lvUsers);
        db = new DbHepler(this);
        fetch_users();

        shared = getSharedPreferences("meh", Context.MODE_PRIVATE);
    }

    private void fetch_users() {

        all_users = db.getAllUsers();
        adapter = new ListViewAdapter(this, R.layout.adapter_user, all_users);
        lvUsers.setAdapter(adapter);
        registerForContextMenu(lvUsers);
    }

    private class ListViewAdapter extends ArrayAdapter {

        LayoutInflater inflater;
        TextView tvFullname, tvUsername;
        ImageView btnEdit, btnDelete;

        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> all_users) {
            super(context, resource, all_users);
            inflater = LayoutInflater.from(context);

        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.adapter_user, parent,false);
            tvFullname = convertView.findViewById(R.id.tvFullname);
            tvUsername = convertView.findViewById(R.id.tvUsername);
            btnEdit = convertView.findViewById(R.id.btnEdit);
            btnDelete = convertView.findViewById(R.id.btnDelete);

            tvFullname.setText(all_users.get(position).get(db.TBL_USER_FULLNAME));
            tvUsername.setText(all_users.get(position).get(db.TBL_USER_USERNAME));

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));db.deleteUser(userID);
                    Toast.makeText(DisplayActivity.this, "User Successfully Deleted", Toast.LENGTH_SHORT).show();
                    fetch_users();
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));

                    Intent intent = new Intent(getContext(), EditUserActivity.class);
                    intent.putExtra(db.TBL_USER_ID,userID);

                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnLogout:
                shared.edit().remove(db.TBL_USER_ID).commit();
                startActivity(new Intent(this, MainActivity.class));
                DisplayActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        fetch_users();
        if(!shared.contains(db.TBL_USER_ID)){
            this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onResume();
    }
}
