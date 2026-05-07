package com.csws.mymaps.domain.locations;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationItem implements Parcelable {
    public String id;
    public String name;
    public String type;
    public double lat;
    public double lng;

    public PolygonConfig polygonConfig;
    public MarkerConfig markerConfig;


    public LocationItem(String id, String name, String type, double lat, double lng, PolygonConfig polygonConfig, MarkerConfig markerConfig) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.polygonConfig = polygonConfig;
        this.markerConfig = markerConfig;
    }

    // --- Parcelable Constructor ---
    protected LocationItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();

        polygonConfig =
                in.readParcelable(
                        PolygonConfig.class.getClassLoader()
                );

        markerConfig =
                in.readParcelable(
                        MarkerConfig.class.getClassLoader()
                );
    }

    // --- Parcelable Writer ---
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeDouble(lat);
        dest.writeDouble(lng);

        dest.writeParcelable(polygonConfig, flags);
        dest.writeParcelable(markerConfig, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // --- Parcelable Creator ---
    public static final Parcelable.Creator<LocationItem> CREATOR =
            new Parcelable.Creator<LocationItem>() {
                @Override
                public LocationItem createFromParcel(Parcel in) {
                    return new LocationItem(in);
                }

                @Override
                public LocationItem[] newArray(int size) {
                    return new LocationItem[size];
                }
            };
}