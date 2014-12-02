package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
{

    private EditText _username;
    private EditText _password;
    private Button _login;
    private TextView _loginLockedTV;
    private TextView _attemptsLeftTV;
    private TextView _numberOfRemainingLoginAttemptsTV;
    int _numberOfRemainingLoginAttempts = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupVariables();
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

    public void authenticateLogin(View view)
    {
        NetworkRequests nr = NetworkRequests.getNetworkRequestInstance();
        Pair<Boolean, String> authenticationCheck = nr.authenticateLogin(_username.getText().toString(), _password.getText().toString());

        if(authenticationCheck.first)
            launchCreateJoin();
        else
        {
            Toast.makeText(getApplicationContext(), authenticationCheck.second, Toast.LENGTH_LONG).show();
            _numberOfRemainingLoginAttempts--;
            _attemptsLeftTV.setVisibility(View.VISIBLE);
            _numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
            _numberOfRemainingLoginAttemptsTV.setText(Integer.toString(_numberOfRemainingLoginAttempts));

            if (_numberOfRemainingLoginAttempts == 0)
            {
                _login.setEnabled(false);
                _loginLockedTV.setVisibility(View.VISIBLE);
                _loginLockedTV.setBackgroundColor(Color.RED);
                _loginLockedTV.setText("LOGIN LOCKED!!!");
            }
        }
    }
    private void setupVariables() {
        _username = (EditText) findViewById(R.id.usernameET);
        _password = (EditText) findViewById(R.id.passwordET);
        _login = (Button) findViewById(R.id.loginBtn);
        _loginLockedTV = (TextView) findViewById(R.id.loginLockedTV);
        _attemptsLeftTV = (TextView) findViewById(R.id.attemptsLeftTV);
        _numberOfRemainingLoginAttemptsTV = (TextView) findViewById(R.id.numberOfRemainingLoginAttemptsTV);
        _numberOfRemainingLoginAttemptsTV.setText(Integer.toString(_numberOfRemainingLoginAttempts));
    }

    public void launchCreateAccount(View view)
    {
        if(_username.toString() == null || _username.toString() == "")
            Toast.makeText(this, "You must enter a user name.", Toast.LENGTH_LONG).show();
        else
        {
            Intent intent = new Intent(this, CreateAccount.class);
            intent.putExtra("userName", _username.getText());
            startActivity(intent);
        }
    }

    public void launchCreateJoin()
    {
        Intent intent = new Intent(this, CreateJoinTournament.class);
        startActivity(intent);
    }

}


        /*if (_username.getText().toString().equals("admin") &&
                _password.getText().toString().equals("admin"))
        {
            Toast.makeText(getApplicationContext(), "Hello admin!",
                    Toast.LENGTH_SHORT).show();
            launchCreateJoin();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Seems like you 're not admin!",
                    Toast.LENGTH_SHORT).show();
            _numberOfRemainingLoginAttempts--;
            _attemptsLeftTV.setVisibility(View.VISIBLE);
            _numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
            _numberOfRemainingLoginAttemptsTV.setText(Integer.toString(_numberOfRemainingLoginAttempts));

            if (_numberOfRemainingLoginAttempts == 0)
            {
                _login.setEnabled(false);
                _loginLockedTV.setVisibility(View.VISIBLE);
                _loginLockedTV.setBackgroundColor(Color.RED);
                _loginLockedTV.setText("LOGIN LOCKED!!!");
            }
        }*/