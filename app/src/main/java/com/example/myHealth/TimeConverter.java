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
}
