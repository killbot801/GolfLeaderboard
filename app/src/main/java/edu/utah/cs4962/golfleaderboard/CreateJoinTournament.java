package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        return super.onOptionsItemSelected(item);
    }

    public void launchJoinTournament(View view)
    {
        openJoinTournament();
    }

    public void launchCreateTournament(View view)
    {
        openCreateTournament();
    }

    public void openCreateTournament()
    {
        Intent intent = new Intent(this, CreateTournament.class);
        startActivity(intent);
    }

    public void openJoinTournament()
    {
        Toast.makeText(this, "You clicked the join tournament button!", Toast.LENGTH_SHORT).show();
    }
}
