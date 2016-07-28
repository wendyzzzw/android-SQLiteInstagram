package project3.wenproject3.model;

/**
 * Created by zhouwen on 6/15/16.
 */
public class Follow {
    private String sourceUsername;
    private String destinationUsername;

    public Follow (String sourceUsername, String destinationUsername){
        this.sourceUsername = sourceUsername;
        this.destinationUsername = destinationUsername;
    }

    public String getSourceUsername(){
        return sourceUsername;
    }

    public String getDestinationUsername(){
        return destinationUsername;
    }

}
