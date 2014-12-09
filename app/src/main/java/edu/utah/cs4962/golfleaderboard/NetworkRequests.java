package edu.utah.cs4962.golfleaderboard;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljohnson on 12/1/14.
 */
public class NetworkRequests
{
    public static NetworkRequests _instance = null;
    public String _userID;
    public String _tournamentID;

    public static NetworkRequests getNetworkRequestInstance()
    {
        if(_instance == null)
            _instance = new NetworkRequests();

        return _instance;
    }

    public Pair<Boolean, String> authenticateLogin(final String userName, final String userPass)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> authenticateLogin = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";
                Pair<Boolean, String> loginStatus;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/authenticatePlayer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("userName", userName);
                    jsonObject.accumulate("userPass", userPass);

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        loginStatus = new Pair<Boolean, String>(boolToggle, authMessage);

                        return loginStatus;
                    }
                    catch (Exception e)
                    {
                        Log.i("Authenticate Login: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        loginStatus = new Pair<Boolean, String>(false, "Authenticate Login: Exception found during JSON conversion.");
                        return loginStatus;
                    }
                }
                catch (IOException e)
                {
                    Log.i("Authenticate Login: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    loginStatus = new Pair<Boolean, String>(false, "Authenticate Login: Exception found during HTTP request.");
                    return loginStatus;
                }
                catch (JSONException e)
                {
                    Log.i("Authenticate Login: ", "Exception found during JSON setup.");
                    e.printStackTrace();
                    loginStatus = new Pair<Boolean, String>(false, "Authenticate Login: Exception found during JSON conversion.");
                    return loginStatus;
                }
            }
        };

        authenticateLogin.execute();

        try
        {
            return authenticateLogin.get();
        }
        catch (Exception e)
        {
            Log.e("Authenticate Login: ", "There was an error authenticating the login.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: Could not authenticate login.");
        }
    }

    public Pair<Boolean, String> getUserID(final String userName)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> getUserID = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";
                Pair<Boolean, String> loginStatus;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet("http://www.memnochdacoder.com/getUserIDByName/" + userName);
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        if(contentString.equals("404"))
                            return new Pair<Boolean, String>(false, "Error: Username reports as incorrect.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        loginStatus = new Pair<Boolean, String>(boolToggle, authMessage);

                        return loginStatus;
                    }
                    catch (Exception e)
                    {
                        Log.i("Get User ID: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        loginStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                        return loginStatus;
                    }
                }
                catch (IOException e)
                {
                    Log.i("Get User ID: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    loginStatus = new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                    return loginStatus;
                }
            }
        };

        getUserID.execute();

        try
        {
            return getUserID.get();
        }
        catch (Exception e)
        {
            Log.e("Get User ID: ", "There was an error getting the user ID.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: Could not get user ID.");
        }
    }

    public Pair<Boolean, String> createUser(final String userName, final String userPass, final String firstName, final String lastName, final String city, final String state, final String email)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> createUser = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";
                Pair<Boolean, String> addUserStatus;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/newPlayer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("UserName", userName);
                    jsonObject.accumulate("Password", userPass);
                    jsonObject.accumulate("FirstName", firstName);
                    jsonObject.accumulate("LastName", lastName);
                    jsonObject.accumulate("City", city);
                    jsonObject.accumulate("State", state);
                    jsonObject.accumulate("Email", email);

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        addUserStatus = new Pair<Boolean, String>(boolToggle, authMessage);

                        return addUserStatus;
                    }
                    catch (Exception e)
                    {
                        Log.i("Add New User: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                        return addUserStatus;
                    }
                }
                catch (IOException e)
                {
                    Log.i("Add New User: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                    return addUserStatus;
                }
                catch (JSONException e)
                {
                    Log.i("Add New User: ", "Exception found during JSON setup.");
                    e.printStackTrace();
                    addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                    return addUserStatus;
                }
            }
        };

        createUser.execute();

        try
        {
            return createUser.get();
        }
        catch (Exception e)
        {
            Log.e("Add New User: ", "There was an error authenticating the login.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: Could not authenticate login.");
        }
    }

    public Pair<Boolean, String> validateTournament(final String TName, final String userPass, final String Date, final String Admin, final String CourseName, final String CourseCity, final String CourseState)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> validateTournament = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";
                Pair<Boolean, String> addUserStatus;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/validateTournament");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("TName", TName);
                    jsonObject.accumulate("Password", userPass);
                    jsonObject.accumulate("Date", Date);
                    jsonObject.accumulate("Admin", Admin);
                    jsonObject.accumulate("CourseName", CourseName);
                    jsonObject.accumulate("CourseCity", CourseCity);
                    jsonObject.accumulate("CourseState", CourseState);

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        addUserStatus = new Pair<Boolean, String>(boolToggle, authMessage);

                        return addUserStatus;
                    }
                    catch (Exception e)
                    {
                        Log.i("Add New User: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                        return addUserStatus;
                    }
                }
                catch (IOException e)
                {
                    Log.i("Add New User: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                    return addUserStatus;
                }
                catch (JSONException e)
                {
                    Log.i("Add New User: ", "Exception found during JSON setup.");
                    e.printStackTrace();
                    addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                    return addUserStatus;
                }
            }
        };

        validateTournament.execute();

        try
        {
            return validateTournament.get();
        }
        catch (Exception e)
        {
            Log.e("Add New User: ", "There was an error authenticating the login.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: Could not authenticate login.");
        }
    }

    public Pair<Boolean, String> createTournament(final ArrayList<String> tournamentValues, final ArrayList<Integer> parValues)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> createTournament = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";
                Pair<Boolean, String> addUserStatus;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/addTournament");
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.accumulate("TName", tournamentValues.get(0));
                    jsonObject.accumulate("Password", tournamentValues.get(1));
                    jsonObject.accumulate("Date", tournamentValues.get(2));
                    jsonObject.accumulate("Admin", tournamentValues.get(3));
                    jsonObject.accumulate("CourseName", tournamentValues.get(4));
                    jsonObject.accumulate("CourseCity", tournamentValues.get(5));
                    jsonObject.accumulate("CourseState", tournamentValues.get(6));

                    int holes = 1;

                    for(int parValuesIndex = 0; parValuesIndex < 18; parValuesIndex++)
                    {
                        jsonObject.accumulate(("Par" + holes++), parValues.get(parValuesIndex));
                    }

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        addUserStatus = new Pair<Boolean, String>(boolToggle, authMessage);

                        return addUserStatus;
                    }
                    catch (Exception e)
                    {
                        Log.i("Add Tournament: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                        return addUserStatus;
                    }
                }
                catch (IOException e)
                {
                    Log.i("Add Tournament: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                    return addUserStatus;
                }
                catch (JSONException e)
                {
                    Log.i("Add Tournament: ", "Exception found during JSON setup.");
                    e.printStackTrace();
                    addUserStatus = new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                    return addUserStatus;
                }
            }
        };

        createTournament.execute();

        try
        {
            return createTournament.get();
        }
        catch (Exception e)
        {
            Log.e("Add Tournament: ", "There was an error adding the tournament.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: Could not add the tournament, please try again.");
        }
    }

    public ArrayList<TournamentItem> getTournamentListForJoin(final String state, final String date)
    {
        AsyncTask<String, Integer, ArrayList<TournamentItem>> getTournamentListForJoin = new AsyncTask<String, Integer, ArrayList<TournamentItem>>()
        {
            @Override
            protected ArrayList<TournamentItem> doInBackground(String... params)
            {
                String contentString = "";
                ArrayList<TournamentItem> tournamentData = new ArrayList<TournamentItem>();
                TournamentItem tournamentItem;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet("http://www.memnochdacoder.com/getTournamentList/" + state + "/" + date);
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new ArrayList<TournamentItem>();

                        if(contentString.contains("Error:"))
                        {
                            tournamentItem = new TournamentItem(1000, "ERROR", contentString);
                            tournamentData.add(tournamentItem);
                            return tournamentData;
                        }

                        JSONArray jsonArray = new JSONArray(contentString);

                        //Iterate through the array and get the info in a format we can use.
                        for(int arrayIndex = 0; arrayIndex < jsonArray.length(); arrayIndex++)
                        {
                            tournamentItem = new TournamentItem(jsonArray.getJSONObject(arrayIndex).getInt("Item1"),
                                    jsonArray.getJSONObject(arrayIndex).getString("Item2"), jsonArray.getJSONObject(arrayIndex).getString("Item3"));
                            tournamentData.add(tournamentItem);
                        }

                        return tournamentData;
                    }
                    catch (Exception e)
                    {
                        Log.i("Get Tournament Data: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        return new ArrayList<TournamentItem>();
                    }
                }
                catch (IOException e)
                {
                    Log.i("Get Tournament Data: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    return new ArrayList<TournamentItem>();
                }
            }
        };

        getTournamentListForJoin.execute();

        try
        {
            return getTournamentListForJoin.get();
        }
        catch (Exception e)
        {
            Log.e("Get Tournament Data: ", "There was an error getting the user ID.");
            e.printStackTrace();
            return new ArrayList<TournamentItem>();
        }
    }

    public Pair<Boolean, String> joinTournament(final String tID, final String uID)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> joinTournament = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/addUserToTournament/" + uID + "/" + tID);
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.accumulate("userName", uID);
                    jsonObject.accumulate("tournamentID", tID);

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        return new Pair<Boolean, String>(boolToggle, authMessage);
                    }
                    catch (Exception e)
                    {
                        Log.i("Get Tournament Data: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        return new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                    }
                }
                catch (IOException e)
                {
                    Log.i("Get Tournament Data: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                }
                catch (JSONException e)
                {
                    Log.i("Get Tournament Data: ", "Exception found during JSON request.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception found during JSON request.");
                }
            }
        };

        joinTournament.execute();

        try
        {
            return joinTournament.get();
        }
        catch (Exception e)
        {
            Log.e("Join Tournament: ", "There was an error getting the tournament information.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: Issue retrieving join tournament information.");
        }
    }

    public Pair<Boolean, String> updatePlayerScore(final String userID, final String tournamentID, final int currentHoleValue, final int currentHole)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> updatePlayerScore = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/updateTournamentValue");
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.accumulate("userID", userID);
                    jsonObject.accumulate("tournamentID", tournamentID);
                    jsonObject.accumulate("holeToUpdate", currentHole);
                    jsonObject.accumulate("holeValue", currentHoleValue);

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: Return value from server empty.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        return new Pair<Boolean, String>(boolToggle, authMessage);
                    }
                    catch (Exception e)
                    {
                        Log.i("Get Tournament Data: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        return new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                    }
                }
                catch (IOException e)
                {
                    Log.i("Update Tournament Data: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                }
                catch (JSONException e)
                {
                    Log.i("Update Tournament Data: ", "Exception found during JSON setup.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception setting up the network call.");
                }
            }
        };

        updatePlayerScore.execute();

        try
        {
            return updatePlayerScore.get();
        }
        catch (Exception e)
        {
            Log.e("Update Tournament Data: ", "There was an error update the tournament value.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: There was an error update the tournament value.");
        }
    }

    public ArrayList<Pair<String, Integer>> getLeaderboard(final String tid)
    {
        AsyncTask<String, Integer, ArrayList<Pair<String, Integer>>> getLeaderboard = new AsyncTask<String, Integer, ArrayList<Pair<String, Integer>>>()
        {
            @Override
            protected ArrayList<Pair<String, Integer>> doInBackground(String... params)
            {
                String contentString = "";
                ArrayList<Pair<String, Integer>> playerInfo = new ArrayList<Pair<String, Integer>>();
                Pair<String, Integer> playerData;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet("http://www.memnochdacoder.com/getLeaderboard/" + tid);
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new ArrayList<Pair<String, Integer>>();

                        JSONArray jsonArray = new JSONArray(contentString);

                        //Iterate through the array and get the info in a format we can use.
                        for(int arrayIndex = 0; arrayIndex < jsonArray.length(); arrayIndex += 2)
                        {
                            playerData = new Pair<String, Integer>(jsonArray.getString(arrayIndex), jsonArray.getInt(arrayIndex + 1));
                            playerInfo.add(playerData);
                        }

                        return playerInfo;
                    }
                    catch (Exception e)
                    {
                        Log.i("Get Tournament Leaderboard: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        return new ArrayList<Pair<String, Integer>>();
                    }
                }
                catch (IOException e)
                {
                    Log.i("Get Tournament Leaderboard: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    return new ArrayList<Pair<String, Integer>>();
                }
            }
        };

        getLeaderboard.execute();

        try
        {
            return getLeaderboard.get();
        }
        catch (Exception e)
        {
            Log.e("Get Tournament Leaderboard: ", "There was an error getting the user leaderboard.");
            e.printStackTrace();
            return new ArrayList<Pair<String, Integer>>();
        }
    }

    public Pair<Boolean, String> getScoreById(final String userID, final String tournamentID)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> getScoreById = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/getScoreById");
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.accumulate("userID", userID);
                    jsonObject.accumulate("tournamentID", tournamentID);

                    String json = jsonObject.toString();

                    request.setEntity(new StringEntity(json));
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: Return value from server empty.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        return new Pair<Boolean, String>(boolToggle, authMessage);
                    }
                    catch (Exception e)
                    {
                        Log.i("Get Player Score: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        return new Pair<Boolean, String>(false, "Error: Exception found during JSON conversion.");
                    }
                }
                catch (IOException e)
                {
                    Log.i("Get Player Score: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception found during HTTP request.");
                }
                catch (JSONException e)
                {
                    Log.i("Get Player Score: ", "Exception found during JSON setup.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception setting up the network call.");
                }
            }
        };

        getScoreById.execute();

        try
        {
            return getScoreById.get();
        }
        catch (Exception e)
        {
            Log.e("Get Player Score: ", "There was an error update the tournament value.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: There was an error update the tournament value.");
        }
    }

    public Pair<Boolean, String> getPlayerNameFromID(final String userID)
    {
        AsyncTask<String, Integer, Pair<Boolean, String>> getPlayerNameFromID = new AsyncTask<String, Integer, Pair<Boolean, String>>()
        {
            @Override
            protected Pair<Boolean, String> doInBackground(String... params)
            {
                String contentString = "";
                Pair<Boolean, String> playerData;

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet("http://www.memnochdacoder.com/getNameById/" + userID);
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-Type", "application/json");
                    HttpResponse response = client.execute(request);

                    InputStream content = response.getEntity().getContent();

                    BufferedReader httpReader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = httpReader.readLine()) != null)
                        contentString += line;

                    try
                    {
                        if (contentString.length() <= 0)
                            return new Pair<Boolean, String>(false, "Error: No data returned from server.");

                        JsonElement ele = new JsonParser().parse(contentString);
                        JsonObject obj = ele.getAsJsonObject();
                        Boolean boolToggle = obj.get("Success").getAsBoolean();
                        String authMessage = obj.get("Message").getAsString();

                        return new Pair<Boolean, String>(boolToggle, authMessage);
                    }
                    catch (Exception e)
                    {
                        Log.i("Player Name: ", "Exception found during JSON conversion.");
                        e.printStackTrace();
                        return new Pair<Boolean, String>(false, "Error: JSON conversion failed.");
                    }
                }
                catch (IOException e)
                {
                    Log.i("Player Name: ", "Exception found during HTTP request.");
                    e.printStackTrace();
                    return new Pair<Boolean, String>(false, "Error: Exception during HTTP request.");
                }
            }
        };

        getPlayerNameFromID.execute();

        try
        {
            return getPlayerNameFromID.get();
        }
        catch (Exception e)
        {
            Log.e("Player Name: ", "There was an error getting the user leaderboard.");
            e.printStackTrace();
            return new Pair<Boolean, String>(false, "Error: General error retrieving the user name.");
        }
    }
}
