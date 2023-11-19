package com.example.myHealth;

import static android.content.ContentValues.TAG;
import static com.example.myHealth.TimeConverter.convertToDecimal;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentManager {
    FirebaseFirestore db = myFirestore.getDBInstance();
    FirebaseAuth mAuth = myFirestore.getmAuthInstance();

    private static final int APPOINTMENT_DURATION_MINUTES = 30;

    // Other properties or dependencies, such as Firestore database reference

    public AppointmentManager(/* Pass necessary dependencies */) {
        // Initialize your properties or dependencies
    }

    //this function is meant to return a boolean array of indexes based on the avaliable operating hours of the clinic
    public ArrayList availableDayTimes(String clinicId, String appointmentDate) {
        ArrayList<Boolean> availableTimes = new ArrayList<>();
        DocumentReference clinicRef = db.collection("clinics").document(clinicId);
        //get the clinic document to get hours and machines
        clinicRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    int clinicStartHour = Math.toIntExact(document.getLong("sHour"));
                    int clinicFinalHour = (Math.toIntExact(document.getLong("fHour")) + 12);
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
                                //add the appointments to the daily schedule
                                for (int i = 0; i < dayArraySize; i++) {
                                    if (i >= appointmentStartIndex && i <= appointmentEndIndex) {
                                        dailyAppointmentSchedule[i]++;
                                    }
                                }
                                //find available times for new appointments based on current daily schedule
                                for (int i = 0; i < dayArraySize; i++) {
                                    if (dailyAppointmentSchedule[i] < clinicNumOfMachines) {
                                        availableTimes.set(i, true);
                                    } else {
                                        for (int j = (i - 8); j < i; j++)
                                            if (j >= 0) {
                                                availableTimes.set(j, false);
                                            }
                                    }
                                }
                                //make sure they can schedule an appointment that goes beyond the end time
                                for (int i = (dayArraySize - 8); i < dayArraySize; i++)
                                    if (i >= 0) {
                                        availableTimes.set(i, false);
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
    public void makeSingleAppointment(String clinicId, Date appointmentDate, ArrayList<Boolean> availableTimes, int appointmentTime) {
        if (availableTimes.get(appointmentTime) == true) {
            DocumentReference clinicRef = db.collection("clinic").document(clinicId);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String dateId = String.valueOf(appointmentDate);
            DocumentReference dateAppointmentRef = clinicRef
                    .collection("dates")
                    .document(dateId)
                    .collection("appointments")
                    .document(currentUser.getUid());


            dateAppointmentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Log.d(TAG, "task successful");
                        if (!document.exists()) {
                            Log.d(TAG, "document doesnt exist");
                            // Create a new clinic
                            Map<String, Object> appointment = new HashMap<>();
                            appointment.put("date", dateId);
                            appointment.put("startTime", appointmentTime);
                            appointment.put("endTime", appointmentTime + 8);

                            dateAppointmentRef.set(appointment)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // User document created successfully
                                            Log.d(TAG, "DocumentSnapshot added;");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure to create user document
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "document exists");
                        // Handle failure to check if user document exists
                    }
                }
            });
        }
    }

    public void makeRecurringAppointments(String clinicId, List<Date> appointmentTimes) {
        // Logic to make recurring appointments
        // Check availability, update database, etc.
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
                    int clinicFinalHour = (Math.toIntExact(document.getLong("closeHour")) + 12);
                    Log.d("Clinic Info", "clinic start time: " + clinicStartHour);
                    Log.d("Clinic Info", "clinic end  time: " + clinicFinalHour);

                    //Make an array to add all current appointments to
                    int dayArraySize = (clinicFinalHour - clinicStartHour) * 2;
                    Log.d("TAG", "day array size: " + dayArraySize);
                    int[] dailyAppointmentSchedule = new int[dayArraySize];
                    for (int i = clinicStartHour * 60; i < clinicFinalHour * 60; i += 30) {
                        int hours = i / 60;
                        if (hours > 12) {
                            hours -= 12;
                        }
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

