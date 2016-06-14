package com.example.android.twittercopycat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.application.TwitterCopyCatApplication;
import com.example.android.twittercopycat.entities.TweetItem;
import com.example.android.twittercopycat.helpers.Constants;
import com.example.android.twittercopycat.helpers.TwitterCopyCatHelper;

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

    private static final String LOG_TAG = "TimelineFragment";
    private static int DELAY = 15000;
    private static int PERIODICAL_TIME = 15000;

    protected String username;
    protected String password;

    protected OnFragmentInteractionListener mListener;

    protected ListView mListView;
    protected ListScreenAdapter mListAdapter;
    protected List<TweetItem> timeline;
    protected static String API_URL = Constants.API_URL;
    protected TwitterCopyCatApplication app;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        long time = getUpdatePeriod();
        // FIXME: 16-05-2016 this is awfully done needs to stop the handler when the user

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

    @Override
    public void onStop() {
        super.onStop();
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
    public static TimelineFragment newInstance(String username, String password) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(Constants.USERNAME, username);
        args.putString(Constants.PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = TwitterCopyCatApplication.getInstance();

        if (getArguments() != null) {
            username = getArguments().getString(Constants.USERNAME);
            password = getArguments().getString(Constants.PASSWORD);
        }

        timeline = new ArrayList<>();

        handler.postDelayed(runnable, 100);
    }

    protected long getUpdatePeriod(){
        if(isPublic()){
            return  TwitterCopyCatHelper.getTimeInMiliSeconds(
                    Long.valueOf(0), Long.valueOf(0), Long.valueOf(Constants.PUBLIC_SYNC_PERIOD));
        } else {
            long interval = app.getSyncFrequencyPref();
            if(interval != -1){
                return TwitterCopyCatHelper.getTimeInMiliSeconds(
                        Long.valueOf(0), Long.valueOf(0), Long.valueOf(app.getSyncFrequencyPref()));
            }
            return -1;
        }
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
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Starts asynchronous tasks to get either offline (persistent) or online tweets
     * @param offline - true if fetch from database
     *                - false if fetch from server
     */
    public void updateTweets(boolean offline) {
        Log.d(LOG_TAG, "Sync period       ?    " + String.valueOf(getUpdatePeriod()));
        Log.d(LOG_TAG, "Number of tweets  ?    " + String.valueOf(app.getNumberOfTweetsPref()));
        Log.d(LOG_TAG, "Wifi only mode    ?    " + String.valueOf(app.getWifiOnlyPref()));

        if(offline){
            FetchOfflineTweetTask tweetTask = new FetchOfflineTweetTask(getActivity(), timeline);
            tweetTask.execute();
        } else {
            Bundle args = new Bundle();

            int maxTweets = isPublic()
                    ? getResources().getInteger(R.integer.last_public_tweets)
                    : app.getNumberOfTweetsPref();

            args.putInt(Constants.MAX_TWEETS, maxTweets);
            if(!isPublic()) {
                args.putString(Constants.USERNAME, username);
                args.putString(Constants.PASSWORD, password);
            }

            FetchOnlineTweetTask tweetTask = new FetchOnlineTweetTask(getActivity(), timeline);
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
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d - h:mm a");
        return format.format(date).toString();
    }

    /**
     * Tweet Online Fetching
     */
    public class FetchOnlineTweetTask extends AsyncTask<Bundle, Void, List<TweetItem>> {

        private final String LOG_TAG = FetchOnlineTweetTask.class.getSimpleName();

        private final Context mContext;
        private List<TweetItem> timeline;

        public FetchOnlineTweetTask(Context context, List<TweetItem> givenTimeline) {
            mContext = context;
            timeline = givenTimeline;
        }

        private boolean DEBUG = true;

        @Override
        protected List<TweetItem> doInBackground(Bundle... params) {

            List<Twitter.Status> fetchedTimeline = getFetchedOnlineTimeline(params[0]);

            if(fetchedTimeline == null){
                return null;
            }

            //delete old tweets from database
            TwitterCopyCatHelper.deleteOldTweets(isPublic());

            List<TweetItem> TweetItems = new LinkedList<>();

            for(Twitter.Status tweet : fetchedTimeline) {
                TweetItem newTweet = new TweetItem(
                        tweet.getId(),                                      //id
                        tweet.getUser().getName(),                          //author's name
                        tweet.getUser().getProfileImageUrl().toString(),    //author's picture
                        tweet.getUser().getDescription(),                   //author's description
                        getReadableDateString(tweet.getCreatedAt()),        //date
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
     * Offline Tweet Fetching
     */
    public class FetchOfflineTweetTask extends AsyncTask<Void, Void, List<TweetItem>> {

        private final String LOG_TAG = FetchOfflineTweetTask.class.getSimpleName();

        // TODO: 14-06-2016 check if it is unnecessary and test
        private final Context mContext;
        private List<TweetItem> fetchedTimeline;

        public FetchOfflineTweetTask(Context context, List<TweetItem> givenTimeline) {
            mContext = context;
            fetchedTimeline = givenTimeline;
        }

        private boolean DEBUG = true;

        @Override
        protected List<TweetItem> doInBackground(Void... params) {

            List<TweetItem> fetchedTimeline = TwitterCopyCatHelper.getOfflineTimeline(isPublic());

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

    //defined in child classes
    protected List<Twitter.Status> getFetchedOnlineTimeline(Bundle params) {
        return null;
    }
}
