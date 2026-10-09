package dev.komeniki.adbtoggle;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/** 全ウィジェット共通: タップで切替、状態に応じた配色、一括更新。 */
abstract class BaseWidget extends AppWidgetProvider {

    private static final String ACTION_TOGGLE = "dev.komeniki.adbtoggle.TOGGLE";

    abstract RemoteViews build(Context c);

    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        m.updateAppWidget(ids, build(c));
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        if (ACTION_TOGGLE.equals(intent.getAction()) && intent.getData() != null) {
            String key = intent.getData().getLastPathSegment();
            if (key != null && !AdbSettings.toggle(c, key)) {
                Toast.makeText(c, R.string.no_permission, Toast.LENGTH_LONG).show();
            }
            refreshAll(c);
            return;
        }
        super.onReceive(c, intent);
    }

    static void refreshAll(Context c) {
        AppWidgetManager m = AppWidgetManager.getInstance(c);
        BaseWidget[] all = {
                new BarWidget(), new ListWidget(),
                new UsbWidget(), new WirelessWidget()
        };
        for (BaseWidget w : all) {
            int[] ids = m.getAppWidgetIds(new ComponentName(c, w.getClass()));
            if (ids.length > 0) m.updateAppWidget(ids, w.build(c));
        }
    }

    /** 同じ intent でも data(キー)が違えば別の PendingIntent になる。 */
    PendingIntent toggleIntent(Context c, String key) {
        Intent i = new Intent(c, getClass())
                .setAction(ACTION_TOGGLE)
                .setData(Uri.parse("adbtoggle://toggle/" + key));
        return PendingIntent.getBroadcast(c, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    /**
     * 1設定分の表示とタップ動作を設定する。0 の ID は無視。
     * icon: 円形アイコン(ON=青の円+白、OFF=灰の円+灰)、state: ON/OFF の文字、
     * sw: スイッチ画像。
     */
    void bind(Context c, RemoteViews rv, String key, int root, int icon, int state, int sw) {
        boolean on = AdbSettings.isOn(c, key);
        if (icon != 0) {
            rv.setInt(icon, "setBackgroundResource", on ? R.drawable.circle_on : R.drawable.circle_off);
            rv.setInt(icon, "setColorFilter",
                    c.getColor(on ? R.color.widget_icon_on : R.color.widget_icon_off));
        }
        if (state != 0) {
            rv.setTextViewText(state, c.getString(on ? R.string.state_on : R.string.state_off));
            rv.setTextColor(state, c.getColor(on ? R.color.widget_circle_on : R.color.widget_subtext));
        }
        if (sw != 0) {
            rv.setImageViewResource(sw, on ? R.drawable.switch_on_img : R.drawable.switch_off_img);
        }
        rv.setOnClickPendingIntent(root, toggleIntent(c, key));
    }
}
