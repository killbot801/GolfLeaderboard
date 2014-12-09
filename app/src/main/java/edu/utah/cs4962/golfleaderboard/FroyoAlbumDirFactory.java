package edu.utah.cs4962.golfleaderboard;

import android.os.Environment;

import java.io.File;

/**
 * Created by ljohnson on 12/8/14.
 */
public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory
{

    @Override
    public File getAlbumStorageDir(String albumName)
    {
        // TODO Auto-generated method stub
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
    }
}