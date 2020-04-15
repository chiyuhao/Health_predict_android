package com.example.myapplication;
 
import android.Manifest;
import android.content.pm.PackageManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * @author why
 * @date 2019-2-18 14:00:34
 */
public class SimplePerActivity extends Activity implements View.OnClickListener {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
 
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "�����������Ȩ��", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "��ܾ������Ȩ��", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                //TODO multi permissions request result notification
                boolean result = true;
                for (int resultCode : grantResults) {
                    if (resultCode != PackageManager.PERMISSION_GRANTED) {
                        result = false;
                    }
                }
                if (result) {
                    Toast.makeText(this, "�����������������Ȩ��", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "��û���������������Ȩ��", Toast.LENGTH_SHORT).show();
                }
                break;
        }
 
    }
 
    @Override
    public void onClick(View v) {
        
           
               
                //TODO here we can check Permissions authorised by calling checkPermissions(String[] rawPermission)
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);//�������Ȩ�޺Ͷ�дȨ�ޣ�����Ӷ����String������

        
    }
 
 
    /**
     * Ȩ��У��,��ȡΪ��Ȩ��Ȩ��
     *
     * @param rawPermission
     * @return
     */
    public String[] checkPermission(String[] rawPermission) {
        List<String> requestList = new ArrayList();
        for (String permission : rawPermission) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestList.add(permission);
            }
        }
        return requestList.toArray(new String[requestList.size()]);// return the permission needed to be requested
    }
}
