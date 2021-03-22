package com.example.awsuploadapplication.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract  class AppDatabase extends RoomDatabase {

    static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE conversations ADD COLUMN lastMessageLocalTime INTEGER");
            database.execSQL("UPDATE conversations SET lastMessageLocalTime = lastMessageTime");

        }
    };

    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB Name")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_0_1)
                    .build();
        }
        return INSTANCE;
    }
}
