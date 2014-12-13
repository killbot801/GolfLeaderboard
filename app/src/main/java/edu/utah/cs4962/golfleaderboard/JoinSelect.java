package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ljohnson on 12/5/14.
 */
public class JoinSelect extends Activity
{
    String _userID;
    ArrayList<TournamentItem> _theList;
    ArrayList<String> _tournaments;
    String _tournamentSelected;
    int _arrayPosition;
    EditText _tournamentPasscode;
    Spinner _tournamentSpinner;
    String _tournamentID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_tournament_select);
        _userID = getUserID();
        setupGlobals();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, _tournaments);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _tournamentSpinner.setAdapter(adapter);

        _tournamentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                _tournamentSelected = parent.getItemAtPosition(position).toString();
                _tournamentID = Integer.toString(_theList.get(position).gettID());
                _arrayPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
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
        if (id == R.id.login)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.create_account)
        {
            Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
            startActivity(intent);
        }
        else if (id == R.id.join_tournament)
        {
            Intent intent = new Intent(getApplicationContext(), JoinTournament.class);
            startActivity(intent);
        }
        else if (id == R.id.create_tournament)
        {
            Intent intent = new Intent(getApplicationContext(), CreateTournament.class);
            startActivity(intent);
        }

        else if (id == R.id.edit_account)
        {
            Intent intent = new Intent(getApplicationContext(), EditAccount.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupGlobals()
    {

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            _theList = extras.getParcelableArrayList("TournamentValues");
            if (_theList != null && _theList.size() != 0)
            {
                _tournaments = new ArrayList<String>();
                for (int listIndex = 0; listIndex < _theList.size(); listIndex++)
                {
                    _tournaments.add(_theList.get(listIndex).gettName());
                }
            }
        }

        _tournamentPasscode = (EditText) findViewById(R.id.tournamentPasscodeEntry);
        _tournamentSpinner = (Spinner) findViewById(R.id.tournamentSelectionSpinner);
    }

    public String getUserID()
    {
        String returnValue = "";

        try
        {
            File file = new File(getApplicationContext().getFilesDir(), "GTourny.txt");
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(textReader);
            String line = null;
            line = bufferedReader.readLine();
            Gson gson = new Gson();
            Type type = new TypeToken<String>()
            {
            }.getType();

            returnValue = gson.fromJson(line, type);

            bufferedReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return returnValue;
    }

    public void joinTournament(View view)
    {
        NetworkRequests networkRequests = new NetworkRequests();
        Pair<Boolean, String> validationResponse = networkRequests.validateTournamentPasscode(Integer.toString(_theList.get(_arrayPosition).gettID()), _tournamentPasscode.getText().toString());

        if (_tournamentPasscode.getText().toString() == "")
            Toast.makeText(getApplicationContext(), "You must enter a passcode.", Toast.LENGTH_SHORT).show();

        if (_tournamentSelected == "")
            Toast.makeText(getApplicationContext(), "You must select a tournament.", Toast.LENGTH_SHORT).show();

        if (!validationResponse.first)
            Toast.makeText(getApplicationContext(), validationResponse.second, Toast.LENGTH_LONG).show();
        else
        {
            Pair<Boolean, String> joinReturnValue = networkRequests.joinTournament(_tournamentID, _userID);
            if (joinReturnValue.first)
                launchPlayerTournamentValues();
            else
                Toast.makeText(getApplicationContext(), joinReturnValue.second, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            _theList = extras.getParcelableArrayList("TournamentValues");
            if (_theList != null && _theList.size() != 0)
            {
                _tournaments = new ArrayList<>();
                for (int listIndex = 0; listIndex < _theList.size(); listIndex++)
                {
                    _tournaments.add(_theList.get(listIndex).gettName());
                }
            }
        }
    }

    public void launchPlayerTournamentValues()
    {
        saveTournamentID();
        Intent intent = new Intent(getApplicationContext(), PlayerTournamentValues.class);
        startActivity(intent);
    }

    public void saveTournamentID()
    {
        String fileName = "tournamentID.txt";
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
                String json = gson.toJson(_tournamentID, type);
                buffWriter.write(json);
                buffWriter.newLine();
                buffWriter.close();
            }
            else
            {
                BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file, false));
                Gson gson = new Gson();
                Type type = new TypeToken<String>()
                {
                }.getType();
                String json = gson.toJson(_tournamentID, type);
                buffWriter.write(json);
                buffWriter.newLine();
                buffWriter.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
