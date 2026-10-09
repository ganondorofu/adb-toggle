package io.github.ganondorofu.adbtoggle;

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

    private static final String ACTION_TOGGLE = "io.github.ganondorofu.adbtoggle.TOGGLE";

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
                new UsbWidget(), new WirelessWidget(), new DevWidget()
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

    /** ボタン1つ分の配色・状態表示・タップ動作を設定する。0 の ID は無視。 */
    void bindButton(Context c, RemoteViews rv, String key,
                    int bg, int icon, int label, int state) {
        boolean on = AdbSettings.isOn(c, key);
        rv.setInt(bg, "setBackgroundResource",
                on ? R.drawable.widget_btn_on : R.drawable.widget_btn_off);
        int fg = c.getColor(on ? R.color.widget_on_fg : R.color.widget_off_fg);
        if (icon != 0) rv.setInt(icon, "setColorFilter", fg);
        if (label != 0) rv.setTextColor(label, fg);
        if (state != 0) {
            rv.setTextColor(state, fg);
            rv.setTextViewText(state, c.getString(on ? R.string.state_on : R.string.state_off));
        }
        rv.setOnClickPendingIntent(bg, toggleIntent(c, key));
    }
}
