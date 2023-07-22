package de.p2l.ui.ingame.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.p2l.R;

public class PlayScreen extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String level;

    private Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        myDialog = new Dialog(this);

        Drawable d = getDrawable(R.drawable.menu);
        level = getIntent().getStringExtra("level_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(d);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            myDialog.setContentView(R.layout.symbols);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button button = (Button) myDialog.findViewById(R.id.backBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.setContentView(R.layout.symbols2);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button button2 = (Button) myDialog.findViewById(R.id.backBtn);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.setContentView((R.layout.symbols3));
                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Button button3 = (Button) myDialog.findViewById(R.id.backBtn);
                            button3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.hide();
                                }
                            });
                            myDialog.show();
                        }
                    });
                    myDialog.show();
                }
            });
            myDialog.show();
        }
        if (id == R.id.Grundlagen) {
            myDialog.setContentView(R.layout.spielprinzip);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button button = (Button) myDialog.findViewById(R.id.backBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
            myDialog.show();
        }
        if (id == R.id.loop){
            myDialog.setContentView(R.layout.aufbauloop);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button button = (Button) myDialog.findViewById(R.id.backBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
            myDialog.show();
        }
        if(id == R.id.branch){
            myDialog.setContentView(R.layout.aufbaubranch);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button button = (Button) myDialog.findViewById(R.id.backBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
            myDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_play_screen, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                MapFragment fragment1 = new MapFragment();
                switch(level){
                    case "tut1": fragment1.setLevel(11); break;
                    case "tut2": fragment1.setLevel(12); break;
                    case "tut3": fragment1.setLevel(13); break;
                    case "tut4": fragment1.setLevel(14); break;
                    case "tut5": fragment1.setLevel(15); break;

                    case "easy1": fragment1.setLevel(21); break;
                    case "easy2": fragment1.setLevel(22); break;
                    case "easy3": fragment1.setLevel(23); break;
                    case "easy4": fragment1.setLevel(24); break;

                    case "midd1": fragment1.setLevel(31); break;
                    case "midd2": fragment1.setLevel(32); break;
                    case "midd3": fragment1.setLevel(33); break;
                    case "midd4": fragment1.setLevel(34); break;

                    case "diff1": fragment1.setLevel(41); break;
                    case "diff2": fragment1.setLevel(42); break;
                    case "diff3": fragment1.setLevel(43); break;
                    case "diff4": fragment1.setLevel(44); break;

                    default: break;
                }
                return fragment1;
            }
            else if(position==1){
                CommandsFragment fragment2 = new CommandsFragment();
                switch (level){
                    case "tut1": fragment2.setLevel(11); break;
                    case "tut2": fragment2.setLevel(12); break;
                    case "tut3": fragment2.setLevel(13); break;
                    case "tut4": fragment2.setLevel(14); break;
                    case "tut5": fragment2.setLevel(15); break;

                    case "easy1": fragment2.setLevel(21); break;
                    case "easy2": fragment2.setLevel(22); break;
                    case "easy3": fragment2.setLevel(23); break;
                    case "easy4": fragment2.setLevel(24); break;

                    case "midd1": fragment2.setLevel(31); break;
                    case "midd2": fragment2.setLevel(32); break;
                    case "midd3": fragment2.setLevel(33); break;
                    case "midd4": fragment2.setLevel(34); break;

                    case "diff1": fragment2.setLevel(41); break;
                    case "diff2": fragment2.setLevel(42); break;
                    case "diff3": fragment2.setLevel(43); break;
                    case "diff4": fragment2.setLevel(44); break;
                    default: break;
                }
                return fragment2;
            }
            else if(position==2){
                AnnotationsFragment fragment3 = new AnnotationsFragment();
                switch (level){
                    case "tut1": fragment3.setLevel(11); break;
                    case "tut2": fragment3.setLevel(12); break;
                    case "tut3": fragment3.setLevel(13); break;
                    case "tut4": fragment3.setLevel(14); break;
                    case "tut5": fragment3.setLevel(15); break;

                    case "easy1": fragment3.setLevel(21); break;
                    case "easy2": fragment3.setLevel(22); break;
                    case "easy3": fragment3.setLevel(23); break;
                    case "easy4": fragment3.setLevel(24); break;

                    case "midd1": fragment3.setLevel(31); break;
                    case "midd2": fragment3.setLevel(32); break;
                    case "midd3": fragment3.setLevel(33); break;
                    case "midd4": fragment3.setLevel(34); break;

                    case "diff1": fragment3.setLevel(41); break;
                    case "diff2": fragment3.setLevel(42); break;
                    case "diff3": fragment3.setLevel(43); break;
                    case "diff4": fragment3.setLevel(44); break;
                    default: break;
                }
                return fragment3;
            }
            else {
                System.out.println("error");
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
