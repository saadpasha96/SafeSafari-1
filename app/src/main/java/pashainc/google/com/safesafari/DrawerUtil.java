package pashainc.google.com.safesafari;

/**
 * Created by black on 3/29/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class DrawerUtil {
	public static void getDrawer(final Activity activity, Toolbar toolbar) {
		//if you want to update the items at a later time it is recommended to keep it in a variable
		PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
		drawerEmptyItem.withEnabled(false);

		PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1)
				.withName("Home");
		PrimaryDrawerItem history = new PrimaryDrawerItem()
				.withIdentifier(2).withName("History");

		//create the drawer and remember the `Drawer` result object
		Drawer result = new DrawerBuilder()
				.withActivity(activity)
				.withToolbar(toolbar)
				.withActionBarDrawerToggle(true)
				.withActionBarDrawerToggleAnimated(true)
				.withCloseOnClick(true)
				.withSelectedItem(-1)
				.addDrawerItems(
						drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
						home,
						history)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						if (drawerItem.getIdentifier() == 2 && !(activity instanceof CurrentLocation)) {
							// load tournament screen
//							Intent intent = new Intent(activity, HistoryActivity.class);
//							view.getContext().startActivity(intent);
							Toast.makeText(activity, "Hii", Toast.LENGTH_SHORT).show();
						}
						return true;
					}
				})
				.build();
	}
}
