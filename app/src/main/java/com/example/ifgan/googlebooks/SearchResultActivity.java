package com.example.ifgan.googlebooks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = SearchResultActivity.class.getSimpleName();

    /**
     * URL to query the Google dataset for earthquake information
     */
    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        BookAsyncTask task = new BookAsyncTask();
        URL url = createUrl(GOOGLE_REQUEST_URL);
        task.execute(url);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private class BookAsyncTask extends AsyncTask<URL, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(URL... params) {

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(params[0]);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            List<Book> books = extractBookFromJson(jsonResponse);

            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {

            //super.onPostExecute(book);
            if (books == null) {
                return;
            }

            updateUi(books);
        }

        /**
         * Update the screen to display information from the given {@link Book}.
         */
        private void updateUi(List<Book> books) {

            // Find a reference to the {@link ListView} in the layout
            ListView bookListView = (ListView) findViewById(R.id.list);

            // Create a new adapter that takes the list of books as input
            final BookAdapter adapter = new BookAdapter(SearchResultActivity.this, books);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            bookListView.setAdapter(adapter);
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Return an {@link Book} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private List<Book> extractBookFromJson(String bookJSON) {

            List<Book> books = new ArrayList<>();

            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(bookJSON)) {
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray itemArray = baseJsonResponse.getJSONArray("items");

                // If there are results in the features array
                for (int i = 0; i < itemArray.length(); i++) {

                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstItem = itemArray.getJSONObject(i);
                    JSONObject volumeInfo = firstItem.getJSONObject("volumeInfo");

                    // Extract out the title, author
                    String title  = volumeInfo.getString("title");
                    String author = volumeInfo.getString("authors").toString();
                    //Remove char from string
                    author = author.replace("[", "").replace("]", "").replace("\"", " ");

                    // Create a new {@link Event} object
                    Book book = new Book(title, author);
                    books.add(book);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return books;
        }
    }
}
