package pashainc.google.com.safesafari;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Created by black on 3/28/2018.
 */

public class MediaP {

	public static MediaPlayer mp;

	Context context;

	public MediaP(Context context) {
		this.context = context;
	}

	public void mp (){
		//        Playing Audio

		int resID = R.raw.alertme;

		mp = MediaPlayer.create(context, resID);
		mp.start();


	}

	public MediaPlayer mpr(){
		if(mp!=null){
			return  mp;
		}
		return  null;
	}

}
