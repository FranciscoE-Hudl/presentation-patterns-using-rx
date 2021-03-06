package com.yoelglus.presentation.patterns.rmvp;

import com.yoelglus.presentation.patterns.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import rx.Observable;

import static com.jakewharton.rxbinding.view.RxView.clicks;
import static com.jakewharton.rxbinding.widget.RxTextView.textChangeEvents;
import static com.memoizrlabs.Shank.provideNew;

public class RmvpAddItemActivity extends AppCompatActivity implements AddItemPresenter.View {

    private AddItemPresenter presenter;
    private View addButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = provideNew(AddItemPresenter.class, this);
        setContentView(R.layout.activity_add_item);
        addButton = findViewById(R.id.add_button);
        presenter.takeView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.dropView(this);
    }
    @Override
    public void setAddButtonEnabled(boolean enabled) {
        addButton.setEnabled(enabled);
    }

    @Override
    public Observable<String> contentTextChanged() {
        return getObservableForTextView(R.id.content);
    }

    @Override
    public Observable<String> detailTextChanged() {
        return getObservableForTextView(R.id.detail);
    }

    @Override
    public Observable<Void> addButtonClicks() {
        return clicks(addButton);
    }

    @Override
    public Observable<Void> cancelButtonClicks() {
        return clicks(findViewById(R.id.cancel_button));
    }

    @NonNull
    private Observable<String> getObservableForTextView(int viewId) {
        return textChangeEvents((TextView) findViewById(viewId))
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString());
    }
}
