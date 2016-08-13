package org.vscan;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBHelper {
    public static void populateDB(Context context,SQLiteDatabase db)
    {
        String mCSVfile = "vscan_db.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try
        {
            inStream = manager.open(mCSVfile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        db.execSQL("DROP TABLE product_tbl;");
        db.execSQL("CREATE TABLE IF NOT EXISTS product_tbl(" +
                "barcode VARCHAR," +
                "name VARCHAR," +
                "company VARCHAR," +
                "food_type VARCHAR," +
                "product_type VARCHAR," +
                "is_vegan VARCHAR," +
                "why_not VARCHAR," +
                "image VARCHAR);");
        Cursor c=db.rawQuery("SELECT * FROM product_tbl", null);
        if(c.getCount()==0)
        {
            try
            {
                String line = "";
                db.beginTransaction();
                try
                {
                    while ((line = buffer.readLine()) != null)
                    {
                        String[] colums = line.split(",");
                        if (colums.length != 8) {
                            Log.d("CSVParser", "Skipping Bad CSV Row");
                            continue;
                        }
                        ContentValues cv = new ContentValues(3);
                        cv.put("barcode", colums[0].trim());
                        cv.put("name", colums[1].trim());
                        Log.d("CSVParser", colums[1].trim());
                        cv.put("company", colums[2].trim());
                        cv.put("food_type", colums[3].trim());
                        cv.put("product_type", colums[4].trim());
                        cv.put("is_vegan", colums[5].trim());
                        cv.put("why_not", colums[6].trim());
                        cv.put("image", colums[7].trim());
                        db.insert("product_tbl", null, cv);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            catch (Exception ex)
            {
            }
        }
    }
}
