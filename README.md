# tumbller-speed-control
Speed control for Elegoo Tumbller

**tumbller-speed-control** allows direct manual control of the Tumbller via an Android device. Because the driving speed and turning speed can be controlled independently, it can be used to continuously make turns and straight sections. Difficult maneuvers are also possible thanks to precise control, even at low speeds.

tumbller-speed-control includes an extension of the included Arduino sketch and an Android app.

### The Arduino sketch
Only the *.ino* file and *mode.h* have been changed. *SpeedControl.h* is a new additional file. The other files of the original sketch are taken over unchanged in order to load the program onto the Tumbller with the Arduino IDE. With the new program, the functions and modes of the old program will continue to work. Also the Elegoo BLE Tool works (if the tumbller-speed-control app is not running).

### The Android app
The *android-app* folder contains the changed and additional files of a standard Android application. Create a new Empty Activity project with Android Studio and copy the files from *android-app* into it. Build the project and run the app. Under App permissions, allow Location. First switch on the Tumbller, then close the Elegoo BLE Tool if it is running, start the **tumbller-speed-control** app and check whether the Bluetooth LED on the Tumbller is lit.

<img src="android-app/screenshot.png" height="400" />

Now the Tumbller can be moved with the red dot. With the DOWN button it can be placed in the rest position (function 4 - Tilt Forward). Press UP and it gets back up (function 5 - Stand Up Instantly).

The Rocker Control in the Elegoo BLE Tool looks good. But it is not possible to go from cornering to driving straight ahead without stopping in between. It's difficult for old people with slow sausage fingers like me to steer the vehicle with it.

**Update:** The **tumbller-speed-control** app can now also control a programmed motion sequence. Copy a CSV file named *motion.prog* into the app's data folder (*\<internal shared storage\>/Android/data/tumbller.speed_control/files*). You can find an example [here](programmable-motion/motion.prog). Each row contains three values: waiting time in tenths of a second, car speed, and turn speed. Allow the app's Storage permission. Double tap the control panel. Here is an example of a run:

<img src="android-app/screenshot-progmotion.png" height="400" />

### Extra: programmable motion for Linux
Install TinyB from https://github.com/intel-iot-devkit/tinyb

Compile it, for example with these commands:
```
cd programmable-motion
javac -cp /usr/local/lib/java/tinyb.jar -d bin *.java
```
Run it, for example:
```
java -cp bin:/usr/local/lib/java/tinyb.jar -Djava.library.path=/usr/local/lib tumbller.ProgrammableMotion
```
Have fun!

#### Tumbller in action
https://youtu.be/U0L_Iky_Qdk
