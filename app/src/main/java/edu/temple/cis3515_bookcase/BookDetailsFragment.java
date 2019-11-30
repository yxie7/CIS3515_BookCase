package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.concurrent.TimeUnit;


public class BookDetailsFragment extends Fragment {
    View v;

    public static final String ARG_BOOK = "parcelable book string";

    private Book book;

    TextView tvTitle;
    ImageView imgCover;
    TextView tvAuthor;
    TextView tvYear;
    TextView tvDuration;

    ImageButton ibtnPlay;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book b) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, b);
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

        ibtnPlay = (ImageButton)v.findViewById(R.id.ibtnPlay);
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        displayBook(book);

        return v;
    }


    public void displayBook(Book book) {
        if (book != null) {
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            imgCover = (ImageView) v.findViewById(R.id.imgCover);
            tvAuthor = (TextView) v.findViewById(R.id.tvAuthor);
            tvYear = (TextView) v.findViewById(R.id.tvYear);
            tvDuration = (TextView) v.findViewById(R.id.tvDuration);

            tvTitle.setText(book.getTitle());
            Picasso.get().load(book.getCoverURL()).into(imgCover);
            tvAuthor.setText(book.getAuthor());
            tvYear.setText(String.valueOf(book.getPublished()));

            long seconds = book.getDuration();
            long minutes = TimeUnit.SECONDS.toMinutes(seconds);
            seconds = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
            String duration = minutes + ":" + seconds;
            tvDuration.setText(duration);
        }
    }

    public interface onPlayClick{
        void play(Book book);
    }

}
