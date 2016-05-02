package com.example.android.twittercopycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.net.URI;

public class TweetItem extends SugarRecord implements Parcelable {
    private long id;
    private String authorName;
    private URI authorPicture;
    private String authorDescription;
    private String date;
    private String text;

    public TweetItem(long id, String author, URI authorPicture, String authorDescription, String date, String text){
        this.id = id;
        this.authorName = author;
        this.date = date;
        this.text = text;
        this.authorPicture = authorPicture;
        this.authorDescription = authorDescription;
    }

    public long getTweetId(){
        return id;
    }
    public String getTweetAuthorName () {
        return authorName;
    }
    public URI getTweetAuthorPic(){
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

    protected TweetItem(Parcel in) {
        id = in.readLong();
        authorName = in.readString();
        authorPicture = (URI) in.readValue(URI.class.getClassLoader());
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
        dest.writeLong(id);
        dest.writeString(authorName);
        dest.writeValue(authorPicture);
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
