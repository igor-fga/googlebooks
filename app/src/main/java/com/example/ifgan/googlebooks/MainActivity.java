package com.example.ifgan.googlebooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent(MainActivity.this, SearchResultActivity.class);
                startActivity(resultIntent);
            }
        });
    }
}
