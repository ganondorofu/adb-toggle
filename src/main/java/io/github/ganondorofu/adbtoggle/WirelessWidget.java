package io.github.ganondorofu.adbtoggle;

public class WirelessWidget extends SingleWidget {
    @Override String key() { return AdbSettings.WIRELESS; }
    @Override int icon() { return R.drawable.ic_wifi; }
    @Override int label() { return R.string.label_wireless; }
}
