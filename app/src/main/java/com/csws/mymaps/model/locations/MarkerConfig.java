package com.csws.mymaps.model.locations;

import android.os.Parcel;
import android.os.Parcelable;

public class MarkerConfig implements Parcelable{
    public float colorHue; // Google Maps hue value
    public String iconType; // "default", "home", "work", etc.

    public MarkerConfig() {
        this.colorHue = 0f;
        this.iconType = "default";
    }

    public MarkerConfig(float colorHue, String iconType) {
        this.colorHue = colorHue;
        this.iconType = iconType;
    }

    // --- Parcelable ---
    protected MarkerConfig(Parcel in) {
        colorHue = in.readFloat();
        iconType = in.readString();
    }

    public static final Parcelable.Creator<MarkerConfig> CREATOR = new Parcelable.Creator<MarkerConfig>() {
        @Override
        public MarkerConfig createFromParcel(Parcel in) {
            return new MarkerConfig(in);
        }

        @Override
        public MarkerConfig[] newArray(int size) {
            return new MarkerConfig[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(colorHue);
        dest.writeString(iconType);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
