package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.nkonda.jokesandroidlib.DisplayJokeActivity;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    @Nullable private EndpointsIdlingResource mIdlingResource;

    private TextView tvTextView;
    private Button btnTellJoke;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTextView = (TextView) findViewById(R.id.instructions_text_view);

        btnTellJoke = (Button) findViewById(R.id.btn_tell_joke);
        btnTellJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellJoke();
            }
        });

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke() {
        new EndpointsAsyncTask(new TellJokeCallback() {
            @Override
            public void done(String jokeStr) {
                Intent intent = new Intent(MainActivity.this, DisplayJokeActivity.class);
                intent.putExtra(DisplayJokeActivity.ARG_JOKE, jokeStr);
                startActivity(intent);
            }
        }).execute();
        startLoading();
    }

    private void startLoading() {
        tvTextView.setVisibility(View.INVISIBLE);
        btnTellJoke.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private MyApi myApiService = null;
        private TellJokeCallback mCallback;

        public EndpointsAsyncTask(TellJokeCallback mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            if(mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                Thread.sleep(2000);
                return myApiService.tellJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            } catch (InterruptedException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String jokeStr) {
            mCallback.done(jokeStr);
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        }
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new EndpointsIdlingResource();
        }
        return mIdlingResource;
    }
}
