# README
A command line timer that will send a notification when the specified duration passes.

This is more feature reach than the [timebox.sh](https://github.com/mcomella/timebox.sh/) script.

## Installation & Usage
Pre-requisites:
- Java 8

To install:
```bash
./tools/install.sh
```

This will copy **(and overwrite!)** files in `~/bin/timebox*`. Once installed, and assuming `~/bin` is in your path, run with:

```bash
timebox <minutes>
```

A notification will be sent when the specified duration elapses.

## Development
We recommend loading project into Intellij Community Edition.

To compile and run:
```bash
./gradlew jar
java -jar build/libs/timebox.jar
```
