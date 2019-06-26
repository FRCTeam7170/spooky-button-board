# spooky-button-board
Java code for controlling custom IO expander Raspberry Pi HAT for a button
board.

The IO expanders used are MCP23S17's, which have 16 GPIOs with an SPI
interface. The library used to interface with the chips can be found
[here](https://github.com/Ro5bert/MCP23S17) (JAR releases of it are included
in `lib`.)

## Building
To build, clone this repository and run

    ./gradlew shadowJar
    
(Note that, since this was copied from Windows, `gradlew` might not be
executable by default. To make it executable, run `chmod 750 gradlew`.)

This will produce an executable JAR in `build/libs`.

## Running
To execute the JAR produced under Building, simply run

    java -jar /path/to/jar
    
which might need root privileges to access the SPI hardware (prepend with
`sudo`).

## Configuration

### Enabling SPI Interface
The Raspberry Pi's SPI interface must be enabled in order to communicate with
the SPI IO expanders; by default, it is disabled. To enable it run

    raspi-config
    
(needs root privileges; prepend with `sudo` if not root user) and navigate
with the up/down arrow keys to "5 Interfacing Options", then "P4 SPI", and
enable it. Then, use the left/right arrow keys to select "Finish".

### Executing JAR on Boot
It is probably desirable to have to JAR execute when the Raspberry Pi boots.
To do so, we can execute the JAR in `/etc/rc.local`, which is executed on
startup. Edit `rc.local` (`sudo nano /etc/rc.local`) and insert the following
line before the `exit` statement:

    java -jar /path/to/jar > /var/log/sbb.log 2>&1 &
    
(Note `sudo` is not necessary here as `rc.local` is executed as the root
user.)

The "`&`" at the end of the line makes the command run in the background and
is recommended as, without it, the rest of the initialization process will be
blocked as it waits for Java to terminate. (You also will not be able to
manually terminate it via Ctrl-C because it is not started from a shell you
are logged into!) The "`> /var/log/sbb.log`" and "`2>&1`" parts are optional;
they redirect standard output to `/var/log/sbb.log` ("sbb" for
"spooky-button-board") and standard error to standard output (and,
consequently, `/var/log/sbb.log`).
