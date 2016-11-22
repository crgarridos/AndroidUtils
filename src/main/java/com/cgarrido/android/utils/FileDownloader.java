package com.cgarrido.android.utils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FileDownloader extends AsyncTask<Void, Integer, File> {

    private static final String TAG = FileDownloader.class.getSimpleName();
    private static final int KILOBYTE = 1024 * 1024;
    private static final long TIMEOUT_DIALOG = 10000;

    private String mFileUrl;
    private String mDestinationDirectory;
    private DownloadListener mListener;
    private boolean mOverrideIfExists = true;
    private int mFileSizeCheck = -1;
    private ProgressDialog mProgressDialog;
    private String downloadUrl;


    public FileDownloader(String fileUrl, String destinationDirectory, DownloadListener listener) {
        mFileUrl = fileUrl;
        mDestinationDirectory = destinationDirectory;
        mListener = listener;
    }

    /**
     * Use as your own risk, this client will become https completely vulnerable to be insecure and trust any SSL certificate
     */
    private static void enableInsecureSSL() {
        Log.d(TAG, "enableInsecureSSL");
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String deduceFileName(String fileUrl) {
        try {
            String[] splatted = URLDecoder.decode(fileUrl, "UTF-8").split("/");
            return splatted[splatted.length - 1];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "DownloadedFile-" + DateUtils.formatDate(Calendar.getInstance().getTime(), "yyyyMMdd");
        }
    }

    public FileDownloader enableAbortIfFileExists(int fileSizeCheck) {
        mOverrideIfExists = false;
        mFileSizeCheck = fileSizeCheck;
        return this;
    }

    public void download() {
        this.execute();
        if (mProgressDialog != null) {
            mProgressDialog.setMessage("Downloading " + deduceFileName(mFileUrl));
            mProgressDialog.show();
//            new CountDownTimer(TIMEOUT_DIALOG, TIMEOUT_DIALOG + 1) {
//                public void onTick(long ignored) {
//                }
//
//                @Override
//                public void onFinish() {
//                    if (mProgressDialog.isShowing()) {
//                        AndroidUtils.toast("timeout " + mProgressDialog.getProgress());
//                        mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
//                    }
//                }
//            }.start();
        }
        Log.d(TAG, "Start download from " + mFileUrl);
    }

    private void downloadFile(String fileUrl, File outputFile) throws IOException {
        Log.d(TAG, "Downloading from " + fileUrl);
        fileUrl = fixFileNameInURL(fileUrl, outputFile);
        URL url = new URL(fileUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        int totalSize = urlConnection.getContentLength();
        totalSize = totalSize > 0 || mFileSizeCheck < 0 ? totalSize : mFileSizeCheck;

        byte[] buffer = new byte[KILOBYTE];
        int bufferLength;
        int bufferCount = 0;
        while ((bufferLength = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, bufferLength);
            bufferCount += bufferLength;
            publishProgress(bufferLength, bufferCount, totalSize);
            if (isCancelled()) {
                urlConnection.disconnect();
                inputStream.close();
                fileOutputStream.close();
                throw new IOException("Download canceled manually");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fileOutputStream.close();
        inputStream.close();
        urlConnection.disconnect();
    }

    private String fixFileNameInURL(String url, File fullPathFile) throws UnsupportedEncodingException {
        String fileName = fullPathFile.getName();
        return url.replace(fileName, URLEncoder.encode(fileName, "UTF-8"));
    }

    @Override
    protected File doInBackground(Void... strings) {
        File folder = new File(mDestinationDirectory);
        try {
            if (folder.exists() || folder.mkdir()) {
                File pdfFile = new File(folder, deduceFileName(mFileUrl));
                if (pdfFile.exists()) {
                    Log.d(TAG, "Try download from " + mFileUrl + ", sizes => wanted: " + mFileSizeCheck + " - existent: " + pdfFile.length());
                    if (mOverrideIfExists) {
                        pdfFile.delete();
                    } else if (mFileSizeCheck != pdfFile.length()) {
                        Log.d(TAG, "Force download from " + mFileUrl + ", enableAbortIfFileExists is setted but size check failed ! ");
                        pdfFile.delete();
                    } else {
                        Log.d(TAG, "Download aborted from " + mFileUrl + " (File already exists, use enableAbortIfFileExists(0) to force the download)");
                        mListener.onDownloadProgress(mFileSizeCheck, mFileSizeCheck, mFileSizeCheck);
                        return pdfFile;
                    }
                }
                if (pdfFile.createNewFile()) {
                    try {
//                        enableInsecureSSL();
                        downloadFile(mFileUrl, pdfFile);
                        return pdfFile;
                    } catch (IOException e) {
                        Log.d(TAG, "Download failed from " + mFileUrl + " " + e.toString());
                        e.printStackTrace();
                        mListener.onDownloadFailed(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int read = values[0];
        int progress = values[1];
        int total = values[2];
        mListener.onDownloadProgress(read, progress, total);
        int percent = (int) ((progress * 1.0 / total) * 100);
        if (mProgressDialog != null)
            mProgressDialog.setProgress(percent);
//        Log.d(TAG, "Download progress from " + mFileUrl + " (" + percent + "%)");
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        Log.d(TAG, "Download finished from " + mFileUrl + " (" + (result != null ? "success" : "failed") + ")");
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        if (result != null)
            mListener.onDownloaded(result);
        else
            mListener.onDownloadFailed(isCancelled());
    }

    public FileDownloader enableProgressDialog(ProgressDialog progressDialog) {
        this.mProgressDialog = progressDialog;
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        mProgressDialog.setCancelable(true);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        mProgressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "holq00", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                AndroidUtils.toast("chao");
//                mProgressDialog.show();
//            }
//        });
//        mProgressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
//            }
//        });
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                FileDownloader.this.cancel(true);
            }
        });
        return this;
    }

    @Override
    protected void onCancelled(File file) {
        super.onCancelled(file);
        mListener.onDownloadFailed(true);
    }

    public interface DownloadListener {
        void onDownloaded(File outputFile);

        void onDownloadFailed(boolean cancelled);

        void onDownloadProgress(int read, int progress, int total);
    }
}