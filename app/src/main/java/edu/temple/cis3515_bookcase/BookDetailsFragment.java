package edu.temple.cis3515_bookcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;


public class BookDetailsFragment extends Fragment {
    SQLiteDatabase db;
    SQLiteOpenHelper helper;

    View v;

    public static final String ARG_BOOK = "parcelable book string";

    private Book book;

    TextView tvTitle;
    ImageView imgCover;
    TextView tvAuthor;
    TextView tvYear;
    TextView tvDuration;

    onPlayClick ibtnPlayListener;
    ImageButton ibtnPlay;
    ImageButton ibtnDownloadDelete;

    File bookMP3;

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

    public interface saveDB {
        void saveDB(int id, String title, String author, int duration, int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(ARG_BOOK);
            bookMP3 = new File(getContext().getFilesDir(), book.getId() + ".mp3");

        }
        helper = new dbHelper(getContext());
        db = helper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_book_details, container, false);
        bookMP3 = new File(getContext().getFilesDir(), book.getId() + ".mp3");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayBook(book);
        ibtnPlay = v.findViewById(R.id.ibtnPlay);
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookMP3.exists()) {
                    ibtnPlayListener.play(book, bookMP3);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = Toast.makeText(getActivity(), "Playing from MP3 file...", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
                } else {
                    ibtnPlayListener.play(book);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = Toast.makeText(getActivity(), "Streaming audiobook...", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
                }
            }
        });

        ibtnDownloadDelete = v.findViewById(R.id.ibtnDownloadDelete);
        updateDownloadDelete();
        ibtnDownloadDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread ddThread = new Thread() {
                    @Override
                    public void run() {
                        Log.d("mp3", bookMP3.getName() + " exists? " + bookMP3.exists());
                        if (bookMP3.exists()) {
                            bookMP3.delete();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ibtnDownloadDelete.setImageResource(R.drawable.btndownload);
                                }
                            });
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast t = Toast.makeText(getActivity(), "Deleting MP3 file...", Toast.LENGTH_SHORT);
                                    //t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                }
                            });
                        } else { // book is not downloaded
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast t = Toast.makeText(getActivity(), "Downloading MP3 file...", Toast.LENGTH_SHORT);
                                        //t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                    }
                                });
                                URL url = new URL(" https://kamorris.com/lab/audlib/download.php?id=" + book.getId());
                                URLConnection urlconn = url.openConnection();
                                byte[] buffer = new byte[urlconn.getContentLength()];
                                DataInputStream in = new DataInputStream(url.openStream());
                                in.readFully(buffer);
                                in.close();
                                DataOutputStream out = new DataOutputStream(new FileOutputStream(bookMP3));
                                out.write(buffer);
                                out.flush();
                                out.close();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ibtnDownloadDelete.setImageResource(R.drawable.btntrash);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                ddThread.start();
            }
        });
    }

    public void updateDownloadDelete() {
        Log.d("mp3", "update: " + bookMP3.getName() + " exists? " + bookMP3.exists());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bookMP3.exists()) {
                    ibtnDownloadDelete.setImageResource(R.drawable.btntrash);
                } else {
                    ibtnDownloadDelete.setImageResource(R.drawable.btndownload);
                }
            }
        });
    }

    Handler downloadHandler = new Handler((new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj instanceof File) {

            }
            return false;
        }
    }));

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onPlayClick) {
            ibtnPlayListener = (onPlayClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickListener");
        }
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
            String duration = minutes + " mins " + seconds + " secs";
            tvDuration.setText(duration);
        }
    }

    public interface onPlayClick {
        void play(Book book);

        void play(Book book, File mp3);
    }
}
