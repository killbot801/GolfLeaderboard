package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by killbot on 12/11/14.
 */
public class LeaderBoard extends Activity
{
    TextView _player1, _player2, _player3, _player4, _player5, _player6, _player7, _player8, _player9, _player10;
    TextView _score1, _score2, _score3, _score4, _score5, _score6, _score7, _score8, _score9, _score10;
    ArrayList<String> _playerNames;
    ArrayList<String> _playerScores;
    String _tid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        setupGlobals();
        getLeaderboardData(_tid);

    }

    public void setupGlobals()
    {
        _playerNames = new ArrayList<String>();
        _playerScores = new ArrayList<String>();
        _tid = getTournamentID();

        for (int i = 0; i < 10; i++)
        {
            _playerNames.add("");
            _playerScores.add("");
        }

        _player1 = (TextView) findViewById(R.id.place1user);
        _score1 = (TextView) findViewById(R.id.place1score);
        _player2 = (TextView) findViewById(R.id.place2user);
        _score2 = (TextView) findViewById(R.id.place2score);
        _player3 = (TextView) findViewById(R.id.place3user);
        _score3 = (TextView) findViewById(R.id.place3score);
        _player4 = (TextView) findViewById(R.id.place4user);
        _score4 = (TextView) findViewById(R.id.place4score);
        _player5 = (TextView) findViewById(R.id.place5user);
        _score5 = (TextView) findViewById(R.id.place5score);
        _player6 = (TextView) findViewById(R.id.place6user);
        _score6 = (TextView) findViewById(R.id.place6score);
        _player7 = (TextView) findViewById(R.id.place7user);
        _score7 = (TextView) findViewById(R.id.place7score);
        _player8 = (TextView) findViewById(R.id.place8user);
        _score8 = (TextView) findViewById(R.id.place8score);
        _player9 = (TextView) findViewById(R.id.place9user);
        _score9 = (TextView) findViewById(R.id.place9score);
        _player10 = (TextView) findViewById(R.id.place10user);
        _score10 = (TextView) findViewById(R.id.place10score);
    }

    private void getLeaderboardData(String tid)
    {
        NetworkRequests nr = new NetworkRequests();
        ArrayList<Pair<String, Integer>> data = nr.getLeaderboard(tid);
        for (int i = 0; i < data.size(); i++)
        {
            _playerNames.add(i, data.get(i).first);
            _playerScores.add(i, data.get(i).second.toString());
        }

        _player1.setText(_playerNames.get(0));
        _score1.setText(_playerScores.get(0));
        _player2.setText(_playerNames.get(1));
        _score2.setText(_playerScores.get(1));
        _player3.setText(_playerNames.get(2));
        _score3.setText(_playerScores.get(2));
        _player4.setText(_playerNames.get(3));
        _score4.setText(_playerScores.get(3));
        _player5.setText(_playerNames.get(4));
        _score5.setText(_playerScores.get(4));
        _player6.setText(_playerNames.get(5));
        _score6.setText(_playerScores.get(5));
        _player7.setText(_playerNames.get(6));
        _score7.setText(_playerScores.get(6));
        _player8.setText(_playerNames.get(7));
        _score8.setText(_playerScores.get(7));
        _player9.setText(_playerNames.get(8));
        _score9.setText(_playerScores.get(8));
        _player10.setText(_playerNames.get(9));
        _score10.setText(_playerScores.get(9));
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

    public void leaderboardBack(View v)
    {
        Intent intent = new Intent(getApplicationContext(), PlayerTournamentValues.class);
        startActivity(intent);
    }
}
