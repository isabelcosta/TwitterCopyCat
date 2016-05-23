package com.example.android.twittercopycat.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.entities.TweetItem;
import com.example.android.twittercopycat.application.TwitterCopyCatApplication;
import com.example.android.twittercopycat.helpers.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        // To retrieve object in second Activity
        TweetItem tweet = (TweetItem) getIntent().getParcelableExtra(Constants.TWEET_ITEM);

        //Populate content
        TextView id = (TextView) findViewById(R.id.textView_id);
        id.setText(String.valueOf(tweet.getTweetId()));

        TextView date = (TextView) findViewById(R.id.textView_date);
        date.setText(String.valueOf(tweet.getTweetDate()));

        TextView authorName = (TextView) findViewById(R.id.textView_name);
        authorName.setText(String.valueOf(tweet.getTweetAuthorName()));

        TextView authorDescription = (TextView) findViewById(R.id.textView_description);
        authorDescription.setText(String.valueOf(tweet.getTweetAuthorDesc()));

        TextView text = (TextView) findViewById(R.id.textView_tweet);
        text.setText(String.valueOf(tweet.getTweetText()));

        ImageView authorPic = (ImageView) findViewById(R.id.imageView_author);
        ImageLoader.getInstance().displayImage(
                tweet.getTweetAuthorPic(),
                authorPic,
                TwitterCopyCatApplication.getInstance().getOptions()
        );
    }
}
