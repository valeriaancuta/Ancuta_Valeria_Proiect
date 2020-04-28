package com.example.book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("love.json")
    Call<BookResponse>getBooks();

    @GET("quotes")
    Call<List<Quote>>getQuotes();
}
