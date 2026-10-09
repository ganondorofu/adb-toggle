package dev.komeniki.adbtoggle;

import android.content.Context;
import android.widget.RemoteViews;

/** 2ボタンを横に並べたウィジェット。 */
public class BarWidget extends BaseWidget {
    @Override
    RemoteViews build(Context c) {
        RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widget_bar);
        bind(c, rv, AdbSettings.USB, R.id.bar_usb, R.id.bar_usb_icon, 0, 0);
        bind(c, rv, AdbSettings.WIRELESS, R.id.bar_wireless, R.id.bar_wireless_icon, 0, 0);
        return rv;
    }
}
