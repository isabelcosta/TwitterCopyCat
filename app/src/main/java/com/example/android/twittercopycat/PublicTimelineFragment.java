package com.example.android.twittercopycat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
 * {@link PublicTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicTimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private ListScreenAdapter mListAdapter;
    private List<TweetItem> publicTimeline;
    private int maxCharacters = 20;

    public PublicTimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicTimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicTimelineFragment newInstance(String param1, String param2) {
        PublicTimelineFragment fragment = new PublicTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        publicTimeline = new ArrayList<TweetItem>();
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
        if(((TwitterCopyCatApplication) getActivity().getApplication()).isNetworkAvailable()){
            updateTweets();
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
            fillAdapterWithOfflineTweets();
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

    private void updateTweets() {
        FetchPublicTweetTask tweetTask = new FetchPublicTweetTask(getActivity(), publicTimeline);
        tweetTask.execute();
    }

    private void fillAdapterWithOnlineTweets(){
        if (mListAdapter != null) {
            mListAdapter.clear();

            //erase old tweets from database
            //TweetItem.deleteAll(TweetItem.class);

            for(TweetItem tweet : publicTimeline) {
                mListAdapter.add(tweet);
            }
        }
    }

    private void fillAdapterWithOfflineTweets(){
        if (mListAdapter != null) {
            mListAdapter.clear();
            List<TweetItem> publicOfflineTimeline =  TweetItem.listAll(TweetItem.class);
            for(TweetItem tweet : publicOfflineTimeline) {
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
            View v = layoutInflater.inflate(R.layout.list_item_public_tweet, null);

            TweetItem item = getItem(position) ;

            TextView tvAuthor = (TextView) v.findViewById(R.id.list_item_tweet_author);
            tvAuthor.setText(item.getTweetAuthorName());

            TextView tvDate = (TextView) v.findViewById(R.id.list_item_tweet_date);
            tvDate.setText(item.getTweetDate());

            TextView tvText = (TextView) v.findViewById(R.id.list_item_tweet_text);
            tvText.setText(
                    item.getTweetText().length() >= maxCharacters
                    ? item.getTweetText().substring(0, maxCharacters - 1)
                    : item.getTweetText()); //should only show first 20 characters

            return v;
        }
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

    public class FetchPublicTweetTask extends AsyncTask<Void, Void, List<TweetItem>> {

        private final String LOG_TAG = FetchPublicTweetTask.class.getSimpleName();
        private final int maxPubTweets = 10;
        private final String apiUrl = "http://yamba.newcircle.com/api";

        private PublicTimelineFragment.ListScreenAdapter mTweetAdapter;
        private final Context mContext;
        private List<TweetItem> timeline;

        public FetchPublicTweetTask(Context context, List<TweetItem> givenTimeline) {
            mContext = context;
            timeline = givenTimeline;
        }

        private boolean DEBUG = true;

        @Override
        protected List<TweetItem> doInBackground(Void... params) {

            Twitter t = new Twitter();
            t.setAPIRootUrl(apiUrl);
            t.setCount(maxPubTweets);
            List<Twitter.Status> fetchedTimeline = t.getPublicTimeline();

            if(fetchedTimeline == null){
                return null;
            }

//
//            for(TweetItem twiii : tweets){
//                twiii.delete();
//            }

            //erase old tweets from database
            TweetItem.deleteAll(TweetItem.class);
            //TweetItem.saveT();

            List<TweetItem> tweets = TweetItem.listAll(TweetItem.class);
            tweets.size();
            List<TweetItem> TweetItems = new LinkedList<>();

            for(Twitter.Status tweet : fetchedTimeline) {
                TweetItem newTweet = new TweetItem(
                        tweet.getId(),                                      //id
                        tweet.getUser().getName(),                          //author's name
                        tweet.getUser().getProfileImageUrl().toString(),    //author's picture
                        tweet.getUser().getDescription(),                   //author's description
                        tweet.getCreatedAt().toString(),                    //date
                        tweet.getText()                                     //tweet text
                );
                TweetItems.add(newTweet);
                newTweet.save();
            }

            return TweetItems;
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
            fillAdapterWithOnlineTweets();
        }
    }
}
