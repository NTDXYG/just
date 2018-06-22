package com.example.just.Gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yg on 18-6-21.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
