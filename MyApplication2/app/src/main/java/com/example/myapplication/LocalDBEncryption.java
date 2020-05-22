package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteFullException;

import java.io.File;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class LocalDBEncryption {

    private Context context;
    private SQLiteDatabase database;

    //private static Map<String, String> tempData = new HashMap<String, String>();
    private static final String DATABASE_NAME = "testing";    // Database Name
    private static final String TABLE_NAME = "item";   // Table Name
    private static final String TABLE_NAME1 = "history";   // Table Name

    private static final String CREATE_TABLE = "CREATE TABLE  " + TABLE_NAME +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "kodeItem    VARCHAR(255) ," +
            "namaItem    VARCHAR(225) ," +
            "keterangan  VARCHAR(225) ," +
            "hargaModal  INTEGER ," +
            "hargaJual   INTEGER ," +
            "stock       INTEGER ," +
            "minStock    INTEGER );";

    private static final String CREATE_TABLE1 = "CREATE TABLE  " + TABLE_NAME1 +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "kodeItem    VARCHAR(255) ," +
            "namaItem    VARCHAR(225) ," +
            "hargaModal  INTEGER ," +
            "hargaJual   INTEGER ," +
            "jumlah       INTEGER );";

    //private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    public LocalDBEncryption(Context context){
        this.context = context;
        InitializeSQLCipher();
    }

    private void InitializeSQLCipher() {
        File databaseFile = context.getDatabasePath(DATABASE_NAME);
        SQLiteDatabase.loadLibs(context);

        if(!databaseFile.exists()) {

            databaseFile.mkdirs();
            databaseFile.delete();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, GetKeyPass(), null);
            database.execSQL(CREATE_TABLE);
            database.execSQL(CREATE_TABLE1);

            //Log.d("SQLiteStatus", "Creating");
        }else{
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, GetKeyPass(), null);
            //Log.d("SQLiteStatus","ALREADY CREATE READY");
        }
    }

    public static String GetKeyPass() {
        try {
            String password = "s52f452j3gdf32fbd7723tgf235bgfddy53r";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            //convert the byte to hex format
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            //System.out.println("Digest(in hex format):: " + sb.toString());
            //convert the byte to hex format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            //System.out.println("Digest(in hex format):: " + hexString.toString());
            return hexString.toString();
        } catch (Exception e) {
        }
        return "";
    }

    public boolean createData(String kode, String nama, String keterangan, Integer hargaModal, Integer hargaJual, Integer stock, Integer minStock)
    {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("kodeItem", kode);
            contentValues.put("namaItem", nama);
            contentValues.put("keterangan", keterangan);
            contentValues.put("hargaModal", hargaModal);
            contentValues.put("hargaJual", hargaJual);
            contentValues.put("stock", stock);
            contentValues.put("minStock", minStock);
            long id = database.insert(TABLE_NAME, null , contentValues);
//            Log.d("SQLiteStatus","INSERT : " + id);
            if(id > 0){return true;}

        }catch (Exception e){
//            Log.d("SQLiteStatus", "ERROR : " + e);
            return false;
        }
        return false;
    }

    public boolean createHistory(String kode, String nama, Integer hargaModal, Integer hargaJual, Integer jumlah)
    {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("kodeItem", kode);
            contentValues.put("namaItem", nama);
            contentValues.put("hargaModal", hargaModal);
            contentValues.put("hargaJual", hargaJual);
            contentValues.put("jumlah", jumlah);
            long id = database.insert(TABLE_NAME1, null , contentValues);
//            Log.d("SQLiteStatus","INSERT : " + id);
            if(id > 0){return true;}

        }catch (Exception e){
//            Log.d("SQLiteStatus", "ERROR : " + e);
            return false;
        }
        return false;
    }

    public class item implements Serializable {
        Integer _id;
        String _kode;
        String _nama;
        String _keterangan;
        Integer _hargaModal;
        Integer _hargaJual;
        Integer _stock;
        Integer _minStock;
        public item(Integer id, String kode, String nama, String keterangan, Integer hargaModal, Integer hargaJual, Integer stock, Integer minStock){
            _id = id;
            _kode = kode;
            _nama = nama;
            _keterangan = keterangan;
            _hargaModal = hargaModal;
            _hargaJual = hargaJual;
            _stock = stock;
            _minStock = minStock;
        }
    }

    public class history implements Serializable {
        Integer _id;
        String _kode;
        String _nama;
        Integer _hargaModal;
        Integer _hargaJual;
        Integer _jumlah;
        public history(Integer id, String kode, String nama, Integer hargaModal, Integer hargaJual, Integer jumlah){
            _id = id;
            _kode = kode;
            _nama = nama;
            _hargaModal = hargaModal;
            _hargaJual = hargaJual;
            _jumlah = jumlah;
        }
    }

    public item readData(String id)
    {
        try {
            String[] whereArgs ={id};
            Cursor cursor = database.rawQuery("SELECT * FROM item WHERE id = ?", whereArgs);
            cursor.moveToFirst();

            Integer ids = cursor.getInt(cursor.getColumnIndex("id"));
            String kode = cursor.getString(cursor.getColumnIndex("kodeItem"));
            String nama = cursor.getString(cursor.getColumnIndex("namaItem"));
            String keterangan = cursor.getString(cursor.getColumnIndex("keterangan"));
            Integer hargaModal = cursor.getInt(cursor.getColumnIndex("hargaModal"));
            Integer hargaJual = cursor.getInt(cursor.getColumnIndex("hargaJual"));
            Integer stock = cursor.getInt(cursor.getColumnIndex("stock"));
            Integer minStock = cursor.getInt(cursor.getColumnIndex("minStock"));

            item itm = new item(ids, kode, nama, keterangan, hargaModal, hargaJual, stock, minStock);
            return itm;
        }catch (SQLiteFullException e) {
            return null;
        }
    }

    public List<item> readAllData()
    {
        List<item> result = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("select * from "+TABLE_NAME, null);
            while(cursor.moveToNext())
            {
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String kode = cursor.getString(cursor.getColumnIndex("kodeItem"));
                String nama = cursor.getString(cursor.getColumnIndex("namaItem"));
                String keterangan = cursor.getString(cursor.getColumnIndex("keterangan"));
                Integer hargaModal = cursor.getInt(cursor.getColumnIndex("hargaModal"));
                Integer hargaJual = cursor.getInt(cursor.getColumnIndex("hargaJual"));
                Integer stock = cursor.getInt(cursor.getColumnIndex("stock"));
                Integer minStock = cursor.getInt(cursor.getColumnIndex("minStock"));

                item itm = new item(id, kode, nama, keterangan, hargaModal, hargaJual, stock, minStock);
                result.add(itm);
            }
        }catch (SQLiteFullException e){
            return null;
        }
        return result;
    }

    public List<history> readAllHistory()
    {
        List<history> result = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("select * from "+TABLE_NAME1, null);
            while(cursor.moveToNext())
            {
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String kode = cursor.getString(cursor.getColumnIndex("kodeItem"));
                String nama = cursor.getString(cursor.getColumnIndex("namaItem"));
                Integer hargaModal = cursor.getInt(cursor.getColumnIndex("hargaModal"));
                Integer hargaJual = cursor.getInt(cursor.getColumnIndex("hargaJual"));
                Integer jumlah = cursor.getInt(cursor.getColumnIndex("jumlah"));

                history itm = new history(id, kode, nama, hargaModal, hargaJual, jumlah);
                result.add(itm);
            }
        }catch (SQLiteFullException e){
            return null;
        }
        return result;
    }

    public boolean updateData(String id, String kode, String nama, String keterangan, Integer hargaModal, Integer hargaJual, Integer stock, Integer minStock)
    {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("kodeItem", kode);
            contentValues.put("namaItem", nama);
            contentValues.put("keterangan", keterangan);
            contentValues.put("hargaModal", hargaModal);
            contentValues.put("hargaJual", hargaJual);
            contentValues.put("stock", stock);
            contentValues.put("minStock", minStock);
            long cek = database.update(TABLE_NAME,contentValues,"id="+id, null);
//            Log.d("SQLiteStatus","INSERT : " + id);
            if(cek > 0){return true;}

        }catch (Exception e){
//            Log.d("SQLiteStatus", "ERROR : " + e);
            return false;
        }
        return false;
    }

    public boolean deleteData(String id)
    {
        try {
            long cek = database.delete(TABLE_NAME,"id="+id, null);
//            Log.d("SQLiteStatus","INSERT : " + id);
            if(cek > 0){return true;}

        }catch (Exception e){
//            Log.d("SQLiteStatus", "ERROR : " + e);
            return false;
        }
        return false;
    }

}
