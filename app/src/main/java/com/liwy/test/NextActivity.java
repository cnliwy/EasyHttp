package com.liwy.test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liwy.easyhttp.EasyHttp;
import com.liwy.easyhttp.base.EasyFile;
import com.liwy.easyhttp.callback.DownloadCallback;
import com.liwy.easyhttp.callback.ErrorCallback;
import com.liwy.easyhttp.callback.SuccessCallback;
import com.liwy.test.bean.TestResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NextActivity extends AppCompatActivity {
    TextView contentTv;
    Button downloadBtn;
    Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        downloadBtn = (Button)findViewById(R.id.btn_download);
        uploadBtn = (Button)findViewById(R.id.btn_upload);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("上传中");
                upload();
            }
        });
        contentTv = (TextView)findViewById(R.id.tv_content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyHttp.getInstance().cancelHttp("update");
    }

    /**
     * use the get request of EasyHttp
     */
    public void get(){
        Map<String,Object> params = new HashMap<>();
        params.put("ver","1");
        EasyHttp.getInstance().get("/login/update", params,"update","", new SuccessCallback<TestResult>() {
            @Override
            public void success(TestResult result) {
//                    JSONObject jsonObject = new JSONObject(result);
                    System.out.println("next result = " + result);
            }
        }, new ErrorCallback() {
            @Override
            public void error(Object... values) {
                System.out.println(values[0].toString());
            }
        });
    }

    public void download(){
//        String url = "http://img5q.duitang.com/uploads/item/201506/23/20150623203928_HzBWU.jpeg";
        String url = "item/201506/23/20150623203928_HzBWU.jpeg";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/aliwy";
        String fileName = "20150623203928_HzBWU.jpeg";
        System.out.println("save path :" + filePath);
        EasyHttp.getBuilder()
                .setFileUrl(url)
                .setFileName(fileName)
                .setTag("download")
                .setFilePath(filePath)
                .setDownloadCallback(new DownloadCallback<File>() {
                    @Override
                    public void onSuccess(File o) {
                        System.out.println("---->下载成功" + o.getAbsolutePath());
                    }

                    @Override
                    public void onError(String err) {
                        System.out.println("---->下载失败");
                    }

                    @Override
                    public void onProgress(long total, long current) {
                        System.out.println("---->total=" + total + ",current=" + current);
                    }
                })
                .download();
    }
    // upload files
    public void upload(){
        String url = "http://192.168.131.19:8886/login/upload";
        // 参数
        Map<String,Object> params = new HashMap<>();
        params.put("title","upload head icon and apk");
        params.put("uploadUser","cnliwy");
        params.put("uploadType","image and apk");

        List<EasyFile> files = getFiles();
        EasyHttp.getInstance().postFile(url, params, files, "upload","", new SuccessCallback<String>() {
            @Override
            public void success(String result) {
                System.out.println("上传成功，" + result);
                contentTv.setText("上传成功，" + result);
            }
        }, new ErrorCallback() {
            @Override
            public void error(Object... values) {
                System.out.println(values[0].toString());
                contentTv.setText("上传失败"+ values[0].toString());
            }
        });
    }
    public List<EasyFile> getFiles(){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/aliwy/";
        List<EasyFile> files = new ArrayList<>();
        files.add(new EasyFile("bgimg",filePath + "guide_one.png","image/png",new File(filePath + "guide_one.png")));
//        files.add(new EasyFile("filw2",filePath + "guide_two.png","image/png",new File(filePath + "guide_two.png")));
        files.add(new EasyFile("fil2",filePath + "test1.apk","application/vnd.android.package-archive",new File(filePath + "test.apk")));
//        files.add(new EasyFile("bgimg",filePath + "ic_launcher.png","image/png",new File(filePath + "ic_launcher.png")));
        return files;
    }

}
