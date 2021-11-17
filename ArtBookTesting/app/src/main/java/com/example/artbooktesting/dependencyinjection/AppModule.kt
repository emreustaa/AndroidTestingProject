package com.example.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.artbooktesting.R
import com.example.artbooktesting.api.RetrofitAPI
import com.example.artbooktesting.repo.ArtRepository
import com.example.artbooktesting.repo.ArtRepositoryInterface
import com.example.artbooktesting.roomdb.ArtDao
import com.example.artbooktesting.roomdb.ArtsDatabase
import com.example.artbooktesting.util.Util.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ArtsDatabase::class.java, "ArtBookDB").build()

    @Singleton
    @Provides
    fun injectDao(database: ArtsDatabase) = database.artDao()

    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    var gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun injectRetrofitAPI(): RetrofitAPI {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).client(client)
            .baseUrl(BASE_URL).build().create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )

    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, api: RetrofitAPI) =
        ArtRepository(dao, api) as ArtRepositoryInterface
}