package com.sample.booklistapplication.viewmodel;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;

import androidx.lifecycle.MutableLiveData;

import com.sample.booklistapplication.model.Books;
import com.sample.booklistapplication.model.Lists;
import com.sample.booklistapplication.util.AppConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BooksRepository {
    private ArrayList<Books> mBooksArrayList = new ArrayList<>();
    private MutableLiveData<List<Books>> mBooksResponseLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Books>> getmBooksResponseLiveData() {
        return mBooksResponseLiveData;
    }

    public void fetchAllBooksFromAPI(String date) {
        // Server Request URL
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(AppConstants.HTTPS)
                .authority(AppConstants.HOSTURL)
                .appendPath(AppConstants.SVC)
                .appendPath(AppConstants.BOOKS)
                .appendPath(AppConstants.V2)
                .appendPath(AppConstants.KEYLIST)
                .appendPath(AppConstants.JSONNAME)
                .appendQueryParameter(AppConstants.PUBLISHEDDATE, date).appendQueryParameter(AppConstants.APIKEY, AppConstants.APIKEY_VALUE);
        String serviceUrl = builder.build().toString();

        new BackgroundTaskToFetchData().execute(serviceUrl);

    }


    private class BackgroundTaskToFetchData extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(in, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals(AppConstants.KEYRESULTS)) { // Check if desired key
                        readResults(jsonReader);
                        break;
                    } else {
                        jsonReader.skipValue(); // Skip values of other keys
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            mBooksResponseLiveData.postValue(mBooksArrayList);
        }

        public void readResults(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals(AppConstants.KEYLIST)) {

                    List<Lists> lists = readListsArray(reader);

                    for (Lists alist : lists) {
                        mBooksArrayList.addAll(alist.getBooks());
                    }

                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        }

        private List<Lists> readListsArray(JsonReader reader) throws IOException {
            List<Lists> lists = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                lists.add(readList(reader));
            }
            reader.endArray();
            return lists;
        }

        private Lists readList(JsonReader reader) throws IOException {
            Lists lists = new Lists();
            reader.beginObject();
            while (reader.hasNext()) {

                String name = reader.nextName();
                if (name.equals(AppConstants.KEYBOOKS)) {
                    lists.setBooks(readBooksArray(reader));
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return lists;
        }

        public List<Books> readBooksArray(JsonReader reader) throws IOException {
            List<Books> messages = new ArrayList<Books>();

            reader.beginArray();
            while (reader.hasNext()) {
                messages.add(readBook(reader));
            }
            reader.endArray();
            return messages;
        }


        public Books readBook(JsonReader reader) throws IOException {

            Books book = new Books();
            reader.beginObject();
            while (reader.hasNext()) {
                String keyName = reader.nextName();
                if (keyName.equals(AppConstants.KEYTITLE)) {
                    book.setTitle(reader.nextString());
                } else if (keyName.equals(AppConstants.KEYAUTHOR)) {

                    book.setAuthor(reader.nextString());
                } else if (keyName.equals(AppConstants.KEYCONTRIBUTOR)) {

                    book.setContributor(reader.nextString());
                } else if (keyName.equals(AppConstants.KEYDESCRIPTION)) {

                    book.setDescription(reader.nextString());
                } else if (keyName.equals(AppConstants.KEYPUBLISHER)) {

                    book.setPublisher(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return book;
        }

    }
}



