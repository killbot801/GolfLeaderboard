package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ljohnson on 12/5/14.
 */
public class JoinSelect extends Activity
{
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    String _userID;
    ArrayList<TournamentItem> _theList;
    ArrayList<String> _tournaments;
    Spinner _tournamentSpinner;
    String _tournamentSelected;
    int _arrayPosition;
    EditText _tournamentDate;

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

    public void openStartDatePicker(View view)
    {
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {
                        String day = "0";

                        if(dayOfMonth < 10)
                            day = day + dayOfMonth;
                        else
                            day = Integer.toString(dayOfMonth);

                        _tournamentDate.setText(year + "-"
                                + (monthOfYear + 1) + "-" + day);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();

        findViewById(R.id.tournamentDateDisplay).setVisibility(View.VISIBLE);
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

        _tournamentDate = (EditText) findViewById(R.id.tournamentDateDisplay);
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
        if(_tournamentDate.getText().toString() == "")
            Toast.makeText(getApplicationContext(), "You must select a date.", Toast.LENGTH_SHORT).show();

        if(_tournamentSelected == "")
            Toast.makeText(getApplicationContext(), "You must select a tournament.", Toast.LENGTH_SHORT).show();

        NetworkRequests networkRequests = new NetworkRequests();
        Pair<Boolean, String> joinReturnValue = networkRequests.joinTournament(Integer.toString(_theList.get(_arrayPosition).gettID()), getUserID());

        if(joinReturnValue.first)
            launchPlayerTournamentValues();
        else
            Toast.makeText(getApplicationContext(), joinReturnValue.second, Toast.LENGTH_LONG).show();
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
                String json = gson.toJson(_tournamentSelected, type);
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
