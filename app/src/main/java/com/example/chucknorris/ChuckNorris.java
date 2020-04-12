package com.example.chucknorris;

import com.google.gson.annotations.SerializedName;

public class ChuckNorris {

    @SerializedName("value")
    private String quote;

    public String getQuotes() {
        return quote;
    }

}
