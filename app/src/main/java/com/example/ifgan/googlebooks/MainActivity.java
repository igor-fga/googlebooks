package com.example.ifgan.googlebooks;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        editSearch = (EditText) findViewById(R.id.editSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editSearch.getText().toString().trim().length() <= 0) {
                    Toast.makeText(MainActivity.this, R.string.empty_search, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isOnline()) {
                    Toast.makeText(MainActivity.this, R.string.disconnect, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent(MainActivity.this, SearchResultActivity.class);
                resultIntent.putExtra("EditTextValue", editSearch.getText().toString());
                startActivity(resultIntent);
            }
        });
    }

    /**
     * Check the internet conectivity
     *
     * @return true for online and false for disconnect
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
