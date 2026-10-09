package io.github.ganondorofu.adbtoggle;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private View permMissing;
    private View permOk;
    private Switch swUsb;
    private Switch swWireless;
    private Switch swDev;

    private final ContentObserver observer = new ContentObserver(new Handler(Looper.getMainLooper())) {
        @Override
        public void onChange(boolean selfChange) {
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permMissing = findViewById(R.id.perm_missing);
        permOk = findViewById(R.id.perm_ok);
        swUsb = findViewById(R.id.sw_usb);
        swWireless = findViewById(R.id.sw_wireless);
        swDev = findViewById(R.id.sw_dev);

        final String cmd = AdbSettings.grantCommand(this);
        ((TextView) findViewById(R.id.perm_command)).setText(cmd);
        findViewById(R.id.perm_copy).setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("adb", cmd));
            Toast.makeText(this, R.string.perm_copied, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.open_dev).setOnClickListener(v -> {
            try {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
            } catch (Exception e) {
                Toast.makeText(this, R.string.open_dev_failed, Toast.LENGTH_SHORT).show();
            }
        });

        bind(swUsb, AdbSettings.USB);
        bind(swWireless, AdbSettings.WIRELESS);
        bind(swDev, AdbSettings.DEV);
    }

    private void bind(Switch sw, final String key) {
        sw.setOnCheckedChangeListener((CompoundButton b, boolean checked) -> {
            if (!AdbSettings.set(this, key, checked)) {
                Toast.makeText(this, R.string.no_permission, Toast.LENGTH_LONG).show();
            }
            refresh();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (String key : new String[]{AdbSettings.USB, AdbSettings.WIRELESS, AdbSettings.DEV}) {
            getContentResolver().registerContentObserver(
                    Settings.Global.getUriFor(key), false, observer);
        }
        refresh();
    }

    @Override
    protected void onStop() {
        getContentResolver().unregisterContentObserver(observer);
        super.onStop();
    }

    private void refresh() {
        boolean granted = AdbSettings.hasPermission(this);
        permMissing.setVisibility(granted ? View.GONE : View.VISIBLE);
        permOk.setVisibility(granted ? View.VISIBLE : View.GONE);
        set(swUsb, AdbSettings.USB, granted);
        set(swWireless, AdbSettings.WIRELESS, granted);
        set(swDev, AdbSettings.DEV, granted);
        AdbSettings.refreshWidgets(this);
    }

    /** リスナーを呼ばずに状態だけ反映する。 */
    private void set(Switch sw, String key, boolean enabled) {
        sw.setOnCheckedChangeListener(null);
        sw.setChecked(AdbSettings.isOn(this, key));
        sw.setEnabled(enabled);
        bind(sw, key);
    }
}
