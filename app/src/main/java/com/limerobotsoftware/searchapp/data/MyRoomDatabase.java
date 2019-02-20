package com.limerobotsoftware.searchapp.data;

import android.content.Context;
import android.os.AsyncTask;

import com.limerobotsoftware.searchapp.data.dao.ProductDao;
import com.limerobotsoftware.searchapp.data.entity.Product;
import com.limerobotsoftware.searchapp.data.entity.ProductFts;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Product.class, ProductFts.class}, version = 2)
public abstract class MyRoomDatabase extends RoomDatabase {
    private static MyRoomDatabase instance;

    public static MyRoomDatabase getDatabase(final Context context) {
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
            productDao.insert(new Product[] {new Product("572067325", "Keebler", "Chips Deluxe cookies", "15.8 oz", "Enriched flour (wheat flour, niacin, reduced iron, vitamin B1 [thiamin mononitrate], vitamin B2 [riboflavin], folic acid), soybean and palm oil, sugar, semisweet chocolate (chocolate, sugar, dextrose, soy lecithin, artificial flavor), chocolatey chips (sugar, hydrogenated palm kernel oil, cocoa, cocoa processed with alkali, dextrose, soy lecithin). Contains 2% or less of high fructose corn syrup, molasses, corn syrup, salt, baking soda, eggs, artificial flavor, whey protein concentrate.", "https://i5.walmartimages.com/asr/6fec73fa-f49e-41e6-805d-e1b68a1ecd7b_1.be264cfa914115538c438083c00b7368.jpeg"),
                    new Product("571976895", "Pepperidge Farm", "Milano Dark Chocolate Cookies", "7.5 oz", "Made From: Enriched Wheat Flour (Flour, Niacin, Reduced Iron, Thiamine Mononitrate, Riboflavin, Folic Acid), Semi Sweet Chocolate (Sugar, Chocolate, Chocolate Processed With Alkali, Cocoa Butter, Milkfat, Soy Lecithin, Vanilla Extract), Sugar, Palm And/Or Soybean And Hydrogenated Soybean Oils, Eggs, Contains 2% Or Less Of: Cornstarch, Salt, Baking Soda, Soy Lecithin, Natural Flavors, Nonfat Milk. Contains: Wheat, Milk, Soy, Eggs.", "https://i5.walmartimages.com/asr/12aaeb92-1c56-4d8a-8a22-8c84a167dd92_1.b9a3fdc4e8a30b9c7f680ac4b3108000.jpeg"),
                    new Product("6082194", "Google", "Home Mini - Chalk", null, null, "https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6082/6082194_sd.jpg")});
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
