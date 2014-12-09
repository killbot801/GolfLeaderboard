package edu.utah.cs4962.golfleaderboard;

import java.io.File;

/**
 * Created by ljohnson on 12/8/14.
 */
abstract class AlbumStorageDirFactory
{
    public abstract File getAlbumStorageDir(String albumName);
}
