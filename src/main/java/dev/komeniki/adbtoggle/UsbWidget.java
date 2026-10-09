package dev.komeniki.adbtoggle;

public class UsbWidget extends SingleWidget {
    @Override String key() { return AdbSettings.USB; }
    @Override int icon() { return R.drawable.ic_usb; }
    @Override int label() { return R.string.label_usb; }
}
