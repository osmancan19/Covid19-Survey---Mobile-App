# Requirements

1. Yarn or NPM
2. A Physical Android Device with USB Debugging enabled or an emulator
3. ADB
4. Simulator for IOS tests

# Running Android Tests

To test on real devices check if your device is connected by running

`adb devices`

or launch an Android Emulator.

To run the tests execute,

`cd Appium`

`yarn test:android` or `npm run test:android`

## Android Requirements

In PATH settings, `ANDROID_SDK_ROOT` and `ANDROID_HOME` should be set to `C:\Users\%USERNAME%\AppData\Local\Android\Sdk`

like in the example:

    PATH VARIABLE: ANDROID_SDK_ROOT

    PATH VALUE: C:\Users\Osmancan\AppData\Local\Android\Sdk

    ------------------------------

    PATH VARIABLE: ANDROID_HOME

    PATH VALUE: C:\Users\Osmancan\AppData\Local\Android\Sdk

1. In PATH settings, `JAVA_HOME` should be set to `C:\Program Files\Java\%JDK%`

Example:

    PATH VARIABLE: JAVA_HOME

    PATH VALUE: C:\Program Files\Java\jdk1.8.0_251

Install Node from its [website](https://nodejs.org/en/).

Install Appium via Node:

`npm install -g appium`

# Running iOS Tests

To run the tests execute,

`cd Appium`

`yarn test:ios` or `npm run test:ios`

# Running React Native App Locally

Install dependencies with

`yarn` or `npm install`

Run Expo

`yarn start`

# Possible Problems

1. For devices:

Computer may not see the device although it is connected. In that case, You have to open "Developer Settings" in phone's general settings. For every phone model, opening "Developer Settings" varies. In "Developer Settings", allow for "USB Debugging". This helps you to set up programs into your devices. For android devices, after having connected, there may be 3 options like "only charge","file transfer" and "media transfer"

In this case. Press "only charge".

2. PATH problem:

Although you have done the paths, the project cannot see the path has been already set. After having set the all paths, reboot the computer then open the project and run.
