package com.artofliving.volunteer.sleepwithwisdom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.artofliving.volunteer.sleepwithwisdom.includes.Utils;

public class SplashActivity extends Activity {

    // DO NOT APPEND '/' at end here
    private String DIRECTORY_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/Sleep";

    // APPEND '/' at end here
    private String MEDIA_DIRECTORY_URL_PATH = "https://s.artofliving.org/sleepwithwisdom/";
    private String[] audioFiles;
    private Handler handler;
    private ProgressDialog pDialog;
    Thread timer;

    // Progress dialog type
    public static final int progress_bar_type = 0;

    // a counter for number of files downloaded
    private int i = 0, fileSize = 2871798;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // making Splash FULL screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // get the audio files names
        audioFiles = this.getResources().getStringArray(
                R.array.audio_files_mp3);

        // Set View
        setContentView(R.layout.splash_screen);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivityForResult(i, 0);
                return false;
            }
        });

        timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(new Message());
                }
            }
        }; // timer is started in fileExists function

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialogUtil.showPermissionDialog(SplashActivity.this, UIConstants.PERMISSIONS_REQUEST_CODE_READ_EXTERNAL_STORAGE, "Storage Permission", "Please allow storage access to download media files.", permissions);
            } else {
                requestPermissions(permissions, UIConstants.PERMISSIONS_REQUEST_CODE_READ_EXTERNAL_STORAGE);
            }
        } else {
            File myDir = new File(DIRECTORY_PATH + "/");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            // contains Timer.start()
            fileExists(audioFiles[i]);
        }
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message)
                .setNeutralButton("EXIT", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // exit application
                        Toast.makeText(getApplicationContext(),
                                "HAVE A GREAT DAY!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        AlertDialog dismissApp;
        dismissApp = builder.create();
        dismissApp.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(), "HAVE A GREAT DAY!",
                Toast.LENGTH_SHORT).show();
        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        pDialog = new ProgressDialog(this);
        switch (id) {
            case 1:
                pDialog.setMessage("Downloading " + String.valueOf(i + 1)
                        + " of 2 audio file(s)");
                break;
            case 0:
                pDialog.setMessage("Downloading " + String.valueOf(i)
                        + " of 2 audio files");
                break;
        }

        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgressNumberFormat(null);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        return pDialog;
    }

    private class downloadAudioFiles extends AsyncTask<String, String, String> {

        int corrupt;

        public downloadAudioFiles(boolean b) {
            if (b)
                corrupt = 1;
            else
                corrupt = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(corrupt);
        }

        @Override
        protected String doInBackground(String... file_name) {

            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(MEDIA_DIRECTORY_URL_PATH + file_name[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }

                // getting file length
                int lenghtOfFile = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(DIRECTORY_PATH + "/"
                        + file_name[0]);

                byte data[] = new byte[1024];

                long total = 0;

                int count;

                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    // but for now back button has been disable in dialog
                    if (isCancelled()) {
                        output.flush();
                        output.close();
                        input.close();
                        return "This is sample string so that app does not give Illegal Argument Exception in postExecute";
                    }
                    total += count;
                    // publishing the progress....
                    if (lenghtOfFile > 0) // only if total length is known
                        publishProgress(""
                                + (int) ((total * 100) / lenghtOfFile));

                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) {
                        output.flush();
                        output.close();
                    }
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return "This is sample string so that app does not give Illegal Argument Exception in postExecute";

        }

        @Override
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String result) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (i < audioFiles.length) {
                fileExists(audioFiles[i]);
            } else if (i == audioFiles.length) {
                Intent intent = new Intent(SplashActivity.this,
                        HomeActivity.class);
                startActivityForResult(intent, 0);
            }

            super.onPostExecute(result);

        }
    }

    protected void fileExists(String file_name) {
        if (i < audioFiles.length) {
            File file = new File(DIRECTORY_PATH + "/" + file_name);
            if (file.exists() && file.length() == fileSize) {
                // check if it is the last file because
                // audioFiles[++i] will make is out of bounds
                if (i == audioFiles.length - 1) {
                    timer.start();
                } else
                    fileExists(audioFiles[++i]);
            } else {
                if (Utils.isInternetAvailable(SplashActivity.this)) {
                    if (file.length() != fileSize) {
                        // Delete the corrupt file
                        file.delete();
                        // download the same file again
                        new downloadAudioFiles(true).execute(audioFiles[i]);
                    } else {
                        // Download the next file
                        new downloadAudioFiles(false).execute(audioFiles[i++]);
                    }
                } else {
                    String title = "INTERNET REQUIRED";
                    String message = "Hi! the App needs to download audio files. "
                            + "Please make sure you are connected to Internet.";
                    showDialog(title, message);
                }
            }
        } else {
            timer.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
