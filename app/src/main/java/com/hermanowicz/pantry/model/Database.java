package com.hermanowicz.pantry.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Database {

    private DatabaseMode databaseMode = DatabaseMode.LOCAL;

    public enum DatabaseMode {LOCAL, ONLINE}

    public DatabaseMode getDatabaseMode() {
        return databaseMode;
    }

    public void setDatabaseMode(DatabaseMode databaseMode) {
        this.databaseMode = databaseMode;
    }

    public static DatabaseReference getOnlineDatabaseReference(String dbTableName){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        return db.getReference().child(dbTableName).child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
    }
}