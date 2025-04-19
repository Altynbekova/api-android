package com.example.vk_id;

import android.os.Bundle;
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

import java.net.URL;

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
//                resultOutput.setText("Account is found by id="+searchInput.getText());
                resultOutput.setText(url.toString());
            }
        };
        button.setOnClickListener(listener);
    }
}