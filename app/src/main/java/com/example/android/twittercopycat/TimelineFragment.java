package com.example.android.twittercopycat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import winterwell.jtwitter.Twitter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {

    protected static final String LOG_TAG = "TimelineFragment";
    protected static final String USERNAME = Constants.USERNAME;
    protected static final String PASSWORD = Constants.PASSWORD;
    private static int DELAY = 15000;
    private static int PERIODICAL_TIME = 15000;

    protected String username;
    protected String password;

    protected OnFragmentInteractionListener mListener;

    protected ListView mListView;
    protected ListScreenAdapter mListAdapter;
    protected List<TweetItem> timeline;
    protected int maxCharacters = 20;
    protected static String API_URL = Constants.API_URL;
    protected TwitterCopyCatApplication app;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long time = getUpdatePeriod();
            // FIXME: 16-05-2016 this is awfully done needs to stop the handler when the user
//
//            if(isPublic()){
//
//            } else {}
//


            // chooses to update its timeline manually if time =-1

            Log.d(LOG_TAG, "Sync time is " + String.valueOf(time));
            Log.d(LOG_TAG, "Is Timeline onResume? " + String.valueOf(TwitterCopyCatApplication.isActivityVisible()));
            if(TwitterCopyCatApplication.isActivityVisible()){
                if(time != -1){
                    if(app.isNetworkAvailable()) {
                        updateTweets(false);
                    }
                    handler.postDelayed(this, time);
                }
            }
        }
    };

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        TwitterCopyCatApplication.activityResumed();
    }
    @Override
    public void onPause() {
        super.onPause();
        TwitterCopyCatApplication.activityPaused();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username user username if logged.
     * @param password user password if logged.
     * @return A new instance of fragment PublicTimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String username, String password) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = TwitterCopyCatApplication.getInstance();

        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            password = getArguments().getString(PASSWORD);
        }

        timeline = new ArrayList<TweetItem>();

        handler.postDelayed(runnable, 100);
    }

    protected long getUpdatePeriod(){
        if(isPublic()){
            return  getTimeInMiliSeconds(Long.valueOf(0), Long.valueOf(0), Long.valueOf(15));
        } else {
            long interval = app.getSyncFrequencyPref();
            if(interval != -1){
                return getTimeInMiliSeconds(Long.valueOf(0), Long.valueOf(0), Long.valueOf(app.getSyncFrequencyPref()));
            }
            return -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mListAdapter = new ListScreenAdapter(
                getActivity(),
                R.layout.list_item_public_tweet,
                new ArrayList<TweetItem>());

        //PublicTimelineFragment View
        View rootView = inflater.inflate(R.layout.fragment_public_timeline_screen, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_all_tweets);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TweetItem tweet = mListAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailScreen.class)
                        .putExtra(Constants.TWEET_ITEM, tweet);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(app.isNetworkAvailable()){
            updateTweets(false);
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
            updateTweets(true);
            // Get Tweets from DB
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    protected void updateTweets(boolean offline) {
        Log.d(LOG_TAG, "TIMINGGGG    ?    " + String.valueOf(getUpdatePeriod()));
        Log.d(LOG_TAG, "NUMBERTWEETS ?    " + String.valueOf(app.getNumberOfTweetsPref()));
        Log.d(LOG_TAG, "WIFI ONLY    ?    " + String.valueOf(app.getWifiOnlyPref()));

        if(offline){
            FetchPublicOfflineTweetTask tweetTask = new FetchPublicOfflineTweetTask(getActivity(), timeline);
            tweetTask.execute();
        } else {
            Bundle args = new Bundle();
            if(!isPublic()) {
                args.putString(USERNAME, username);
                args.putString(PASSWORD, password);
            }
            FetchPublicOnlineTweetTask tweetTask = new FetchPublicOnlineTweetTask(getActivity(), timeline);
            tweetTask.execute(args);
        }
    }

    private void fillAdapterWithTweets(){
        if (mListAdapter != null) {
            mListAdapter.clear();

            for(TweetItem tweet : timeline) {
                mListAdapter.add(tweet);
            }
        }
    }

    public class ListScreenAdapter extends ArrayAdapter<TweetItem> {
        public ListScreenAdapter(Context context, int resourceId, ArrayList<TweetItem> objects) {
            super(context , resourceId, objects) ;
        }

        @Override
        public View getView(int position , View convertView , ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext()) ;
            TweetItem item = getItem(position) ;
            View v = getMyView(item, layoutInflater);

            return v;
        }
    }

    protected View getMyView(TweetItem item, LayoutInflater layoutInflater) {
        return null;
    }

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(Date date){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    /**
     * Tweet Online Fetching
     */
    public class FetchPublicOnlineTweetTask extends AsyncTask<Bundle, Void, List<TweetItem>> {

        private final String LOG_TAG = FetchPublicOnlineTweetTask.class.getSimpleName();
        // FIXME: 18-05-2016 shouldn't be hardcoded
        private final int maxPubTweets = 10;

        private final Context mContext;
        private List<TweetItem> timeline;

        public FetchPublicOnlineTweetTask(Context context, List<TweetItem> givenTimeline) {
            mContext = context;
            timeline = givenTimeline;
        }

        private boolean DEBUG = true;

        @Override
        protected List<TweetItem> doInBackground(Bundle... params) {

            List<Twitter.Status> fetchedTimeline = getFetchedOnlineTimeline(
                    params.length == 0 ? null : params[0],
                    LOG_TAG,
                    isPublic() ? maxPubTweets : app.getNumberOfTweetsPref()
            );

            if(fetchedTimeline == null){
                return null;
            }

            //delete old tweets from database
            deleteOldTweets();

            List<TweetItem> TweetItems = new LinkedList<>();

            for(Twitter.Status tweet : fetchedTimeline) {
                TweetItem newTweet = new TweetItem(
                        tweet.getId(),                                      //id
                        tweet.getUser().getName(),                          //author's name
                        tweet.getUser().getProfileImageUrl().toString(),    //author's picture
                        tweet.getUser().getDescription(),                   //author's description
                        tweet.getCreatedAt().toString(),                    //date
                        tweet.getText(),                                    //tweet text
                        isPublic()
                );
                TweetItems.add(newTweet);
                newTweet.save();
            }

            return TweetItems;
        }

        @Override
        protected void onPostExecute(List<TweetItem> result) {

            if (result != null) {
//                Log.d(LOG_TAG, "I GOT SOMETHING FROM SERVER");
                timeline.clear();
                for(TweetItem tweet : result) {
                    timeline.add(tweet);
                }
                // New data is back from the server.  Hooray!
            }
            fillAdapterWithTweets();
        }
    }

    /**
     * Tweet Offline Fetching
     */
    public class FetchPublicOfflineTweetTask extends AsyncTask<Void, Void, List<TweetItem>> {

        private final String LOG_TAG = FetchPublicOfflineTweetTask.class.getSimpleName();

        private final Context mContext;
        private List<TweetItem> fetchedTimeline;

        public FetchPublicOfflineTweetTask(Context context, List<TweetItem> givenTimeline) {
            mContext = context;
            fetchedTimeline = givenTimeline;
        }

        private boolean DEBUG = true;

        @Override
        protected List<TweetItem> doInBackground(Void... params) {

            List<TweetItem> fetchedTimeline = getFetchedOfflineTimeline();

            if(fetchedTimeline == null){
                return null;
            }

            return fetchedTimeline;
        }

        @Override
        protected void onPostExecute(List<TweetItem> result) {

            if (result != null) {
                Log.d(LOG_TAG, "I GOT SOMETHING FROM SERVER");
                timeline.clear();
                for(TweetItem tweet : result) {
                    timeline.add(tweet);
                }
                // New data is back from the server.  Hooray!
            }
            fillAdapterWithTweets();
        }
    }

    protected boolean isPublic() {
        return true;
    }

    protected List<TweetItem> getFetchedOfflineTimeline() {
//        For SugarDB testing purposes
//        List<TweetItem> t1 = TweetItem.listAll(TweetItem.class, "id");
//        List<TweetItem> t2 = TweetItem.find(TweetItem.class, "is_public = ?", String.valueOf(isPublic()));
//        List<TweetItem> t3 = TweetItem.find(TweetItem.class, "is_public = ?", "0");
//        List<TweetItem> t4 = TweetItem.find(TweetItem.class, "is_public = ?", "1");
//        List<TweetItem> t4 = TweetItem.find(TweetItem.class, "is_public = ?", "1");
//        List<TweetItem> t5 = Select.from(TweetItem.class).whereOr(Condition.prop("is_public").eq(String.valueOf(isPublic()))).list();
        return TweetItem.find(TweetItem.class, "is_public = ?", isPublic() ? "1" : "0");
    }

    protected void deleteOldTweets(){
        List<TweetItem> oldTweets = getFetchedOfflineTimeline();
        for(TweetItem t : oldTweets){
            t.delete();
        }
    }

    //defined in child classes
    protected List<Twitter.Status> getFetchedOnlineTimeline(Bundle params, String LOG_TAG, int maxMyTweets) {
        return null;
    }

    protected long getTimeInMiliSeconds(Long hours, Long minutes, Long seconds) {
        long miliSeconds = 1000;
        long hoursInMiliSec = 0;
        long minutesInMiliSec = 0;
        long secondsInMiliSec = 0;

        if (hours != null) {
            hoursInMiliSec = hours * 60 * 60 * miliSeconds;
        }

        if (minutes != null) {
            minutesInMiliSec = minutes * 60 * miliSeconds;
        }

        if (seconds != null) {
            secondsInMiliSec = seconds * miliSeconds;
        }

        return hoursInMiliSec + minutesInMiliSec + secondsInMiliSec;
    }
}
