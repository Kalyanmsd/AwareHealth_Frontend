package com.example.awarehealth.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "AwareHealthPrefs"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_EMAIL_PATIENT = "saved_email_patient"
        private const val KEY_PASSWORD_PATIENT = "saved_password_patient"
        private const val KEY_EMAIL_DOCTOR = "saved_email_doctor"
        private const val KEY_PASSWORD_DOCTOR = "saved_password_doctor"
        private const val KEY_USER_TYPE = "saved_user_type"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_DOCTOR_ID = "doctor_id"  // For doctors - stores doctor_id from doctors table
    }
    
    /**
     * Save login credentials for Remember Me feature
     */
    fun saveCredentials(email: String, password: String, userType: String, rememberMe: Boolean) {
        val editor = prefs.edit()
        
        if (rememberMe) {
            if (userType == "doctor") {
                editor.putString(KEY_EMAIL_DOCTOR, email)
                editor.putString(KEY_PASSWORD_DOCTOR, password)
            } else {
                editor.putString(KEY_EMAIL_PATIENT, email)
                editor.putString(KEY_PASSWORD_PATIENT, password)
            }
            editor.putBoolean(KEY_REMEMBER_ME, true)
            editor.putString(KEY_USER_TYPE, userType)
        } else {
            // Clear saved credentials if Remember Me is unchecked
            clearCredentials(userType)
            editor.putBoolean(KEY_REMEMBER_ME, false)
        }
        
        editor.apply()
    }
    
    /**
     * Get saved email for a user type
     */
    fun getSavedEmail(userType: String): String? {
        return if (userType == "doctor") {
            prefs.getString(KEY_EMAIL_DOCTOR, null)
        } else {
            prefs.getString(KEY_EMAIL_PATIENT, null)
        }
    }
    
    /**
     * Get saved password for a user type
     */
    fun getSavedPassword(userType: String): String? {
        return if (userType == "doctor") {
            prefs.getString(KEY_PASSWORD_DOCTOR, null)
        } else {
            prefs.getString(KEY_PASSWORD_PATIENT, null)
        }
    }
    
    /**
     * Check if Remember Me is enabled
     */
    fun isRememberMeEnabled(): Boolean {
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }
    
    /**
     * Get saved user type
     */
    fun getSavedUserType(): String? {
        return prefs.getString(KEY_USER_TYPE, null)
    }
    
    /**
     * Check if credentials are saved for a user type
     */
    fun hasSavedCredentials(userType: String): Boolean {
        val email = getSavedEmail(userType)
        val password = getSavedPassword(userType)
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }
    
    /**
     * Clear saved credentials for a user type
     */
    fun clearCredentials(userType: String) {
        val editor = prefs.edit()
        if (userType == "doctor") {
            editor.remove(KEY_EMAIL_DOCTOR)
            editor.remove(KEY_PASSWORD_DOCTOR)
        } else {
            editor.remove(KEY_EMAIL_PATIENT)
            editor.remove(KEY_PASSWORD_PATIENT)
        }
        editor.apply()
    }
    
    /**
     * Clear all saved credentials
     */
    fun clearAllCredentials() {
        val editor = prefs.edit()
        editor.remove(KEY_EMAIL_PATIENT)
        editor.remove(KEY_PASSWORD_PATIENT)
        editor.remove(KEY_EMAIL_DOCTOR)
        editor.remove(KEY_PASSWORD_DOCTOR)
        editor.remove(KEY_REMEMBER_ME)
        editor.remove(KEY_USER_TYPE)
        editor.remove(KEY_IS_LOGGED_IN)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_EMAIL)
        editor.remove(KEY_USER_PHONE)
        editor.apply()
    }
    
    /**
     * Save user session data
     */
    fun saveUserSession(userData: UserData) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, userData.id)
        editor.putString(KEY_USER_NAME, userData.name)
        editor.putString(KEY_USER_EMAIL, userData.email)
        editor.putString(KEY_USER_TYPE, userData.userType)
        userData.phone?.let { editor.putString(KEY_USER_PHONE, it) }
        userData.doctorId?.let { editor.putString(KEY_DOCTOR_ID, it) }
        editor.apply()
    }
    
    /**
     * Get saved user session
     */
    fun getSavedUserSession(): UserData? {
        if (!prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            return null
        }
        
        val id = prefs.getString(KEY_USER_ID, null) ?: return null
        val name = prefs.getString(KEY_USER_NAME, null) ?: return null
        val email = prefs.getString(KEY_USER_EMAIL, null) ?: return null
        val userType = prefs.getString(KEY_USER_TYPE, null) ?: return null
        val phone = prefs.getString(KEY_USER_PHONE, null)
        val doctorId = prefs.getString(KEY_DOCTOR_ID, null)
        
        return UserData(
            id = id,
            name = name,
            email = email,
            userType = userType,
            phone = phone,
            doctorId = doctorId
        )
    }
    
    /**
     * Get saved doctor ID
     */
    fun getDoctorId(): String? {
        return prefs.getString(KEY_DOCTOR_ID, null)
    }
    
    /**
     * Save doctor ID
     */
    fun saveDoctorId(doctorId: String) {
        val editor = prefs.edit()
        editor.putString(KEY_DOCTOR_ID, doctorId)
        editor.apply()
    }
    
    /**
     * Clear user session
     */
    fun clearUserSession() {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_EMAIL)
        editor.remove(KEY_USER_PHONE)
        editor.apply()
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}

