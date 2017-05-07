package com.haikarose.primepost.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 2/1/17.
 */

public class DateHelper {

    public static String getPresentableDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date presentableDate=null;
        String theDate=null;
        try{
            presentableDate=format.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            theDate=formatter.format(presentableDate);
        }catch (Exception ex){

        }
        return theDate;
    }
}
