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
    EditText tournyStartDateSet = (EditText) findViewById(R.id.tournyStartDateSet);
    EditText tournyEndDateValue = (EditText) findViewById(R.id.tournyEndDateValue);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tournament);
        findViewById(R.id.tournyEndDateValue).setVisibility(View.INVISIBLE);
        findViewById(R.id.endDateButton).setVisibility(View.VISIBLE);
        findViewById(R.id.startDateButton).setVisibility(View.VISIBLE);
        findViewById(R.id.tournyStartDateSet).setVisibility(View.INVISIBLE);
    }

    public void openStartDatePicker()
    {
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {
                        tournyStartDateSet.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();

        findViewById(R.id.startDateButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.tournyStartDateSet).setVisibility(View.VISIBLE);
    }

    public void openEndDatePicker()
    {
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {
                        tournyEndDateValue.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();

        findViewById(R.id.tournyEndDateValue).setVisibility(View.VISIBLE);
        findViewById(R.id.endDateButton).setVisibility(View.INVISIBLE);
    }

    public void generatePasscode()
    {
        String newPassword = UUID.randomUUID().toString();
        newPassword = newPassword.substring(0, 8);
        EditText passVal = (EditText)findViewById(R.id.passcodeValue);
        passVal.setText(newPassword);
    }

    public void submitTournament()
    {
        //TODO: send call to the server to validate, handle return
        Intent intent = new Intent(this, SetupTournamentValues.class);
        startActivity(intent);
    }
}
