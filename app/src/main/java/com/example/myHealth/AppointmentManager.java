package com.example.myHealth;

import static android.content.ContentValues.TAG;
import static com.example.myHealth.TimeConverter.convertToDecimal;
import static com.example.myHealth.TimeConverter.convertToString;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AppointmentManager {
    FirebaseFirestore db = myFirestore.getDBInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();

    public  AppointmentManager(/* Pass necessary dependencies */) {
        // Initialize your properties or dependencies
    }

    //this function is meant to return a boolean array of indexes based on the avaliable operating hours of the clinic
    public ArrayList availableDayTimes(String clinicId, String appointmentDate, OnDataReadyListener listener) {
        ArrayList<Boolean> availableTimes = new ArrayList<>();
        Log.d("TAG", "clinic id:" + clinicId);
        DocumentReference clinicRef = db.collection("clinic").document(clinicId);
        //get the clinic document to get hours and machines
        clinicRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int clinicStartHour = Math.toIntExact(document.getLong("startHour"));
                    int clinicFinalHour = Math.toIntExact(document.getLong("closeHour"));
                    int clinicNumOfMachines = Math.toIntExact(document.getLong("numOfMachines"));

                    //Make an array to add all current appointments to
                    int dayArraySize = (clinicFinalHour - clinicStartHour) * 2;
                    int[] dailyAppointmentSchedule = new int[dayArraySize];
                    for (int i = 0; i < dayArraySize; i++) {
                        availableTimes.add(true);
                    }


                    // Convert appointmentDate to a String for the subcollection document ID
                    String dateId = String.valueOf(appointmentDate);

                    //get the appointments for that day
                    CollectionReference dateAppointmentsRef = clinicRef
                            .collection("dates")
                            .document(dateId)
                            .collection("appointments");

                    // Query appointments for the specified date
                    dateAppointmentsRef.get().addOnCompleteListener(appointmentTask -> {
                        if (appointmentTask.isSuccessful()) {
                            for (QueryDocumentSnapshot appointmentDoc : appointmentTask.getResult()) {
                                Appointment appointment = appointmentDoc.toObject(Appointment.class);
                                // get the start and end index of each appointment
                                double appointmentStartTimeDecimal = convertToDecimal(appointment.getStartTime());
                                double appointmentEndTimeDecimal = convertToDecimal(appointment.getEndTime());
                                int appointmentStartIndex = (int) ((appointmentStartTimeDecimal - clinicStartHour) * 2);
                                int appointmentEndIndex = (int) ((appointmentEndTimeDecimal - clinicStartHour) * 2);

                                if (appointmentStartIndex < 0) {
                                    appointmentStartIndex = (int) (((appointmentStartTimeDecimal + 12) - clinicStartHour) * 2);
                                }
                                if (appointmentEndIndex < 0) {
                                    appointmentEndIndex = (int) (((appointmentEndTimeDecimal + 12) - clinicStartHour) * 2);
                                }
                                if (appointmentEndIndex < appointmentStartIndex) {
                                    appointmentEndIndex = (int) (((appointmentEndTimeDecimal + 12) - clinicStartHour) * 2);
                                }
                                Log.d("appointmentStartIndex", "start :" + appointmentStartIndex);
                                Log.d("appointmentEndIndex", "end :" + appointmentEndIndex);
                                //add the appointments to the daily schedule
                                for (int i = 0; i < dayArraySize; i++) {
                                    if (i >= appointmentStartIndex && i <= appointmentEndIndex) {
                                        dailyAppointmentSchedule[i]++;
                                        Log.d("ArrayListInfo", "dailyAppointmentSchedule incremented");
                                    }
                                }
                            }
                            //find available times for new appointments based on current daily schedule
                            for (int i = 0; i < dayArraySize; i++) {
                                if (dailyAppointmentSchedule[i] < clinicNumOfMachines) {
                                    availableTimes.set(i, true);
                                } else {
                                    for (int j = (i - 7); j <= i; j++)
                                        if (j >= 0) {
                                            availableTimes.set(j, false);
                                        }
                                }
                            }
                            //make sure they cant schedule an appointment that goes beyond the end time
                            for (int i = (dayArraySize - 7); i < dayArraySize; i++) {
                                if (i >= 0) {
                                    availableTimes.set(i, false);
                                }
                            }
                            Log.d("ArrayListInfo", "Size of boolean array: " + availableTimes.size());

                            // Log all elements in the ArrayList
                            if (availableTimes.size() > 0) {
                                for (int i = 0; i < availableTimes.size(); i++) {
                                    Log.d("ArrayListInfo", "dailyAppointmentSchedule: " + dailyAppointmentSchedule[i]);
                                    Log.d("ArrayListInfo", "availableTimes: " + availableTimes.get(i));
                                }
                                if (listener != null) {
                                    listener.onDataReady(availableTimes);
                                }
                            }
                        } else {
                            // Handle the error in fetching appointments
                            Log.e("TAG", "Error getting appointments", appointmentTask.getException());
                        }
                    });

                } else {
                    // clinic document doesn't exist, handle accordingly
                    Log.e("TAG", "Error getting clinic");
                }
            } else {
                // Handle failure to retrieve clinic document
                Log.e("TAG", "Error getting appointments");
            }
        });
        return availableTimes;
    }

    //this function is meant to return a boolean array of indexes based on the avaliable operating hours of the clinic
    public ArrayList availableRecurringDayTimes(String clinicId, String recurringDay, OnDataReadyListener listener) {
        ArrayList<Boolean> availableTimes = new ArrayList<>();
        Log.d("TAG", "clinic id:" + clinicId);
        DocumentReference clinicRef = db.collection("clinic").document(clinicId);
        //get the clinic document to get hours and machines
        clinicRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int clinicStartHour = Math.toIntExact(document.getLong("startHour"));
                    int clinicFinalHour = Math.toIntExact(document.getLong("closeHour"));
                    int clinicNumOfMachines = Math.toIntExact(document.getLong("numOfMachines"));

                    //Make an array to add all current appointments to
                    int dayArraySize = (clinicFinalHour - clinicStartHour) * 2;
                    int[] dailyAppointmentSchedule = new int[dayArraySize];
                    for (int i = 0; i < dayArraySize; i++) {
                        availableTimes.add(false);
                    }

                    //get the appointments for that day
                    CollectionReference dateAppointmentsRef = clinicRef
                            .collection("days")
                            .document(recurringDay)
                            .collection("appointments");

                    // Query appointments for the specified date
                    dateAppointmentsRef.get().addOnCompleteListener(appointmentTask -> {
                        if (appointmentTask.isSuccessful()) {
                            for (QueryDocumentSnapshot appointmentDoc : appointmentTask.getResult()) {
                                Appointment appointment = appointmentDoc.toObject(Appointment.class);
                                // get the start and end index of each appointment
                                double appointmentStartTimeDecimal = convertToDecimal(appointment.getStartTime());
                                double appointmentEndTimeDecimal = convertToDecimal(appointment.getEndTime());
                                int appointmentStartIndex = (int) ((appointmentStartTimeDecimal - clinicStartHour) * 2);
                                int appointmentEndIndex = (int) ((appointmentEndTimeDecimal - clinicStartHour) * 2);

                                if (appointmentStartIndex < 0) {
                                    appointmentStartIndex = (int) (((appointmentStartTimeDecimal + 12) - clinicStartHour) * 2);
                                }
                                if (appointmentEndIndex < 0) {
                                    appointmentEndIndex = (int) (((appointmentEndTimeDecimal + 12) - clinicStartHour) * 2);
                                }
                                if (appointmentEndIndex < appointmentStartIndex) {
                                    appointmentEndIndex = (int) (((appointmentEndTimeDecimal + 12) - clinicStartHour) * 2);
                                }
                                Log.d("appointmentStartIndex", "start :" + appointmentStartIndex);
                                Log.d("appointmentEndIndex", "end :" + appointmentEndIndex);
                                //add the appointments to the daily schedule
                                for (int i = 0; i < dayArraySize; i++) {
                                    if (i >= appointmentStartIndex && i <= appointmentEndIndex) {
                                        dailyAppointmentSchedule[i]++;
                                        Log.d("ArrayListInfo", "dailyAppointmentSchedule incremented");
                                    }
                                }
                            }
                            //find available times for new appointments based on current daily schedule
                            for (int i = 0; i < dayArraySize; i += 8) {
                                if (dailyAppointmentSchedule[i] < clinicNumOfMachines) {
                                    availableTimes.set(i, true);
                                } else {
                                    availableTimes.set(i, false);
                                }
                            }
                            //make sure they cant schedule an appointment that goes beyond the end time
                            for (int i = (dayArraySize - 7); i < dayArraySize; i++) {
                                if (i >= 0) {
                                    availableTimes.set(i, false);
                                }
                            }
                            Log.d("ArrayListInfo", "Size of boolean array: " + availableTimes.size());

                            // Log all elements in the ArrayList
                            if (availableTimes.size() > 0) {
                                for (int i = 0; i < availableTimes.size(); i++) {
                                    Log.d("ArrayListInfo", "dailyAppointmentSchedule: " + dailyAppointmentSchedule[i]);
                                    Log.d("ArrayListInfo", "availableTimes: " + availableTimes.get(i));
                                }
                                if (listener != null) {
                                    listener.onDataReady(availableTimes);
                                }
                            }
                        } else {
                            // Handle the error in fetching appointments
                            Log.e("TAG", "Error getting appointments", appointmentTask.getException());
                        }
                    });

                } else {
                    // clinic document doesn't exist, handle accordingly
                    Log.e("TAG", "Error getting clinic");
                }
            } else {
                // Handle failure to retrieve clinic document
                Log.e("TAG", "Error getting appointments");
            }
        });
        return availableTimes;
    }

    //this function makes an appointment at a specified time/date given that time is available
    public void makeSingleAppointment(String clinicId, String appointmentDate, double appointmentTime, Boolean recurring) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference appointmentsRef = db.collection("clinic").document(clinicId).collection("dates").document(appointmentDate).collection("appointments");
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Create a new appointment document with the user's ID as the document ID
            DocumentReference newAppointmentRef = appointmentsRef.document(userId);

            // convert times to strings
            String startTime = convertToString(appointmentTime);
            String endTime = convertToString((appointmentTime + 4));

            // Create a Map to store the data
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("startTime", startTime);
            appointmentData.put("endTime", endTime);
            appointmentData.put("date", appointmentDate);
            appointmentData.put("recurring", recurring);

            // Set the data in the document
            newAppointmentRef.set(appointmentData)
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully written
                        Log.d("Firestore", "Appointment document added successfully!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("Firestore", "Error adding appointment document", e);
                    });
            }
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference userAppointmentsRef = db.collection("users").document(userId).collection("dates").document(appointmentDate).collection("appointments");


            // Create a new appointment document with the user's ID as the document ID
            DocumentReference newAppointmentRef = appointmentsRef.document(userId);

            // convert times to strings
            String startTime = convertToString(appointmentTime);
            String endTime = convertToString((appointmentTime + 4));

            // Create a Map to store the data
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("startTime", startTime);
            appointmentData.put("endTime", endTime);
            appointmentData.put("date", appointmentDate);
            appointmentData.put("recurring", recurring);

            // Set the data in the document
            newAppointmentRef.set(appointmentData)
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully written
                        Log.d("Firestore", "Appointment document added successfully!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("Firestore", "Error adding appointment document", e);
                    });
        }
    }

    public void makeSingleRecurringAppointment(String clinicId, String appointmentDay, double appointmentTime, Boolean recurring) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference appointmentsRef = db.collection("clinic").document(clinicId).collection("days").document(appointmentDay).collection("appointments");
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Create a new appointment document with the user's ID as the document ID
            DocumentReference newAppointmentRef = appointmentsRef.document(userId);

            // convert times to strings
            String startTime = convertToString(appointmentTime);
            String endTime = convertToString((appointmentTime + 4));

            // Create a Map to store the data
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("startTime", startTime);
            appointmentData.put("endTime", endTime);
            appointmentData.put("recurring", recurring);

            // Set the data in the document
            newAppointmentRef.set(appointmentData)
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully written
                        Log.d("Firestore", "Appointment document added successfully!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("Firestore", "Error adding appointment document", e);
                    });
        }
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference userRecurringRef = db.collection("users").document(userId).collection("recurringAppointments");


            // Create a new appointment document with the user's ID as the document ID
            DocumentReference newRecurringAppointmentRef = appointmentsRef.document(appointmentDay);

            // convert times to strings
            String startTime = convertToString(appointmentTime);
            String endTime = convertToString((appointmentTime + 4));

            // Create a Map to store the data
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("clinic", clinicId);
            appointmentData.put("startTime", startTime);
            appointmentData.put("endTime", endTime);
            appointmentData.put("recurring", recurring);

            // Set the data in the document
            newRecurringAppointmentRef.set(appointmentData)
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully written
                        Log.d("Firestore", "Appointment document added successfully!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("Firestore", "Error adding appointment document", e);
                    });
        }

    }

    //adds a year of appointments for the selected date
    public void makeMultipleRecurringAppointments(String clinicId, String appointmentDay, double appointmentTime, Boolean recurring) {
        // Parse the selected day to DayOfWeek
        DayOfWeek selectedDayOfWeek = DayOfWeek.valueOf(appointmentDay.toUpperCase());

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Find the next occurrence of the selected day of the week
        LocalDate nextAppointmentDate = currentDate.with(DayOfWeek.valueOf(appointmentDay.toUpperCase()));

        // Create a list to store all appointment dates for the next 52 weeks
        List<LocalDate> appointmentDates = new ArrayList<>();

        // Add the next 52 occurrences of the selected day of the week
        for (int i = 0; i < 52; i++) {
            appointmentDates.add(nextAppointmentDate);
            nextAppointmentDate = nextAppointmentDate.plusWeeks(1);
        }

        // Call makeSingleRecurringAppointment for each date in the list
        for (LocalDate date : appointmentDates) {
            makeSingleAppointment(clinicId, date.toString(), appointmentTime, recurring);
        }
    }

    // Other methods as needed

    private boolean isAppointmentSlotAvailable(String clinicId, Date appointmentTime) {
        // Logic to check if the appointment slot is available
        // Query your database or use other relevant checks
        return true; // Placeholder, replace with actual logic
    }

    public ArrayList dayTimes(String clinicId, String appointmentDate, OnDataReadyListener listener) {
        Log.d("TAG", "clinic id:" + clinicId);
        DocumentReference clinicRef = db.collection("clinic").document(clinicId);
        ArrayList<String> timeSlots = new ArrayList<>();
        //get the clinic document to get hours and machines
        clinicRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int clinicStartHour = Math.toIntExact(document.getLong("startHour"));
                    int clinicFinalHour = Math.toIntExact(document.getLong("closeHour"));
                    Log.d("Clinic Info", "clinic start time: " + clinicStartHour);
                    Log.d("Clinic Info", "clinic end  time: " + clinicFinalHour);

                    //Make an array to add all current appointments to
                    int dayArraySize = (clinicFinalHour - clinicStartHour) * 2;
                    Log.d("TAG", "day array size: " + dayArraySize);
                    int[] dailyAppointmentSchedule = new int[dayArraySize];
                    for (int i = clinicStartHour * 60; i < clinicFinalHour * 60; i += 30) {
                        int hours = i / 60;
                        int minutes = i % 60;
                        String timeSlot = String.format("%02d:%02d", hours, minutes);
                        Log.d("TAG", "timeslot: " + timeSlot);
                        timeSlots.add(timeSlot);
                    }
                    Log.d("ArrayListInfo", "Size of timeslots: " + timeSlots.size());

                    // Log all elements in the ArrayList
                    if (timeSlots.size() > 0) {
                        for (int i = 0; i < timeSlots.size(); i++) {
                            Log.d("ArrayListInfo", "Element " + i + ": " + timeSlots.get(i));
                        }
                        if (listener != null) {
                            listener.onDataReady(timeSlots);
                        }
                    } else {
                        Log.d("ArrayListInfo", "timeslots is Empty");
                    }
                } else {
                    // clinic document doesn't exist, handle accordingly
                    Log.e("TAG", "Error getting clinic document");
                }
            } else {
                // Handle failure to retrieve clinic document
                Log.e("TAG", "Error getting clinic");
            }
        });
        Log.d("ArrayListInfo", "Size of timeslots: " + timeSlots.size());

        // Log all elements in the ArrayList
        if (timeSlots.size() > 0) {
            for (int i = 0; i < timeSlots.size(); i++) {
                Log.d("ArrayListInfo", "Element " + i + ": " + timeSlots.get(i));
            }
        } else {
            Log.d("ArrayListInfo", "timeslots is Empty");
        }
        return timeSlots;
    }
}
