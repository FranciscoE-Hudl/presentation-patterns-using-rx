package com.yoelglus.presentation.patterns.mvvm;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.memoizrlabs.Shank;
import com.yoelglus.presentation.patterns.R;
import com.yoelglus.presentation.patterns.viewmodel.AddItemViewModel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import rx.Observable;
import rx.internal.util.SubscriptionList;

public class MvvmAddItemActivity extends AppCompatActivity {

    private AddItemViewModel mAddItemViewModel;
    private SubscriptionList mSubscriptionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mAddItemViewModel = Shank.provideNew(AddItemViewModel.class);

        bindViewModel();

        mAddItemViewModel.onStart();
    }

    private void bindViewModel() {
        mSubscriptionList = new SubscriptionList();
        mSubscriptionList.add(RxView.clicks(findViewById(R.id.add_button))
                .doOnNext(mAddItemViewModel.addItemClicks())
                .subscribe());
        mSubscriptionList.add(RxView.clicks(findViewById(R.id.cancel_button))
                .doOnNext(mAddItemViewModel.cancelClicks())
                .subscribe());
        mSubscriptionList.add(getTextChangeObservable(R.id.content).doOnNext(mAddItemViewModel.contentTextChanged())
                .subscribe());
        mSubscriptionList.add(getTextChangeObservable(R.id.detail).doOnNext(mAddItemViewModel.detailTextChanged())
                .subscribe());
        mSubscriptionList.add(mAddItemViewModel.dismiss().doOnNext(aVoid -> finish()).subscribe());
        mSubscriptionList.add(mAddItemViewModel.addButtonEnabled()
                .doOnNext(RxView.enabled(findViewById(R.id.add_button)))
                .subscribe());
    }

    @NonNull
    private Observable<String> getTextChangeObservable(int viewId) {
        return RxTextView.textChangeEvents((EditText) findViewById(viewId))
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddItemViewModel.onStop();
        mSubscriptionList.unsubscribe();
    }
}