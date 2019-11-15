package edu.temple.cis3515_bookcase;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class BookListFragment extends Fragment {
    public static final String ARG_BOOKS = "books";

    private ArrayList<Book> books;

    private OnListClickListener mListener;

    public BookListFragment() {
        // Required empty public constructor
    }


    public static BookListFragment newInstance(Bundle b) {
        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            books = getArguments().getParcelableArrayList(ARG_BOOKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);

        ListView lv = ((ListView)v.findViewById(R.id.lvBooks));

        lv.setAdapter(new ListAdapter(getActivity(),books));
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.displayBook(position);
            }
        });


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListClickListener) {
            mListener = (OnListClickListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListClickListener {
        // TODO: Update argument type and name
        void displayBook(int index);
    }

    public ArrayList<Book> getBook(){
        return books;
    }
}
