package com.hermanowicz.pantry.module;

import android.content.Context;
import android.content.res.Resources;

import com.hermanowicz.pantry.data.repository.DatabaseBackupRepositoryImpl;
import com.hermanowicz.pantry.data.repository.OwnCategoryRepository;
import com.hermanowicz.pantry.data.repository.OwnCategoryRepositoryImpl;
import com.hermanowicz.pantry.data.repository.PdfDocumentsRepositoryImpl;
import com.hermanowicz.pantry.data.repository.PhotoRepositoryImpl;
import com.hermanowicz.pantry.data.repository.ProductRepositoryImpl;
import com.hermanowicz.pantry.data.repository.SharedPreferencesRepository;
import com.hermanowicz.pantry.data.repository.SharedPreferencesRepositoryImpl;
import com.hermanowicz.pantry.data.repository.StorageLocationRepositoryImpl;
import com.hermanowicz.pantry.ui.database_mode.DatabaseModeUseCaseImpl;
import com.hermanowicz.pantry.ui.edit_product.EditProductUseCaseImpl;
import com.hermanowicz.pantry.ui.filter_product.FilterProductUseCaseImpl;
import com.hermanowicz.pantry.ui.new_category.NewCategoryUseCaseImpl;
import com.hermanowicz.pantry.ui.new_product.NewProductUseCaseImpl;
import com.hermanowicz.pantry.ui.new_storage_location.NewStorageLocationUseCaseImpl;
import com.hermanowicz.pantry.ui.own_categories.OwnCategoriesUseCaseImpl;
import com.hermanowicz.pantry.ui.own_category_detail.OwnCategoryDetailUseCaseImpl;
import com.hermanowicz.pantry.ui.print_qr_codes.PrintQRCodesUseCaseImpl;
import com.hermanowicz.pantry.ui.product.ProductUseCaseImpl;
import com.hermanowicz.pantry.ui.product_details.ProductDetailsUseCaseImpl;
import com.hermanowicz.pantry.ui.products_photo.ProductsPhotoUseCaseImpl;
import com.hermanowicz.pantry.ui.scan_product.ScanProductUseCaseImpl;
import com.hermanowicz.pantry.ui.settings.SettingsUseCaseImpl;
import com.hermanowicz.pantry.ui.storage_location_detail.StorageLocationDetailUseCaseImpl;
import com.hermanowicz.pantry.ui.storage_locations.StorageLocationsUseCaseImpl;
import com.hermanowicz.pantry.util.Notifications;
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
    public NewProductUseCaseImpl provideNewProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        Resources resources = context.getResources();
        Notifications notifications = new Notifications(context);
        return new NewProductUseCaseImpl(productRepository, storageLocationRepository, ownCategoryRepository, resources, notifications);
    }

    @Provides
    @Singleton
    public EditProductUseCaseImpl provideEditProductUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        Resources resources = context.getResources();
        return new EditProductUseCaseImpl(productRepository, storageLocationRepository, ownCategoryRepository, resources);
    }

    @Provides
    @Singleton
    public FilterProductUseCaseImpl provideFilterProductUseCase(@ApplicationContext Context context) {
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new FilterProductUseCaseImpl(storageLocationRepository, ownCategoryRepository);
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
    public OwnCategoryDetailUseCaseImpl provideOwnCategoryDetailUseCase(@ApplicationContext Context context) {
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new OwnCategoryDetailUseCaseImpl(ownCategoryRepository);
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
        PhotoRepositoryImpl photoRepository = new PhotoRepositoryImpl(context);
        Resources resources = context.getResources();
        Notifications notifications = new Notifications(context);
        return new ProductDetailsUseCaseImpl(productRepository, photoRepository, resources, notifications);
    }

    @Provides
    @Singleton
    public ScanProductUseCaseImpl provideScanProductUseCase(@ApplicationContext Context context) {
        SharedPreferencesRepositoryImpl sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(context);
        Resources resources = context.getResources();
        return new ScanProductUseCaseImpl(sharedPreferencesRepository, resources);
    }

    @Provides
    @Singleton
    public SettingsUseCaseImpl provideSettingsUseCase(@ApplicationContext Context context) {
        PremiumAccess premiumAccess = new PremiumAccess(context);
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        OwnCategoryRepositoryImpl ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        DatabaseBackupRepositoryImpl databaseBackupRepository = new DatabaseBackupRepositoryImpl(context);
        SharedPreferencesRepositoryImpl sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(context);
        Notifications notifications = new Notifications(context);
        return new SettingsUseCaseImpl(premiumAccess, productRepository, ownCategoryRepository, storageLocationRepository, databaseBackupRepository, sharedPreferencesRepository, notifications);
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
    public OwnCategoriesUseCaseImpl provideCategoryUseCase(@ApplicationContext Context context) {
        OwnCategoryRepository ownCategoryRepository = new OwnCategoryRepositoryImpl(context);
        return new OwnCategoriesUseCaseImpl(ownCategoryRepository);
    }

    @Provides
    @Singleton
    public StorageLocationsUseCaseImpl provideStorageLocationsUseCase(@ApplicationContext Context context) {
        StorageLocationRepositoryImpl storageLocationRepository = new StorageLocationRepositoryImpl(context);
        return new StorageLocationsUseCaseImpl(storageLocationRepository);
    }

    @Provides
    @Singleton
    public PrintQRCodesUseCaseImpl providePrintQRCodesUseCase(@ApplicationContext Context context) {
        PdfDocumentsRepositoryImpl pdfDocumentsRepository = new PdfDocumentsRepositoryImpl(context);
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(context);
        return new PrintQRCodesUseCaseImpl(pdfDocumentsRepository, sharedPreferencesRepository);
    }

    @Provides
    @Singleton
    public ProductsPhotoUseCaseImpl provideProductsPhotoUseCase(@ApplicationContext Context context) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl(context);
        PhotoRepositoryImpl photoRepository = new PhotoRepositoryImpl(context);
        return new ProductsPhotoUseCaseImpl(productRepository, photoRepository);
    }
}