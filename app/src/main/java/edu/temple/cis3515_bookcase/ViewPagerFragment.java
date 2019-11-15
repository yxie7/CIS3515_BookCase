package edu.temple.cis3515_bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_BOOKS = "books";

    // TODO: Rename and change types of parameters
    ArrayList<Book> books;
    ArrayList<Fragment> fragments;
    public static int NUM_PAGE = 0;

    ViewPager vp;
    BookViewPagerAdapter bvpa;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    public static ViewPagerFragment newInstance(ArrayList<Book> books) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(ARG_BOOKS, books);
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
        View v = inflater.inflate(R.layout.fragment_view_pager, container, false);

        fragments = new ArrayList<>(books.size());
        for (int i = 0; i < books.size(); i++) {
            Fragment bdf = BookDetailsFragment.newInstance(books.get(i));
            fragments.add(bdf);
        }
        bvpa = new BookViewPagerAdapter((getChildFragmentManager()));

        vp = v.findViewById(R.id.vp);
        vp.setAdapter(bvpa);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == 0) {
                    if (vp.getCurrentItem() == 0)
                        vp.setCurrentItem(books.size(), false);
                    if (vp.getCurrentItem() == books.size() + 1)
                        vp.setCurrentItem(1, false);
                }
            }
        });
        vp.setCurrentItem(1);

        return v;
    }

    public ArrayList<Book> getBook() {
        return books;
    }

    public class BookViewPagerAdapter extends FragmentStatePagerAdapter {

        public BookViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            // index is the perceived position, while as i is the actual position
            int index = (i - 1) % books.size();
            Log.d("loop", "getItem: " + i + "," + index);

            if (index < 0) {
                index += books.size();
            }
            return BookDetailsFragment.newInstance(books.get(index));
        }

        @Override
        public int getCount() {
            return books.size() + 2;
        }


    }
}