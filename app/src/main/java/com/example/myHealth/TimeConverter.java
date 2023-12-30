package com.example.myHealth;

//class used for quickly converting time in string format to decimal format
public class TimeConverter {
    public static double convertToDecimal(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        double decimalTime = hours + (minutes / 60.0);
        return decimalTime;
    }

    public static String convertToString(double time) {
        int hours = (int) time;
        int minutes = (int) ((time - hours) * 60);

        // Format the time as a string
        String timeString = String.format("%02d:%02d", hours, minutes);
        return timeString;
    }
}
