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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by ljohnson on 11/20/14.
 */
public class CreateTournament extends Activity
{
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    EditText _tournamentDate;
    EditText _passcodeValue;
    EditText _stateSelected;
    EditText _courseCity;
    EditText _courseName;
    EditText _tournamentName;
    Spinner _stateSpinner;
    Boolean _justStarted = true;
    String _state;

    private AdapterView.OnItemSelectedListener _spinnerSelection = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            if (!_justStarted)
            {
                _stateSelected.setText(parent.getItemAtPosition(position).toString());
                _justStarted = false;
                _state = _stateSelected.getText().toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tournament);
        setupGlobals();
        findViewById(R.id.startDateButton).setVisibility(View.VISIBLE);
        findViewById(R.id.tournyStartDateSet).setVisibility(View.INVISIBLE);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _stateSpinner.setAdapter(adapter);

        _stateSpinner.setOnItemSelectedListener(_spinnerSelection);
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
                        _tournamentDate.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();

        findViewById(R.id.startDateButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.tournyStartDateSet).setVisibility(View.VISIBLE);
    }

    public void setupGlobals()
    {
        _tournamentDate = (EditText) findViewById(R.id.tournyStartDateSet);
        _passcodeValue = (EditText) findViewById(R.id.passcodeValue);
        _stateSpinner = (Spinner) findViewById(R.id.state);
        _stateSelected = (EditText) findViewById(R.id.courseStateValue);
        _courseCity = (EditText) findViewById(R.id.courseCityValue);
        _courseName = (EditText) findViewById(R.id.tournyLocationValue);
        _tournamentName = (EditText) findViewById(R.id.tournyNameValue);
    }

    public void generatePasscode(View view)
    {
        String newPassword = UUID.randomUUID().toString();
        newPassword = newPassword.substring(0, 8);
        _passcodeValue.setText(newPassword);
    }

    public void submitTournament(View view)
    {
        NetworkRequests nr = new NetworkRequests();
        Pair<Boolean, String> result = nr.validateTournament(_tournamentName.getText().toString(), _passcodeValue.getText().toString(),
                _tournamentDate.getText().toString(), getUserID(), _courseName.getText().toString(), _courseCity.getText().toString(),
                _state);

        if (result.first)
        {
            Intent intent = new Intent(this, SetupTournamentValues.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), result.second, Toast.LENGTH_LONG).show();
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
}
