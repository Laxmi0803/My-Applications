package com.sample.booklistapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sample.booklistapplication.model.Books;

import java.util.List;

public class BooksViewModel extends AndroidViewModel {
    private BooksRepository mBooksRepository;

    public BooksViewModel(@NonNull Application application) {
        super(application);
        mBooksRepository = new BooksRepository();
    }

    public LiveData<List<Books>> getAllBooks() {
        return mBooksRepository.getmBooksResponseLiveData();
    }

    public void fetchAllbooks(String date) {
        mBooksRepository.fetchAllBooksFromAPI(date);
    }

}

