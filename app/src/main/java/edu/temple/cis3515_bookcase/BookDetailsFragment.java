package edu.temple.cis3515_bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class BookDetailsFragment extends Fragment {
    View v;

    public static final String ARG_BOOK= "parcelable book string";

    private Book book;

    TextView tvTitle;
    ImageView imgCover;
    TextView tvAuthor;
    TextView tvYear;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book b) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK,b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(ARG_BOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_book_details, container, false);

        displayBook(book);

        return v;
    }


    public void displayBook(Book book){
        if (book != null) {
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            imgCover = (ImageView) v.findViewById(R.id.imgCover);
            tvAuthor = (TextView) v.findViewById(R.id.tvAuthor);
            tvYear = (TextView) v.findViewById(R.id.tvYear);

            tvTitle.setText(book.getTitle());
            //imgCover =
            tvAuthor.setText(book.getAuthor());
            tvYear.setText(String.valueOf(book.getPublished()));
        }
    }

}
