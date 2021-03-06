package yunseok.de.acPocketCamp;

import android.app.ActivityManager;
import android.support.v8.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isDeviceRooted()) {
            try {
                killApp();
                deleteXml();
                startApp();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Device is not rooted.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void killApp() {
        ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).killBackgroundProcesses("com.nintendo.zaba");
    }

    private void startApp() {
        startActivity(getPackageManager().getLaunchIntentForPackage("com.nintendo.zaba"));
    }

    private boolean deleteXml() throws Exception {
        final Process su = Runtime.getRuntime().exec("su");
        final DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
        outputStream.writeBytes("rm /data/data/com.nintendo.zaba/shared_prefs/deviceAccount:.xml\n");
        outputStream.flush();
        outputStream.writeBytes("exit\n");
        outputStream.close();
        return su.waitFor() == 0;
    }

    private boolean isDeviceRooted() {
        return checkRootMethodOne() || checkRootMethodTwo();
    }

    private boolean checkRootMethodOne() {
        return android.os.Build.TAGS != null && android.os.Build.TAGS.contains("test-keys");
    }

    private boolean checkRootMethodTwo() {
        final String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }
}
