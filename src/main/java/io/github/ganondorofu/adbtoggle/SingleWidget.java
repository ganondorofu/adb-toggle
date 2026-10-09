package io.github.ganondorofu.adbtoggle;

import android.content.Context;
import android.widget.RemoteViews;

/** 1つの設定だけを切り替えるウィジェットの共通実装。 */
abstract class SingleWidget extends BaseWidget {

    abstract String key();

    abstract int icon();

    abstract int label();

    @Override
    RemoteViews build(Context c) {
        RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widget_single);
        rv.setImageViewResource(R.id.s_icon, icon());
        rv.setTextViewText(R.id.s_label, c.getString(label()));
        bind(c, rv, key(), R.id.s_root, R.id.s_icon, R.id.s_state, 0);
        return rv;
    }
}
