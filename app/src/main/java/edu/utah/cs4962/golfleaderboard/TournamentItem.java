package edu.utah.cs4962.golfleaderboard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ljohnson on 12/5/14.
 */
public class TournamentItem implements Parcelable
{
    private int _tID;
    private String _tName;
    private String _cName;

    public TournamentItem(Parcel p)
    {
        _tID = p.readInt();
        _tName = p.readString();
        _cName = p.readString();
    }

    public TournamentItem(int tid, String tname, String cname)
    {
        this._tID = tid;
        this._tName = tname;
        this._cName = cname;
    }

    public int gettID()
    {
        return _tID;
    }

    public String gettName()
    {
        return _tName;
    }

    public String getcName()
    {
        return _cName;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(_tID);
        dest.writeString(_tName);
        dest.writeString(_cName);
    }

    public static final Creator<TournamentItem> CREATOR =
            new Creator<TournamentItem>()
            {
                public TournamentItem createFromParcel(Parcel in)
                {
                    return new TournamentItem(in);
                }

                public TournamentItem[] newArray(int size)
                {
                    return new TournamentItem[size];
                }
            };
}
