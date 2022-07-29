package com.hermanowicz.pantry.module;

import android.content.Context;
import android.content.res.Resources;

import com.hermanowicz.pantry.repository.OwnCategoryRepositoryImpl;
import com.hermanowicz.pantry.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.repository.ProductRepositoryImpl;
import com.hermanowicz.pantry.repository.SharedPreferencesRepositoryImpl;
import com.hermanowicz.pantry.repository.StorageLocationRepositoryImpl;
import com.hermanowicz.pantry.ui.category.CategoryUseCaseImpl;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeUseCaseImpl;
import com.hermanowicz.pantry.ui.edit_product.EditProductUseCaseImpl;
import com.hermanowicz.pantry.ui.filter_product.FilterProductUseCaseImpl;
import com.hermanowicz.pantry.ui.my_pantry.MyPantryUseCaseImpl;
import com.hermanowicz.pantry.ui.new_category.NewCategoryUseCaseImpl;
import com.hermanowicz.pantry.ui.new_product.NewProductUseCaseImpl;
import com.hermanowicz.pantry.ui.new_storage_location.NewStorageLocationUseCaseImpl;
import com.hermanowicz.pantry.ui.own_categories.OwnCategoriesUseCaseImpl;
import com.hermanowicz.pantry.ui.own_category_detail.OwnCategoryDetailUseCaseImpl;
import com.hermanowicz.pantry.ui.product.ProductUseCaseImpl;
import com.hermanowicz.pantry.ui.product_details.ProductDetailsUseCaseImpl;
import com.hermanowicz.pantry.ui.scan_product.ScanProductUseCaseImpl;
import com.hermanowicz.pantry.ui.settings.SettingsUseCaseImpl;
import com.hermanowicz.pantry.ui.storage_location_detail.StorageLocationDetailUseCaseImpl;
import com.hermanowicz.pantry.ui.storage_locations.StorageLocationsUseCaseImpl;
import com.hermanowicz.pantry.util.PremiumAccess;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class AppModule {

    @Provides
    @Singleton
    public MyPantryUseCaseImpl provideMyPantryUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        return new MyPantryUseCaseImpl(productRepository);
    }

    @Provides
    @Singleton
    public NewProductUseCaseImpl provideNewProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        Resources resources = context.getResources();
        return new NewProductUseCaseImpl(productRepository, storageLocationRepository, ownCategoryRepository, resources);
    }

    @Provides
    @Singleton
    public EditProductUseCaseImpl provideEditProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new EditProductUseCaseImpl(productRepository, storageLocationRepository, ownCategoryRepository);
    }

    @Provides
    @Singleton
    public FilterProductUseCaseImpl provideFilterProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new FilterProductUseCaseImpl(productRepository, storageLocationRepository, ownCategoryRepository);
    }

    @Provides
    @Singleton
    public NewCategoryUseCaseImpl provideNewCategoryUseCase(@ApplicationContext Context context) {
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new NewCategoryUseCaseImpl(ownCategoryRepository);
    }

    @Provides
    @Singleton
    public NewStorageLocationUseCaseImpl provideNewStorageLocationUseCase(@ApplicationContext Context context) {
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        return new NewStorageLocationUseCaseImpl(storageLocationRepository);
    }

    @Provides
    @Singleton
    public OwnCategoriesUseCaseImpl provideOwnCategoriesUseCase(@ApplicationContext Context context) {
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new OwnCategoriesUseCaseImpl(ownCategoryRepository);
    }

    @Provides
    @Singleton
    public OwnCategoryDetailUseCaseImpl provideOwnCategoryDetailUseCase(@ApplicationContext Context context) {
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new OwnCategoryDetailUseCaseImpl(ownCategoryRepository);
    }

    @Provides
    @Singleton
    public StorageLocationsUseCaseImpl provideStorageLocationsUseCase(@ApplicationContext Context context) {
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        return new StorageLocationsUseCaseImpl(storageLocationRepository);
    }

    @Provides
    @Singleton
    public StorageLocationDetailUseCaseImpl provideStorageLocationDetailUseCase(@ApplicationContext Context context) {
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        return new StorageLocationDetailUseCaseImpl(storageLocationRepository);
    }

    @Provides
    @Singleton
    public ProductDetailsUseCaseImpl provideProductDetailsUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        Resources resources = context.getResources();
        return new ProductDetailsUseCaseImpl(productRepository, resources);
    }

    @Provides
    @Singleton
    public ScanProductUseCaseImpl provideScanProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        return new ScanProductUseCaseImpl(productRepository);
    }

    @Provides
    @Singleton
    public SettingsUseCaseImpl provideSettingsUseCase(@ApplicationContext Context context) {
        PremiumAccess premiumAccess = new PremiumAccess(context);
        return new SettingsUseCaseImpl(premiumAccess);
    }

    @Provides
    @Singleton
    public DatabaseModeUseCaseImpl provideDatabaseModeUseCase(@ApplicationContext Context context) {
        SharedPreferencesRepositoryImpl sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(context);
        return new DatabaseModeUseCaseImpl(sharedPreferencesRepository);
    }

    @Provides
    @Singleton
    public ProductUseCaseImpl provideProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        return new ProductUseCaseImpl(productRepository);
    }

    @Provides
    @Singleton
    public CategoryUseCaseImpl provideCategoryUseCase(@ApplicationContext Context context) {
        OwnCategoryRepository ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new CategoryUseCaseImpl(ownCategoryRepository);
    }
}