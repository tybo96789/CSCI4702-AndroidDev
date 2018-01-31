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
        //Generate Unique identifier
        this.mID = UUID.randomUUID();
        this.mDate = new Date();

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
}
