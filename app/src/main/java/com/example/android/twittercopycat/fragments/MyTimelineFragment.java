package com.example.android.twittercopycat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.entities.TweetItem;
import com.example.android.twittercopycat.helpers.Constants;

import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Created by IsabelCosta on 21-04-2016.
 */

/**
 * MyTimelineFragment is responsible to show sent tweets by the user
 */
public class MyTimelineFragment extends TimelineFragment {

    public MyTimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     * @param password Parameter 2.
     * @return A new instance of fragment MyTimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTimelineFragment newInstance(String username, String password) {
        MyTimelineFragment fragment = new MyTimelineFragment();
        Bundle args = new Bundle();
        args.putString(Constants.USERNAME, username);
        args.putString(Constants.PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mListAdapter = new ListScreenAdapter(
                getActivity(),
                R.layout.list_item_my_tweet,
                new ArrayList<TweetItem>()
        );

        // MyTimelineFragment View
        View rootView = inflater.inflate(R.layout.fragment_my_timeline_screen, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_all_tweets);
        mListView.setAdapter(mListAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    protected View getMyView(TweetItem item, LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.list_item_my_tweet, null);

        TextView tvDate = (TextView) v.findViewById(R.id.list_item_tweet_date);
        tvDate.setText(item.getTweetDate());

        TextView tvText = (TextView) v.findViewById(R.id.list_item_tweet_text);
        tvText.setText(item.getTweetText());

        return v;
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
    protected List<Twitter.Status> getFetchedOnlineTimeline(Bundle params, String LOG_TAG) {
        String username = params.getString(Constants.USERNAME);
        String password = params.getString(Constants.PASSWORD);
        Twitter t = new Twitter(username, password);

        Log.d(LOG_TAG, "USERNAME: " + username);
        Log.d(LOG_TAG, "PASSWORD: " + password);

        t.setAPIRootUrl(API_URL);
        t.setCount(params.getInt(Constants.MAX_TWEETS));
        return t.getUserTimeline();
    }

    @Override
    protected boolean isPublic() {
        return false;
    }
}

