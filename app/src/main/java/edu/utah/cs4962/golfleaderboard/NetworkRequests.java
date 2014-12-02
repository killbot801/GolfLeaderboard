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
                Pair<Boolean, String> loginStatus = new Pair<Boolean, String>(false, "Error: Authentication failed.");

                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost("http://www.memnochdacoder.com/authenticatePlayer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("userName", userName);
                    jsonObject.accumulate("userPass", userPass);

                    String json = jsonObject.toString();

                    /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("userName", userName));
                    nameValuePairs.add(new BasicNameValuePair("userPass", userPass));*/

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
                            return null;

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
}
