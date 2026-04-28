package com.csws.mymaps.model.locations;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolygonConfig implements Parcelable {
    public float colorHue;
    public List<LatLng> points;

    public PolygonConfig(){
        this.colorHue = 0f;
        this.points = new ArrayList<>();
    }

    public PolygonConfig(float colorHue){
        this.colorHue = colorHue;
        this.points = new ArrayList<>();
    }

    public PolygonConfig(List<LatLng> points){
        this.colorHue = 0f;
        this.points = points;
    }

    public PolygonConfig(float colorHue, List<LatLng> points){
        this.colorHue = colorHue;
        this.points = points;
    }

    // --- Parcelable ---
    protected PolygonConfig(Parcel in) {
        colorHue = in.readFloat();
        points = in.createTypedArrayList(LatLng.CREATOR);
    }

    public static final Creator<PolygonConfig> CREATOR = new Creator<PolygonConfig>() {
        @Override
        public PolygonConfig createFromParcel(Parcel in) {
            return new PolygonConfig(in);
        }

        @Override
        public PolygonConfig[] newArray(int size) {
            return new PolygonConfig[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(colorHue);
        dest.writeTypedList(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
