package jnkadks.namada.namadaexplorer.dependency_injection

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moandjiezana.toml.Toml
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jnkadks.namada.namadaexplorer.BuildConfig
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.local.TomlLocal
import jnkadks.namada.namadaexplorer.local.TomlLocalImpl
import jnkadks.namada.namadaexplorer.networks.NamadaRedNetwork
import jnkadks.namada.namadaexplorer.networks.OuterRpcNetwork
import jnkadks.namada.namadaexplorer.networks.RpcNetwork
import jnkadks.namada.namadaexplorer.networks.StakepoolNetwork
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManualInject {
    @Provides
    fun registerClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)

    @Provides
    @Singleton
    fun registerGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    @Singleton
    @Provides
    fun registerHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also { interceptor ->
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Singleton
    @Provides
    fun registerRpcNetwork(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        factory: Gson,
        clientBuilder: OkHttpClient.Builder
    ): RpcNetwork {
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(Common.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(clientBuilder.build())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun registerOuterRpcNetwork(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        factory: Gson,
        clientBuilder: OkHttpClient.Builder
    ): OuterRpcNetwork {
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(Common.OUTER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(clientBuilder.build())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun registerRedNetwork(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        factory: Gson,
        clientBuilder: OkHttpClient.Builder
    ): NamadaRedNetwork {
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(Common.RED_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(clientBuilder.build())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun registerStakepoolNetwork(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        factory: Gson,
        clientBuilder: OkHttpClient.Builder
    ): StakepoolNetwork {
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(Common.STAKEPOOL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(clientBuilder.build())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun registerTomlLocal(
        @ApplicationContext context: Context
    ): TomlLocal {
        val toml = Toml()
        return TomlLocalImpl(
            toml,
            context
        )
    }
}