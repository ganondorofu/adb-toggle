package io.github.ganondorofu.adbtoggle;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

/** Settings.Global 上の ADB 関連スイッチを読み書きする。 */
final class AdbSettings {

    static final String USB = "adb_enabled";
    static final String WIRELESS = "adb_wifi_enabled";
    static final String DEV = "development_settings_enabled";

    static final String PERMISSION = "android.permission.WRITE_SECURE_SETTINGS";

    private AdbSettings() {}

    static boolean hasPermission(Context c) {
        return c.checkSelfPermission(PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    static boolean isOn(Context c, String key) {
        return Settings.Global.getInt(c.getContentResolver(), key, 0) > 0;
    }

    /** 書き込みに成功したら true。権限がなければ false。 */
    static boolean set(Context c, String key, boolean on) {
        try {
            boolean ok = Settings.Global.putInt(c.getContentResolver(), key, on ? 1 : 0);
            refreshWidgets(c);
            return ok;
        } catch (SecurityException e) {
            return false;
        }
    }

    static boolean toggle(Context c, String key) {
        return set(c, key, !isOn(c, key));
    }

    static String grantCommand(Context c) {
        return "adb shell pm grant " + c.getPackageName() + " " + PERMISSION;
    }

    static void refreshWidgets(Context c) {
        AppWidgetManager m = AppWidgetManager.getInstance(c);
        int[] ids = m.getAppWidgetIds(new ComponentName(c, ToggleWidget.class));
        if (ids.length > 0) ToggleWidget.update(c, m, ids);
    }
}
