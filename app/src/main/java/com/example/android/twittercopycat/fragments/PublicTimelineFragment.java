package com.example.android.twittercopycat.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.entities.TweetItem;
import com.example.android.twittercopycat.helpers.Constants;
import com.example.android.twittercopycat.screens.DetailScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import winterwell.jtwitter.Twitter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicTimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicTimelineFragment extends TimelineFragment {

    private int maxCharacters = 20;
    private int maxPublicTweets = 10;

    private Timer timer;

    public PublicTimelineFragment() {
        // Required empty public constructor
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

    @Override
    protected View getMyView(TweetItem item, LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.list_item_public_tweet, null);

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

    @Override
    protected List<Twitter.Status> getFetchedOnlineTimeline(Bundle params, String LOG_TAG, int maxTweets) {

        // TODO: 09-05-2016 what to do with maxTweets that isn't used 
        
        Twitter t = new Twitter();
        t.setAPIRootUrl(API_URL);
        t.setCount(maxPublicTweets);
        return t.getPublicTimeline();
    }

    @Override
    protected boolean isPublic() {
        return true;
    }
}
