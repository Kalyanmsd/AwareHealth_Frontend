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
    // Update this IP to match your computer's current IP address
    // Find your IP: Run "ipconfig" in Command Prompt and look for "IPv4 Address"
    private const val BASE_URL = "http://172.20.10.2/AwareHealth/api/" // For Physical Device - Updated IP
    
    // Flask AI API Base URL (Python Flask server on port 5000)
    // For Physical Android Device:
    private const val FLASK_BASE_URL = "http://172.20.10.2:5000/" // Flask AI API - Updated IP
    // For Android Emulator (AVD):
    // private const val FLASK_BASE_URL = "http://10.0.2.2:5000/" // Flask AI API for Emulator
    
    // Expose BASE_URL for error messages
    const val BASE_URL_PUBLIC = BASE_URL
    const val FLASK_BASE_URL_PUBLIC = FLASK_BASE_URL
    
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
    
    // PHP API Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // Flask AI API Retrofit instance
    private val flaskRetrofit = Retrofit.Builder()
        .baseUrl(FLASK_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // PHP API Service
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    
    // Flask AI API Service
    val flaskApiService: FlaskApiService = flaskRetrofit.create(FlaskApiService::class.java)
}
