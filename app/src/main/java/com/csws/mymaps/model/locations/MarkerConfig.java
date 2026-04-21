package com.csws.mymaps.model.locations;

public class MarkerConfig {
    public float colorHue; // Google Maps hue value
    public String iconType; // "default", "home", "work", etc.

    public MarkerConfig(float colorHue, String iconType) {
        this.colorHue = colorHue;
        this.iconType = iconType;
    }
}
