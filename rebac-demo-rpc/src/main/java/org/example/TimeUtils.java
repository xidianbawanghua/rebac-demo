package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    private static String timeFormat = "yyyy-MM-dd HH:mm:ss";


    /**
     * 字符串转换为日期时间
     */
    public static Date string2Date(String strDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(strDate);
    }

    public static long string2Timestamp(String strDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(strDate);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


//
//
//
//    public static long convertToTimestamp(String date) throws ParseException {
//        (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateStr).getTime();
//        Date parse = null;
//        try {
//            parse = simpleDateFormat.parse(strDate);
//            return parse.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static void main(String[] args) throws ParseException {
        String time_regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        String chatResponseMsg = "以下是提取出的日期列表：\\n\\n[\\\"2023-05-13 15:00:00\\\", \\\"2023-06-18 08:00:00\\\",\\\"2023-06-18 08:00:00\\\",\\\"2023-06-18 08:00:00\\\"]";
        Matcher matcher = Pattern.compile(time_regex).matcher(chatResponseMsg);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

        String temp_content = "。请提取以上文字中的日期，如果没有指定年份，请默认为%s年，如果没有指定月份，默认为%s月，将其转换为\"yyyy-mm-dd hh:mm:ss\"标准格式，返回到一个list，提取出list";
        System.out.println(String.format(temp_content, getCurrentYear(), getCurrentMonth()));



    }

}
