package com.hermanowicz.pantry.repository;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.common.collect.ImmutableList;
import com.hermanowicz.pantry.interfaces.PricingListener;

import java.util.List;

public class PricingRepositoryImpl implements PricingRepository, PurchasesUpdatedListener, BillingClientStateListener {

    private BillingClient billingClient;
    private List<ProductDetails> productDetailsList;
    private PricingListener pricingListener;

    @Override
    public void setPremiumActivationListenerAndBuildBillingClient(Context context, PricingListener pricingListener) {
        this.pricingListener = pricingListener;
        billingClient = BillingClient
                .newBuilder(context)
                .enablePendingPurchases()
                .setListener(this)
                .build();
        billingClient.startConnection(this);
    }

    @Override
    public void setupBillingClient() {
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                .setProductId("premium")
                .setProductType(BillingClient.ProductType.INAPP)
                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(
                params,
                (billingResult, productDetailsList) -> {
                    this.productDetailsList = productDetailsList;
                    if(productDetailsList.size() > 0) {
                        pricingListener.isBillingClientReady();
                        if (isAlreadyOwned(billingResult.getResponseCode())) {
                            pricingListener.activatePremiumFeatures();
                        }
                    }
                });
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchaseList) {
        int responseCode = billingResult.getResponseCode();
        if (ifAvailableToBuy(responseCode, purchaseList) && purchaseList != null) {
            for (Purchase purchase : purchaseList) {
                handlePurchase(purchase);
            }
        }
        if (isAlreadyOwned(responseCode)) {
            pricingListener.activatePremiumFeatures();
        }
    }

    private boolean isAlreadyOwned(int responseCode) {
        return responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED;
    }

    private boolean ifAvailableToBuy(int responseCode, @Nullable List<Purchase> purchaseList) {
        if (responseCode == BillingClient.BillingResponseCode.OK)
            return purchaseList != null;
        else
            return false;
    }

    private void handlePurchase(Purchase purchase) {
        if (isAlreadyOwned(purchase.getPurchaseState())) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                pricingListener.activatePremiumFeatures();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                });
            }
        }
    }

    @Override
    public void initPremiumPurchase(Activity activity) {
        ProductDetails productDetails = productDetailsList.get(0);
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    @Override
    public void onBillingServiceDisconnected() {
        billingClient.startConnection(this);
    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        setupBillingClient();
    }
}