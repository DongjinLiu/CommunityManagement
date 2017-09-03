package com.example.jin.communitymanagement;

import java.util.Date;

/**
 * Created by summe on 2017/8/15.
 */

public class AssociationActivity {
    private String associationName;
    private String activityName;
    private String myTime;
    private int imageId;
    //op
    public AssociationActivity(String associationName, String activityName, String myTime, int imageId) {
        this.associationName = associationName;
        this.activityName = activityName;
        this.myTime = myTime;
        this.imageId = imageId;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getMyTime() {
        return myTime;
    }

    public void setMyTime(String myTime) {
        this.myTime = myTime;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
