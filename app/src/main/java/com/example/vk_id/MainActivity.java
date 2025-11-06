package com.example.vk_id;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vk_id.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText searchInput;
    private Button button;
    private TextView resultOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = findViewById(R.id.et_search);
        button = findViewById(R.id.btn_search);
        resultOutput = findViewById(R.id.tv_search_res);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = NetworkUtils.generateUrl(searchInput.getText().toString());
//                new ApiQueryTask().execute(url);
                new TaskRunner().executeAsync(
                        () -> {
                            String response;
                            try {
                                response = NetworkUtils.getResponse(url);
                                resultOutput.setText(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            return response;
                        },
                        result -> {
                            String name, website;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                name = jsonObject.getString("name");
                                website = jsonObject.getString("website");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            resultOutput.setText(
                                    String.format("Name: %s\nWebsite: %s", name, website)
                            );
                        }
                );

                /*String response;
                try {
                    response = NetworkUtils.getResponse(url);
                    resultOutput.setText(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
            }
        };
        button.setOnClickListener(listener);
    }

    class ApiQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String response;
            try {
                response = NetworkUtils.getResponse(urls[0]);
                resultOutput.setText(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            resultOutput.setText(response);
        }
    }

    class TaskRunner {
        private ExecutorService executor = Executors.newSingleThreadExecutor();
        private Handler handler = new Handler(Looper.getMainLooper());

        public interface Callback<R> {
            void onComplete(R result);
        }

        public <T> void executeAsync(Callable<T> callable, Callback<T> callback) {
            executor.execute(() -> {
                try {
                    final T result = callable.call();
                    handler.post(() -> {
                        callback.onComplete(result);
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            executor.shutdown();
        }
    }
}