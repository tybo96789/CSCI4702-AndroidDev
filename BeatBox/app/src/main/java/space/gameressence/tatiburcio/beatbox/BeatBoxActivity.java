package space.gameressence.tatiburcio.beatbox;


import android.support.v4.app.Fragment;

/**
 * Created by tybo96789 on 2/12/18.
 */

public class BeatBoxActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}
