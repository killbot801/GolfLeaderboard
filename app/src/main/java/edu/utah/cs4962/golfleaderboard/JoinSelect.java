package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
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
    Spinner _tournamentSpinner;
    String _tournamentSelected;
    int _arrayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_tournament_select);
        _userID = getUserID();
        setupGlobals();

        ArrayAdapter<String> tournamentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, _tournaments);

        tournamentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _tournamentSpinner.setAdapter(tournamentAdapter);

        _tournamentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                _tournamentSelected = parent.getItemAtPosition(position).toString();
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

        return super.onOptionsItemSelected(item);
    }

    public void setupGlobals()
    {
        _tournamentSpinner = (Spinner) findViewById(R.id.tournamentSelectionSpinner);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            _theList = extras.getParcelableArrayList("TournamentValues");
            if(_theList != null && _theList.size() != 0)
            {
                _tournaments = new ArrayList<String>();
                for(int listIndex = 0; listIndex < _theList.size(); listIndex++)
                {
                    _tournaments.add(_theList.get(listIndex).gettName());
                }
            }
        }
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

    @Override
    protected void onResume()
    {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            _theList = extras.getParcelableArrayList("TournamentValues");
            if(_theList != null && _theList.size() != 0)
            {
                _tournaments = new ArrayList<String>();
                for(int listIndex = 0; listIndex < _theList.size(); listIndex++)
                {
                    _tournaments.add(_theList.get(listIndex).gettName());
                }
            }
        }
    }
}
