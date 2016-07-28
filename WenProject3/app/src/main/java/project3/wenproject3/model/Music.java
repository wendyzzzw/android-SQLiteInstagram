package project3.wenproject3.model;

/**
 * Created by zhouwen on 6/21/16.
 */
public class Music {
    private final String mName;
    private final String mPath;

    public Music(String name, String path) {
        mName = name;
        mPath = path;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }
}

