package com.example.android.rubric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//    private ArrayAdapter<String> mImageAdapter;
//    private String[] mImageAdapter;
    private ImageAdapter mImageAdapter;

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
            updateMoviesList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        updateMoviesList();

        // Load fragment layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Load component that will hold data (ListView, GridView,..)
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);

        mImageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = mImageAdapter.getItem(position).toString();
                Intent intent = new Intent(getActivity(), DetailsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, message);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMoviesList();
    }

    private void updateMoviesList() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movie_criteria = prefs.getString(getString(R.string.pref_criteria_key),
                getString(R.string.pref_criteria_default));
        moviesTask.execute(movie_criteria);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private String[] getMovieDataFromJson(String moviesJsonStr) throws JSONException {
            // names from json that should be extracted
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            final String POSTER_PATH_BASE_URL =   "http://image.tmdb.org/t/p/w185/";
            String finalPath;

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

            String[] resultPaths = new String[moviesArray.length()];
            for(int i = 0; i < moviesArray.length(); i++) {
                String poster_path;
                JSONObject jsonObj = moviesArray.getJSONObject(i);
                poster_path = jsonObj.getString(TMDB_POSTER_PATH);
                finalPath = POSTER_PATH_BASE_URL.concat(poster_path);
                resultPaths[i] = finalPath;
            }

//            for (String s : resultPaths) {
//                Log.v(LOG_TAG, "Movie Poster Path: " + s);
//            }

            return resultPaths;
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
            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            mImageAdapter = result;
            if (result != null) {
//                mImageAdapter.clear();
                ImageAdapter.moviePosterImages = result;
//                for(String dayForecastStr : result) {
//                    mImageAdapter.add(dayForecastStr);
//                }
                // New data is back from the server.  Hooray!
            }
            mImageAdapter.notifyDataSetChanged();
        }
    }
}
