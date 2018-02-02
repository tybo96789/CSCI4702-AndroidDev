package space.gameressence.tatiburcio.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    //Chapter 10 improve crimelab performance
    private List<Integer> mUUIDS;
    private Integer[] mCalculatedHash;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        this.mUUIDS = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
            //CHapter 10 improve performance
            this.mUUIDS.add(crime.getId().hashCode());
            System.out.println(this.mUUIDS.get(i));
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }


    /*
    public Crime getCrime(UUID id) {
        int hash = id.hashCode();
        if(mUUIDS.get(hash%this.getCrimes().size()) == hash)
            return this.mCrimes.get(hash%this.getCrimes().size());
        else
        for(int i = (hash%this.getCrimes().size())+1; i < this.getCrimes().size();i++)
        {
            if
            {
                boolean done = false;
                int j = i +1;
                while(!done)
                {
                    if(j >= hash.size())
                        j = 0;
                    if(scratch[j] != 0)
                    {
                        scratch[j] = hash.get(i);
                        break;
                    }
                }
            }
        }
        return scratch;

        return null;
    }
    */


    public Crime getCrime(UUID id) {
        //Linear search
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }


    //Chapter 10 challenges improve performance
    private static Integer[] calculateHash(List<Integer> hash)
    {

        Integer[] scratch = new Integer[hash.size()];

        for(int i = 0; i < hash.size();i++)
        {
            if(scratch[Math.abs(hash.get(i))%hash.size()] != 0)
                scratch[Math.abs(hash.get(i))%hash.size()] = hash.get(i);
            else
            {
                boolean done = false;
                int j = i +1;
                while(!done)
                {
                    if(j >= hash.size())
                        j = 0;
                    if(scratch[j] != 0)
                    {
                        scratch[j] = hash.get(i);
                        break;
                    }
                }
            }
        }
        return scratch;
    }
}
