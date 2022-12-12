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

/**
 * @file Bluetooth.java
 * This file imports TinyB - the Tiny Bluetooth LE Library:
 * https://github.com/intel-iot-devkit/tinyb
 */

package tumbller;

import tinyb.*;
import java.time.Duration;

public class Bluetooth {
  private static final String DEVICE_NAME = "ELEGOO BT16";
  private static final String SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
  private static final String CHARACTERISTIC = "0000ffe2-0000-1000-8000-00805f9b34fb";
  private static final byte SWITCH_TO_SPEED_CONTROL = '+';
  private static final byte EXIT_SPEED_CONTROL = -128;
  private static BluetoothDevice device;
  private static BluetoothGattCharacteristic characteristic;

  static boolean start() {
    try {
      BluetoothManager manager = BluetoothManager.getBluetoothManager();

      if(manager.startDiscovery()) {
        device = manager.find(DEVICE_NAME, null, null, Duration.ofSeconds(30));
        manager.stopDiscovery();

        if(device != null && device.connect()) {
          BluetoothGattService service = device.find(SERVICE, Duration.ofSeconds(10));
          if(service != null) {
            characteristic = service.find(CHARACTERISTIC, Duration.ofSeconds(10));
            if(characteristic != null) {
              characteristic.writeValue(new byte[]{ SWITCH_TO_SPEED_CONTROL });
              return true;
            }
          }
        }
      }
    }
    catch(BluetoothException e) {
      e.printStackTrace();
    }
    return false;
  }

  static void stop() {
    sendControlValue(EXIT_SPEED_CONTROL, (byte) 0);
    device.disconnect();
  }

  static void sendControlValue(byte first, byte second) {
    byte[] ctrlVal = { first, second };
    characteristic.writeValue(ctrlVal);
  }
}