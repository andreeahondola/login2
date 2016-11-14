package com.example.andreea.login.content;

/**
 * Created by Andreea on 11.11.2016.
 */

public class Note {

    public enum Status {
        active,
        archived;

    }
    private String mId;
    private String mUserId;
    private String mText;
    private Status mStatus = Status.active;
    private long mUpdated;
    private int mNersion;

    public Note() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public long getUpdated() {
        return mUpdated;
    }

    public void setUpdated(long updated) {
        mUpdated = updated;
    }

    public int getVersion() {
        return mNersion;
    }

    public void setVersion(int version) {
        mNersion = version;
    }

    @Override
    public String toString() {
        return "Note{" +
                "mId='" + mId + '\'' +
                ", mUserId='" + mUserId + '\'' +
                ", mText='" + mText + '\'' +
                ", mStatus=" + mStatus +
                ", mUpdated=" + mUpdated +
                ", mNersion=" + mNersion +
                '}';
    }
}
