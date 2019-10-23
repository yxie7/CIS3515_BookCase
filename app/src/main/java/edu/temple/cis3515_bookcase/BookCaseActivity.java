package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);

        String[] b = getResources().getStringArray(R.array.books);
        ArrayList<String> books = new ArrayList<>(10);
        books.addAll(Arrays.asList(b));
    }
}
