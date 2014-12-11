package edu.utah.cs4962.golfleaderboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ljohnson on 12/8/14.
 */
public class EditAccount extends Activity
{
    EditText _password;
    EditText _firstName;
    EditText _lastName;
    EditText _city;
    EditText _state;
    EditText _email;
    String _avatarPath;

    private AlbumStorageDirFactory _mAlbumStorageDirFactory = null;

    ImageView _picButton;
    private String _currentPhotoPath;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int ACTION_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_edit);
        setupGlobals();

        //Button picBtn = (Button) findViewById(R.id.btnIntend);
        _picButton = (ImageView) findViewById(R.id.btnIntend);
        setBtnListenerOrDisable(_picButton, mTakePicOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
        {
            _mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        }
        else
        {
            _mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        getAccountData();
        if (_avatarPath != "default")
            Ion.with(_picButton).error(R.drawable.default_user).load(_avatarPath);
    }

    private void getAccountData()
    {
        NetworkRequests nr = new NetworkRequests();
        ArrayList<String> data = nr.getAccountData("1");
        _firstName.setText(data.get(0));
        _lastName.setText(data.get(1));
        _email.setText(data.get(2));
        _city.setText(data.get(3));
        _state.setText(data.get(4));
        _password.setText(data.get(5));
        _avatarPath = data.get(6);
        //image is index 6
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

    public void setupGlobals()
    {
        _password = (EditText) findViewById(R.id.createPassword);
        _firstName = (EditText) findViewById(R.id.firstName);
        _lastName = (EditText) findViewById(R.id.lastNameTextEntry);
        _city = (EditText) findViewById(R.id.cityEntry);
        _state = (EditText) findViewById(R.id.stateEntry);
        _email = (EditText) findViewById(R.id.emailEntry);
        _avatarPath = "";
    }

    private void setBtnListenerOrDisable(ImageView btn, Button.OnClickListener onClickListener, String intentName)
    {
        if (isIntentAvailable(this, intentName))
        {
            btn.setOnClickListener(onClickListener);
        }
        else
        {
            //btn.setText(getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

    public static boolean isIntentAvailable(Context context, String action)
    {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    Button.OnClickListener mTakePicOnClickListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case ACTION_TAKE_PHOTO:
            {
                if (resultCode == RESULT_OK)
                {
                    handlePhoto();
                }
                break;
            }
        }
    }

    private void handlePhoto()
    {

        if (_currentPhotoPath != null)
        {
            setPic();
            galleryAddPic();
            //TODO: ADD CALL TO UPDATE PATH IN SQL
            _currentPhotoPath = null;
        }

    }

    private void setPic()
    {
        Ion.with(_picButton).placeholder(R.drawable.apptheme_btn_check_holo_light).error(R.drawable.default_user).load(_currentPhotoPath);
    }


    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(_currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode)
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try
        {
            f = setUpPhotoFile();
            _currentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            f = null;
            _currentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, actionCode);
    }

    private File setUpPhotoFile() throws IOException
    {

        File f = createImageFile();
        _currentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir()
    {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            if (storageDir != null)
            {
                if (!storageDir.mkdirs())
                {
                    if (!storageDir.exists())
                    {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        }
        else
        {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }


    public void updateAccount(View view)
    {
        Toast.makeText(getApplicationContext(), "HERRO", Toast.LENGTH_SHORT).show();
    }
}