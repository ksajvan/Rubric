package com.example.android.rubric;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute("popular");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Load ragment layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Load component that will hold data (ListView, GridView,..)
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);

//        ImageView imageView = (ImageView) rootView.findViewById(R.id.poster_item_imageview);
        // Set Content to GridView in this case
        gridView.setAdapter(new ImageAdapter(getActivity()));
//        gridView.setAdapter(new ImageAdapter(getActivity(), imageView));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), String.valueOf(position),
                        Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private String[] getMovieDataFromJson(String moviesJsonStr) {
            return null;
        }

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                final String MOVIES_DB_BASE_URL =   "http://api.themoviedb.org/3/movie";
                final String API_KEY_PARAM = "api_key";

                // Popular movies list url: http://api.themoviedb.org/3/movie/popular?api_key=YOUR_API_KEY
                Uri builtUri = Uri.parse(MOVIES_DB_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.OPEN_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Popular Movies JSON String: " + moviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
