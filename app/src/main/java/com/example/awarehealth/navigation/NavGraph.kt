package com.example.awarehealth.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
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

@Composable
fun AwareHealthNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    apiService: ApiService? = null
) {
    val repository = remember { AppRepository(apiService) }
    val user = remember { mutableStateOf(User(name = "John Smith")) }
    // Patient appointments - using local PatientAppointment (has doctorName)
    val patientAppointments = remember { 
        mutableStateListOf<PatientAppointment>()
    }
    var selectedDoctor by rememberSaveable { mutableStateOf<Doctor?>(null) }
    var selectedDateTime by rememberSaveable { mutableStateOf<SelectedDateTime?>(null) }
    var selectedUserType by rememberSaveable { mutableStateOf("") }

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
                userType = userType,
                onLoginSuccess = {
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
                onGoogleLogin = {
                    // Google login - navigate directly without validation
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
                userType = userType,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.createRoute(userType)) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() },
                onGoogleSignup = {}
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLogin = { navController.popBackStack() },
                onResetNavigate = { navController.navigate(Screen.ResetPassword.route) }
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen {
                navController.navigate(Screen.Login.createRoute(selectedUserType)) {
                    popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                }
            }
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
                        name = user.value.name,
                        email = "john@example.com",
                        phone = "+1234567890"
                    ),
                    totalAppointments = patientAppointments.size,
                    completedAppointments = patientAppointments.count { it.status == "accepted" },
                    onEditProfile = { navController.popBackStack() }
                )
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
                    }
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
                        val appointmentId = System.currentTimeMillis().toString()
                        val date = selectedDateTime?.date ?: ""
                        val time = selectedDateTime?.time ?: ""
                        val patientName = user.value.name
                        val doctorName = selectedDoctor?.name ?: ""
                        
                        // Create patient appointment - PatientAppointment (has doctorName)
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

        composable(Screen.AppointmentAccepted.route) {
            Column {
                Header(showBack = true, onBackClick = { navController.popBackStack() })
                AppointmentAcceptedScreen(
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
            PendingRequests(navController = navController)
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
