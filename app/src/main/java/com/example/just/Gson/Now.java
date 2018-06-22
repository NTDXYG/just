package com.example.just.Gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yg on 18-6-21.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More {
        @SerializedName("txt")
        public String info;
    }
}
