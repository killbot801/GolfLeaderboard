package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

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
    EditText _email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    public void launchCreateJoinTournament()
    {
        Intent intent = new Intent(this, CreateJoinTournament.class);
        intent.putExtra("userName", _userName.toString());
        startActivity(intent);
    }

    public void validateUserData()
    {
        EditText userName = (EditText) findViewById(R.id.createUserName);
        EditText password = (EditText) findViewById(R.id.createPassword);
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastNameTextEntry);
        EditText city = (EditText) findViewById(R.id.cityEntry);
        EditText email = (EditText) findViewById(R.id.emailEntry);

        //TODO: create the calls to validate the gathered fields

        boolean valid = false;

        if(valid)
        {
            setupGlobals(userName, password, firstName, lastName, city, email);
            launchCreateJoinTournament();
        }
    }

    public void setupGlobals(EditText userName, EditText password, EditText firstName, EditText lastName, EditText city, EditText email)
    {
        _userName = userName;
        _password = password;
        _firstName = firstName;
        _lastName = lastName;
        _city = city;
        _email = email;
    }
}
