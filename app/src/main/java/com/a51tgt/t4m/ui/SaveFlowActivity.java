package com.a51tgt.t4m.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;

public class SaveFlowActivity extends BaseActivity implements OnNoticeUI{
private TextView  saveFlowText;
private ToggleButton  saveFlowBtn;
private DeviceSetDialog setDialog = new DeviceSetDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitu_save_flow_layout);
saveFlowBtn = (ToggleButton) findViewById(R.id.tb_open_flow);
saveFlowBtn.setChecked(APIConstants.deviceInfo.isBlockSwitch()?true:false);
MZApplication.getInstance().setOnNoticeUiListener(this,OnNoticeUI.KEY_TYPE_SAVE_FLOW );
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
saveFlowBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){

            setDialog.setSaveFlow(true);
        }
        else {
            setDialog.setSaveFlow(false);

        }
    }
});

    }

    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        switch (NOTICE_TYPE){
            //省流量模式
            case 40:
//                Log.i("xiugaimima",object.toString());
                Toast.makeText(this,object.toString().equals("true")? R.string.tip_operation_success:R.string.tip_operation_failed,Toast.LENGTH_SHORT).show();;
                break;
        }
    }
}
