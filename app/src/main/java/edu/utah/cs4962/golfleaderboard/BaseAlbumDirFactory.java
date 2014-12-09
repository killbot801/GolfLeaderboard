package edu.utah.cs4962.golfleaderboard;

import android.os.Environment;

import java.io.File;

/**
 * Created by ljohnson on 12/8/14.
 */
public final class BaseAlbumDirFactory extends AlbumStorageDirFactory
{

    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName)
    {
        return new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + albumName);
    }
}