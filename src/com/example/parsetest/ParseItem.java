package com.example.parsetest;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("TestObject") //實體名稱
public class ParseItem extends ParseObject {
    final String KEY = "foo"; //欄位名稱
    // Ensure that your subclass has a public default constructor

    public ParseItem() {
        super();
    }

    public String getKey() {
        return getString(KEY);
    }

    public void setKey(String key) {
        put(KEY, key);
        saveInBackground();
    }
    

}
