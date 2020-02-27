package com.example.smedicine;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.smedicine.bean.PlanDetail;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static int DB_VERSION = 1;

    private Context mContext;

    public static final String CREATE_TABLE = "create table app (login integer DEFAULT 0," +
            "username text,"+
            "mcid text"+
            ")";

    public static final String CREATE_TABLE_PLAN = "create table medicineplan(id integer," +
            "medicinename text," +
            "hour int," +
            "minute int," +
            "requestcode int" +
            ")";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;

    }

    public static Boolean initialize(SQLiteDatabase db,String username,String mcid){
        String sqlstr = "insert into " + " app "+ " (login,username,mcid) values (?,?,?);";
        Object[] args = new Object[]{0,username,mcid};
        try{
            db.execSQL(sqlstr,args);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public static Boolean checkLogin(SQLiteDatabase db){
        Cursor cursor= db.rawQuery("select * from app where login = ?",new String[]{"1"});
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }
    public static Boolean loginLogin(SQLiteDatabase db){

        /**
         * UPDATE table_name
         * SET column1 = value1, column2 = value2...., columnN = valueN
         * WHERE [condition];
         *
         */

        db.execSQL("update app set login = 1  where login = ?",new String[]{"0"});
        return true;
    }

    public static Boolean checkMcid(SQLiteDatabase db){
        Cursor cursor= db.rawQuery("select * from app",null);
        if(cursor.moveToFirst()){
            String mcid = cursor.getString(cursor.getColumnIndex("mcid"));
            if(mcid.equals("null")){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public static String getUsername(SQLiteDatabase db){
        Cursor cursor= db.rawQuery("select * from app",null);
        if(cursor.moveToFirst()){
            String username = cursor.getString(cursor.getColumnIndex("username"));
            return username;
        }
        else {
            return null;
        }
    }

    public static String getMcid(SQLiteDatabase db){
        Cursor cursor= db.rawQuery("select * from app",null);
        if(cursor.moveToFirst()){
            String mcid = cursor.getString(cursor.getColumnIndex("mcid"));
            return mcid;
        }
        else {
            return null;
        }
    }

    public static Boolean updateMcid(SQLiteDatabase db,String mcid){
        //UPDATE COMPANY SET ADDRESS = 'Texas', SALARY = 20000.00;
        db.execSQL("update app set mcid = ?",new String[] {mcid});
        return true;
    }

    public static Boolean updateUsername(SQLiteDatabase db,String username){
        //UPDATE COMPANY SET ADDRESS = 'Texas', SALARY = 20000.00;
        db.execSQL("update app set username = ?",new String[] {username});
        return true;
    }

    public static Boolean emptyMcid(SQLiteDatabase db){
        db.execSQL("update app set mcid = ?",new String[] {"null"});
        return true;
    }

    public static Boolean emptyUsername(SQLiteDatabase db){
        db.execSQL("update app set username = ?",new String[] {"null"});
        return true;
    }

    public static Boolean checkPlan(SQLiteDatabase db,int id){
        Cursor cursor= db.rawQuery("select * from app where id = ?",new String[]{""+id});
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }
    public static PlanDetail getPlan(SQLiteDatabase db, int id){
        PlanDetail planDetail = new PlanDetail();
        Cursor cursor= db.rawQuery("select * from app where id = ?",new String[]{""+id});
        if(cursor.moveToFirst()){
            String medicinename = cursor.getString(cursor.getColumnIndex("medicinename"));
            int hour = cursor.getInt(cursor.getColumnIndex("hour"));
            int minute = cursor.getInt(cursor.getColumnIndex("minute"));
            planDetail.setMedicineName(medicinename);
            planDetail.setHour(hour);
            planDetail.setMinute(minute);
            return planDetail;
        }
        else {
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        initialize(db,"null","null");
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT);
        db.execSQL(CREATE_TABLE_PLAN);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists app");
        db.execSQL("drop table if exists medicineplan");
        onCreate(db);
    }
}
