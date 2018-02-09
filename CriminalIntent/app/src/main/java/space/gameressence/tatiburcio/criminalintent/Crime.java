package space.gameressence.tatiburcio.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tyleratiburcio on 1/28/18.
 */

public class Crime {

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;


    public Crime()
    {
        //Removed chapter 14
//        //Generate Unique identifier
//        this.mID = UUID.randomUUID();
//        this.mDate = new Date();
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mID = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mID;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    //Begin chapter 15
    private String mSuspect;

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    //chapter 16
    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
