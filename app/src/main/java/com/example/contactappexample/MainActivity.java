package com.example.contactappexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactappexample.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contactList;
    private ContactsAdapter contactsAdapter;
    private AppDatabase appDatabase;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        init();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(MainActivity.this, AddContactActivity.class));
                startActivity(intent);
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                contactList.clear();
                contactList.addAll(contactDao.getAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) menuItem.getActionView();
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search contact");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return false;
            }
        }  );
        return true;
    }

    private void searchContact(String query) {
        if (!query.isEmpty()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    contactList.clear();
                    contactList.addAll(contactDao.searchContacts("%" + query + "%"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
        else {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    contactList.clear();
                    contactList.addAll(contactDao.getAll());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                contactList.clear();
                contactList.addAll(contactDao.getAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    private void init() {
        binding.rvContact.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<Contact>();
        contactsAdapter = new ContactsAdapter(contactList);
        binding.rvContact.setAdapter(contactsAdapter);
        contactDao = AppDatabase.getInstance(this).contactDao();
    }
}