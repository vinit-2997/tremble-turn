package com.trembleturn.trembleturn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by
 *
 * @author Sarfaraz Iraqui
 * @email sarfarazghlm@gmail.com
 * on 2/2/19
 */
public class BaseActivity extends AppCompatActivity {

    public void saveLogcatToFile(Context context) throws IOException {
        String fileName = "logcat_"+System.currentTimeMillis()+".log";
        File outputFile = new File(context.getExternalCacheDir(), fileName);
        @SuppressWarnings("unused")
        Process process = Runtime.getRuntime().exec("logcat -df "+outputFile.getAbsolutePath());
    }
}
