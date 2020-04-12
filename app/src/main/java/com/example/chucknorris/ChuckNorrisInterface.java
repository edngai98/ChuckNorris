package com.example.chucknorris;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChuckNorrisInterface {
    //GET annotation
    @GET("/jokes/random?category=dev")

    //Create the getQuote method to retrieve the quotes from the API
    Call<ChuckNorris> getQuotes();

}
