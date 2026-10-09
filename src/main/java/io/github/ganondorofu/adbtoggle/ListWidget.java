package io.github.ganondorofu.adbtoggle;

import android.content.Context;
import android.widget.RemoteViews;

/** 2行の縦リスト。各行に ON/OFF 表示が付く。 */
public class ListWidget extends BaseWidget {
    @Override
    RemoteViews build(Context c) {
        RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widget_list);
        bindButton(c, rv, AdbSettings.USB, R.id.list_usb, R.id.list_usb_icon, R.id.list_usb_label, R.id.list_usb_state);
        bindButton(c, rv, AdbSettings.WIRELESS, R.id.list_wireless, R.id.list_wireless_icon, R.id.list_wireless_label, R.id.list_wireless_state);
        return rv;
    }
}
