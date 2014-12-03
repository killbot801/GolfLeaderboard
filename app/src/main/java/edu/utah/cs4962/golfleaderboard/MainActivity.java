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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends Activity
{
    private EditText _username;
    private EditText _password;
    private Button _login;
    private TextView _loginLockedTV;
    private TextView _attemptsLeftTV;
    private TextView _numberOfRemainingLoginAttemptsTV;
    int _numberOfRemainingLoginAttempts = 3;
    private String _userID;

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

    public String getUserID()
    {
        return _userID;
    }

    public void authenticateLogin(View view)
    {
        NetworkRequests nr = NetworkRequests.getNetworkRequestInstance();
        Pair<Boolean, String> authenticationCheck = nr.authenticateLogin(_username.getText().toString(), _password.getText().toString());

        if(authenticationCheck.first)
        {
            Pair<Boolean, String> userIDResponse = nr.getUserID(_username.getText().toString());
            if(userIDResponse.first)
            {
                _userID = userIDResponse.second;
                saveUserValues();
                launchCreateJoin();
            }
            else
                Toast.makeText(getApplicationContext(), userIDResponse.second, Toast.LENGTH_LONG).show();
        }
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
            intent.putExtra("userName", _username.getText().toString());
            startActivity(intent);
        }
    }

    public void launchCreateJoin()
    {
        Intent intent = new Intent(this, CreateJoinTournament.class);
        startActivity(intent);
    }

    public void saveUserValues()
    {
        String fileName = "GTourny.txt";
        try
        {
            File file = new File(getApplicationContext().getFilesDir(), fileName);

            if (!file.exists())
            {
                BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file, true));
                Gson gson = new Gson();
                Type type = new TypeToken<String>()
                {
                }.getType();
                String json = gson.toJson(_userID, type);
                buffWriter.write(json);
                buffWriter.newLine();
                buffWriter.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}