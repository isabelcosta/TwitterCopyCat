package com.example.android.twittercopycat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class MyTimelineScreen extends AppCompatActivity {

    // Views
    private Button signOutBtn;
    private Button refreshBtn;
    private Button sendTweet;
    private EditText tweetText;
    private TextView numberOfCharacters;
    private int maxCharacters;

    private static final String TAG = "MyTimelineScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timeline);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViews();
        addViewListeners();
        maxCharacters = getResources().getInteger(R.integer.max_characters);

        TwitterCopyCatApplication app = (TwitterCopyCatApplication) getApplication();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MyTimelineFragment.newInstance(app.getUsername(), app.getPassword()))
                    .commit();
        }
    }

    private void addViewListeners(){
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                TwitterCopyCatApplication app = (TwitterCopyCatApplication) getApplication();
                                app.saveCredentials(false, null, null);

                                Intent intent = new Intent(view.getContext(), LoginScreen.class);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MyTimelineScreen.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        sendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = tweetText.getText().toString();

                if(tweet.length() < 1 || tweet.length() > 140) {
                    Context context = getApplicationContext();
                    CharSequence text = "Numero invalido de caracteres";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    SendTweetTask tweetTask = new SendTweetTask(getApplicationContext());
                    tweetTask.execute(tweet);
                }
            }
        });

        tweetText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                numberOfCharacters.setText(String.valueOf(maxCharacters - s.length()));
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTimelineFragment frag = (MyTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                frag.updateTweets();
          }
        });
    }

    private void findViews(){
        signOutBtn = (Button) findViewById(R.id.button_sign_out);
        refreshBtn = (Button) findViewById(R.id.button_refresh);
        sendTweet = (Button) findViewById(R.id.button_send_tweet);
        tweetText = (EditText) findViewById(R.id.editText_tweet);
        numberOfCharacters = (TextView) findViewById(R.id.tweet_number_of_characters);
    }

    public class SendTweetTask extends AsyncTask<String, Void, Boolean> {

        private final String LOG_TAG = SendTweetTask.class.getSimpleName();
        private final String apiUrl = "http://yamba.newcircle.com/api";

        private final Context mContext;

        public SendTweetTask(Context context) {
            mContext = context;
        }

        private boolean DEBUG = true;

        @Override
        protected Boolean doInBackground(String... tweet) {

            try{
                TwitterCopyCatApplication app = (TwitterCopyCatApplication) getApplication();
                //@TODO set username and password on application when login
                Twitter t = new Twitter(app.getUsername(), app.getPassword());
                t.setAPIRootUrl(apiUrl);
                t.updateStatus(tweet[0]);
                Log.d(LOG_TAG, "Sending a tweet");
            } catch (TwitterException e){
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                // Sent successfully the tweet.  Hooray!
                Toast.makeText(mContext, "Your Tweet was sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Your Tweet couldn't be sent now. Will'll send it later ;) ", Toast.LENGTH_LONG).show();
            }

            //clear the text box
            tweetText.setText("");
        }
    }
}
