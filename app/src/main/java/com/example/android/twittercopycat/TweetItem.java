package com.example.android.twittercopycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class TweetItem extends SugarRecord implements Parcelable {
    private long tweetId;               //id
    private String authorName;          //author_name
    private String authorPicture;       //author_picture
    private String authorDescription;   //author_description
    private String date;                //date
    private String text;                //text
    private boolean isPublic;           //is_public

    public TweetItem(){

    }

    public TweetItem(long id, String author, String authorPicture, String authorDescription, String date, String text, boolean isPublic){
        this.tweetId = id;
        this.authorName = author;
        this.date = date;
        this.text = text;
        this.authorPicture = authorPicture;
        this.authorDescription = authorDescription;
        this.isPublic = isPublic;
    }

    public long getTweetId(){
        return tweetId;
    }
    public String getTweetAuthorName () {
        return authorName;
    }
    public String getTweetAuthorPic(){
        return authorPicture;
    }
    public String getTweetAuthorDesc(){
        return authorDescription;
    }
    public String getTweetDate(){
        return date;
    }
    public String getTweetText () {
        return text;
    }
    public boolean getTweetIsPublic () {
        return isPublic;
    }

    protected TweetItem(Parcel in) {
        tweetId = in.readLong();
        authorName = in.readString();
        authorPicture = in.readString();
        authorDescription = in.readString();
        date = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tweetId);
        dest.writeString(authorName);
        dest.writeString(authorPicture);
        dest.writeString(authorDescription);
        dest.writeString(date);
        dest.writeString(text);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TweetItem> CREATOR = new Parcelable.Creator<TweetItem>() {
        @Override
        public TweetItem createFromParcel(Parcel in) {
            return new TweetItem(in);
        }

        @Override
        public TweetItem[] newArray(int size) {
            return new TweetItem[size];
        }
    };
}
