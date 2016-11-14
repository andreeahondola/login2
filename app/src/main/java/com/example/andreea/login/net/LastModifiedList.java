package com.example.andreea.login.net;

/**
 * Created by Andreea on 11.11.2016.
 */

import java.util.Date;
import java.util.List;

public class LastModifiedList<E> {
    private String mLastModified;
    private List<E> mList;

    public LastModifiedList(String lastModified, List<E> list) {
        mLastModified = lastModified;
        mList = list;
    }

    public String getLastModified() {
        return mLastModified;
    }

    public List<E> getList() {
        return mList;
    }
}
