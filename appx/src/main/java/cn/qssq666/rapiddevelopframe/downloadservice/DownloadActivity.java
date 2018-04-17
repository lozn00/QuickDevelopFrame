package cn.qssq666.rapiddevelopframe.downloadservice;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    public void onClick(View v) {

    }
/*    private Button btnStart;
    private Button btnPause;
    private Button btnCancel;
    private DownloadService.DownloadBinder mDownloadBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mDownloadBinder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.btn_start_download);
        btnPause = findViewById(R.id.btn_pause_download);
        btnCancel = findViewById(R.id.btn_cancel_download);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Intent downloadService = new Intent(this, DownloadService.class);
        startService(downloadService);
        bindService(downloadService,serviceConnection,BIND_AUTO_CREATE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onClick(View view) {
        if(mDownloadBinder ==null){
            return;
        }
        switch (view.getId()){
            case R.id.btn_start_download:
//                String url ="https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                String url ="http://bky-test.oss-cn-beijing.aliyuncs.com/app/yunguanjia-dev-debug.apk";
                mDownloadBinder.startDownload(url);
                break;
            case R.id.btn_pause_download:
                mDownloadBinder.pauseDownload();
                break;
            case R.id.btn_cancel_download:
                mDownloadBinder.cancelDownload();
                break;
                default:
                    break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }*/
}
