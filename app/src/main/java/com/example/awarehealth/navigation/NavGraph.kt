package com.example.awarehealth.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import android.util.Log
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Column
import com.example.awarehealth.ui.components.Header
import com.example.awarehealth.ui.screens.*
import com.example.awarehealth.ui.doctor.DoctorHome
import com.example.awarehealth.ui.doctor.DoctorMenu
import com.example.awarehealth.ui.doctor.PendingRequests
import com.example.awarehealth.ui.doctor.ManageAppointment
import com.example.awarehealth.ui.doctor.AcceptedRequests
import com.example.awarehealth.ui.doctor.CriticalAppointments
import com.example.awarehealth.ui.doctor.AppointmentDetails
import com.example.awarehealth.ui.doctor.DoctorStatistics
import com.example.awarehealth.ui.doctor.DoctorProfile
import com.example.awarehealth.ui.doctor.EditDoctorProfile
import com.example.awarehealth.ui.doctor.DoctorNotifications
import com.example.awarehealth.data.ApiService
import com.example.awarehealth.data.AppRepository

// Local Appointment class for patient screens (has doctorName, no patientId/symptoms)
data class PatientAppointment(
    val id: String,
    val doctorName: String,
    val patientName: String,
    val date: String,
    val time: String,
    val status: String
)

// Data class to hold appointment info for confirmation screen
data class BookedAppointmentInfo(
    val doctorName: String,
    val doctorSpecialty: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val location: String
)

