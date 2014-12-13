package edu.utah.cs4962.golfleaderboard;

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
    PlayerParFragmentAdapter _parFragmentAdapter;
    ViewPager mViewPager;
    ArrayList<String> _tournamentValues;
    ArrayList<Pair<String, Integer>> _leaderboard;
    ArrayList<Integer> _playerHoleValues;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_tournament_values);
        setLeaderboardView();

        getParValues();
        getPlayerParValues();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        _parFragmentAdapter = new PlayerParFragmentAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(_parFragmentAdapter);

        if(getResources().getConfiguration().orientation == 1)
            findViewById(R.id.leaderboardTextView).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.leaderboardTextView).setVisibility(View.GONE);
    }

    public class PlayerParFragmentAdapter extends FragmentStatePagerAdapter
    {
        public PlayerParFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            Fragment fragment = new PlayerParValueView();

            Bundle args = new Bundle();

            args.putInt(PlayerParValueFragment.ARG_OBJECT, i + 1);
            args.putString(PlayerParValueFragment.PAR_OBJECT, "Par for current hole:\n" + _tournamentValues.get(i));
            args.putInt(PlayerParValueFragment.PLAYER_PAR_VALUE, _playerHoleValues.get(i));
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
            return "Enter Score for Hole " + (position + 1);
        }
    }

    public static class PlayerParValueFragment extends android.app.Fragment
    {
        public static final String ARG_OBJECT = "object";
        public static final String PAR_OBJECT = "ParValue";
        public static final String PLAYER_PAR_VALUE = "PlayerScore";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState)
        {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.player_par_value_view, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(R.id.parEntry)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));
            ((TextView) rootView.findViewById(R.id.parEntry)).setText(args.getInt(PLAYER_PAR_VALUE));
            return rootView;
        }
    }

    public void submitValues(View view)
    {
        //This is what will be needed for the updates on the user's tournament values page.
        int pos = mViewPager.getCurrentItem();
        PlayerParValueView parValue = (PlayerParValueView) _parFragmentAdapter.getItem(pos);
        int currentHoleValue = _playerHoleValues.get(pos);
        int currentHole = pos + 1;

        NetworkRequests nr = new NetworkRequests();
        Pair<Boolean, String> serverReturnValue = nr.updatePlayerScore(getUserID(), getTournamentID(), currentHoleValue, currentHole);
        setLeaderboardView();
        Toast.makeText(getApplicationContext(), serverReturnValue.second, Toast.LENGTH_LONG).show();
    }

    public void setParValueArray(int pos, int value)
    {
        _playerHoleValues.set(pos, value);
    }

    public String getUserID()
    {
        String returnValue = "";

        try
        {
            File file = new File(getApplicationContext().getFilesDir(), "GTourny.txt");
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(textReader);
            String line;
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

    public String getTournamentID()
    {
        String returnValue = "";

        try
        {
            File file = new File(getApplicationContext().getFilesDir(), "tournamentID.txt");
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

    public void setLeaderboardView()
    {
        TextView leaderboardText = (TextView) findViewById(R.id.leaderboardTextView);
        String textToShow = "";
        NetworkRequests nr = new NetworkRequests();
        _leaderboard = nr.getLeaderboard(getTournamentID());
        Pair<Boolean, String> playerScore = nr.getScoreById(getUserID(), getTournamentID());
        String playerScoreFromServer = "0";
        String playerRank = "0";
        Pair<Boolean, String> getPlayerName = nr.getPlayerNameFromID(getUserID());
        String playerName = "";
        String currentLeader = "";

        if (getPlayerName.first)
            playerName = getPlayerName.second;

        if (!playerName.equals(""))
        {
            currentLeader = _leaderboard.get(0).first;

            for (int leaderboardIndex = 0; leaderboardIndex < _leaderboard.size(); leaderboardIndex++)
            {
                if (_leaderboard.get(leaderboardIndex).first.equals(playerName))
                {
                    playerRank = Integer.toString(leaderboardIndex + 1);
                    playerScoreFromServer = Integer.toString(_leaderboard.get(leaderboardIndex).second);
                }
            }
        }

        if (_leaderboard.size() != 0)
        {
            textToShow = "Current Leader: " + _leaderboard.get(0).first + "\n" +
                    "Current Rank: " + playerRank + "\n" +
                    "Current Score: " + playerScoreFromServer;
        }
        else
        {
            textToShow = "Current Leader: " + currentLeader + "\n" +
                    "Current Rank: " + playerRank + "\n" +
                    "Current Score: " + playerScoreFromServer;
        }

        leaderboardText.setText(textToShow);
    }

    public void getParValues()
    {
        String tournamentID = getTournamentID();
        NetworkRequests nr = new NetworkRequests();
        _tournamentValues = nr.getParValues(tournamentID);

        if (_tournamentValues.size() == 0)
        {
            for (int i = 0; i < 18; i++)
                _tournamentValues.add("0");
        }
    }

    public void getPlayerParValues()
    {
        String tid = getTournamentID();
        String uid = getUserID();
        NetworkRequests nr = new NetworkRequests();
        _playerHoleValues = nr.getPlayerParValues(tid, uid);
    }

    public void launchLeaderboard(View view)
    {
        Toast.makeText(getApplicationContext(), "CLICKED!!", Toast.LENGTH_SHORT).show();
    }
}
