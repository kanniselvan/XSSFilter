package com.xssFilter.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.*;
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

    public static boolean isValidURLPattern(String uri) {
        AtomicBoolean flag= new AtomicBoolean(false);
        String[]  urls=uri.split("\\/");

        Arrays.stream(urls).filter(e->!StringUtils.isEmpty(e)).forEach(url->{
            String val=String.valueOf(url);
            Map<String, Object> mapping= jsonToMap(new JSONObject(val));
            System.out.println("Map; "+mapping);
            mapping.forEach((key,value)->{
             //   System.out.println("key  "+key+"  value:"+value);
                Matcher matcher = pattern.matcher(String.valueOf(value));
                if (!matcher.matches()) {
                    System.out.println(key+"  : Invalid char found!!!!!");
                    flag.set(true);
                }else{
                    System.out.println("valid char found: "+String.valueOf(value));
                }
            });

        });
        return !flag.get();
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json,retMap);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object, Map<String, Object> map) throws JSONException {


        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

         //   System.out.println("key  "+key+"  value:"+ value);

            if(value instanceof JSONArray) {
                value = toList(key,(JSONArray) value,map);
            }else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value,map);
            }else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static List<Object> toList(String key,JSONArray array,Map<String, Object> map ) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList(key,(JSONArray) value,map);
            }else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value,map);
            }else{
                String mapValue=String.valueOf(value);
                if(map.containsKey(key)){
                    mapValue+=","+String.valueOf(map.get(key));
                }
                map.put(key, mapValue);
            }
            list.add(value);
        }
        return list;
    }
}
