package pashainc.google.com.safesafari;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by black on 4/19/2018.
 */

public class SharedPrefRead {
	private String Name;
	private String guard_name;
	private String guard_phone;
	private Context context;
	private SharedPreferences sharedpref;

	public SharedPrefRead() {
	}

	public String getName() {
//		sharedpref = context.getSharedPreferences("UserData",Context.MODE_PRIVATE);
//		this.Name = sharedpref.getString("UserName", Name);
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getGuard_name() {
		return guard_name;
	}

	public void setGuard_name(String guard_name) {
		this.guard_name = guard_name;
	}

	public String getGuard_phone() {
		return guard_phone;
	}

	public void setGuard_phone(String guard_phone) {
		this.guard_phone = guard_phone;
	}


}
