package me.lancer.airfree.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import me.lancer.airfree.util.ApplicationUtil;
import me.lancer.distance.R;

public class BrightActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    ApplicationUtil app;

    private Button btnBack;
    private SeekBar sbBright;

    private Thread mThreadClient = null;
    private String recvMessageClient = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bright);
        init();
    }

    private void init() {
        app = (ApplicationUtil) BrightActivity.this.getApplication();
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        sbBright = (SeekBar) findViewById(R.id.sb_bright);
        sbBright.setProgress(50);
        sbBright.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            setResult(RESULT_OK, null);
            finish();
        }
        mThreadClient = new Thread(mRunnable);
        mThreadClient.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
            return false;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sendMessage("bright", "" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            char[] buffer = new char[256];
            int count = 0;
            if (app.getmBufferedReaderClient() != null) {
                try {
//                    if ((count = app.getmBufferedReaderClient().read(buffer)) > 0) {
//                        recvMessageClient = getInfoBuff(buffer, count);
                    recvMessageClient = app.getmBufferedReaderClient().readLine();
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
//                    }
                } catch (Exception e) {
                    Log.e("IP & PORT", "接收异常:" + e.getMessage());
                    recvMessageClient = "接收异常:" + e.getMessage();
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("IP & PORT", "接收成功:" + recvMessageClient);
            }
        }
    };
}
