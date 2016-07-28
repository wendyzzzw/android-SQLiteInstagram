package project3.wenproject3.model;

import java.util.UUID;

/**
 * Created by zhouwen on 6/15/16.
 */
public class Photo {
    private UUID id;
    private String filename;
    private String path;
    private int likes;
    private String ownerUsername;

    public Photo (UUID id){
        this.id = id;
        filename = "IMG_" + id.toString() + ".jpg";
        likes=0;
    }

    public Photo (){
        this(UUID.randomUUID());
    }

    public UUID getId(){
        return id;
    }

    public String getFilename(){
        return filename;
    }

    public String getPath(){
        return path;
    }

    public int getLikes(){
        return likes;
    }

    public String getOwnerUsername(){
        return ownerUsername;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setLikes (int likes){
        this.likes =likes;
    }

    public void setOwnerUsername(String username){
        this.ownerUsername=username;
    }
}
