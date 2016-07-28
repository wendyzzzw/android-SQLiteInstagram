package project3.wenproject3.database;

/**
 * Created by zhouwen on 6/15/16.
 */
public class MySchema {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "wen_project3_database.db";

    public static final class UserTable{
        public static final String NAME = "users";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String USERNAME = "username";
            public static final String PASSWORD="password";
            public static final String POSTS ="posts";
            public static final String FOLLOWERS = "followers";
            public static final String FOLLOWINGS = "followings";
            public static final String PROFILE_PIC = "profile_pic";
        }
    }

    public static final class PhotoTable{
        public static final String NAME = "photos";

        public static final class Cols{
            public static final String ID = "id";
            public static final String FILENAME = "filename";
            public static final String PATH = "path";
            public static final String LIKES = "likes";
            public static final String OWNER_USERNAME = "owner_username";
        }
    }

    public static final class FollowTable{
        public static final String NAME = "follows";

        public static final class Cols{
            public static final String SOURCE_USERNAME = "source_username";
            public static final String DESTINATION_USERNAME = "destination_username";
        }
    }

    public static final class LikeTable{
        public static final String NAME = "likes";

        public static final class Cols{
            public static final String SOURCE_USERNAME = "source_username";
            public static final String DESTINATION_PHOTO_ID = "destination_photo_id";
        }
    }

}
