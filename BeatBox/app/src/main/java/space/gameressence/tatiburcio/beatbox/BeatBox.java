package space.gameressence.tatiburcio.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tybo96789 on 2/13/18.
 */

public class BeatBox {

    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";

    private List<Sound> mSounds = new ArrayList<>();

    private AssetManager mAssets;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        //Chapter 21 Start
        // This old constructor is deprecated but needed for compatibility
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        //Chapter 21 end
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");

        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }
            //Chapter 21; Removed for newer version below
//        for (String filename : soundNames) {
//            String assetPath = SOUNDS_FOLDER + "/" + filename;
//            Sound sound = new Sound(assetPath);
//            mSounds.add(sound);
//        }
        for (String filename : soundNames) {
            System.out.println(filename);
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
        }
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    //Chapter 21
    private static final int MAX_SOUNDS = 5;
    private SoundPool mSoundPool;

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            Log.d(TAG,"soundID null");
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }

}
