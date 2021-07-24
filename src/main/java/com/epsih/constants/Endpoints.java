package com.epsih.constants;

public class Endpoints {

   public static final String API_PREFIX = "/api";

   // UserController
   public static final String USER_ROOT = API_PREFIX + "/user";
   public static final String USER_ME = "/me";
   public static final String USER_ID = "/{id}";

   // PatientController
   public static final String PATIENT_ROOT = API_PREFIX + "/patient";
   public static final String PATIENT_ME = "/me";
   public static final String PATIENT_MEETINGS = "/meetings";
   public static final String PATIENT_MEETING_ID = "/meetings/{id}";
   public static final String PATIENT_MEETING_TERMINS = "/meetings/{id}/termins";
   public static final String PATIENT_REVIEWS = "/reviews";
   public static final String PATIENT_MEETING_REQUEST = "/meetingRequest";

   // DoctorController
   public static final String DOCTOR_ROOT = API_PREFIX + "/doctor";
   public static final String DOCTOR_ME = "/me";
   public static final String DOCTOR_MEETINGS = "/meetings";
   public static final String DOCTOR_MEETING_ID = "/meetings/{id}";
   public static final String DOCTOR_MEETING_TERMINS = "/meetings/{id}/termins";
   public static final String DOCTOR_REVIEWS = "/reviews";
   public static final String DOCTOR_ACCEPT_MEETING_REQUEST = "/acceptMeetingRequest";
   public static final String DOCTOR_CLOSE_MEETING = "/closeMeeting";

   // ServiceController
   public static final String SERVICE_ROOT = API_PREFIX + "/service";
   public static final String SERVICE_ID = "/{id}";
   public static final String SERVICE_DOCTORS = "/{id}/doctors";

   // AuthController
   public static final String AUTH_ROOT = API_PREFIX;
   public static final String AUTH_LOGIN = "/authenticate";
   public static final String AUTH_REGISTER = "/register";
   public static final String AUTH_ACTIVATE = "/activate/{token}";
   public static final String AUTH_RESET_PASSWORD = "/resetPassword";
   public static final String AUTH_REQUEST_RESET_PASSWORD = "/requestResetPassword";
   public static final String AUTH_CHANGE_PASSWORD = "/changePassword";

   // CategoryController
   public static final String CATEGORY_ROOT = API_PREFIX + "/category";
   public static final String CATEGORY_ID = "/{id}}";

   // ReviewController
   public static final String REVIEW_ROOT = API_PREFIX + "/review";
   public static final String REVIEW_ID = "/{id}}";

   // Termin Controller
   public static final String TERMIN_ROOT = API_PREFIX + "/termin";
   public static final String TERMIN_ID = "/{id}";

}
