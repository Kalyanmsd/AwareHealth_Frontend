package com.example.awarehealth.navigation

sealed class Screen(val route: String) {
    // Onboarding
    object Splash : Screen("splash")
    object UserTypeSelection : Screen("user_type_selection")
    
    // Auth
    object Login : Screen("login/{userType}") {
        fun createRoute(userType: String) = "login/$userType"
    }
    object Register : Screen("register/{userType}") {
        fun createRoute(userType: String) = "register/$userType"
    }
    object ForgotPassword : Screen("forgot_password")
    object ResetPassword : Screen("reset_password")
    
    // Patient
    object PatientHome : Screen("patient_home")
    object PatientMenu : Screen("patient_menu")
    object PatientProfile : Screen("patient_profile")
    
    // Appointments
    object SelectDoctor : Screen("select_doctor")
    object SelectDateTime : Screen("select_date_time")
    object AppointmentSummary : Screen("appointment_summary")
    object AppointmentSuccess : Screen("appointment_success")
    object AppointmentAccepted : Screen("appointment_accepted/{doctorName}/{doctorSpecialty}/{appointmentDate}/{appointmentTime}/{location}") {
        fun createRoute(
            doctorName: String = "",
            doctorSpecialty: String = "",
            appointmentDate: String = "",
            appointmentTime: String = "",
            location: String = ""
        ) = "appointment_accepted/${java.net.URLEncoder.encode(doctorName, "UTF-8")}/${java.net.URLEncoder.encode(doctorSpecialty, "UTF-8")}/${java.net.URLEncoder.encode(appointmentDate, "UTF-8")}/${java.net.URLEncoder.encode(appointmentTime, "UTF-8")}/${java.net.URLEncoder.encode(location, "UTF-8")}"
    }
    object AppointmentRejected : Screen("appointment_rejected")
    object MyAppointments : Screen("my_appointments")
    
    // Chatbot
    object ChatbotMain : Screen("chatbot_main")
    object ChatWindow : Screen("chat_window/{conversationId}") {
        fun createRoute(conversationId: String = "new") = "chat_window/$conversationId"
    }
    object ChatbotSettings : Screen("chatbot_settings")
    object DaysQuestion : Screen("days_question")
    object QuickReplies : Screen("quick_replies")
    object DiseaseResponse : Screen("disease_response")
    object BookingPrompt : Screen("booking_prompt")
    object NearbyHospital : Screen("nearby_hospital")
    object NearbyPharmacy : Screen("nearby_pharmacy")
    object SuggestedMedicines : Screen("suggested_medicines")
    object PrecautionSteps : Screen("precaution_steps")
    
    // Health Info
    object DiseaseList : Screen("disease_list")
    object DiseaseDetails : Screen("disease_details/{diseaseId}") {
        fun createRoute(diseaseId: String) = "disease_details/$diseaseId"
    }
    object SymptomExplanation : Screen("symptom_explanation/{symptomId}") {
        fun createRoute(symptomId: String) = "symptom_explanation/$symptomId"
    }
    object AwarenessArticle : Screen("awareness_article")
    object VaccinationReminder : Screen("vaccination_reminder")
    object PreventionTips : Screen("prevention_tips/{diseaseId}") {
        fun createRoute(diseaseId: String) = "prevention_tips/$diseaseId"
    }
    
    // Doctor
    object DoctorHome : Screen("doctor_home")
    object DoctorMenu : Screen("doctor_menu")
    object PendingRequests : Screen("pending_requests")
    object AcceptedRequests : Screen("accepted_requests")
    object RejectedRequests : Screen("rejected_requests")
    object CriticalAppointments : Screen("critical_appointments")
    object AppointmentDetails : Screen("appointment_details/{appointmentId}") {
        fun createRoute(appointmentId: String) = "appointment_details/$appointmentId"
    }
    object ManageAppointment : Screen("manage_appointment/{appointmentId}") {
        fun createRoute(appointmentId: String) = "manage_appointment/$appointmentId"
    }
    object DoctorProfile : Screen("doctor_profile")
    object DoctorStatistics : Screen("doctor_statistics")
    object EditDoctorProfile : Screen("edit_doctor_profile")
    object DoctorNotifications : Screen("doctor_notifications")
}
