package com.example.xinyichen.reflect;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by xinyichen on 10/7/17.
 */

public class Utils {
    // everything in this class should be static
    // purpose: to have clean code
    static DatabaseReference dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://calhacks2017-62707.firebaseio.com/");
    static StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://calhacks2017-62707.appspot.com");
}
