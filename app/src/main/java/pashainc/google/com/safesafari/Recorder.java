package pashainc.google.com.safesafari;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Created by black on 5/15/2018.
 */

public class Recorder {

	private FirebaseAuth mAuth = FirebaseAuth.getInstance();
	String user = mAuth.getCurrentUser().getUid();
	private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Audio Proofs").child(user).push();

	ProgressDialog progressDialog;
	private FirebaseStorage mStorage = FirebaseStorage.getInstance();

	String filename = null;

	MediaRecorder myAudioRecorder = new MediaRecorder();

	Context context;

	public Recorder (Context context){
		this.context = context.getApplicationContext();
	}

	public void record() throws IOException {


		filename = Environment.getExternalStorageDirectory().getAbsolutePath();
		filename += "/recorded_proof.3gp";
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myAudioRecorder.setOutputFile(filename);
		myAudioRecorder.setMaxDuration(5000);
		myAudioRecorder.prepare();
		myAudioRecorder.start();

		myAudioRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mediaRecorder, int what, int i1) {
				if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
					myAudioRecorder.stop();
					Upload();
					Log.e("Audio", "DONE");
				}
			}
		});
	}


	public void Upload(){
//		progressDialog = new ProgressDialog(context);
//		progressDialog.setMessage("Uploading");
//		progressDialog.show();
		final StorageReference filepath = mStorage.getReference().child("Audio").child("audio1");
		Uri uri = Uri.fromFile(new File(filename));
		filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				Log.e("Storage ", "done");
				String audiopath = filepath.getDownloadUrl().toString();
				mDatabase.setValue(audiopath);
//				progressDialog.dismiss();
			}
		});
	}


}
