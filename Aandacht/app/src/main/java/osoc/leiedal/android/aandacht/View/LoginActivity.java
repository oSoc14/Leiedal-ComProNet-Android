package osoc.leiedal.android.aandacht.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.model.apiAccess.DummyAPIAccess;
import osoc.leiedal.android.aandacht.View.model.apiAccess.iAPIAccess;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view){
        String login = ( (EditText)findViewById(R.id.login_txtLogin)).getText().toString();
        String pass  = ( (EditText)findViewById(R.id.login_txtPass) ).getText().toString();

        iAPIAccess api = DummyAPIAccess.getInstance();

        if (api.login(login, pass)) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_tstSucces), Toast.LENGTH_LONG);
            toast.show();

            Intent gotoPref = new Intent(this,ViewReportsActivity.class);
            gotoPref.putExtra("user",login);
            startActivity(gotoPref);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_tstFail), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
