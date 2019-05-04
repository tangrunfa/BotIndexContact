package com.tyz.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

//import rx.Observable;
//import rx.Subscriber;
//import rx.android.MainThreadSubscription;

/**
 * Created by com on 2017/4/10.
 */

public class TextViewChangedOnSubscribe implements FlowableOnSubscribe<String> {

    private TextView mTextView;

    public void addTextViewWatcher(TextView mTextView) {
        this.mTextView = mTextView;
    }

//    @Override  // rxjava1改成rxjava2
//    public void call(final Subscriber<? super String> subscriber) {
//        final TextWatcher watcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!subscriber.isUnsubscribed()) {
//                    subscriber.onNext(s.toString().trim());
//                }
//            }
//        };
//        mTextView.addTextChangedListener(watcher);
//        subscriber.add(new MainThreadSubscription() {
//            @Override
//            protected void onUnsubscribe() {
//                mTextView.removeTextChangedListener(watcher);
//            }
//        });
//    }
//

    @Override
    public void subscribe(final FlowableEmitter<String> emitter) throws Exception {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                emitter.onNext(s.toString().trim());

            }
        };
        mTextView.addTextChangedListener(watcher);
        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                mTextView.removeTextChangedListener(watcher);
            }
        });
    }
}
