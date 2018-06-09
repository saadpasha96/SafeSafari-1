package pashainc.google.com.safesafari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class FAQ extends AppCompatActivity {


	TextView ques1, ques2 , ques3 , ques4;
	TextView ans1, ans2, ans3, ans4;
	Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);

		toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerUtil.getDrawer(this, toolbar);

		String q1 = "What is a Phone Code?";
		String a1 = "A Phone is a Specific set of numbers send to you by the Server for Secure Login.";

		String q2 = "Why cannot I Register with an Email?";
		String a2 = "As per our Policy, you must Register with your Phone Number in order to access all the feature of application.";

		String q3 = "How do I send an Alert Message Manually during Ride?";
		String a3 = "To send an Emergency Alert Message, just click on the ''Send Alert'' button during a ride.";

		String q4 = "What if the OCR does not detect the License Plate characters correctly?";
		String a4 = "There are certain conditions for OCR errors, possibly due to Low Light, Autofocus etc. The alternative is to type in the characters manually.";

		ques1 = (TextView) findViewById(R.id.q1);
		ques1.setText(q1);
		ques2 = (TextView) findViewById(R.id.q2);
		ques2.setText(q2);
		ques3 = (TextView) findViewById(R.id.q3);
		ques3.setText(q3);
		ques4 = (TextView) findViewById(R.id.q4);
		ques4.setText(q4);

		ans1 = (TextView) findViewById(R.id.a1);
		ans1.setText(a1);
		ans2 = (TextView) findViewById(R.id.a2);
		ans2.setText(a2);
		ans3 = (TextView) findViewById(R.id.a3);
		ans3.setText(a3);
		ans4 = (TextView) findViewById(R.id.a4);
		ans4.setText(a4);


	}


}
