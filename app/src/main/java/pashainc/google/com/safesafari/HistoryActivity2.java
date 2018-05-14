package pashainc.google.com.safesafari;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HistoryActivity2 extends AppCompatActivity {

	RecyclerView rc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history2);



		rc = (RecyclerView) findViewById(R.id.rc);
		rc.setHasFixedSize(true);
		rc.setLayoutManager(new LinearLayoutManager(this));



		DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
		Query query =mDatabase.child("rides").child("UEWS0hFJelPBHjvrrK5HMwcbB8m1");

		FirebaseRecyclerOptions<ReadData> options = new FirebaseRecyclerOptions.Builder<ReadData>().setQuery(query, ReadData.class)
				.build();
		FirebaseRecyclerAdapter<ReadData,ReaDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReadData, ReaDataViewHolder>(
				options
		) {
			@Override
			protected void onBindViewHolder(@NonNull ReaDataViewHolder holder, int position, @NonNull ReadData model) {


				holder.tvEndLocation.setText(model.getDestAddress());
				holder.tvStartLocation.setText(model.getStartAddress());


				holder.tvStartLocation.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

					}
				});




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
		TextView tvEndLocation,tvStartLocation;
		CardView tvFlag,cdALL;
		public ReaDataViewHolder(View itemView) {
			super(itemView);

			view = itemView;
			tvEndLocation  = (TextView) view.findViewById(R.id.tvEndLocation);
			tvStartLocation  = (TextView) view.findViewById(R.id.tvStartLocation);
			tvFlag  = (CardView) view.findViewById(R.id.cdFlag);
			cdALL  = (CardView) view.findViewById(R.id.cdAll);

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
