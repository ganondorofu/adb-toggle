package io.github.ganondorofu.adbtoggle;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

abstract class BaseTile extends TileService {

    /** Settings.Global のキー名 */
    abstract String settingKey();

    @Override
    public void onStartListening() {
        refresh();
    }

    @Override
    public void onClick() {
        if (!AdbSettings.toggle(this, settingKey())) {
            Toast.makeText(this, R.string.no_permission, Toast.LENGTH_LONG).show();
        }
        refresh();
    }

    private void refresh() {
        Tile tile = getQsTile();
        if (tile == null) return;
        tile.setState(AdbSettings.isOn(this, settingKey()) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }
}
