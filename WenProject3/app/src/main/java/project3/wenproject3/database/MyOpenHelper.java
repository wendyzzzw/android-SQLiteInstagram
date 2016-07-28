package project3.wenproject3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhouwen on 6/15/16.
 */
public class MyOpenHelper extends SQLiteOpenHelper{
    public MyOpenHelper(Context context) {
        super(context, MySchema.DATABASE_NAME, null, MySchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MySchema.UserTable.NAME
                + "(_id integer primary key autoincrement, "
                + MySchema.UserTable.Cols.NAME + ", "
                + MySchema.UserTable.Cols.USERNAME + ", "
                + MySchema.UserTable.Cols.PASSWORD + ", "
                + MySchema.UserTable.Cols.POSTS + ", "
                + MySchema.UserTable.Cols.FOLLOWERS + ", "
                + MySchema.UserTable.Cols.FOLLOWINGS + ", "
                + MySchema.UserTable.Cols.PROFILE_PIC + ")");

        db.execSQL("create table " + MySchema.PhotoTable.NAME
                + "(_id integer primary key autoincrement, "
                + MySchema.PhotoTable.Cols.ID + ", "
                + MySchema.PhotoTable.Cols.FILENAME + ", "
                + MySchema.PhotoTable.Cols.PATH + ", "
                + MySchema.PhotoTable.Cols.LIKES + ", "
                + MySchema.PhotoTable.Cols.OWNER_USERNAME + ")");

        db.execSQL("create table " + MySchema.FollowTable.NAME
                + "(_id integer primary key autoincrement, "
                + MySchema.FollowTable.Cols.SOURCE_USERNAME + ", "
                + MySchema.FollowTable.Cols.DESTINATION_USERNAME + ")");

        db.execSQL("create table " + MySchema.LikeTable.NAME
                + "(_id integer primary key autoincrement, "
                + MySchema.LikeTable.Cols.SOURCE_USERNAME + ", "
                + MySchema.LikeTable.Cols.DESTINATION_PHOTO_ID + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
