package com.example.awarehealth.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // IMPORTANT: Update this URL based on your testing environment
    // 
    // For Android Emulator (AVD):
    //   Use: "http://10.0.2.2/AwareHealth/api/"
    //   (10.0.2.2 is a special IP that maps to localhost on your computer)
    //
    // For Physical Android Device:
    //   Use: "http://YOUR_COMPUTER_IP/AwareHealth/api/"
    //   Current detected IP: 172.20.10.2
    //   Example: "http://172.20.10.2/AwareHealth/api/"
    //
    // Note: XAMPP uses port 80 (default HTTP), so no port number needed
    // Make sure your device/emulator is on the same Wi-Fi network as your computer
    
    // For Android Emulator (AVD):
    // private const val BASE_URL = "http://10.0.2.2/AwareHealth/api/" // For Emulator
    // For Physical Android Device:
    private const val BASE_URL = "http://192.168.1.11/AwareHealth/api/" // For Physical Device - Updated IP
    
    // Expose BASE_URL for error messages
    const val BASE_URL_PUBLIC = BASE_URL
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
        .readTimeout(30, TimeUnit.SECONDS) // Read timeout
        .writeTimeout(30, TimeUnit.SECONDS) // Write timeout
        .retryOnConnectionFailure(true) // Retry on connection failure
        .callTimeout(45, TimeUnit.SECONDS) // Total call timeout
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
