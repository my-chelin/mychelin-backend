package com.a206.mychelin.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimestampToDateString {
    public static String getPassedTime(Timestamp writtenTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY년 MM월 dd일");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        long tmp = (timestamp.getTime() - writtenTime.getTime());

        int seconds = (int) tmp / 1000;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = (seconds % 3600) % 60;

        if (hours < 840 && hours >= 672) {
            return "4주 전";
        }
        if (hours < 672 && hours >= 504) {
            return "3주 전";
        }
        if (hours < 504 && hours >= 336) {
            return "2주 전";
        }
        if (hours < 336 && hours >= 168) {
            return "1주 전";
        }
        if (hours < 168 && hours >= 24) { //n일 전
            int day = hours / 24;
            return day + "일 전";
        }
        if (hours < 24 && hours >= 1) {
            return hours + "시간 전";
        }
        if (hours < 1) {
            return minutes + "분 전";
        }
        return sdf.format(writtenTime);
    }
}
