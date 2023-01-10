/*
 * tumbller-speed-control
 * Copyright (c) 2022 Peter Nebe (mail@peter-nebe.dev)
 *
 * This file is part of tumbller-speed-control.
 *
 * tumbller-speed-control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * tumbller-speed-control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with tumbller-speed-control.  If not, see <https://www.gnu.org/licenses/>.
 */

package tumbller.speed_control;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bluetooth.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bluetooth.stop();
    }

    public void btnUpClick(View view) {
        Bluetooth.sendCommand(BluetoothKt.COMMAND_UP);
    }

    public void btnDownClick(View view) {
        Bluetooth.sendCommand(BluetoothKt.COMMAND_DOWN);
    }

    public void btnCancelClick(View view) {
        ProgrammableMotion.cancel();
    }
}