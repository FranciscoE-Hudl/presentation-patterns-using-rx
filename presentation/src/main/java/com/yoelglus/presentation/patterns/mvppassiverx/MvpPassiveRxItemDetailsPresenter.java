package com.yoelglus.presentation.patterns.mvppassiverx;

import com.yoelglus.presentation.patterns.domain.usecases.GetItem;
import com.yoelglus.presentation.patterns.entities.Item;
import com.yoelglus.presentation.patterns.model.ItemModel;
import com.yoelglus.presentation.patterns.presenter.AbstractPresenter;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class MvpPassiveRxItemDetailsPresenter extends AbstractPresenter<MvpPassiveRxItemDetailsPresenter.View> {

    private GetItem mGetItem;
    private Subscription mGetItemSubscription;

    public MvpPassiveRxItemDetailsPresenter(GetItem getItem) {
        mGetItem = getItem;
    }


    @Override
    public void onTakeView() {
        mGetItemSubscription = mGetItem.execute().map(new Func1<Item, ItemModel>() {
            @Override
            public ItemModel call(Item item) {
                return ItemModel.from(item);
            }
        }).subscribe(mView.showItem());
    }

    @Override
    public void onDropView() {
        mGetItemSubscription.unsubscribe();
    }

    public interface View {
        Action1<ItemModel> showItem();
    }
}
