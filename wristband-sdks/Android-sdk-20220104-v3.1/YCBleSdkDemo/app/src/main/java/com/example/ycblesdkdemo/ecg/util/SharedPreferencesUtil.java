package com.example.ycblesdkdemo.ecg.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

/**
 * @author StevenLiu
 * @date 2021/12/30
 * @desc one word for this class
 */
public class SharedPreferencesUtil {

    public static void saveEcgList(List<Integer> plist, String timeStr, Context context) {
        if (plist == null) {
            plist = new ArrayList<>();
        }

        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString("saveEcgList", null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray aaaa = new JSONArray();
        aaaa.put(timeStr);


        for (int i = 0; i < array.length(); i++) {
            try {
                String object = array.getString(i);
                aaaa.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //存入数据
        JSONArray aaa = new JSONArray(plist);
        Editor editor = sp.edit();
        editor.putString("saveEcgList", aaaa.toString());
        editor.putString(timeStr, aaa.toString());
        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void delEcgList(String timeStr, Context context) {

        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString("saveEcgList", null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                String object = array.getString(i);
                if (object.equals(timeStr)) {
                    array.remove(i);
                }
            } catch (JSONException e) {

            }
        }


        Editor editor = sp.edit();
        editor.putString("saveEcgList", array.toString());
        editor.remove(timeStr);
        editor.commit();
    }

    public static List<String> readEcgList(Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString("saveEcgList", null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<String> timeList = new ArrayList<>();


        for (int i = 0; i < array.length(); i++) {
            try {
                String object = array.getString(i);
                timeList.add(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return timeList;
    }

    public static List<Integer> readEcgListMsg(String timeStr, Context context) {
        //value 1开始
        SharedPreferences sp = context.getSharedPreferences("smartam", context.MODE_PRIVATE);

        String strValue = sp.getString(timeStr, null);

        JSONArray array = new JSONArray();
        if (strValue != null) {
            try {
                array = new JSONArray(strValue);
            } catch (JSONException e) {

            }
        }

        List<Integer> timeList = new ArrayList<>();


        for (int i = 0; i < array.length(); i++) {
            try {
                Integer object = array.getInt(i);
                timeList.add(object);

            } catch (JSONException e) {

            }
        }

        return timeList;
    }
}
