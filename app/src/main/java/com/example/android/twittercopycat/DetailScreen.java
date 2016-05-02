package com.example.android.twittercopycat;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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

        /*
        URI oldUri = tweet.getTweetAuthorPic();
        Uri newUri  = new Uri.Builder().scheme(oldUri.getScheme())
                .encodedAuthority(oldUri.getRawAuthority())
                .encodedPath(oldUri.getRawPath())
                .query(oldUri.getRawQuery())
                .fragment(oldUri.getRawFragment())
                .build();
        */
        //Log.d("Debugging image: ",tweet.getTweetAuthorPic().toString());

        ImageView authorPic = (ImageView) findViewById(R.id.imageView_author);
        authorPic.setImageURI(Uri.parse(tweet.getTweetAuthorPic().toString()));

    }
}
