package com.a51tgt.t4m.net;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.utils.CommUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//import com.mstaz.app.toolset.log.MLog;

/**
 * Created by liuke on 11/10/2016.
 */

public class SendRequest {

    private static final String TAG = "SendRequest";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Handler mHandler;
    private Integer mWhat;
    private OkHttpClientManager.Param[] mParams;
    public static Call call;

    public SendRequest(String url, OkHttpClientManager.Param[] params, Handler handler, Integer what) {
        mHandler = handler;
        mParams = params;
        mWhat = what;
        try {
            if (mParams != null && mParams.length > 0)
                postAsyn(mParams, url);
            else
                getAsyn(url);
        } catch (Exception e) {
        }
    }

    public SendRequest(String url, List<String> file_paths, OkHttpClientManager.Param[] params, Handler handler, Integer what) {
        mHandler = handler;
        mParams = params;
        mWhat = what;
        try {
            PostFile(file_paths, mParams, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void postAsyn(OkHttpClientManager.Param[] params, final String url) throws Exception {

        final Message msg = mHandler.obtainMessage();
        OkHttpClientManager.postAsyn(url, params, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                msg.obj = null;
                msg.what = mWhat;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(String response) {
                String jsonString = response;

                if (!TextUtils.isEmpty(jsonString)) {
                    msg.obj = jsonString;
                    msg.what = mWhat;
                    mHandler.sendMessage(msg);
                } else {
                    msg.obj = null;
                    msg.what = mWhat;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    private void getAsyn(final String url) throws Exception {

        final Message msg = mHandler.obtainMessage();
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                msg.obj = null;
                msg.what = mWhat;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(String response) {
                String jsonString = response;

                if (!TextUtils.isEmpty(jsonString)) {
                    msg.obj = jsonString;
                    msg.what = mWhat;
                    mHandler.sendMessage(msg);
                } else {
                    msg.obj = null;
                    msg.what = mWhat;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    public void PostFile(List<String> file_paths, OkHttpClientManager.Param[] params, final String url) throws Exception {
        final Message msg = mHandler.obtainMessage();

        File[] files = new File[file_paths.size()];
        String[] fileKeys = new String[file_paths.size()];

        for (int i = 0; i < file_paths.size(); i++) {
            File file = new File(Environment.getExternalStorageDirectory(), file_paths.get(i));
            if (file.exists()) {
                files[i] = file;
                fileKeys[i] = file.getName();
            }
        }
        OkHttpClientManager.getUploadDelegate().postAsyn(url, fileKeys, files, params, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                msg.obj = null;
                msg.what = mWhat;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(String response) {
                String jsonString = response;

                if (!TextUtils.isEmpty(jsonString)) {
                    msg.obj = jsonString;
                    msg.what = mWhat;
                    mHandler.sendMessage(msg);
                } else {
                    msg.obj = null;
                    msg.what = mWhat;
                    mHandler.sendMessage(msg);
                }

            }
        }, null);
    }

    /**
     * 下载文件
     */
    public static void downLoadFile(String fileUrl, final String destFileDir, final String fileName, final ProgressCallBack callBack)
    {
        //先删除目录及下面的所有文件
        File path = new File(destFileDir);
        CommUtil.deleteDirWihtFile(path);

        if (!path.exists())
        {
            path.mkdirs();
        }
        final File file = new File(destFileDir, fileName);
        Log.i("fnieognieognioeg",fileUrl);
        final Request request = new Request.Builder().url(fileUrl).build();
        OkHttpClient mHttpClient = new OkHttpClient();
        call = mHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                callBack.onError();
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                if(!response.isSuccessful())
                {
                    callBack.onError();
                    return;
                }
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try
                {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1)
                    {
                        current += len;
                        fos.write(buf, 0, len);
                        callBack.onProgress(total, current);
                    }
                    callBack.onComplete();
                    fos.flush();
                } catch (IOException e)
                {
                    callBack.onError();
                } finally
                {
                    try
                    {
                        if (is != null)
                        {
                            is.close();
                        }
                        if (fos != null)
                        {
                            fos.close();
                        }
                    } catch (IOException e)
                    {
                        callBack.onError();
                    }
                }
            }
        });
    }

    public interface ProgressCallBack
    {
        void onProgress(long total, long current);

        void onComplete();

        void onError();
    }

}
