package com.sample.booklistapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.booklistapplication.R;
import com.sample.booklistapplication.databinding.BookListItemBinding;
import com.sample.booklistapplication.model.Books;

import java.util.ArrayList;

public class BookListDataAdapter extends RecyclerView.Adapter<BookListDataAdapter.BookDataViewHolder> implements Filterable {

    private ArrayList<Books> books;
    private ArrayList<Books> bookListFiltered = new ArrayList<>();

    @NonNull
    @Override
    public BookDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookListItemBinding bookListItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.book_list_item, parent, false);
        return new BookDataViewHolder(bookListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookDataViewHolder bookDataViewHolder, int i) {
        Books currentBook = bookListFiltered.get(i);
        bookDataViewHolder.bookListItemBinding.setBook(currentBook);
    }

    @Override
    public int getItemCount() {
        if (bookListFiltered != null) {
            return bookListFiltered.size();
        } else {
            return 0;
        }
    }

    public void setBookList(ArrayList<Books> books) {
        this.books = books;
        this.bookListFiltered = books;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    bookListFiltered = books;
                } else {
                    ArrayList<Books> filteredList = new ArrayList<>();
                    for (Books row : books) {
                        if (row.getAuthor().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    bookListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = bookListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bookListFiltered = (ArrayList<Books>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class BookDataViewHolder extends RecyclerView.ViewHolder {

        private BookListItemBinding bookListItemBinding;

        public BookDataViewHolder(@NonNull BookListItemBinding bookListItemBinding) {
            super(bookListItemBinding.getRoot());

            this.bookListItemBinding = bookListItemBinding;
        }
    }
}
