package pashainc.google.com.safesafari;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HistoryActivity extends AppCompatActivity {

	RecyclerView rc;
	DatabaseReference mDatabase;
	Button btn;


	DatabaseReference nRef;
	DatabaseReference ridesRef;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);


		btn = (Button) findViewById(R.id.button2);
		rc = (RecyclerView) findViewById(R.id.rc);
		rc.setHasFixedSize(true);
		rc.setLayoutManager(new LinearLayoutManager(this));

		mDatabase = FirebaseDatabase.getInstance().getReference().child("dsf");
/////////////////////////////////////////////////
			nRef = FirebaseDatabase.getInstance().getReference();
			ridesRef =nRef.child("rides").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("-L8vJw6oRXsIdS63eccl");
			Query query = ridesRef;

			query.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					Log.w("",""+dataSnapshot.child("currentAddress").getValue());
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});

/////////////////////////////////////////////////////////////////

//		Query query = mDatabase.child("rides");


		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("dsf").child("rides").push();
				db.child("rideNo").setValue("asdmkasjdl").addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
					}
				});

			}
		});

		FirebaseRecyclerOptions<ReadData> options = new FirebaseRecyclerOptions.Builder<ReadData>().setQuery(query, ReadData.class)
						.build();
		FirebaseRecyclerAdapter<ReadData,ReaDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReadData, ReaDataViewHolder>(
				options
		) {
			@Override
			protected void onBindViewHolder(@NonNull ReaDataViewHolder holder, int position, @NonNull ReadData model) {


				String push = getRef(position).getKey();
				DatabaseReference m = FirebaseDatabase.getInstance().getReference().child("rides").child(push);


			}

			@Override
			public ReaDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.single_view, parent, false);
				return new ReaDataViewHolder(view);
			}
		};
		firebaseRecyclerAdapter.startListening();
		rc.setAdapter(firebaseRecyclerAdapter);



	}


	public static class ReaDataViewHolder extends RecyclerView.ViewHolder{

		View view;
		TextView tv;
		public ReaDataViewHolder(View itemView) {
			super(itemView);

			view = itemView;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
