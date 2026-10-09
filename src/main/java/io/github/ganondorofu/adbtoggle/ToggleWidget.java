package io.github.ganondorofu.adbtoggle;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ToggleWidget extends AppWidgetProvider {

    private static final String ACTION_TOGGLE = "io.github.ganondorofu.adbtoggle.TOGGLE";
    private static final String EXTRA_KEY = "key";

    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        update(c, m, ids);
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        if (ACTION_TOGGLE.equals(intent.getAction())) {
            String key = intent.getStringExtra(EXTRA_KEY);
            if (key != null && !AdbSettings.toggle(c, key)) {
                Toast.makeText(c, R.string.no_permission, Toast.LENGTH_LONG).show();
            }
            AdbSettings.refreshWidgets(c);
            return;
        }
        super.onReceive(c, intent);
    }

    static void update(Context c, AppWidgetManager m, int[] ids) {
        RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widget_toggle);
        bind(c, rv, R.id.w_usb, AdbSettings.USB, 0);
        bind(c, rv, R.id.w_wireless, AdbSettings.WIRELESS, 1);
        bind(c, rv, R.id.w_dev, AdbSettings.DEV, 2);
        m.updateAppWidget(ids, rv);
    }

    private static void bind(Context c, RemoteViews rv, int viewId, String key, int requestCode) {
        boolean on = AdbSettings.isOn(c, key);
        rv.setInt(viewId, "setBackgroundResource",
                on ? R.drawable.widget_btn_on : R.drawable.widget_btn_off);
        rv.setTextColor(viewId, c.getColor(on ? R.color.widget_text_on : R.color.widget_text_off));

        Intent i = new Intent(c, ToggleWidget.class)
                .setAction(ACTION_TOGGLE)
                .putExtra(EXTRA_KEY, key);
        PendingIntent pi = PendingIntent.getBroadcast(c, requestCode, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        rv.setOnClickPendingIntent(viewId, pi);
    }
}
