package project3.wenproject3.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import project3.wenproject3.model.Follow;
import project3.wenproject3.model.Like;
import project3.wenproject3.model.Photo;
import project3.wenproject3.model.User;

/**
 * Created by zhouwen on 6/15/16.
 */
public class MyWrapper extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MyWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String name = getString(getColumnIndex(MySchema.UserTable.Cols.NAME));
        String username = getString(getColumnIndex(MySchema.UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(MySchema.UserTable.Cols.PASSWORD));
        int posts = getInt(getColumnIndex(MySchema.UserTable.Cols.POSTS));
        int followers = getInt(getColumnIndex(MySchema.UserTable.Cols.FOLLOWERS));
        int followings = getInt(getColumnIndex(MySchema.UserTable.Cols.FOLLOWINGS));
        String profilePic = getString(getColumnIndex(MySchema.UserTable.Cols.PROFILE_PIC));

        User user = new User(username,password);
        user.setName(name);
        user.setPosts(posts);
        user.setFollowers(followers);
        user.setFollowings(followings);
        user.setProfilePic(profilePic);
        return user;
    }

    public Photo getPhoto(){
        UUID id = UUID.fromString(getString(getColumnIndex(MySchema.PhotoTable.Cols.ID)));
        String filename = getString(getColumnIndex(MySchema.PhotoTable.Cols.FILENAME));
        String path = getString(getColumnIndex(MySchema.PhotoTable.Cols.PATH));
        int likes = getInt(getColumnIndex(MySchema.PhotoTable.Cols.LIKES));
        String ownerUsername = getString(getColumnIndex(MySchema.PhotoTable.Cols.OWNER_USERNAME));

        Photo photo = new Photo(id);
        photo.setPath(path);
        photo.setLikes(likes);
        photo.setOwnerUsername(ownerUsername);
        return photo;
    }

    public Follow getFollow(){
        String sourceUsername = getString(getColumnIndex(MySchema.FollowTable.Cols.SOURCE_USERNAME));
        String destinationUsername = getString(getColumnIndex(MySchema.FollowTable.Cols.DESTINATION_USERNAME));
        Follow follow = new Follow(sourceUsername,destinationUsername);
        return follow;
    }

    public Like getLike (){
        String sourceUsername = getString(getColumnIndex(MySchema.LikeTable.Cols.SOURCE_USERNAME));
        UUID destinationPhotoId = UUID.fromString(getString(getColumnIndex(MySchema.LikeTable.Cols.DESTINATION_PHOTO_ID)));
        Like like = new Like(sourceUsername,destinationPhotoId);
        return like;
    }

}
