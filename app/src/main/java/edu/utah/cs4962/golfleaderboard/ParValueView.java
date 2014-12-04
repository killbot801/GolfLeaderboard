package edu.utah.cs4962.golfleaderboard;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by ljohnson on 12/3/14.
 */
public class ParValueView extends Fragment
{
    Button _increment, _decrement;
    EditText _parValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.par_value_view, null);
        _increment = (Button) v.findViewById(R.id.increment_button);
        _decrement = (Button) v.findViewById(R.id.decrement_button);
        _parValue = (EditText) v.findViewById(R.id.parEntry);
        _parValue.setText("3");
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void incrementValue(View view)
    {
        String currentValue = _parValue.getText().toString();
        int currentIntValue = Integer.parseInt(currentValue);
        _parValue.setText(currentIntValue++);
    }

    public void decrementValue(View view)
    {
        String currentValue = _parValue.getText().toString();
        int currentIntValue = Integer.parseInt(currentValue);
        _parValue.setText(currentIntValue--);
    }
}
