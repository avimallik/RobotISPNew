package com.armavi_bsd.robotispnew.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.armavi_bsd.robotispnew.apiService.ApiService;
import com.armavi_bsd.robotispnew.model.AgentModel;
import com.armavi_bsd.robotispnew.model.BillCollectionModel;
import com.armavi_bsd.robotispnew.retroFitClient.RretrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillCollectionViewModel extends ViewModel {
    private MutableLiveData<List<BillCollectionModel>> billCollections = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    RretrofitClient retrofitClient = new RretrofitClient();

    public LiveData<List<BillCollectionModel>> getBillCollection() {
        return billCollections;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchBillCollection(){
        isLoading.setValue(true);
        ApiService apiService = retrofitClient.getData().create(ApiService.class);
        apiService.getBillCollection().enqueue(new Callback<List<BillCollectionModel>>() {
            @Override
            public void onResponse(Call<List<BillCollectionModel>> call, Response<List<BillCollectionModel>> response) {
                if (response.isSuccessful()) {
                    billCollections.setValue(response.body());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<BillCollectionModel>> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }

}
