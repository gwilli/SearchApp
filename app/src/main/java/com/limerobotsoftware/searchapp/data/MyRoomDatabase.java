package com.limerobotsoftware.searchapp.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.limerobotsoftware.searchapp.data.dao.ProductDao;
import com.limerobotsoftware.searchapp.data.entity.Product;
import com.limerobotsoftware.searchapp.util.DataLoadUtil;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {
    private static final String TAG = "MyRoomDatabase";
    private static MyRoomDatabase instance;
    private static Context appContext;

    public static MyRoomDatabase getDatabase(final Context context) {
        appContext = context.getApplicationContext();
        if (instance == null) {
            synchronized (MyRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                MyRoomDatabase.class, "productDatabase.db")
                                .addCallback(roomDatabaseCallback)
                                .build();
                }
            }
        }

        return instance;
    }

    public abstract ProductDao productDao();

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(instance).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private ProductDao productDao;

        public PopulateDbAsync(MyRoomDatabase database) {
            productDao = database.productDao();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.v(TAG, String.format("loaded %d rows", DataLoadUtil.loadDataFile(appContext, productDao, 1000)));
            return null;
        }
    }
}
