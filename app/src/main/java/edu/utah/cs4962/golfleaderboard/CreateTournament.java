package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

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
    EditText _tournyStartDateSet;
    EditText _passcodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tournament);
        setupGlobals();
        findViewById(R.id.startDateButton).setVisibility(View.VISIBLE);
        findViewById(R.id.tournyStartDateSet).setVisibility(View.INVISIBLE);
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
                        _tournyStartDateSet.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();

        findViewById(R.id.startDateButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.tournyStartDateSet).setVisibility(View.VISIBLE);
    }

    public void setupGlobals()
    {
        _tournyStartDateSet = (EditText) findViewById(R.id.tournyStartDateSet);
        _passcodeValue = (EditText)findViewById(R.id.passcodeValue);
    }

    public void generatePasscode(View view)
    {
        String newPassword = UUID.randomUUID().toString();
        newPassword = newPassword.substring(0, 8);
        _passcodeValue.setText(newPassword);
    }

    public void submitTournament()
    {
        //TODO: send call to the server to validate, handle return
        Intent intent = new Intent(this, SetupTournamentValues.class);
        startActivity(intent);
    }
}
