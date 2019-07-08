package com.a51tgt.t4m.ui;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.utils.TipUtil;

public class ReductionActivity extends BaseActivity implements View.OnClickListener,OnNoticeUI{
//    private Button setDeaultPwdBtn;
    private Button reductionBtn;
   private  ImageView   iv_back;

    private DeviceSetDialog setDialog = new DeviceSetDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reduction_layout);
//        setDeaultPwdBtn = (Button)findViewById(R.id.setDefaultPwd);
        reductionBtn = (Button)findViewById(R.id.reduction);
        iv_back = (ImageView)findViewById(R.id.iv_back);
//        setDeaultPwdBtn.setOnClickListener(this);
        reductionBtn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
//        MZApplication.getInstance().addActivity(this);
        MZApplication.getInstance().setOnNoticeUiListener( this, OnNoticeUI.KEY_TYPE_DEVICE_REDUCTION);

//        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){

    case R.id.iv_back:
    finish();
    break;
    case R.id.reduction:
        String deviceVersion = APIConstants.deviceInfo.getAppVersion();

        String str =  deviceVersion.split("V")[1].replace("","");


        String strNew = str.replaceAll("[a-zA-Z]","");
        Log.i("versionnew",strNew);

        String[] array = strNew.split("\\.");
        try{
            if (Integer.valueOf(array[0]).intValue() <3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle(R.string.tip_title);
                builder.setMessage(R.string.error_version_low);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
//                    [self showDeviceLow];
                return;
            }else if(Integer.valueOf(array[0]).intValue()==3) {

                if (Integer.valueOf(array[1]).intValue()==0) {
                    if (Integer.valueOf(array[2]).intValue()>23) {
//                            [self connectWifi];
                        showDialog(R.string.tile_restore_tip,TcpConfig.BT_REDUCTION);

                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
                        builder.setTitle(R.string.tip_title);
                        builder.setMessage(R.string.error_version_low);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.show();
//                            [self showDeviceLow];
                        return;
                    }

                }else {
                    showDialog(R.string.tile_restore_tip,TcpConfig.BT_REDUCTION);


                }


            }else if (Integer.valueOf(array[0]).intValue()>3) {
                showDialog(R.string.tile_restore_tip,TcpConfig.BT_REDUCTION);



            }


        }catch (Exception exception) {




        }
        Log.i("version", str);
//        if (Integer.valueOf(str).intValue()<3023){
//
//            return;
//
//        }
        break;
        default:
            break;
//    case R.id.setDefaultPwd:
//        showDialog(R.string.tile_restore_pwtip,TcpConfig.SET_WIFIANDPASSWORD);
//        break;

}

    }


    private  void showDialog(int message, final String command){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton(getResources().getText(R.string.button_cancel),null);
        builder.setPositiveButton(getResources().getText(R.string.button_commit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDialog.setDefaultPwd(command);
                Toast.makeText(ReductionActivity.this,R.string.tip_operation_success,Toast.LENGTH_LONG).show();



            }
        });

          builder.show();


    }


    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){
            //恢复默认密码
            case 38:
                Log.i("xiugaimima",object.toString());
                Toast.makeText(this,object.toString().equals("true")? R.string.tip_operation_success:R.string.tip_operation_failed,Toast.LENGTH_LONG).show();
            break;
            case 42://还原出厂设置

                Toast.makeText(this,object.toString().equals("true")? R.string.tip_operation_success:R.string.tip_operation_failed,Toast.LENGTH_LONG).show();
             break;
        }
    }
}
