package edu.utah.cs4962.golfleaderboard;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
}
