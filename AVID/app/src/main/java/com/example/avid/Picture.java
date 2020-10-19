package com.example.avid;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Picture implements Parcelable {

    private String mKey;
    private Bitmap mImageBitmap;
    private String mCaregiverMessage;
    private String mChildMessage;

    public Picture(String key, Bitmap imageBitmap, String caregiverMessage, String childMessage)
    {
        mKey = key;
        mImageBitmap = imageBitmap;
        mCaregiverMessage = caregiverMessage;
        mChildMessage = childMessage;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public Bitmap getImageBitmap() {
        return mImageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        mImageBitmap = imageBitmap;
    }

    public String getCaregiverMessage() {
        return mCaregiverMessage;
    }

    public void setCaregiverMessage(String caregiverMessage) {
        this.mCaregiverMessage = caregiverMessage;
    }

    public String getmChildMessage() {
        return mChildMessage;
    }

    public void setmChildMessage(String mChildMessage) {
        this.mChildMessage = mChildMessage;
    }

    protected Picture(Parcel in) {
        mKey = in.readString();
        mImageBitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        mCaregiverMessage = in.readString();
        mChildMessage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeValue(mImageBitmap);
        dest.writeString(mCaregiverMessage);
        dest.writeString(mChildMessage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}