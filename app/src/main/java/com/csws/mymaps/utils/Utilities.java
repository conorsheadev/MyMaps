package com.csws.mymaps.utils;

import com.csws.mymaps.R;

public class Utilities {
    public static int getIconResource64px(String iconType)
    {
        switch (iconType){
            case "home":
                return R.drawable.home_64px;
            case "work":
                return R.drawable.work_64px;
            case "school":
                return R.drawable.school_64px;
            default:
                return R.drawable.location_on_64px;
        }
    }
    public static int getIconResource24px(String iconType)
    {
        switch (iconType){
            case "home":
                return R.drawable.home_24px;
            case "work":
                return R.drawable.work_24px;
            case "school":
                return R.drawable.school_24px;
            default:
                return R.drawable.location_on_24px;
        }
    }
}
