package space.gameressence.tatiburcio.criminalintent;


import android.support.v4.app.Fragment;

/**
 * Created by tybo96789 on 1/29/18.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
