package com.a206.mychelin.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RealTimeDataBase {

    public void setNotice(String nickname){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        List<String> tt = new ArrayList<>();

        DatabaseReference myref = firebaseDatabase.getReference("notice");

        myref.child(nickname).setValue("NOTICE",null);
    }

}
