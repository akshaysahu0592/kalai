package com.tcit.vms.vms.util;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {

    public static Date getDateByMinsOrHours(LocalDateTime dateOfVisit, Integer Duration){

        LocalDateTime dateToConvert= dateOfVisit.plusDays(5).plusHours(Duration);
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


    public static Date addHoursToJavaUtilLocalDateTime(LocalDateTime date, int hours, Integer c) {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        calendar.add(c, hours);
        return calendar.getTime();
    }
    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
}

