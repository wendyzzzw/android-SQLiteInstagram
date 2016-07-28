package project3.wenproject3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import project3.wenproject3.model.Follow;
import project3.wenproject3.model.Like;
import project3.wenproject3.model.Photo;
import project3.wenproject3.model.User;

/**
 * Created by zhouwen on 6/15/16.
 */
public class MyDAO {
    private static MyDAO DAO;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    private final List<User> mUsersList;
    private final List<Photo> mPhotosList;
    private final List<Photo> mPhotosListByUsername;
    private final List<String> mFollowListBySource;
    private final List<String> mFollowListByDestination;
    private final List<String> mLikeListBySource;
    private final List<Like> mLikeListByDestination;


    private MyDAO (Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MyOpenHelper(context).getWritableDatabase();
        mUsersList = new LinkedList<>();
        mPhotosList = new LinkedList<>();
        mPhotosListByUsername = new LinkedList<>();
        mFollowListBySource = new LinkedList<>();
        mFollowListByDestination = new LinkedList<>();
        mLikeListBySource = new LinkedList<>();
        mLikeListByDestination = new LinkedList<>();
    }

    public static MyDAO get (Context context){
        if(DAO == null) {
            DAO = new MyDAO(context);
        }
        return DAO;
    }

    public void addUser (User user){
        mDatabase.insert(
                MySchema.UserTable.NAME,
                null,
                getUserContentValues(user)
        );
    }

    public void addPhoto (Photo photo){
        mDatabase.insert(
                MySchema.PhotoTable.NAME,
                null,
                getPhotoContentValues(photo)
        );
    }

    public void addFollow (Follow follow){
        mDatabase.insert(
                MySchema.FollowTable.NAME,
                null,
                getFollowContentValues(follow)
        );
    }

    public void addLike (Like like){
        mDatabase.insert(
                MySchema.LikeTable.NAME,
                null,
                getLikeContentValues(like)
        );
    }

    private static ContentValues getUserContentValues (User user){
        ContentValues values = new ContentValues();
        values.put(MySchema.UserTable.Cols.NAME, user.getName());
        values.put(MySchema.UserTable.Cols.USERNAME, user.getUsername());
        values.put(MySchema.UserTable.Cols.PASSWORD, user.getPassword());
        values.put(MySchema.UserTable.Cols.POSTS, user.getPosts());
        values.put(MySchema.UserTable.Cols.FOLLOWERS, user.getFollowers());
        values.put(MySchema.UserTable.Cols.FOLLOWINGS, user.getFollowings());
        values.put(MySchema.UserTable.Cols.PROFILE_PIC, user.getProfilePic());
        return values;
    }

    private static ContentValues getPhotoContentValues (Photo photo){
        ContentValues values = new ContentValues();
        values.put(MySchema.PhotoTable.Cols.ID, photo.getId().toString());
        values.put(MySchema.PhotoTable.Cols.FILENAME, photo.getFilename());
        values.put(MySchema.PhotoTable.Cols.PATH, photo.getPath());
        values.put(MySchema.PhotoTable.Cols.LIKES, photo.getLikes());
        values.put(MySchema.PhotoTable.Cols.OWNER_USERNAME, photo.getOwnerUsername());
        return values;
    }

    private static ContentValues getFollowContentValues (Follow follow){
        ContentValues values = new ContentValues();
        values.put(MySchema.FollowTable.Cols.SOURCE_USERNAME, follow.getSourceUsername());
        values.put(MySchema.FollowTable.Cols.DESTINATION_USERNAME, follow.getDestinationUsername());
        return values;
    }

    private static ContentValues getLikeContentValues (Like like){
        ContentValues values = new ContentValues();
        values.put(MySchema.LikeTable.Cols.SOURCE_USERNAME, like.getSourceUsername());
        values.put(MySchema.LikeTable.Cols.DESTINATION_PHOTO_ID, like.getDestinationPhotoId().toString());
        return values;
    }

