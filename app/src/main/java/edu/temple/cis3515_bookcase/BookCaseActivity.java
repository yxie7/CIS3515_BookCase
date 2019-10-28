package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener{

    Boolean twoPanes;
    ArrayList<String> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);

        twoPanes = (findViewById(R.id.bookDetailsFragment) != null);


        String[] b = getResources().getStringArray(R.array.books);
        books = new ArrayList<>(b.length);
        books.addAll(Arrays.asList(b));


        Bundle args = new Bundle();
        args.putStringArrayList(BookListFragment.ARG_BOOKS, books);

        Fragment blf = BookListFragment.newInstance(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bookListFragment,blf).commit();


    }

    @Override
    public void displayBookDetails(int index) {
        Log.d("bookindex",Integer.toString(index));
    }

}
