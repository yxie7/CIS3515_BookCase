package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener {

    FragmentManager fm;
    Boolean onePane;
    ArrayList<Book> books;
    EditText etSearch;
    Button btnSearch;
    String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);
        fm = getSupportFragmentManager();

        onePane = (findViewById(R.id.container2) == null);

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBooks();
            }
        });


        Bundle args_books = new Bundle();
        args_books.putParcelableArrayList(BookListFragment.ARG_BOOKS, books);


        Fragment vpf = ViewPagerFragment.newInstance(args_books);
        getSupportFragmentManager().beginTransaction().replace(R.id.container1, vpf).commit();
/*
        if (twoPanes) {

            Fragment blf = BookListFragment.newInstance(args_books);
            getSupportFragmentManager().beginTransaction().replace(R.id.bookListContainer, blf).commit();
            Fragment bdf = BookDetailsFragment.newInstance("");
            getSupportFragmentManager().beginTransaction().replace(R.id.bookDetailsContainer, bdf).commit();

        } else { //portrait mode
            Fragment vpf = ViewPagerFragment.newInstance(args_books);
            getSupportFragmentManager().beginTransaction().replace(R.id.viewPagerContainer, vpf).commit();
        }
        */
    }

    // Calls the public method in details fragment to display the selected book
    @Override
    public void displayBook(int index) {
        ((BookDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container2)).displayBook(books.get(index));
    }

    // Empties out the ArrayList of books, then searches retrieves and populates it with book from api
    public void getBooks() {
        books = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                URL url;
                try {
                    search = etSearch.getText().toString();
                    url = new URL(" https://kamorris.com/lab/audlib/booksearch.php?search=" + search);
                    BufferedReader r = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));
                    String response = "", tmpResponse;
                    tmpResponse = r.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = r.readLine();
                    }
                    JSONArray booksArray = new JSONArray(response);
                    Message msg = Message.obtain();
                    msg.obj = booksArray;
                    responseHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler responseHandler = new Handler((new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONArray responseArray = (JSONArray) msg.obj;
            try {
                for (int i = 0; i < responseArray.length(); i++) {
                    int book_id = responseArray.getJSONObject(i).getInt("book_id");
                    String title = responseArray.getJSONObject(i).getString("title");
                    String author = responseArray.getJSONObject(i).getString("author");
                    int published = responseArray.getJSONObject(i).getInt("published");
                    String cover_url = responseArray.getJSONObject(i).getString("cover_url");
                    Log.d("json", "b,t,a,p,c::" + Integer.toString(book_id) + title + author + Integer.toString(published) + cover_url);
                    books.add(new Book(book_id, title, author, published, cover_url));

                }                    Log.d("al", Arrays.deepToString(books.toArray()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }));
}
