package com.example.android.twittercopycat;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Created by IsabelCosta on 21-04-2016.
 */


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    // TODO: Rename and change types of parameters
    private String username;
    private String password;

    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private ListScreenAdapter mListAdapter;
    private List<Twitter.Status> myTimeline;

    public MyTimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     * @param password Parameter 2.
     * @return A new instance of fragment PublicTimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTimelineFragment newInstance(String username, String password) {
        MyTimelineFragment fragment = new MyTimelineFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            password = getArguments().getString(PASSWORD);
        }

        myTimeline = new ArrayList<Twitter.Status>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mListAdapter = new ListScreenAdapter(
                getActivity(),
                R.layout.list_item_my_tweet,
                new ArrayList<TweetItem>());

        //PublicTimelineFragment View
        View rootView = inflater.inflate(R.layout.fragment_my_timeline_screen, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_all_tweets);
        mListView.setAdapter(mListAdapter);

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
        updateTweets();
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

    public void updateTweets() {
        FetchMyTweetTask tweetTask = new FetchMyTweetTask(getActivity(), myTimeline);
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(PASSWORD, password);
        tweetTask.execute(args);
    }

    private void fillAdapterWithTweets(){
        if (mListAdapter != null) {
            mListAdapter.clear();
            for(Twitter.Status tweet : myTimeline) {
                mListAdapter.add(
                        new TweetItem(
                                tweet.getId(),                          //id
                                tweet.getUser().getName(),              //author's name
                                //@TODO discover how to show the image
                                tweet.getUser().getProfileImageUrl(),   //author's picture
                                tweet.getUser().getDescription(),       //author's description
                                tweet.getCreatedAt().toString(),        //date
                                tweet.getText()                         //tweet text
                        )
                );
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
            View v = layoutInflater.inflate(R.layout.list_item_my_tweet, null);

            TweetItem item = getItem(position) ;

            TextView tvDate = (TextView) v.findViewById(R.id.list_item_tweet_date);
            tvDate.setText(item.getTweetDate());

            TextView tvText = (TextView) v.findViewById(R.id.list_item_tweet_text);
            tvText.setText(item.getTweetText()); //should only show first 20 characters

            return v;
        }
    }

    public class FetchMyTweetTask extends AsyncTask<Bundle, Void, List<Twitter.Status>> {

        private final String LOG_TAG = FetchMyTweetTask.class.getSimpleName();
        private final int maxMyTweets = 5;
        private final String apiUrl = "http://yamba.newcircle.com/api";

        private PublicTimelineFragment.ListScreenAdapter mTweetAdapter;
        private final Context mContext;
        private List<Twitter.Status> timeline;

        public FetchMyTweetTask(Context context, List<Twitter.Status> givenTimeline) {
            mContext = context;
            timeline = givenTimeline;
        }

        private boolean DEBUG = true;

        @Override
        protected List<Twitter.Status> doInBackground(Bundle... params) {

            String username = params[0].getString(USERNAME);
            String password = params[0].getString(PASSWORD);
            Twitter t = new Twitter(username, password);

            Log.d(LOG_TAG, "USERNAME: " + username);
            Log.d(LOG_TAG, "PASSWORD: " + password);

            t.setAPIRootUrl(apiUrl);
            t.setCount(maxMyTweets);
            List<Twitter.Status> fetchedTimeline = t.getUserTimeline();
            Log.d(LOG_TAG, String.valueOf(fetchedTimeline == null));

            return fetchedTimeline;
        }

        @Override
        protected void onPostExecute(List<Twitter.Status> result) {

            if (result != null) {
                Log.d(LOG_TAG, "I GOT SOMETHING FROM SERVER");
                timeline.clear();
                for(Twitter.Status tweet : result) {
                    timeline.add(tweet);
                }
                // New data is back from the server.  Hooray!
            }
            fillAdapterWithTweets();
        }
    }
}

