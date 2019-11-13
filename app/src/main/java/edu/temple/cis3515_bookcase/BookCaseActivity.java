package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener {

    Boolean twoPanes;
    ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);

        final String search = "";
        Thread t = new Thread() {
            @Override
            public void run() {
                URL url;
                try {
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

                    JSONObject booksObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = response;



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        twoPanes = (findViewById(R.id.bookDetailsContainer) != null);

        Bundle args_books = new Bundle();
        args_books.putParcelableArrayList(BookListFragment.ARG_BOOKS, books);

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

    Handler responseHandler = new Handler((new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JSONObject responseObject = (JSONObject)msg.obj;
            try{
                c
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }))
}
