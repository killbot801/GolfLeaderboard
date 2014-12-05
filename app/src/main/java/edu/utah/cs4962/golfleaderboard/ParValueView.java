package edu.utah.cs4962.golfleaderboard;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ljohnson on 12/3/14.
 */
public class ParValueView extends Fragment
{

    EditText _parValue;
    int _position;
    int _editTextValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.par_value_view, null);
        _parValue = (EditText) v.findViewById(R.id.parEntry);

        v.findViewById(R.id.increment_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String currentValue = _parValue.getText().toString();
                int newValue = Integer.parseInt(currentValue) + 1;
                _parValue.setText(newValue + "");
                ((SetupTournamentValues) getActivity()).setParValueArray(_position, newValue);
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
                ((SetupTournamentValues) getActivity()).setParValueArray(_position, newValue);
            }
        });

        Bundle args = getArguments();

        if(args == null)
            Toast.makeText(getActivity().getApplicationContext(), "The args are empty.", Toast.LENGTH_LONG).show();
        else
            _position = args.getInt(SetupTournamentValues.ParValueFragment.ARG_OBJECT) - 1;

        return v;
    }

    public void setPosition(int position)
    {
        _position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
