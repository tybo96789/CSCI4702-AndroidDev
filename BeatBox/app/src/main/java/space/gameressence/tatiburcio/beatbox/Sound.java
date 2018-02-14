package space.gameressence.tatiburcio.beatbox;

/**
 * Created by tybo96789 on 2/13/18.
 */

public class Sound {
    private String mAssetPath;
    private String mName;
    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        mName = filename.replace(".wav", "");
    }
    public String getAssetPath() {
        return mAssetPath;
    }

public String getName() {
        return mName;
        }

        //Chapter 21
        private Integer mSoundId;
    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
