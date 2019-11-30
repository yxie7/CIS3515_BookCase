package edu.temple.cis3515_bookcase;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener, BookDetailsFragment.onPlayClick{

    FragmentManager fm;
    Fragment current1;
    Fragment current2;
    int currentBookPosition = 0;
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
                searchBooks();
            }
        });


        if (savedInstanceState == null) {
            searchBooks();
        } else {
            updateView();
        }

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
    public void searchBooks() {
        books = new ArrayList<>();
        Thread t = new Thread() {
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
        };
        t.start();
    }

    Handler responseHandler = new Handler((new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONArray responseArray = (JSONArray) msg.obj;
            if (responseArray.length() > 0) {
                try {
                    for (int i = 0; i < responseArray.length(); i++) {
                        int book_id = responseArray.getJSONObject(i).getInt("book_id");
                        String title = responseArray.getJSONObject(i).getString("title");
                        String author = responseArray.getJSONObject(i).getString("author");
                        int published = responseArray.getJSONObject(i).getInt("published");
                        String cover_url = responseArray.getJSONObject(i).getString("cover_url");
                        int duration = responseArray.getJSONObject(i).getInt("duration");
                        Log.d("book", "b,t,a,p,c::" + (book_id) + title + author + (published) + cover_url);
                        books.add(new Book(book_id, title, author, published, cover_url, duration));
                    }
                    updateView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                search = "";
                Toast t = Toast.makeText(getApplicationContext(), "No results found...\n\nTry something else", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                View view = t.getView();
                view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(Color.WHITE);
                t.show();
            }
            return false;
        }
    }));


    // called to change what is displayed on screen
    public void updateView() {
        current1 = fm.findFragmentById(R.id.container1); // gets references to existing fragments if any
        current2 = fm.findFragmentById(R.id.container2);

        if (onePane) { //if portrait
            if (current1 instanceof BookListFragment) // check if previously had a BookListFragment(landscape mode)
                books = ((BookListFragment) current1).getBooks();
            ViewPagerFragment vp = ViewPagerFragment.newInstance(books);
            fm.beginTransaction().replace(R.id.container1, vp).commit();

            if (current2 instanceof BookDetailsFragment) // check if a book has previously been selected in from list view
                currentBookPosition = ((BookListFragment) current1).getPosition();
            else currentBookPosition = 0;
            //vp.gotoBook(currentBookPosition);
        } else { // landscape
            if (current1 instanceof ViewPagerFragment) {
                books = ((ViewPagerFragment) current1).getBooks();
                currentBookPosition = ((ViewPagerFragment) current1).getPosition();
            }
            BookListFragment bl = BookListFragment.newInstance(books);
            fm.beginTransaction().replace(R.id.container1, bl).commit();
            fm.beginTransaction().replace(R.id.container2, BookDetailsFragment.newInstance(books.get(currentBookPosition))).commit();
        }
    }

    @Override
    public void play(Book book){

    }
}
