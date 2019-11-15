package edu.temple.cis3515_bookcase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Book> books;
    public ListAdapter(Context context, ArrayList<Book> data){
        this.context = context;
        this.books = data;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setId(position);
        label.setText(books.get(position).getTitle());
        label.setTextSize(20);
        label.setPadding(16, 25, 16, 25);

        return label;
    }
}
