package pashainc.google.com.safesafari;

/**
 * Created by black on 3/29/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;



public class DrawerUtil {

	 static FirebaseAuth mAuth = FirebaseAuth.getInstance();

	public static void getDrawer(final Activity activity, Toolbar toolbar) {

		// Create the AccountHeader
		AccountHeader headerResult = new AccountHeaderBuilder()
				.withActivity(activity)
				.withHeaderBackground(R.drawable.header)
				.addProfiles(
						new ProfileDrawerItem().withName("Saad")
				)
				.withAlternativeProfileHeaderSwitching(true)
				.build();



		//if you want to update the items at a later time it is recommended to keep it in a variable
		PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
		drawerEmptyItem.withEnabled(false);

		PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1)
				.withName("Home");
		PrimaryDrawerItem rides = new PrimaryDrawerItem()
				.withIdentifier(2).withName("History");
		PrimaryDrawerItem profile = new PrimaryDrawerItem()
				.withIdentifier(2).withName("Profile Settings");
		PrimaryDrawerItem signout = new PrimaryDrawerItem()
				.withIdentifier(2).withName("Sign Out");

		//create the drawer and remember the `Drawer` result object
		final Drawer result = new DrawerBuilder()
				.withActivity(activity)
				.withToolbar(toolbar)
				.withTranslucentStatusBar(true)
				.withActionBarDrawerToggle(true)
				.withAccountHeader(headerResult)
				.withTranslucentStatusBar(false)
				.withCloseOnClick(false)
				.withActionBarDrawerToggleAnimated(true)
				.withSelectedItem(-1)
				.withMultiSelect(true)
				.addDrawerItems(
						home,
						rides,
						profile,
						signout)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						switch (position){
							case 1:
								Intent intent = new Intent(activity, CurrentLocation.class);
								view.getContext().startActivity(intent);
								break;
							case 2:
								Toast.makeText(activity, "Rides", Toast.LENGTH_SHORT).show();
								break;
							case 3:
								Toast.makeText(activity, "Profile Settings", Toast.LENGTH_SHORT).show();
								break;
							case 4:
								mAuth.signOut();
								Toast.makeText(activity, "Sign Out Successful", Toast.LENGTH_SHORT).show();
								Intent intent1 = new Intent(activity, PhoneLogin.class);
								view.getContext().startActivity(intent1);
								break;
						}

						return false;
					}

				})
				.build();


	}


}
