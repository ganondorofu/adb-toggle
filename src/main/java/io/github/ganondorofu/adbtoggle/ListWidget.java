package io.github.ganondorofu.adbtoggle;

import android.content.Context;
import android.widget.RemoteViews;

/** 2行の縦リスト。各行にスイッチ表示が付く。 */
public class ListWidget extends BaseWidget {
    @Override
    RemoteViews build(Context c) {
        RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widget_list);
        bind(c, rv, AdbSettings.USB, R.id.list_usb, R.id.list_usb_icon, 0, R.id.list_usb_switch);
        bind(c, rv, AdbSettings.WIRELESS, R.id.list_wireless, R.id.list_wireless_icon, 0, R.id.list_wireless_switch);
        return rv;
    }
}
