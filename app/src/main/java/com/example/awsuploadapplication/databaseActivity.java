package com.example.awsuploadapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.awsuploadapplication.databinding.ActivityDatabaseBinding;
import com.example.awsuploadapplication.db.AppDatabase;
import com.example.awsuploadapplication.db.User;
import com.example.awsuploadapplication.db.UserListAdapter;

import java.util.List;

public class databaseActivity extends AppCompatActivity {
    ActivityDatabaseBinding databaseBinding;

    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_database);

        initRecyclerView();

        loadUserList();
    }
    private void initRecyclerView() {
        databaseBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        databaseBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        userListAdapter = new UserListAdapter(this);
        databaseBinding.recyclerView.setAdapter(userListAdapter);
    }
    private  void loadUserList() {
        AppDatabase database = AppDatabase.getDbInstance(this.getApplicationContext());
        List<User> userList = database.userDao().getAllUsers();
        userListAdapter.setUserList(userList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            loadUserList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}