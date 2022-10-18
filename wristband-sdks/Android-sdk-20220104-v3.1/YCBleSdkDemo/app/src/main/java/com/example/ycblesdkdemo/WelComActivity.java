package com.example.ycblesdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.ycblesdkdemo.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class WelComActivity extends Activity {


    private String[] permissionArray = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean backBoolean = PermissionUtils.checkPermissionArray(WelComActivity.this, permissionArray, 3);
            if (backBoolean) {
                afterDo();
            }
        } else {
            afterDo();
        }

    }


    //授权后，开始
    private void afterDo() {

        Intent mainIntent = new Intent(WelComActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != 3) {
            return;
        }

        if (grantResults.length > 0) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (deniedPermissionList.isEmpty()) {
                //已经全部授权
                afterDo();
            } else {

                //如果有拒绝的权限，就继续询问
                PermissionUtils.checkPermissionArray(WelComActivity.this, permissionArray, 3);
                //勾选了对话框中”Don’t ask again”的选项, 返回false
                for (String deniedPermission : deniedPermissionList) {
                    boolean flag = shouldShowRequestPermissionRationale(deniedPermission);
                    if (!flag) {
                        //拒绝授权
                        Toast.makeText(WelComActivity.this, "请前往设置页面，手动开启权限", Toast.LENGTH_SHORT).show();
//                        permissionShouldShowRationale(deniedPermissionList);
                        return;
                    }
                }
                //拒绝授权
//                permissionHasDenied(deniedPermissionList);
            }
        }
    }

}
