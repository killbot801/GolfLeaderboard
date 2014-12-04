package edu.utah.cs4962.golfleaderboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ljohnson on 11/21/14.
 */
public class SetupTournamentValues extends FragmentActivity
{
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ParFragmentAdapter _parFragmentAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_values);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        _parFragmentAdapter = new ParFragmentAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(_parFragmentAdapter);
    }

    public class ParFragmentAdapter extends FragmentStatePagerAdapter
    {
        public ParFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            Fragment fragment = new ParValueView();
            Bundle args = new Bundle();

            args.putInt(ParValueFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount()
        {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return "Par Value for Hole " + (position + 1);
        }
    }

    public static class ParValueFragment extends android.app.Fragment
    {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState)
        {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.par_value_view, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(R.id.parEntry)).setText(
                    Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }
}
