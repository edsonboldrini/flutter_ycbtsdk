package com.example.ycblesdkdemo.phone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import static android.provider.BlockedNumberContract.BlockedNumbers.COLUMN_ID;
import static android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME;

public class PhoneUtil {

    public static String getContactNameFromPhoneNum(Context context, String phoneNum) {// 根据电话号码得到联系人名字
        String name = getDisplayNameByNumber(context, phoneNum);
        if (name == null) {
            name = getName(context, phoneNum);
        }
        if (name == null) {
            name = getContactNameByNumber(context, phoneNum);
        }
        return name;
    }


    private static String getDisplayNameByNumber(Context context, String number) {
        if (number == null)
            return null;
        number = number.trim();
        String displayName = null;
        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(number).build();
            String[] projection = new String[]{COLUMN_ID, COLUMN_DISPLAY_NAME};
            cursor = resolver.query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexName = cursor.getColumnIndex(COLUMN_DISPLAY_NAME);
                displayName = cursor.getString(columnIndexName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return displayName;
    }

    private static String getName(Context context, String mNumber) {
        String name = null;
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = context.checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'", null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            name = cursor.getString(nameFieldColumnIndex);
        }
        cursor.close();
        return name;
    }


    private static String getContactNameByNumber(Context context, String phoneNum) {
        String contactName = null;
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phoneNum);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactName;
    }

}
