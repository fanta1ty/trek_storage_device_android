package sg.com.trekstorageauthentication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.ble.BleServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideBleService(@ApplicationContext context: Context): BleService {
        return BleServiceImpl(context)
    }
}