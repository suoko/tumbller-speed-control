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

#define EXIT -128
#define DOWN -127
#define UP   -126
#define SPECIAL_MAX UP

bool (*bluetoothFunc)() = getBluetoothData;
bool speedControl();

void switchToSpeedControl()
{
  bluetoothFunc = speedControl;
  Serial.println("switchToSpeedControl");
}

void exitSpeedControl()
{
  bluetoothFunc = getBluetoothData;
  Serial.println("exitSpeedControl");
}

void handleSpeed(const char speed[])
{
  setting_car_speed = speed[0];
  setting_turn_speed = speed[1];
  motion_mode = SPEED_CONTROL;
  Serial.print("speed car:"); Serial.print(setting_car_speed);
  Serial.print(" turn:"); Serial.println(setting_turn_speed);
}

void handleSpecialCommand(char command)
{
  switch(command)
  {
    case EXIT:
      key_value = 's';
      exitSpeedControl();
      break;
    case DOWN:
      key_value = '4';
      Serial.println("DOWN");
      break;
    case UP:
      key_value = '5';
      Serial.println("UP");
      break;
  }
}

bool speedControl()
{
  if(Serial.available() > 1)
  {
    char bytes[2];
    Serial.readBytes(bytes, 2);

    if(bytes[0] > SPECIAL_MAX)
      handleSpeed(bytes);
    else
      handleSpecialCommand(bytes[0]);

    return true;
  }
  return false;
}
