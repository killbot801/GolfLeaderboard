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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ljohnson on 12/2/14.
 */
public class JoinTournament extends Activity
{
    Spinner _stateSpinner;
    String _stateSelected;
    EditText _tournamentDate;
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_tournament);
        setupGlobals();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _stateSpinner.setAdapter(adapter);

        _stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                _stateSelected = parent.getItemAtPosition(position).toString();
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
        _stateSpinner = (Spinner) findViewById(R.id.stateSelectionForJoin);
        _tournamentDate = (EditText) findViewById(R.id.tournamentDateSelectedToJoin);
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
        findViewById(R.id.joinTournamentDateSelect).setVisibility(View.INVISIBLE);
        findViewById(R.id.tournamentDateSelectedToJoin).setVisibility(View.VISIBLE);

    }

    public void submitJoinSearch(View view)
    {
        NetworkRequests nr = new NetworkRequests();
        ArrayList<TournamentItem> tournamentList = nr.getTournamentListForJoin(_stateSelected, _tournamentDate.getText().toString());
        if(tournamentList.size() == 0)
            Toast.makeText(getApplicationContext(), "There are no tournaments listed for that date/state combination.", Toast.LENGTH_LONG).show();
        else if(tournamentList.get(0).gettID() == 1000)
            Toast.makeText(getApplicationContext(), tournamentList.get(0).gettName(), Toast.LENGTH_LONG);
        else
            openJoinSelect(tournamentList);
    }

    public void openJoinSelect(ArrayList<TournamentItem> tournamentItems)
    {
        Intent intent = new Intent(getApplicationContext(), JoinSelect.class);
        intent.putExtra("TournamentValues", tournamentItems);
        startActivity(intent);
    }
}
