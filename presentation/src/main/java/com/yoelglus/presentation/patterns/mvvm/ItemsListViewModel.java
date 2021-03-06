package com.yoelglus.presentation.patterns.mvvm;

import com.yoelglus.presentation.patterns.data.ItemsRepository;
import com.yoelglus.presentation.patterns.mapper.ItemModelsMapper;
import com.yoelglus.presentation.patterns.model.ItemModel;
import com.yoelglus.presentation.patterns.navigator.Navigator;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

class ItemsListViewModel extends AbstractViewModel {

    private ItemsRepository itemsRepository;
    private Navigator navigator;
    private PublishSubject<List<ItemModel>> itemModelsSubject = PublishSubject.create();
    private Subscription getItemsSubscription;

    ItemsListViewModel(ItemsRepository itemsRepository, Navigator navigator) {
        this.itemsRepository = itemsRepository;
        this.navigator = navigator;
    }

    @Override
    public void onStart() {
        super.onStart();
        getItemsSubscription = itemsRepository.getItems()
                .map(ItemModelsMapper::map)
                .subscribe(itemModelsSubject);
    }

    @Override
    public void onStop() {
        super.onStop();
        getItemsSubscription.unsubscribe();
    }

    Observable<List<ItemModel>> itemModels() {
        return itemModelsSubject.asObservable();
    }

    void addItemClicked() {
        navigator.navigateToAddItem();
    }

}
