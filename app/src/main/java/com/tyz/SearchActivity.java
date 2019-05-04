package com.tyz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
//import rx.Observable;
//import rx.Subscriber;
//import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Func1;
//import rx.schedulers.Schedulers;
import com.tyz.adapter.SearchAdapter;
import com.tyz.cn.CNPinyin;
import com.tyz.cn.CNPinyinIndex;
import com.tyz.cn.CNPinyinIndexFactory;
import com.tyz.search.Contact;
import com.tyz.search.TextViewChangedOnSubscribe;

/**
 * Created by com on 2017/9/12.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    /**
     * activity中的根目录
     */
    private View ll_root;
    /**
     * 窗体控件上一次的高度,用于监听键盘弹起
     */
    private int mLastHeight;

    private EditText et_search;

    private RecyclerView rv_main;
    private SearchAdapter adapter;

    private ArrayList<CNPinyin<Contact>> contactList;
    private Subscription subscription;

    public static void lanuch(Context context, ArrayList<CNPinyin<Contact>> contactList) {
        if (contactList == null) return;
        Intent intent = new Intent(context, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contactList", contactList);
        intent.putExtras(bundle);
//        intent.putExtra("contactList", contactList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        contactList = (ArrayList<CNPinyin<Contact>>) getIntent().getSerializableExtra("contactList");

        contactList = (ArrayList<CNPinyin<Contact>>) getIntent().getExtras().getSerializable("contactList");
        ll_root = findViewById(R.id.ll_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(manager);
        adapter = new SearchAdapter(new ArrayList<CNPinyinIndex<Contact>>());
        rv_main.setAdapter(adapter);

        final View decorView = this.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        decorView.getWindowVisibleDisplayFrame(r);
                        if (mLastHeight != r.bottom) {
                            mLastHeight = r.bottom;
                            ViewGroup.LayoutParams params = ll_root.getLayoutParams();
                            params.height = r.bottom - ll_root.getTop()/*  - statusHeight*/;
                            ll_root.setLayoutParams(params);
                        }
                    }
                });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        actionView.findViewById(R.id.iv_return).setOnClickListener(this);
        et_search = (EditText) actionView.findViewById(R.id.et_search);
        ActionBar.LayoutParams actionBarParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        bar.setCustomView(actionView, actionBarParams);
        ConfigUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));

        /**
         * 下面是搜索框智能
         */
        TextViewChangedOnSubscribe plateSubscribe = new TextViewChangedOnSubscribe();
        plateSubscribe.addTextViewWatcher(et_search);

        Flowable.create(plateSubscribe, BackpressureStrategy.BUFFER).debounce(300,TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .switchMap(new Function<String, Publisher<ArrayList<CNPinyinIndex<Contact>>>>() {
                    @Override
                    public Publisher<ArrayList<CNPinyinIndex<Contact>>> apply(String s) throws Exception {
                        return  createFlowable(s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribe(new Subscriber<ArrayList<CNPinyinIndex<Contact>>>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(ArrayList<CNPinyinIndex<Contact>> cnPinyinIndices) {
                adapter.setNewDatas(cnPinyinIndices);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
//        subscription = Observable.create(plateSubscribe).debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//                .switchMap(new Func1<String, Observable<ArrayList<CNPinyinIndex<Contact>>>>() {
//                    @Override
//                    public Observable<ArrayList<CNPinyinIndex<Contact>>> call(final String s) {
//                        return createObservable(s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//                    }
//                }).subscribe(new Subscriber<ArrayList<CNPinyinIndex<Contact>>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(ArrayList<CNPinyinIndex<Contact>> cnPinyinIndices) {
//                        adapter.setNewDatas(cnPinyinIndices);
//                    }
//                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
//            subscription.unsubscribe();
            subscription.cancel();
        }

        super.onDestroy();
    }

    /**
     * 搜索订阅
     * @return
     */
//    private Observable<ArrayList<CNPinyinIndex<Contact>>> createObservable(final String keywork) {
//        return Observable.create(new Observable.OnSubscribe<ArrayList<CNPinyinIndex<Contact>>>() {
//            @Override
//            public void call(Subscriber<? super ArrayList<CNPinyinIndex<Contact>>> subscriber) {
//                if (!subscriber.isUnsubscribed()) {
//                    subscriber.onNext(CNPinyinIndexFactory.indexList(contactList, keywork));
//                }
//            }
//        });
//    }
    private Flowable<ArrayList<CNPinyinIndex<Contact>>> createFlowable(final String keywork) {
       return  Flowable.create(new FlowableOnSubscribe<ArrayList<CNPinyinIndex<Contact>>>() {
           @Override
           public void subscribe(FlowableEmitter<ArrayList<CNPinyinIndex<Contact>>> emitter) throws Exception {
               emitter.onNext(CNPinyinIndexFactory.indexList(contactList, keywork));

           }
       },BackpressureStrategy.BUFFER);
    }
}