    private MyWrapper queryUser(String where, String[]args){
        Cursor cursor = mDatabase.query(
                MySchema.UserTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return  new MyWrapper(cursor);
    }

    private MyWrapper queryPhoto(String where, String[]args){
        Cursor cursor = mDatabase.query(
                MySchema.PhotoTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return  new MyWrapper(cursor);
    }

    private MyWrapper queryFollow(String where, String[]args){
        Cursor cursor = mDatabase.query(
                MySchema.FollowTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return  new MyWrapper(cursor);
    }

    private MyWrapper queryLike(String where, String[]args){
        Cursor cursor = mDatabase.query(
                MySchema.LikeTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return  new MyWrapper(cursor);
    }

    public User getUser (String username){ //get user by username
        MyWrapper wrapper = queryUser(
                MySchema.UserTable.Cols.USERNAME + "=?",
                new String[]{username}
        );
        User user = null;
        if(wrapper.getCount() != 0) {
            wrapper.moveToFirst();
            user = wrapper.getUser();
        }
        wrapper.close();

        return user;
    }

    public List<User> getUsers(){
        mUsersList.clear();
        MyWrapper wrapper = queryUser(null, null);
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                User user = wrapper.getUser();
                mUsersList.add(user);
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mUsersList;
    }

    public void updateUser (User user){
        String username = user.getUsername();
        ContentValues values = getUserContentValues(user);
        mDatabase.update(
                MySchema.UserTable.NAME,
                values,
                MySchema.UserTable.Cols.USERNAME + "=?",
                new String[]{username}
        );
    }

    public Photo getPhoto (UUID id){ //get photo by id
        MyWrapper wrapper = queryPhoto(
                MySchema.PhotoTable.Cols.ID + "=?",
                new String[]{id.toString()}
        );
        Photo photo = null;
        if(wrapper.getCount() != 0) {
            wrapper.moveToFirst();
            photo = wrapper.getPhoto();
        }
        wrapper.close();

        return photo;
    }

    public void updatePhoto (Photo photo){
        String id = photo.getId().toString();
        ContentValues values = getPhotoContentValues(photo);
        mDatabase.update(
                MySchema.PhotoTable.NAME,
                values,
                MySchema.PhotoTable.Cols.ID + "=?",
                new String[]{id}
        );
    }

    public List<Photo> getPhotos(){
        mPhotosList.clear();
        MyWrapper wrapper = queryPhoto(null, null);
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                Photo photo = wrapper.getPhoto();
                mPhotosList.add(photo);
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mPhotosList;
    }

    public List<Photo> getPhotosByUsername(String username){
        mPhotosListByUsername.clear();
        MyWrapper wrapper = queryPhoto(
                MySchema.PhotoTable.Cols.OWNER_USERNAME + "=?",
                new String[]{username}
        );
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                Photo photo = wrapper.getPhoto();
                mPhotosListByUsername.add(photo);
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mPhotosListByUsername;
    }

    public List<String> getFollowListBySource (String sourceUsername){
        mFollowListBySource.clear();
        MyWrapper wrapper = queryFollow(
                MySchema.FollowTable.Cols.SOURCE_USERNAME + "=?",
                new String[]{sourceUsername}
        );
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                Follow follow = wrapper.getFollow();
                mFollowListBySource.add(follow.getDestinationUsername());
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mFollowListBySource;
    }

    public List<String> getFollowListByDestination (String destinationUsername){
        mFollowListByDestination.clear();
        MyWrapper wrapper = queryFollow(
                MySchema.FollowTable.Cols.DESTINATION_USERNAME + "=?",
                new String[]{destinationUsername}
        );
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                Follow follow = wrapper.getFollow();
                mFollowListByDestination.add(follow.getSourceUsername());
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mFollowListByDestination;
    }

    public List<String> getLikeListBySource (String sourceUsername){
        mLikeListBySource.clear();
        MyWrapper wrapper = queryLike(
                MySchema.LikeTable.Cols.SOURCE_USERNAME + "=?",
                new String[]{sourceUsername}
        );
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                Like like = wrapper.getLike();
                mLikeListBySource.add(like.getDestinationPhotoId().toString());
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mLikeListBySource;
    }

    public List<Like> getLikeListByDestination (UUID destinationId){
        mLikeListByDestination.clear();
        MyWrapper wrapper = queryLike(
                MySchema.LikeTable.Cols.DESTINATION_PHOTO_ID + "=?",
                new String[]{destinationId.toString()}
        );
        try{
            wrapper.moveToFirst();
            while(wrapper.isAfterLast() == false) {
                Like like = wrapper.getLike();
                mLikeListByDestination.add(like);
                wrapper.moveToNext();
            }
        }finally {
            wrapper.close();
        }
        return  mLikeListByDestination;
    }

}
