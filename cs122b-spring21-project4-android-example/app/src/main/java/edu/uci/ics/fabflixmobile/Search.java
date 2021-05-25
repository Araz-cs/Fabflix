package edu.uci.ics.fabflixmobile;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Search extends ActionBarActivity{
    EditText searchMovie;
    Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactionbar);
        searchMovie = findViewById(R.id.searchMovie);
        searchButton = findViewById(R.id.searchButton);

        searchMovie.setOnEditorActionListener((v, actionId, event) -> {
            boolean flag;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                Intent checkList = new Intent(Search.this, ListViewActivity.class);
                checkList.putExtra("query", searchMovie.getText().toString());
                startActivity(checkList);

                flag = true;
            }
            else
            {
                flag=false;
            }
            return flag;
        });
        searchButton.setOnClickListener(view -> {
            Intent checkList = new Intent(Search.this, ListViewActivity.class);
            checkList.putExtra("query", searchMovie.getText().toString());
            startActivity(checkList);
        });


    }
}