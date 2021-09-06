package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    // List to hold the movies
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define the recyclerView
        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        // Initialize the list of movies
        movies = new ArrayList<>();

        // Create the adapter - activity is an instance of the context
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //Create an AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        //Make an API request for now playing movies
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            //on success create a json object
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    //store the data we need from the response in an array. The data we need comes from the key:results
                    JSONArray results = jsonObject.getJSONArray("results");
                    //call the method from movies to create a list of movies using the data from the response
                    movies.addAll(Movie.moviesFromJsonArray(results));
                    // Notify the adapter that data has changed
                    movieAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }
}