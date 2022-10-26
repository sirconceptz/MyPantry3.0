package com.hermanowicz.pantry.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DatabaseMode {

    public static final String dbTableProducts = "products";
    public static final String dbTableCategories = "categories";
    public static final String dbTableStorageLocations = "storage_locations";
    public static final String dbTablePhotos = "categories";

    private Mode databaseMode = Mode.LOCAL; // default value

    public static DatabaseReference getOnlineDatabaseReference(String dbTableName) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(true);
        return db.getReference().child(dbTableName).child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
    }

    public Mode getDatabaseMode() {
        return databaseMode;
    }

    public void setDatabaseMode(Mode databaseMode) {
        this.databaseMode = databaseMode;
    }

    public enum Mode {LOCAL, ONLINE}
}