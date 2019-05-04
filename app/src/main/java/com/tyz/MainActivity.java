package com.tyz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.tyz.adapter.ContactAdapter;
import com.tyz.adapter.ContactQuickAdapter;
import com.tyz.search.CharIndexView;
import com.tyz.search.CharLoadIndexView;
import com.tyz.stickyheader.StickyHeaderDecoration;
import com.tyz.adapter.TestUtils;
import com.tyz.cn.CNPinyin;
import com.tyz.cn.CNPinyinFactory;
import com.tyz.search.Contact;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rv_main;
//    private ContactAdapter adapter;
    private ContactQuickAdapter adapter;
    private CharLoadIndexView iv_main;
    private TextView tv_index;
    private Subscription subscription;
    private ArrayList<CNPinyin<Contact>> contactList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        iv_main = (CharLoadIndexView) findViewById(R.id.iv_main);
        tv_index = (TextView) findViewById(R.id.tv_index);
        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.lanuch(MainActivity.this, contactList);
            }
        });
        findViewById(R.id.bt_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 1");
                contactList.clear();
                adapter.notifyDataSetChanged();
                getPinyinList();
                Log.d(TAG, "onClick: 2");
            }
        });
//        final LinearLayoutManager manager = new LinearLayoutManager(this);
        final GridLayoutManager manager = new GridLayoutManager(this, 4, LinearLayoutManager.HORIZONTAL, false);
        rv_main.setLayoutManager(manager);


        iv_main.setOnCharIndexChangedListener(new CharIndexView.OnCharIndexChangedListener() {
            @Override
            public void onCharIndexChanged(char currentIndex) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getFirstChar() == currentIndex) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }

            @Override
            public void onCharIndexSelected(String currentIndex) {
                if (currentIndex == null) {
//                    tv_index.setVisibility(View.INVISIBLE);
                } else {
//                    tv_index.setVisibility(View.INVISIBLE);
//                    tv_index.setText(currentIndex);
                }
            }
        });

//        adapter = new ContactAdapter(contactList);
          adapter = new ContactQuickAdapter(R.layout.activity_main_item, contactList);
        rv_main.setAdapter(adapter);
        rv_main.addItemDecoration(new StickyHeaderDecoration(adapter));

        Log.d(TAG, "onCreate1: ");
        getPinyinList();
        Log.d(TAG, "onCreate2: ");
    }


    @SuppressLint("CheckResult")
    private void getPinyinList() {
//
        Flowable.create(new FlowableOnSubscribe<List<CNPinyin<Contact>>>() {
            @Override
            public void subscribe(FlowableEmitter<List<CNPinyin<Contact>>> emitter) throws Exception {
                if (!emitter.isCancelled()) {
                    List<CNPinyin<Contact>> contactList = CNPinyinFactory.createCNPinyinList(TestUtils.contactList(MainActivity.this));
                    Collections.sort(contactList);
                    emitter.onNext(contactList);
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CNPinyin<Contact>>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(Integer.MAX_VALUE);   //要调用Subscription的request()方法，指定下游接收数量。若不指定，则下游接收不到任何数据。
                    }

                    @Override
                    public void onNext(List<CNPinyin<Contact>> cnPinyins) {
                        contactList.addAll(cnPinyins);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//
//        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<Contact>>>() {
//            @Override
//            public void call(Subscriber<? super List<CNPinyin<Contact>>> subscriber) {
//                if (!subscriber.isUnsubscribed()) {
//                    List<CNPinyin<Contact>> contactList = CNPinyinFactory.createCNPinyinList(TestUtils.contactList(MainActivity.this));
//                    Collections.sort(contactList);
//                    subscriber.onNext(contactList);
//                    subscriber.onCompleted();
//                }
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<CNPinyin<Contact>>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<CNPinyin<Contact>> cnPinyins) {
//                        contactList.addAll(cnPinyins);
//                        adapter.notifyDataSetChanged();
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
//            subscription.unsubscribe();
            subscription.cancel();
        }
        super.onDestroy();
    }

}
