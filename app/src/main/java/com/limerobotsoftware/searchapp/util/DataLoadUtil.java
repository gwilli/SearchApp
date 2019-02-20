package com.limerobotsoftware.searchapp.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.limerobotsoftware.searchapp.R;
import com.limerobotsoftware.searchapp.data.dao.ProductDao;
import com.limerobotsoftware.searchapp.data.entity.Product;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataLoadUtil {
    private static final String TAG = "DataLoadUtil";

    public static int loadDataFile(Context context, ProductDao productDao, int maxRecords){
        int rowsLoaded = 0;
        InputStream instream = null;
        try {
            boolean firstRow = true;
            instream = context.getResources().openRawResource(R.raw.items);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            for (String str = reader.readLine(); str != null; str = reader.readLine()) {
                if (!TextUtils.isEmpty(str)) {
                    if (!firstRow) { //skip header
                        String[] columns = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        if (columns.length > 4 && isNumeric(columns[0])) {
                            String imageUrl = columns.length > 26 ? columns[26] : "";
                            imageUrl = imageUrl.replaceAll("\"", "").split(",")[0];
                            Product product = new Product(columns[0], columns[1], columns[2], columns[3], columns[4], imageUrl);
                            Log.i(TAG, product.toString());
                            productDao.insert(product);
                            rowsLoaded++;

                            if (rowsLoaded >= maxRecords) {
                                break;
                            }
                        }
                    }
                    firstRow = false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "error loading file to database", e);
        } finally {
            try {
                if (instream != null) instream.close();
            } catch (Exception e) {} //don't care about exception on close()
        }

        return rowsLoaded;
    }

    public static boolean isNumeric(String s) {
        boolean result = false;
        try {
            Long.parseLong(s);
            result = true;
        } catch (Exception e) {
            // ignore
        }
        return result;
    }
}
