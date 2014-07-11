package osoc.leiedal.android.aandacht.View;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import osoc.leiedal.android.aandacht.R;

/**
 * Used for 'global stuffs' like option menu, ...
 */
public class ParentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                this.startActivity(new Intent(this,ViewSettingsActivity.class));
                break;
            case R.id.action_profile:
                //goto profile
                this.startActivity(new Intent(this,ViewProfileActivity.class));
                break;
            case R.id.action_logout:

                //clear login data
                getSharedPreferences(getResources().getString(R.string.app_pref),0).edit().clear().commit();

                this.startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.action_map:
                this.startActivity(new Intent(this,MapsActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Determine what activity I am and find the menu item for that activity
        MenuItem menuItem = null;
        if (getClass().equals(osoc.leiedal.android.aandacht.View.ViewSettingsActivity.class)) {
            menuItem = menu.findItem(R.id.action_settings);
        }else if (getClass().equals(osoc.leiedal.android.aandacht.View.ViewProfileActivity.class)) {
            menuItem = menu.findItem(R.id.action_profile);
        }else if (getClass().equals(osoc.leiedal.android.aandacht.View.LoginActivity.class)) {
            menuItem = menu.findItem(R.id.action_logout);
        }


        // Disable this menu item
        if (menuItem != null) {
            menuItem.setEnabled(false); // Make it non-selectable (even with shortcut)
            menuItem.setVisible(false); // Make it non-visible
        }
        return true;
    }
}
