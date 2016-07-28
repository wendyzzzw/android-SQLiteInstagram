package project3.wenproject3.model;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhouwen on 6/15/16.
 */
public class Like {
    private String sourceUsername;
    private UUID destinationPhotoId;

    public Like (String sourceUsername, UUID destinationPhotoId){
        this.sourceUsername = sourceUsername;
        this.destinationPhotoId = destinationPhotoId;
    }

    public String getSourceUsername(){
        return sourceUsername;
    }

    public UUID getDestinationPhotoId(){
        return destinationPhotoId;
    }

}
