package com.example.anull.grab.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by null on 01/12/2017.
 */

public class DatabaseUserFirebase {
    private  FirebaseDatabase db;
    private  DatabaseReference users;

    public DatabaseUserFirebase() {
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");
    }

    public FirebaseDatabase getDb() {
        return db;
    }

    public void setDb(FirebaseDatabase db) {
        this.db = db;
    }

    public DatabaseReference getUsers() {
        return users;
    }

    public void setUsers(DatabaseReference users) {
        this.users = users;
    }
}