@Composable
fun AwareHealthNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    apiService: ApiService? = null
) {
    val repository = remember { AppRepository(apiService) }
    val user = remember { mutableStateOf(User(name = "John Smith", id = "")) }
    val userData = remember { mutableStateOf<com.example.awarehealth.data.UserData?>(null) }
    // Patient appointments - using local PatientAppointment (has doctorName)
    val patientAppointments = remember { 
        mutableStateListOf<PatientAppointment>()
    }
    var selectedDoctor by rememberSaveable { mutableStateOf<Doctor?>(null) }
    var selectedDateTime by rememberSaveable { mutableStateOf<SelectedDateTime?>(null) }
    var selectedUserType by rememberSaveable { mutableStateOf("") }
    
    // Store latest booked appointment data for confirmation screen
    var latestAppointmentData by remember { 
        mutableStateOf<BookedAppointmentInfo?>(null) 
    }
    
    // Coroutine scope for API calls
    val coroutineScope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = startDestination) {
        
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(Screen.UserTypeSelection.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        composable(Screen.UserTypeSelection.route) {
            UserTypeSelection { userType ->
                selectedUserType = userType
                navController.navigate(Screen.Login.createRoute(userType))
            }
        }

        composable(
            route = Screen.Login.route,
            arguments = listOf(navArgument("userType") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: ""
            LoginScreen(
                repository = repository,
                userType = userType,
                onLoginSuccess = { userDataFromLogin ->
                    // Store user data
                    userData.value = userDataFromLogin
                    user.value = User(
                        id = userDataFromLogin.id,
                        name = userDataFromLogin.name
                    )
                    
                    if (userType == "doctor") {
                        navController.navigate(Screen.DoctorHome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.PatientHome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onRegister = { navController.navigate(Screen.Register.createRoute(userType)) },
                onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onBack = { 
                    navController.navigate(Screen.UserTypeSelection.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Register.route,
            arguments = listOf(navArgument("userType") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: ""
            RegisterScreen(
                repository = repository,
                userType = userType,
                onRegisterSuccess = { userDataFromRegister ->
                    // Store user data
                    userData.value = userDataFromRegister
                    user.value = User(
                        id = userDataFromRegister.id,
                        name = userDataFromRegister.name
                    )
                    
                    // Navigate to appropriate home screen after successful registration
                    if (userType == "doctor") {
                        navController.navigate(Screen.DoctorHome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.PatientHome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLogin = { navController.popBackStack() },
                onOTPVerified = { email, otp ->
                    // Navigate to reset password with email and OTP (OTP is hidden from user)
                    navController.navigate("${Screen.ResetPassword.route}?email=${android.net.Uri.encode(email)}&otp=${android.net.Uri.encode(otp)}") {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                },
                repository = repository
            )
        }

        composable(
            route = "${Screen.ResetPassword.route}?email={email}&otp={otp}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType; defaultValue = "" },
                navArgument("otp") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            ResetPasswordScreen(
                email = email,
                otp = otp,
                onBackToLogin = {
                navController.navigate(Screen.Login.createRoute(selectedUserType)) {
                    popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                }
                },
                repository = repository
            )
        }

        composable(Screen.PatientHome.route) {
            Column {
                Header(showMenu = true, showNotifications = true, onMenuClick = { 
                    navController.navigate(Screen.PatientMenu.route) 
                }, onNotificationClick = {
                    navController.navigate(Screen.AppointmentAccepted.route)
                })
                PatientHomeScreen(
                    user = user.value,
                    appointments = patientAppointments.map { appt ->
                        // PatientHomeScreen.Appointment(id, doctorName, patientName, date, time, status)
                        Appointment(
                            id = appt.id,
                            doctorName = appt.doctorName,
                            patientName = appt.patientName,
                            date = appt.date,
                            time = appt.time,
                            status = appt.status
                        )
                    },
                    onMenuClick = { navController.navigate(Screen.PatientMenu.route) },
                    onBookAppointment = { navController.navigate(Screen.SelectDoctor.route) },
                    onChatbot = { navController.navigate(Screen.ChatbotMain.route) },
                    onHealthInfo = { navController.navigate(Screen.DiseaseList.route) },
                    onMyAppointments = { navController.navigate(Screen.MyAppointments.route) },
                    onViewAll = { navController.navigate(Screen.MyAppointments.route) }
                )
            }
        }

        composable(Screen.PatientMenu.route) {
            PatientMenu(
                userName = user.value.name,
                onClose = { navController.popBackStack() },
                onHome = {
                    navController.navigate(Screen.PatientHome.route) {
                        popUpTo(Screen.PatientMenu.route) { inclusive = true }
                    }
                },
                onChatbot = {
                    navController.navigate(Screen.ChatbotMain.route) {
                        popUpTo(Screen.PatientMenu.route) { inclusive = true }
                    }
                },
                onAppointments = {
                    navController.navigate(Screen.MyAppointments.route) {
                        popUpTo(Screen.PatientMenu.route) { inclusive = true }
                    }
                },
                onHealthInfo = {
                    navController.navigate(Screen.DiseaseList.route) {
                        popUpTo(Screen.PatientMenu.route) { inclusive = true }
                    }
                },
                onNotifications = {
                    navController.navigate(Screen.AppointmentAccepted.route) {
                        popUpTo(Screen.PatientMenu.route) { inclusive = true }
                    }
                },
                onProfile = { navController.navigate(Screen.PatientProfile.route) },
                onLogout = {
                    navController.navigate(Screen.Login.createRoute(selectedUserType)) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PatientProfile.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                PatientProfileScreen(
                    patient = Patient(
                        name = userData.value?.name ?: user.value.name,
                        email = userData.value?.email ?: "john@example.com",
                        phone = userData.value?.phone ?: "+1234567890"
                    ),
                    totalAppointments = patientAppointments.size,
                    completedAppointments = patientAppointments.count { it.status == "accepted" },
                    onEditProfile = { 
                        navController.navigate(Screen.EditPatientProfile.route)
                    }
                )
            }
        }
        
        composable(Screen.EditPatientProfile.route) {
            userData.value?.let { currentUserData ->
                EditPatientProfileScreen(
                    repository = repository,
                    userData = currentUserData,
                    onProfileUpdated = { updatedUser ->
                        // Update user data state
                        userData.value = updatedUser
                        user.value = User(
                            id = updatedUser.id,
                            name = updatedUser.name
                        )
                        // Navigate back to profile
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            } ?: run {
                // If no user data, go back
                navController.popBackStack()
            }
        }

        composable(Screen.SelectDoctor.route) {
            Column {
                Header(showBack = true, showNotifications = true, onBackClick = { navController.popBackStack() }, onNotificationClick = {
                    navController.navigate(Screen.AppointmentAccepted.route)
                })
                SelectDoctorScreen(
                    onBack = { navController.popBackStack() },
                    onDoctorSelected = { doctor ->
                        selectedDoctor = doctor
                        navController.navigate(Screen.SelectDateTime.route)
                    },
                    repository = repository
                )
            }
        }

        composable(Screen.SelectDateTime.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                SelectDateTimeScreen(
                    selectedDoctor = selectedDoctor,
                    onBack = { navController.popBackStack() },
                    onContinue = { dateTime ->
                        selectedDateTime = dateTime
                        navController.navigate(Screen.AppointmentSummary.route)
                    }
                )
            }
        }

        composable(Screen.AppointmentSummary.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                AppointmentSummaryScreen(
                    selectedDoctor = selectedDoctor,
                    selectedDateTime = selectedDateTime,
                    user = user.value,
                    onBack = { navController.popBackStack() },
                    onConfirm = { symptoms ->
                        // Get user email (prefer from userData, fallback to default)
                        val userEmail = userData.value?.email ?: "patient@example.com"
                        val doctorId = selectedDoctor?.id?.toIntOrNull()
                        val date = selectedDateTime?.date ?: ""
                        val time = selectedDateTime?.time ?: ""
                        val patientName = user.value.name
                        
                        // Convert time format if needed (HH:MM to HH:MM:SS)
                        val formattedTime = if (time.length == 5) "$time:00" else time
                        
                        if (doctorId == null || date.isEmpty() || formattedTime.isEmpty()) {
                            // Fallback to local storage if data not available
                            val appointmentId = System.currentTimeMillis().toString()
                            val doctorName = selectedDoctor?.name ?: ""
                            
                            patientAppointments.add(
                                PatientAppointment(
                                    id = appointmentId,
                                    doctorName = doctorName,
                                    patientName = patientName,
                                    date = date,
                                    time = time,
                                    status = "pending"
                                )
                            )
                            navController.navigate(Screen.AppointmentSuccess.route) {
                                popUpTo(Screen.SelectDoctor.route) { inclusive = true }
                            }
                        } else {
                            // Create appointment via NEW API endpoint
                            coroutineScope.launch {
                                try {
                                    val request = com.example.awarehealth.data.BookAppointmentRequest(
                                        patientEmail = userEmail,
                                        doctorId = doctorId.toString(),
                                        appointmentDate = date,
                                        appointmentTime = formattedTime,
                                        patientName = patientName
                                    )
                                    
                                    val response = repository.bookAppointment(request)
                                    
                                    if (response?.isSuccessful == true && response.body()?.success == true) {
                                        val appointmentData = response.body()?.appointment
                                        
                                        // Log the response for debugging
                                        Log.d("NavGraph", "API Response - Success: ${response.body()?.success}")
                                        Log.d("NavGraph", "Appointment Data: $appointmentData")
                                        
                                        // Use appointment data from API response with fallbacks
                                        val appointmentId = appointmentData?.id?.toString() ?: System.currentTimeMillis().toString()
                                        
                                        // Extract doctor name - check both API response and selectedDoctor
                                        val doctorName = when {
                                            !appointmentData?.doctor_name.isNullOrEmpty() -> appointmentData!!.doctor_name!!
                                            !selectedDoctor?.name.isNullOrEmpty() -> selectedDoctor!!.name
                                            else -> "Dr. Doctor"
                                        }
                                        
                                        val doctorSpecialty = when {
                                            !appointmentData?.doctor_specialization.isNullOrEmpty() -> appointmentData!!.doctor_specialization!!
                                            !selectedDoctor?.specialty.isNullOrEmpty() -> selectedDoctor!!.specialty!!
                                            else -> "General Physician"
                                        }
                                        
                                        val appointmentDate = when {
                                            !appointmentData?.appointment_date.isNullOrEmpty() -> appointmentData!!.appointment_date!!
                                            !date.isEmpty() -> date
                                            else -> ""
                                        }
                                        
                                        val appointmentTime = when {
                                            !appointmentData?.appointment_time_display.isNullOrEmpty() -> appointmentData!!.appointment_time_display!!
                                            !time.isEmpty() -> time
                                            else -> ""
                                        }
                                        
                                        val location = when {
                                            !appointmentData?.location.isNullOrEmpty() -> appointmentData!!.location!!
                                            !selectedDoctor?.location.isNullOrEmpty() -> selectedDoctor!!.location!!
                                            else -> "Saveetha Hospital"
                                        }
                                        
                                        // Log extracted values
                                        Log.d("NavGraph", "Extracted Values:")
                                        Log.d("NavGraph", "  Doctor Name: $doctorName")
                                        Log.d("NavGraph", "  Doctor Specialty: $doctorSpecialty")
                                        Log.d("NavGraph", "  Date: $appointmentDate")
                                        Log.d("NavGraph", "  Time: $appointmentTime")
                                        Log.d("NavGraph", "  Location: $location")
                                        
                                        patientAppointments.add(
                                            PatientAppointment(
                                                id = appointmentId,
                                                doctorName = doctorName,
                                                patientName = patientName,
                                                date = appointmentDate,
                                                time = appointmentTime,
                                                status = appointmentData?.status ?: "pending"
                                            )
                                        )
                                        
                                        Log.d("NavGraph", "Appointment created successfully - Doctor: $doctorName")
                                        
                                        // Store appointment data in state
                                        latestAppointmentData = BookedAppointmentInfo(
                                            doctorName = doctorName,
                                            doctorSpecialty = doctorSpecialty,
                                            appointmentDate = appointmentDate,
                                            appointmentTime = appointmentTime,
                                            location = location
                                        )
                                        
                                        // Navigate to AppointmentAccepted with appointment data
                                        val route = Screen.AppointmentAccepted.createRoute(
                                            doctorName = doctorName,
                                            doctorSpecialty = doctorSpecialty,
                                            appointmentDate = appointmentDate,
                                            appointmentTime = appointmentTime,
                                            location = location
                                        )
                                        
                                        Log.d("NavGraph", "Navigating to route: $route")
                                        
                                        navController.navigate(route) {
                                            popUpTo(Screen.SelectDoctor.route) { inclusive = true }
                                        }
                                    } else {
                                        val errorMsg = response?.body()?.message ?: "Network error"
                                        
                                        Log.e("NavGraph", "Failed to create appointment: $errorMsg")
                                        
                                        // Fallback: still navigate but show error later
                                        // For now, we'll proceed with local storage as fallback
                                        val appointmentId = System.currentTimeMillis().toString()
                                        patientAppointments.add(
                                            PatientAppointment(
                                                id = appointmentId,
                                                doctorName = selectedDoctor?.name ?: "",
                                                patientName = patientName,
                                                date = date,
                                                time = time,
                                                status = "pending"
                                            )
                                        )
                                        navController.navigate(Screen.AppointmentSuccess.route) {
                                            popUpTo(Screen.SelectDoctor.route) { inclusive = true }
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("NavGraph", "Exception creating appointment: ${e.message}", e)
                                    
                                    // Fallback to local storage on error
                                    val appointmentId = System.currentTimeMillis().toString()
                                    patientAppointments.add(
                                        PatientAppointment(
                                            id = appointmentId,
                                            doctorName = selectedDoctor?.name ?: "",
                                            patientName = patientName,
                                            date = date,
                                            time = time,
                                            status = "pending"
                                        )
                                    )
                                    navController.navigate(Screen.AppointmentSuccess.route) {
                                        popUpTo(Screen.SelectDoctor.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        composable(Screen.AppointmentSuccess.route) {
            AppointmentSuccessScreen(
                onGoHome = {
                    navController.navigate(Screen.PatientHome.route) {
                        popUpTo(Screen.AppointmentSuccess.route) { inclusive = true }
                    }
                },
                onMyAppointments = {
                    navController.navigate(Screen.MyAppointments.route) {
                        popUpTo(Screen.AppointmentSuccess.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MyAppointments.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                MyAppointmentsScreen(
                    user = user.value,
                    appointments = patientAppointments.map { appt ->
                        // MyAppointmentsScreen uses PatientHomeScreen.Appointment(id, doctorName, patientName, date, time, status)
                        Appointment(
                            id = appt.id,
                            doctorName = appt.doctorName,
                            patientName = appt.patientName,
                            date = appt.date,
                            time = appt.time,
                            status = appt.status
                        )
                    },
                    onBack = { navController.popBackStack() },
                    onAcceptedClick = { navController.navigate(Screen.AppointmentAccepted.route) }
                )
            }
        }

        composable(
            route = Screen.AppointmentAccepted.route,
            arguments = listOf(
                navArgument("doctorName") { type = NavType.StringType; defaultValue = "" },
                navArgument("doctorSpecialty") { type = NavType.StringType; defaultValue = "" },
                navArgument("appointmentDate") { type = NavType.StringType; defaultValue = "" },
                navArgument("appointmentTime") { type = NavType.StringType; defaultValue = "" },
                navArgument("location") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val doctorName = try {
                java.net.URLDecoder.decode(backStackEntry.arguments?.getString("doctorName") ?: "", "UTF-8")
            } catch (e: Exception) { backStackEntry.arguments?.getString("doctorName") ?: "" }
            val doctorSpecialty = try {
                java.net.URLDecoder.decode(backStackEntry.arguments?.getString("doctorSpecialty") ?: "", "UTF-8")
            } catch (e: Exception) { backStackEntry.arguments?.getString("doctorSpecialty") ?: "" }
            val appointmentDate = try {
                java.net.URLDecoder.decode(backStackEntry.arguments?.getString("appointmentDate") ?: "", "UTF-8")
            } catch (e: Exception) { backStackEntry.arguments?.getString("appointmentDate") ?: "" }
            val appointmentTime = try {
                java.net.URLDecoder.decode(backStackEntry.arguments?.getString("appointmentTime") ?: "", "UTF-8")
            } catch (e: Exception) { backStackEntry.arguments?.getString("appointmentTime") ?: "" }
            val location = try {
                java.net.URLDecoder.decode(backStackEntry.arguments?.getString("location") ?: "", "UTF-8")
            } catch (e: Exception) { backStackEntry.arguments?.getString("location") ?: "" }
            
            // Log all extracted values
            Log.d("NavGraph", "=== AppointmentAccepted Screen Parameters ===")
            Log.d("NavGraph", "Doctor Name: '$doctorName' (isEmpty: ${doctorName.isEmpty()})")
            Log.d("NavGraph", "Doctor Specialty: '$doctorSpecialty' (isEmpty: ${doctorSpecialty.isEmpty()})")
            Log.d("NavGraph", "Appointment Date: '$appointmentDate' (isEmpty: ${appointmentDate.isEmpty()})")
            Log.d("NavGraph", "Appointment Time: '$appointmentTime' (isEmpty: ${appointmentTime.isEmpty()})")
            Log.d("NavGraph", "Location: '$location' (isEmpty: ${location.isEmpty()})")
            Log.d("NavGraph", "Latest Appointment Data: $latestAppointmentData")
            
            // Clean values - remove placeholder-like strings
            fun cleanValue(value: String): String {
                if (value.isEmpty()) return ""
                // Remove placeholder-like patterns
                if (value.startsWith("{") && value.endsWith("}")) return ""
                if (value.contains("{") || value.contains("}")) return ""
                return value.trim()
            }
            
            val cleanDoctorName = cleanValue(doctorName)
            val cleanDoctorSpecialty = cleanValue(doctorSpecialty)
            val cleanAppointmentDate = cleanValue(appointmentDate)
            val cleanAppointmentTime = cleanValue(appointmentTime)
            val cleanLocation = cleanValue(location)
            
            // Use route parameters if available, otherwise fall back to stored state
            val finalDoctorName = when {
                cleanDoctorName.isNotEmpty() -> cleanDoctorName
                latestAppointmentData?.doctorName?.isNotEmpty() == true -> latestAppointmentData!!.doctorName
                selectedDoctor?.name?.isNotEmpty() == true -> selectedDoctor!!.name
                else -> ""
            }
            
            val finalDoctorSpecialty = when {
                cleanDoctorSpecialty.isNotEmpty() -> cleanDoctorSpecialty
                latestAppointmentData?.doctorSpecialty?.isNotEmpty() == true -> latestAppointmentData!!.doctorSpecialty
                selectedDoctor?.specialty?.isNotEmpty() == true -> selectedDoctor!!.specialty
                else -> ""
            }
            
            val finalAppointmentDate = when {
                cleanAppointmentDate.isNotEmpty() -> cleanAppointmentDate
                latestAppointmentData?.appointmentDate?.isNotEmpty() == true -> latestAppointmentData!!.appointmentDate
                selectedDateTime?.date?.isNotEmpty() == true -> selectedDateTime!!.date
                else -> ""
            }
            
            val finalAppointmentTime = when {
                cleanAppointmentTime.isNotEmpty() -> cleanAppointmentTime
                latestAppointmentData?.appointmentTime?.isNotEmpty() == true -> latestAppointmentData!!.appointmentTime
                selectedDateTime?.time?.isNotEmpty() == true -> selectedDateTime!!.time
                else -> ""
            }
            
            val finalLocation = when {
                cleanLocation.isNotEmpty() -> cleanLocation
                latestAppointmentData?.location?.isNotEmpty() == true -> latestAppointmentData!!.location
                selectedDoctor?.location?.isNotEmpty() == true -> selectedDoctor!!.location
                else -> ""
            }
            
            Log.d("NavGraph", "=== Final Values to Display ===")
            Log.d("NavGraph", "Final Doctor Name: '$finalDoctorName'")
            Log.d("NavGraph", "Final Doctor Specialty: '$finalDoctorSpecialty'")
            Log.d("NavGraph", "Final Appointment Date: '$finalAppointmentDate'")
            Log.d("NavGraph", "Final Appointment Time: '$finalAppointmentTime'")
            Log.d("NavGraph", "Final Location: '$finalLocation'")
            
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                AppointmentAcceptedScreen(
                    doctorName = finalDoctorName.takeIf { it.isNotEmpty() },
                    doctorSpecialty = finalDoctorSpecialty.takeIf { it.isNotEmpty() },
                    appointmentDate = finalAppointmentDate.takeIf { it.isNotEmpty() },
                    appointmentTime = finalAppointmentTime.takeIf { it.isNotEmpty() },
                    location = finalLocation.takeIf { it.isNotEmpty() },
                    onBack = { navController.popBackStack() },
                    onViewAll = {
                        navController.navigate(Screen.MyAppointments.route) {
                            popUpTo(Screen.AppointmentAccepted.route) { inclusive = true }
                        }
                    },
                    onGoHome = {
                        navController.navigate(Screen.PatientHome.route) {
                            popUpTo(Screen.AppointmentAccepted.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.AppointmentRejected.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                AppointmentRejectedScreen(
                    onBack = { navController.popBackStack() },
                    onBookAgain = {
                        navController.navigate(Screen.SelectDoctor.route) {
                            popUpTo(Screen.AppointmentRejected.route) { inclusive = true }
                        }
                    },
                    onGoHome = {
                        navController.navigate(Screen.PatientHome.route) {
                            popUpTo(Screen.AppointmentRejected.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.ChatbotMain.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                ChatbotMainScreen(
                    onBack = { navController.popBackStack() },
                    onOpenChat = { id -> navController.navigate(Screen.ChatWindow.createRoute(id)) },
                    onNewChat = { navController.navigate(Screen.ChatWindow.createRoute("new")) },
                    onSettings = { navController.navigate(Screen.ChatbotSettings.route) }
                )
            }
        }

        composable(
            route = Screen.ChatWindow.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType; defaultValue = "new" })
        ) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                ChatWindowScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                    onDaysQuestion = {
                        navController.navigate(Screen.DaysQuestion.route)
                    },
                    onQuickReplies = {
                        navController.navigate(Screen.QuickReplies.route)
                    },
                    onDiseaseResponse = {
                        navController.navigate(Screen.DiseaseResponse.route)
                    },
                    onBookingPrompt = {
                        navController.navigate(Screen.BookingPrompt.route)
                    },
                    onNavigateToSelectDoctor = {
                        // Navigate directly to select doctor screen for booking
                        navController.navigate(Screen.SelectDoctor.route) {
                            // Clear chat window from back stack so user can't go back to it
                            popUpTo(Screen.ChatWindow.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.ChatbotSettings.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                ChatbotSettingsScreen(
                    onBack = { navController.popBackStack() },
                    onTerms = { /* Handle terms */ },
                    onHelp = { /* Handle help */ }
                )
            }
        }

        composable(Screen.DaysQuestion.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                DaysQuestionScreen(
                    symptoms = "",
                    onBack = { navController.popBackStack() },
                    onContinue = { days ->
                        navController.navigate(Screen.DiseaseResponse.route)
                    }
                )
            }
        }

        composable(Screen.QuickReplies.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                QuickRepliesScreen(
                    onBack = { navController.popBackStack() },
                    onQuickReply = { reply ->
                        navController.navigate(Screen.ChatWindow.createRoute("new"))
                    },
                    onCustomChat = {
                        navController.navigate(Screen.ChatWindow.createRoute("new"))
                    }
                )
            }
        }

        composable(Screen.DiseaseResponse.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                DiseaseResponseScreen(
                    onBack = { navController.popBackStack() },
                    onPrecautions = {
                        navController.navigate(Screen.PrecautionSteps.route)
                    },
                    onMedicines = {
                        navController.navigate(Screen.SuggestedMedicines.route)
                    },
                    onHospitals = {
                        navController.navigate(Screen.NearbyHospital.route)
                    }
                )
            }
        }

        composable(Screen.BookingPrompt.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                BookingPromptScreen(
                    hospitalName = null,
                    onBack = { navController.popBackStack() },
                    onBookAppointment = {
                        navController.navigate(Screen.SelectDoctor.route)
                    },
                    onContinueChat = {
                        navController.navigate(Screen.ChatWindow.createRoute("new"))
                    }
                )
            }
        }

        composable(Screen.NearbyHospital.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                NearbyHospitalsScreen(
                    onBack = { navController.popBackStack() },
                    onBookAppointment = { hospitalId ->
                        navController.navigate(Screen.SelectDoctor.route)
                    }
                )
            }
        }

        composable(Screen.NearbyPharmacy.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                NearbyPharmacyScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.SuggestedMedicines.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                SuggestedMedicinesScreen(
                    diagnosisName = "your condition",
                    onBack = { navController.popBackStack() },
                    onConsultDoctor = {
                        navController.navigate(Screen.SelectDoctor.route)
                    },
                    onFindPharmacy = {
                        navController.navigate(Screen.NearbyPharmacy.route)
                    }
                )
            }
        }

        composable(Screen.PrecautionSteps.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                PrecautionStepsScreen(
                    diagnosisName = "your condition",
                    onBack = { navController.popBackStack() },
                    onBookDoctor = {
                        navController.navigate(Screen.SelectDoctor.route)
                    }
                )
            }
        }

        composable(Screen.DiseaseList.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                DiseaseListScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                    onMenu = { navController.navigate(Screen.PatientMenu.route) },
                    onDiseaseClick = { disease ->
                        navController.navigate(Screen.DiseaseDetails.createRoute(disease.id))
                    }
                )
            }
        }

        composable(
            route = Screen.DiseaseDetails.route,
            arguments = listOf(navArgument("diseaseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val diseaseId = backStackEntry.arguments?.getString("diseaseId") ?: ""
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                DiseaseDetailsScreen(
                    diseaseId = diseaseId,
                    repository = repository,
                    onBack = { navController.popBackStack() },
                    onAskAI = {
                        navController.navigate(Screen.ChatbotMain.route) {
                            popUpTo(Screen.DiseaseDetails.route) { inclusive = true }
                        }
                    },
                    onViewPreventionTips = {
                        navController.navigate(Screen.PreventionTips.createRoute(diseaseId))
                    }
                )
            }
        }

        composable(
            route = Screen.SymptomExplanation.route,
            arguments = listOf(navArgument("symptomId") { type = NavType.StringType; defaultValue = "fever" })
        ) { backStackEntry ->
            val symptomId = backStackEntry.arguments?.getString("symptomId") ?: "fever"
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                SymptomExplanationScreen(
                    symptomId = symptomId,
                    onBack = { navController.popBackStack() },
                    onAskAI = {
                        navController.navigate(Screen.ChatbotMain.route)
                    },
                    onViewDiseases = {
                        navController.navigate(Screen.DiseaseList.route)
                    }
                )
            }
        }

        composable(Screen.AwarenessArticle.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                AwarenessArticlesScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.VaccinationReminder.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                VaccinationReminderScreen(
                    onBack = { navController.popBackStack() },
                    onBookAppointment = {
                        navController.navigate(Screen.SelectDoctor.route)
                    }
                )
            }
        }

        composable(
            route = Screen.PreventionTips.route,
            arguments = listOf(navArgument("diseaseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val diseaseId = backStackEntry.arguments?.getString("diseaseId") ?: ""
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                PreventionTipsScreen(
                    diseaseId = diseaseId,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.DoctorHome.route) {
            DoctorHome(navController = navController)
        }

        composable(Screen.DoctorMenu.route) {
            DoctorMenu(
                navController = navController,
                onClose = { navController.popBackStack() }
            )
        }

        composable(Screen.PendingRequests.route) {
            PendingRequests(navController = navController, repository = repository)
        }

        composable(Screen.AcceptedRequests.route) {
            AcceptedRequests(navController = navController)
        }

        composable(Screen.CriticalAppointments.route) {
            CriticalAppointments(navController = navController)
        }

        composable(
            route = Screen.AppointmentDetails.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
            AppointmentDetails(
                navController = navController,
                appointmentId = appointmentId
            )
        }

        composable(
            route = Screen.ManageAppointment.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
            ManageAppointment(
                navController = navController,
                appointmentId = appointmentId
            )
        }

        composable(Screen.DoctorStatistics.route) {
            DoctorStatistics(navController = navController)
        }

        composable(Screen.DoctorProfile.route) {
            DoctorProfile(navController = navController)
        }

        composable(Screen.EditDoctorProfile.route) {
            EditDoctorProfile(navController = navController)
        }

        composable(Screen.DoctorNotifications.route) {
            DoctorNotifications(navController = navController)
        }

    }
}
