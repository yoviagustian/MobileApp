package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteFullException;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;

public class LocalDBEncryption {

    private Context context;
    private SQLiteDatabase database;

    //private static Map<String, String> tempData = new HashMap<String, String>();
    private static final String DATABASE_NAME = "testing";    // Database Name
    private static final String TABLE_NAME = "item";   // Table Name

    private static final String CREATE_TABLE = "CREATE TABLE  " + TABLE_NAME +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "kodeItem    VARCHAR(255) ," +
            "namaItem    VARCHAR(225) ," +
            "keterangan  VARCHAR(225) ," +
            "hargaModal  INTEGER ," +
            "hargaJual   INTEGER ," +
            "stock       INTEGER ," +
            "minStock    INTEGER );";

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

    public String readData(String kodeItem)
    {
        String  result = null;

        try {

            String[] columns = {"namaItem"};
            String[] whereArgs ={kodeItem};
            Cursor cursor = database.query(TABLE_NAME,columns,  "kodeItem = ?",
                    whereArgs,null,null,null);

            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("namaItem"));


        }catch (SQLiteFullException e){
            //Log.d("SQLiteStatus", "ERROR : " + e);
            return null;
        }
        return result;
    }

    public ArrayList<String> readAllData()
    {

        ArrayList<String> result =new ArrayList<>();

        try {

            String[] columns = {"namaItem"};
            Cursor cursor = database.rawQuery("select * from "+TABLE_NAME, null);

            while(cursor.moveToNext())
            {
                result.add(cursor.getString(cursor.getColumnIndex("namaItem")));
            }

        }catch (SQLiteFullException e){
            return null;
        }
        return result;
    }

}
