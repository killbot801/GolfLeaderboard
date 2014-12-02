package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ljohnson on 11/18/14.
 */
public class CreateAccount extends Activity
{
    EditText _userName;
    EditText _password;
    EditText _firstName;
    EditText _lastName;
    EditText _city;
    EditText _state;
    EditText _email;
    TextView _userNameTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        setupGlobals();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchCreateJoinTournament()
    {
        Intent intent = new Intent(this, CreateJoinTournament.class);
        intent.putExtra("userName", _userName.toString());
        startActivity(intent);
    }

    public void validateUserData(View view)
    {
        _userNameTaken.setVisibility(View.INVISIBLE);

        NetworkRequests nr = NetworkRequests.getNetworkRequestInstance();
        Pair<Boolean, String> createResponse = nr.createUser(_userName.getText().toString(), _password.getText().toString(),
                _firstName.getText().toString(), _lastName.getText().toString(), _city.getText().toString(), _state.getText().toString(), _email.getText().toString());

        if(createResponse.first)
        {
            launchCreateJoinTournament();
        }
        else
        {
            if(createResponse.second.equals("Your user name is taken, please try another."))
            {
                _userNameTaken.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getApplicationContext(), createResponse.second, Toast.LENGTH_LONG).show();
        }
    }

    public void setupGlobals()
    {
        _userName = (EditText) findViewById(R.id.createUserName);
        _password = (EditText) findViewById(R.id.createPassword);
        _firstName = (EditText) findViewById(R.id.firstName);
        _lastName = (EditText) findViewById(R.id.lastNameTextEntry);
        _city = (EditText) findViewById(R.id.cityEntry);
        _state = (EditText) findViewById(R.id.stateEntry);
        _email = (EditText) findViewById(R.id.emailEntry);
        _userNameTaken = (TextView) findViewById(R.id.usernameTaken);
    }
}
