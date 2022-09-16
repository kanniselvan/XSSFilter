package com.xssFilter.utils;


import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class XSSValidationUtils {

    public final Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?\\s]*$", Pattern.CASE_INSENSITIVE);

     public boolean isValidParam(Map<String, String>  body){

          AtomicBoolean flag= new AtomicBoolean(false);
         body.entrySet().forEach(map->{
                 String val=String.valueOf(map.getValue());
             System.out.println("value:"+val);
             Matcher matcher = pattern.matcher(val);
             if (!matcher.matches()) {
                 System.out.println("Invalid char found!!!!!");
                 flag.set(true);
             }else{
                 System.out.println("valid char found: "+val);
             }
         });
         return !flag.get();

     }

    public static boolean isValidURL(String uri) {
        AtomicBoolean flag= new AtomicBoolean(false);
         String[]  urls=uri.split("\\/");

        Arrays.stream(urls).filter(e->!StringUtils.isEmpty(e)).forEach(url->{
            String val=String.valueOf(url);
            System.out.println("value:"+val);
            Matcher matcher = pattern.matcher(val);
            if (!matcher.matches()) {
                System.out.println("Invalid char found!!!!!");
                flag.set(true);
            }else{
                System.out.println("valid char found: "+val);
            }
        });
        return !flag.get();
    }
}
