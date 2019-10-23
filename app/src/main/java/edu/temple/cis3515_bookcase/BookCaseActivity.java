package edu.temple.cis3515_bookcase;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);

        String[] b = getResources().getStringArray(R.array.books);
        ArrayList<String> books = new ArrayList<>(10);
        books.addAll(Arrays.asList(b));


        Bundle args = new Bundle();
        args.putStringArrayList(BookListFragment.ARG_BOOKS, books);

        Fragment blf = BookListFragment.newInstance(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bookListContainer,blf).commit();

    }

    @Override
    public void displayBookDetails(String index) {
        Log.d("bookindex",index);
    }
}
