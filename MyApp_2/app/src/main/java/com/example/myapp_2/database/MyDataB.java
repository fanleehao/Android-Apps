package com.example.myapp_2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by W--Inarius on 2017/7/11.
 */

public class MyDataB extends SQLiteOpenHelper {
    private Context context;
    public MyDataB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }
    private static final String CREATE_USER = "create table User ( "
            +"Username text primary key, "
            +"Nickname text unique, "
            +"level integer,"
            +"Password text )";
    private static final String CREATE_MISSION = "create table Mission ( "
            +"Missionid integer)";
    private static final String CREATE_RECORD = "create table Record ( "
            +"Nickname text,"
            +"recordt text,"
            +"Missionid integer)";
    private static final String FK1="ALTER TABLE Record ADD CONSTRAINT Usercons FOREIGN KEY(Nickname) REFERENCES User";
    private static final String FK2="ALTER TABLE Record ADD CONSTRAINT Missioncons FOREIGN KEY(Missionid) REFERENCES Mission";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_MISSION);
        sqLiteDatabase.execSQL(CREATE_RECORD);
        //Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertd(int missionid){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Missionid",missionid);
        sqLiteDatabase.insert("Mission",null,contentValues);
    }
    public void insertd(int missionid,String nickname,String time){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Missionid",missionid);
        contentValues.put("Nickname",nickname);
        contentValues.put("recordt",time);
        sqLiteDatabase.insert("Record",null,contentValues);
    }
    public void insertd(String username,String nickname,String password)throws SQLiteConstraintException{
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username",username);
        contentValues.put("Nickname",nickname);
        contentValues.put("Password",password);
        contentValues.put("level",0);
        sqLiteDatabase.insert("User",null,contentValues);
    }
    public void updated(String username,String missionid,String time){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recordt",time);
        sqLiteDatabase.update("Record",contentValues,"Username=? and Missionid=?",new String[]{username,missionid});
    }
    public void updated(String username,int level ){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("level",level);
        sqLiteDatabase.update("User",contentValues,"Username=?",new String[]{username});
    }
    public User selectP(String username){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String pas=null;
        String nickname=null;
        User user;
        int level=-1;
        if(sqLiteDatabase.isOpen()) {
            String[] col = {"Password",};
            String sel = "Username=?";
            String[] args = {username};
            Cursor cursor=sqLiteDatabase.query("User",null,sel,args,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    pas=cursor.getString(3);
                    nickname=cursor.getString(1);
                    level=cursor.getInt(2);
                }while(cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        user=new User(username,nickname,level,pas);
        return user;
    }
    public boolean isUsername(String username){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String pas="";
        String nickname="";
        User user;
        int level=-1;
        if(sqLiteDatabase.isOpen()) {
            String[] col = {"Password",};
            String sel = "Username=?";
            String[] args = {username};
            Cursor cursor=sqLiteDatabase.query("User",null,sel,args,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    pas=cursor.getString(3);
                    nickname=cursor.getString(1);
                    level=cursor.getInt(2);
                }while(cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        user=new User(username,nickname,level,pas);
        return pas.equals("") ? false : true;
    }
    public boolean isNickname(String nickname){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String username = "";
        String pas = "";
        User user;
        int level=-1;
        if(sqLiteDatabase.isOpen()) {
            String[] col = {"Password",};
            String sel = "Nickname=?";
            String[] args = {nickname};
            Cursor cursor=sqLiteDatabase.query("User",null,sel,args,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    username = cursor.getString(0);
                    pas=cursor.getString(3);
                    level=cursor.getInt(2);
                }while(cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        user=new User(username,nickname,level,pas);
        return pas.equals("") ? false : true;
    }
    public boolean isSign(String username,String password){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String pas="";
        String nickname="";
        User user;
        int level=-1;
        if(sqLiteDatabase.isOpen()) {
            String[] col = {"Password",};
            String sel = "Username=?";
            String[] args = {username};
            Cursor cursor=sqLiteDatabase.query("User",null,sel,args,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    pas=cursor.getString(3);
                    nickname=cursor.getString(1);
                    level=cursor.getInt(2);
                }while(cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        user=new User(username,nickname,level,pas);
        return pas.equals(password) && level != -1 ? true : false;
    }
    public ArrayList<Rec> selectn(String missionid){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ArrayList<Rec>recs=new ArrayList<Rec>();
        if(sqLiteDatabase.isOpen()){
            String []col={"Nickname","recordt"};
            String sel="Missionid=?";
            String[] args={missionid};
            String order="recordt";
            Cursor cursor=sqLiteDatabase.query("Record",col,sel,args,null,null,order);
            if (cursor.moveToFirst()){
                do {
                    String nickname=cursor.getString(0);
                    String time=cursor.getString(1);
                    recs.add(new Rec(time,nickname,missionid));
                }while(cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        return recs;
    }
}
