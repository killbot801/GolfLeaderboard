package edu.utah.cs4962.golfleaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ljohnson on 11/21/14.
 */
public class PlayerTournamentValues extends FragmentActivity
{
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ParFragmentAdapter _parFragmentAdapter;
    ViewPager mViewPager;
    ArrayList<Integer> _parValues;
    ArrayList<String> _tournamentValues;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_values);

        _parValues = new ArrayList<Integer>();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        _parFragmentAdapter = new ParFragmentAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(_parFragmentAdapter);

        for (int i = 0; i < 18; i++)
            _parValues.add(0);
    }

    public class ParFragmentAdapter extends FragmentStatePagerAdapter
    {
        public ParFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            Fragment fragment = new ParValueView();
            Bundle args = new Bundle();

            args.putInt(ParValueFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount()
        {
            return 18;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return "Par Value for Hole " + (position + 1);
        }
    }

    public static class ParValueFragment extends android.app.Fragment
    {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState)
        {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.par_value_view, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(R.id.parEntry)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));
            ((TextView) rootView.findViewById(R.id.parEntry)).setText("3");
            return rootView;
        }
    }

    public void submitValues(View view)
    {
        //This is what will be needed for the updates on the user's tournament values page.
        int pos = mViewPager.getCurrentItem();
        ParValueView parValue = (ParValueView) _parFragmentAdapter.getItem(pos);
        int currentHoleValue = parValue._editTextValue;
        int currentHole = pos + 1;

        NetworkRequests nr = new NetworkRequests();
        Pair<Boolean, String> serverReturnValue = nr.updatePlayerScore(getUserID(), currentHoleValue, currentHole);

        Toast.makeText(getApplicationContext(), serverReturnValue.second, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            _tournamentValues = extras.getStringArrayList("TournamentValues");
        }
    }

    private void openJoinCreate()
    {
        Intent intent = new Intent(getApplicationContext(), CreateJoinTournament.class);
        startActivity(intent);
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
