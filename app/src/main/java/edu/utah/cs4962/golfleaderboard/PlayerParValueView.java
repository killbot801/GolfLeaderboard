package edu.utah.cs4962.golfleaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ljohnson on 12/3/14.
 */
public class PlayerParValueView extends Fragment
{

    EditText _parValue;
    int _position;
    int _editTextValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.player_par_value_view, null);
        _parValue = (EditText) v.findViewById(R.id.parEntry);

        v.findViewById(R.id.increment_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String currentValue = _parValue.getText().toString();
                int newValue = Integer.parseInt(currentValue) + 1;
                _editTextValue = newValue;
                Bundle valueBundle = new Bundle();
                _parValue.setText(newValue + "");
                ((PlayerTournamentValues) getActivity()).setParValueArray(_position, newValue);
            }
        });

        v.findViewById(R.id.decrement_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String currentValue = _parValue.getText().toString();
                int newValue = Integer.parseInt(currentValue) - 1;
                _editTextValue = newValue;
                _parValue.setText(newValue + "");
                ((PlayerTournamentValues) getActivity()).setParValueArray(_position, newValue);
            }
        });

        Bundle args = getArguments();

        if (args == null)
            Toast.makeText(getActivity().getApplicationContext(), "The args are empty.", Toast.LENGTH_LONG).show();
        else
        {
            _position = args.getInt(PlayerTournamentValues.PlayerParValueFragment.ARG_OBJECT) - 1;
            TextView parValueTextBox = (TextView) v.findViewById(R.id.parValue);
            parValueTextBox.setText(args.getString(PlayerTournamentValues.PlayerParValueFragment.PAR_OBJECT));
            EditText playerHoleEntry = (EditText) v.findViewById(R.id.parEntry);
            playerHoleEntry.setText(args.getInt(PlayerTournamentValues.PlayerParValueFragment.PLAYER_PAR_VALUE) + "");
        }

        return v;
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


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
