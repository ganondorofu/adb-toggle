package io.github.ganondorofu.adbtoggle;

import android.content.Context;
import android.widget.RemoteViews;

/** 3ボタンを横に並べたウィジェット。 */
public class BarWidget extends BaseWidget {
    @Override
    RemoteViews build(Context c) {
        RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widget_bar);
        bindButton(c, rv, AdbSettings.USB, R.id.bar_usb, R.id.bar_usb_icon, R.id.bar_usb_label, 0);
        bindButton(c, rv, AdbSettings.WIRELESS, R.id.bar_wireless, R.id.bar_wireless_icon, R.id.bar_wireless_label, 0);
        bindButton(c, rv, AdbSettings.DEV, R.id.bar_dev, R.id.bar_dev_icon, R.id.bar_dev_label, 0);
        return rv;
    }
}
