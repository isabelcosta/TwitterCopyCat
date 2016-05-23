package com.example.android.twittercopycat.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import winterwell.jtwitter.Twitter;

/**
 * PublicTimelineFragment is responsible for showing public tweets
 */
public class PublicTimelineFragment extends TimelineFragment {

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
                new ArrayList<TweetItem>()
        );

        //PublicTimelineFragment View
        View rootView = inflater.inflate(R.layout.fragment_public_timeline_screen, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_all_tweets);
        mListView.setAdapter(mListAdapter);

        // Set click action to go to details screen when a button is clicked
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

    @Override
    protected View getMyView(TweetItem item, LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.list_item_public_tweet, null);
        int maxCharacters = getResources().getInteger(R.integer.max_tweet_menu_item_characters);

//        TextView tvAuthor = (TextView) v.findViewById(R.id.list_item_tweet_author);
//        tvAuthor.setText(item.getTweetAuthorName());
//
//        TextView tvDate = (TextView) v.findViewById(R.id.list_item_tweet_date);
//        tvDate.setText(item.getTweetDate());

        TextView tvText = (TextView) v.findViewById(R.id.list_item_tweet_text);
        tvText.setText(
                item.getTweetText().length() >= maxCharacters
                        ? item.getTweetText().substring(0, maxCharacters - 1)
                        : item.getTweetText()); //should only show first 20 characters

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

        Twitter t = new Twitter();
        t.setAPIRootUrl(API_URL);
        t.setCount(params.getInt(Constants.MAX_TWEETS));
        return t.getPublicTimeline();
    }

    @Override
    protected boolean isPublic() {
        return true;
    }
}
