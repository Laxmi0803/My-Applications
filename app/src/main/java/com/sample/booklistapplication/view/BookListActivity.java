package com.sample.booklistapplication.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;

import com.sample.booklistapplication.R;
import com.sample.booklistapplication.adapter.BookListDataAdapter;
import com.sample.booklistapplication.databinding.ActivityBooksBinding;
import com.sample.booklistapplication.model.Books;
import com.sample.booklistapplication.viewmodel.BooksViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private ActivityBooksBinding mDataBinding;
    private BooksViewModel mBookViewModel;
    private BookListDataAdapter mBookListDataAdapter;
    private String mModifiedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initSearchView();
        initDatePicker();

    }

    private void initViews() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_books);
        Toolbar toolbar = mDataBinding.toolbar;
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = mDataBinding.rvBookList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        mBookViewModel = ViewModelProviders.of(this).get(BooksViewModel.class);
        mBookListDataAdapter = new BookListDataAdapter();
        recyclerView.setAdapter(mBookListDataAdapter);

        Date date = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        setUpBookList(currentDate);

    }

    private void setUpBookList(String date) {
        mDataBinding.spinnerLoading.setVisibility(View.VISIBLE);
        mBookViewModel.fetchAllbooks(date);
        mBookViewModel.getAllBooks().observe(this, new Observer<List<Books>>() {
            @Override
            public void onChanged(@Nullable List<Books> books) {

                mDataBinding.spinnerLoading.setVisibility(View.GONE);
                mBookListDataAdapter.setBookList((ArrayList<Books>) books);


            }
        });

    }

    private void initSearchView() {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = mDataBinding.edtSearch;

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getResources().getString(R.string.lbl_search));

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mBookListDataAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mBookListDataAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void initDatePicker() {
        mDataBinding.btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BookListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                calendar.set(year, month, day);
                                Date chosenDate = calendar.getTime();
                                SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format));
                                mModifiedDate = format.format(chosenDate);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);
                                builder.setMessage(String.format(getString(R.string.dialog_message), mModifiedDate));
                                builder.setPositiveButton(R.string.yes_btn_txt, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDataBinding.spinnerLoading.show();
                                        setUpBookList(mModifiedDate);
                                    }
                                });
                                builder.setNegativeButton(R.string.no_btn_txt, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });
    }


}