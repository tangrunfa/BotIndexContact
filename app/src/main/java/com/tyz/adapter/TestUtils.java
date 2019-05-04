package com.tyz.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tyz.R;
import com.tyz.entity.DaoManager;
import com.tyz.search.Contact;

/**
 * Created by com on 2017/9/11.
 */

public class TestUtils {

    public static List<Contact> contactList(Context context) {
        List<Contact> contactList = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        String[] names = context.getResources().getStringArray(R.array.names);
        String[] names1 = new String[3000];
        for (int i = 0; i <3000 ; i++) {
            int j = random.nextInt(names.length - 1);
              names1[i] = names[j]+i;
        };
        for (int i = 0; i < names1.length; i++) {
            int urlIndex = random.nextInt(URLS.length - 1);
            int url = URLS[urlIndex];
            contactList.add(new Contact(names1[i], url));
        }
        return contactList;
    }


    public static List<Contact> saveContact(Context context){
        List<Contact> contactList = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        String[] names = context.getResources().getStringArray(R.array.names);
        String[] names1 = new String[3000];
        for (int i = 0; i <3000 ; i++) {
            int j = random.nextInt(names.length - 1);
            names1[i] = names[j]+i;
        };
        for (int i = 0; i < names1.length; i++) {
            int urlIndex = random.nextInt(URLS.length - 1);
            int url = URLS[urlIndex];
            contactList.add(new Contact(names1[i], url));
            DaoManager.getInstance().getContactDao().insertInTx(new Contact(names1[i], url));
        }
      return  DaoManager.getInstance().getContactDao().loadAll();
    }

    static int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};

}
