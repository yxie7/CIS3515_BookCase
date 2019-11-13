package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class BookCaseActivity extends AppCompatActivity implements BookListFragment.OnListClickListener {

    Boolean twoPanes;
    ArrayList<Book> books;
    EditText etSearch;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcase);
        etSearch = (EditText)findViewById(R.id.etSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String search = etSearch.getText().toString();
                books = new ArrayList<>();
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
                            Log.d("js",response.toString());

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
        });


/*
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
        */
    }

    @Override
    public void displayBookDetails(String title) {
        ((BookDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.bookDetailsContainer)).displayDetails(title);
    }

    Handler responseHandler = new Handler((new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONArray responseArray = (JSONArray)msg.obj;
            try{
                for (int i = 0; i<responseArray.length(); i++){
                    int book_id = responseArray.getJSONObject(i).getInt("book_id");
                    String title = responseArray.getJSONObject(i).getString("title");
                    String author = responseArray.getJSONObject(i).getString("author");
                    int published = responseArray.getJSONObject(i).getInt("published");
                    String cover_url = responseArray.getJSONObject(i).getString("cover_url");
                    Log.d("json","b,t,a,p,c::" + Integer.toString(book_id)+title+author+Integer.toString(published)+cover_url);
                    books.add(new Book(book_id,title,author,published,cover_url));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }));
}
