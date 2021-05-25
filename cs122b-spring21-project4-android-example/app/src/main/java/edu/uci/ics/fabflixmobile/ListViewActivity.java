package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListViewActivity extends Activity {
    private String url;
    private Button nextBtn;
    private Button prevBtn;
    private int pageWeOn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        // TODO: this should be retrieved from the backend server
        url="https://3.22.241.130:8443/Project1/api/";

        final ArrayList<Movie> movies = new ArrayList<>();

        nextBtn = findViewById(R.id.nextButton);
        prevBtn = findViewById(R.id.prevButton);

        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
        movieList(movies, adapter);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        
        nextBtn.setOnClickListener(view -> {
            pageWeOn++;
            movieList(movies, adapter);
            adapter.notifyDataSetChanged();
        });

        prevBtn.setOnClickListener(view -> {
            if(pageWeOn > 0)
                pageWeOn--;
            movieList(movies, adapter);
            adapter.notifyDataSetChanged();
        });
    }
    public void movieList(ArrayList<Movie> movies, MovieListViewAdapter adapter)
    {
        RequestQueue queue = NetworkManager.sharedManager(this).queue;

        Intent getQuery = this.getIntent();
        String query = getQuery.getStringExtra("query");

        Uri uri = new Uri.Builder()
                .appendQueryParameter("page", "" + pageWeOn)
                .appendQueryParameter("query", query)
                .build();

        StringRequest listRequest = new StringRequest(Request.Method.GET, url +"AndroidMovieList"+ uri.toString(), response -> {
            Log.d("login.success", response);
            if(response.equals("[]")) {
                pageWeOn--;
                Log.d("Page:", pageWeOn + "");
                return;
            }
            try{

                JSONArray  jsonArray = new JSONArray(response);
                movies.clear();
                int i =0;
                while(i < jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    movies.add(new Movie(jsonObject.getString("mid"),jsonObject.getString("title"),jsonObject.getString("year"),
                            jsonObject.getString("director")
                    ));
                    i++;
                }
                MovieListViewAdapter adapter1 = new MovieListViewAdapter(movies,
                        ListViewActivity.this);
                ListView listView = findViewById(R.id.list);
                listView.setAdapter(adapter1);


            }catch(JSONException e){
                e.printStackTrace();
            }

        },
                error -> Log.d("list.error", error.toString())) {

        };

        queue.add(listRequest);
    }
}