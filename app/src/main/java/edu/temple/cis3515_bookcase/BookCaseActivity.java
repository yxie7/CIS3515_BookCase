package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener {

    Boolean twoPanes;
    ArrayList<String> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);

        twoPanes = (findViewById(R.id.bookDetailsContainer) != null);

        String[] b = getResources().getStringArray(R.array.books);
        books = new ArrayList<>(b.length);
        books.addAll(Arrays.asList(b));

        Bundle args_books = new Bundle();
        args_books.putStringArrayList(BookListFragment.ARG_BOOKS, books);

        if (twoPanes) {

            Fragment blf = BookListFragment.newInstance(args_books);
            getSupportFragmentManager().beginTransaction().replace(R.id.bookListContainer, blf).commit();
            Fragment bdf = BookDetailsFragment.newInstance("");
            getSupportFragmentManager().beginTransaction().replace(R.id.bookDetailsContainer, bdf).commit();

        } else { //portrait mode
            Fragment vpf = ViewPagerFragment.newInstance(args_books);
            getSupportFragmentManager().beginTransaction().replace(R.id.viewPagerContainer, vpf).commit();
        }
    }
    /*
        @Override
        public void displayBookDetails(int index) {
            Log.d("bookindex", Integer.toString(index));

            ((BookDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.bookDetailsContainer)).displayDetails(index);
        }
    */
    @Override
    public void displayBookDetails(String title) {
        ((BookDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.bookDetailsContainer)).displayDetails(title);
    }
}
