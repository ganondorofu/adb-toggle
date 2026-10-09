package io.github.ganondorofu.adbtoggle;

public class DevWidget extends SingleWidget {
    @Override String key() { return AdbSettings.DEV; }
    @Override int icon() { return R.drawable.ic_code; }
    @Override int label() { return R.string.label_dev; }
}
