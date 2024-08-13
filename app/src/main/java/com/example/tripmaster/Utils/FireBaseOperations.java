package com.example.tripmaster.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseOperations {

    private static FireBaseOperations fireBaseOperations = null;
    private final FirebaseDatabase database;


    public FireBaseOperations(){
        database = FirebaseDatabase.getInstance();

    }

    public static FireBaseOperations getInstance(){
        if(fireBaseOperations == null){
            fireBaseOperations = new FireBaseOperations();
        }
        return fireBaseOperations;
    }

    public DatabaseReference getDatabaseReference(String name) {
        return database.getReference(name);
    }


}
