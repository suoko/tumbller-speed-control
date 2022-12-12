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

package tumbller;

import java.io.*;

public class ProgrammableMotion {
  public static void main(String[] args) {
    try {
      final String filename = "motion.prog";
      try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {

        if(!Bluetooth.start()) {
          System.err.println("Tumbller not connected");
          return;
        }

        String line;
        while((line = reader.readLine()) != null) {
          String[] item = line.split(",");
          if(item.length == 3) {
            int wait = Integer.parseInt(item[0]);
            byte carSpeed = Byte.parseByte(item[1]);
            byte turnSpeed = Byte.parseByte(item[2]);

            System.out.printf("wait: %3d, car: %3d, turn: %3d\n", wait, carSpeed, turnSpeed);
            Thread.sleep(wait * 100);

            Bluetooth.sendControlValue(carSpeed, turnSpeed);
          }
        }

        Thread.sleep(1000);
        Bluetooth.stop();
      }
    }
    catch(Throwable t) {
      t.printStackTrace();
    }
  }
}