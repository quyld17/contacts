package com.example.contactappexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact")
    List<Contact> getAll();
    @Insert
    void insert(Contact... contacts);

    @Query("SELECT * FROM Contact WHERE name LIKE :searchName")
    List<Contact> searchContacts(String searchName);

    @Query("DELETE FROM Contact WHERE id = :id")
    void deleteLastItem(int id);
}
