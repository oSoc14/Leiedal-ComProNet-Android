package osoc.leiedal.android.aandacht.View;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.fragments.ViewSettingsFragment;

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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
