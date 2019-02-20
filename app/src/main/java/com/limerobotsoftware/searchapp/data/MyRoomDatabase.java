package com.limerobotsoftware.searchapp.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.limerobotsoftware.searchapp.data.dao.ProductDao;
import com.limerobotsoftware.searchapp.data.entity.Product;
import com.limerobotsoftware.searchapp.data.entity.ProductFts;
import com.limerobotsoftware.searchapp.util.DataLoadUtil;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class, ProductFts.class}, version = 2)
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
                                .addMigrations(MIGRATION_ADD_FTS)
                                .fallbackToDestructiveMigration()
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

    private static final Migration MIGRATION_ADD_FTS = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // create the new virtual table
            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS product_fts USING FTS4(`upc` TEXT, `brand_name` TEXT, `name` TEXT, `ingredients` TEXT, content=`product`)");
            // initially populate the virtual table from the product table
            database.execSQL("INSERT INTO product_fts(product_fts) VALUES ('rebuild')");
            // add triggers before update & delete and after update & insert to update the records in the virtual content table
            database.execSQL("CREATE TRIGGER product_bu BEFORE UPDATE ON product BEGIN DELETE FROM product_fts where `docid` = old.`rowid`; END;");
            database.execSQL("CREATE TRIGGER product_bd BEFORE DELETE ON product BEGIN DELETE FROM product_fts where `docid` = old.`rowid`; END;");
            database.execSQL("CREATE TRIGGER product_au AFTER UPDATE ON product BEGIN INSERT INTO product_fts (`docid`, `upc`, `brand_name`, `name`, `ingredients`) VALUES (new.`rowid`, new.`upc`, new.`brand_name`, new.`name`, new.`ingredients`); END;");
            database.execSQL("CREATE TRIGGER product_ai AFTER INSERT ON product BEGIN INSERT INTO product_fts (`docid`, `upc`, `brand_name`, `name`, `ingredients`) VALUES (new.`rowid`, new.`upc`, new.`brand_name`, new.`name`, new.`ingredients`); END;");
        }
    };
}
