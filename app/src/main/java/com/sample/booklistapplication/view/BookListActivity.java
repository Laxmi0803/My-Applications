package com.sample.booklistapplication.view;

import android.os.Bundle;

import com.sample.booklistapplication.R;
import com.sample.booklistapplication.databinding.ActivityBooksBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookListActivity extends AppCompatActivity {

    private ActivityBooksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();


    }

    private void initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_books);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = binding.rvBookList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

}