package com.example.awsuploadapplication.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Insert
    void InsertUser(User...users);

    @Update
    void updateUser(User user);

    @Delete
    void delete(User user);

}
