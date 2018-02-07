package space.gameressence.tatiburcio.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //Removed for chapter 14
    //private List<Crime> mCrimes;

    //Chapter 10 improve crimelab performance
    private List<Integer> mUUIDS;
    private Integer[] mCalculatedHash;

    //Chapter 14
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        //Removed for chapter 14
//        mCrimes = new ArrayList<>();
        //Start chapter 13
//        this.mUUIDS = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//            //CHapter 10 improve performance
//            this.mUUIDS.add(crime.getId().hashCode());
//            System.out.println(this.mUUIDS.get(i));
//        }
        //Chapter 14
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Crime> getCrimes() {

//        return new ArrayList<>();
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
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
        //Removed for chapter 14
//        //Linear search
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(id)) {
//                return crime;
//            }
//        }

//        return null;
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }


    //Chapter 10 challenges improve performance
    private static Integer[] calculateHash(List<Integer> hash) {

        Integer[] scratch = new Integer[hash.size()];

        for (int i = 0; i < hash.size(); i++) {
            if (scratch[Math.abs(hash.get(i)) % hash.size()] != 0)
                scratch[Math.abs(hash.get(i)) % hash.size()] = hash.get(i);
            else {
                boolean done = false;
                int j = i + 1;
                while (!done) {
                    if (j >= hash.size())
                        j = 0;
                    if (scratch[j] != 0) {
                        scratch[j] = hash.get(i);
                        break;
                    }
                }
            }
        }
        return scratch;
    }

    //Chapter 13
//    public void addCrime(Crime c) {
//        mCrimes.add(c);
//    }

    //chapter 14
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);


        return values;
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, values,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
}
