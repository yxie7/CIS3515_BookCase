package edu.temple.cis3515_bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class BookDetailsFragment extends Fragment {
    View v;

    public static final String ARG_DETAILS = "details_displayed";

    private String details;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(String b) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DETAILS,b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            details = getArguments().getString(ARG_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView tv = (TextView)v.findViewById(R.id.tvDetails);
        tv.setText(details);
        return v;
    }


    public void displayDetails(int index){
        //assert index > -1;
        TextView tv = (TextView)v.findViewById(R.id.tvDetails);
        tv.setText(getResources().getStringArray(R.array.books)[index]);

    }


    public void displayDetails(String title){
        //assert index > -1;
        TextView tv = (TextView)v.findViewById(R.id.tvDetails);
        tv.setText(title);

    }
}
