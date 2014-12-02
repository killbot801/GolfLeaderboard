package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ljohnson on 11/20/14.
 */
public class CreateJoinTournament extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_join_create);
    }

    public void launchJoinTournament(View view)
    {
        Toast.makeText(this, "You clicked the join tournament button!", Toast.LENGTH_SHORT).show();
    }

    public void launchCreateTournament(View view)
    {
        Toast.makeText(this, "You clicked the create tournament button!", Toast.LENGTH_SHORT).show();
        openCreateTournament();
    }

    public void openCreateTournament()
    {
        Intent intent = new Intent(this, CreateTournament.class);
        startActivity(intent);
    }
}
