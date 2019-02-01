/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.prebhans.checkbluetooth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic sample which shows how to use
 * {@link com.gmail.prebhans.checkbluetooth.SlidingTabLayout} to display a custom
 * {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsColorsFragment extends Fragment {
    private static final String TAG = "SlidTabsColorsFrag";
    private Context context;
    int deviceWidth;

    public SlidingTabsColorsFragment() {

        Log.i(TAG, "SlidingTabsColorsFragment start");
    }

    @SuppressLint("ValidFragment")
    public SlidingTabsColorsFragment(Context context) {
        this.context = context;
        Log.i(TAG, "SlidingTabsColorsFragment start");
    }

    /**
     * This class represents a tab to be displayed by {@link ViewPager} and it's
     * associated {@link SlidingTabLayout}.
     */
    static class SamplePagerItem {
        private final Context mContext;
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;
        static final String TAG = "SamplePagerItem";

        SamplePagerItem(Context context, CharSequence title,
                        int indicatorColor, int dividerColor) {
            mContext = context;
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            Log.i(TAG, "SamplePagerItem start");
        }

        /**
         * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
         */
        Fragment createFragment() {
            Log.i(TAG, "createFragment start");
            Fragment locCreateFragment = null;
            String lScanTitle = mContext.getString(R.string.tab_scanning);
            String lDataTitle = mContext.getString(R.string.tab_database);
            String lPairTitle = mContext.getString(R.string.tab_paired);
            if (mTitle.equals(lScanTitle))
                locCreateFragment = new Scanning();
            else if (mTitle.equals(lDataTitle))
                locCreateFragment = new Database();
            else if (mTitle.equals(lPairTitle)) {
                locCreateFragment = new Paired();
            }
            Log.i(TAG, "createFragment End");
            return locCreateFragment;
        }

        /**
         * @return the title which represents this tab. In this sample this is
         * used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {
            Log.i(TAG, "getTitle start");
            return mTitle;
        }

        /**
         * @return the color to be used for indicator on the
         * {@link SlidingTabLayout}
         */
        int getIndicatorColor() {
            Log.i(TAG, "getIndicatorColor start");
            return mIndicatorColor;
        }

        /**
         * @return the color to be used for right divider on the
         * {@link SlidingTabLayout}
         */
        int getDividerColor() {
            Log.i(TAG, "getDividerColor start");
            return mDividerColor;
        }
    } // End SamplePagerItem

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present
     * in Android v4.0 and above, but is designed to give continuous feedback to
     * the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the
     * {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * List of {@link SamplePagerItem} which represent this sample's tabs.
     */
    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate start");

        // BEGIN_INCLUDE (populate_tabs)
        /**
         * Populate our tab list with tabs. Each item contains a title,
         * indicator color and divider color, which are used by
         * {@link SlidingTabLayout}.
         */
        mTabs.add(new SamplePagerItem(context, context.getString(R.string.tab_scanning), // Title
                Color.BLUE, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(context, context
                .getString(R.string.tab_database), // Title
                Color.RED, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(context, context
                .getString(R.string.tab_paired), // Title
                Color.GREEN, // Indicator color
                Color.GRAY // Divider color
        ));
        // END_INCLUDE (populate_tabs)
    }

    /**
     * Inflates the {@link View} which will be displayed by this
     * {@link Fragment}, from the app's resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView start");
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        return rootView;
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the
     * content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of
     * {@link SampleFragmentPagerAdapter}. The {@link SlidingTabLayout} is then
     * given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in
     *             {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated start");
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display
        // items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(
                getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the
        // ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view
                .findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // BEGIN_INCLUDE (tab_colorizer)
        // Set a TabColorizer to customize the indicator and divider colors.
        // Here we just retrieve
        // the tab at the position, and return it's set color
        mSlidingTabLayout
                .setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

                    @Override
                    public int getIndicatorColor(int position) {
                        return mTabs.get(position).getIndicatorColor();
                    }

                    @Override
                    public int getDividerColor(int position) {
                        return mTabs.get(position).getDividerColor();
                    }

                });
        // END_INCLUDE (tab_colorizer)
        // END_INCLUDE (setup_slidingtablayout)
    }

    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link FragmentPagerAdapter} used to display pages in this sample.
     * The individual pages are instances of
     * {@link ContentFragment} which just
     * display three lines of text. Each page is created by the relevant
     * {@link SamplePagerItem} for the requested position.
     * <p/>
     * The important section of this class is the {@link #getPageTitle(int)}
     * method which controls what is displayed in the {@link SlidingTabLayout}.
     */
    @SuppressWarnings("JavadocReference")
    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        static final String TAG = "SampleFragmentPagerAda";

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.i(TAG, "SampleFragmentPagerAdapter start");
        }

        /**
         * Return the {@link android.support.v4.app.Fragment} to be displayed at
         * {@code position}.
         * <p/>
         * Here we return the value returned from
         * {@link SamplePagerItem#createFragment()}.
         */
        @Override
        public Fragment getItem(int i) {
            Log.i(TAG, "getItem start" + " " + i);
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            Log.i(TAG, "getCount start");
            return mTabs.size();
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)

        /**
         * Return the title of the item at {@code position}. This is important
         * as what this method returns is what is displayed in the
         * {@link SlidingTabLayout}.
         * <p/>
         * Here we return the value returned from
         * {@link SamplePagerItem#getTitle()}.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            Log.i(TAG, "getPageTitle start");
            return mTabs.get(position).getTitle();
        }
        // END_INCLUDE (pageradapter_getpagetitle)

    }

}