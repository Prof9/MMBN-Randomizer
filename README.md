MMBN Randomizer
===============
MMBN Randomizer is a Java utility that "randomizes" a Mega Man Battle Network ROM. Things being randomized include chip codes, chip drops and enemies in random encounters.

To run this program, Java 7 must be installed on your computer.

Usage
-----
Run the MMBN Randomizer like any other JAR file. If no command line arguments are provided, a GUI will be displayed.

```
java -jar MMBNRandomizer.jar inputrom [outputrom [seed]]
```

To use the MMBN Randomizer from the command line, pass it the file path of the input ROM. You can also pass it the path of the output ROM and, if you do that, the seed to use for randomizing the ROM (as a decimal 32-bit signed number).

Compatibility
-------------
Currently MMBN Randomizer works for the following games:

* Mega Man Battle Network 6: All versions.
* Mega Man Battle Network 5: Team Colonel, North American version only.

Support for other games and regions is planned.

Since all chip codes are randomized, save files are locked to the seed that was used. If you use a save file from the clean ROM or a randomized ROM with a different seed, the game will softlock when you enter folder edit and you can't use any of your chips in battle.

Notes
-----
* This is still very much a work-in-progress, so many parts have not yet been polished.

Credits
=======
**MMBN Randomizer (c) 2014**
* Prof. 9
  * Planning
  * Programming
* MidniteW
  * Icon
  * Beta testing

Mega Man and Mega Man Battle Network are (c) Capcom 1987 - 2014

License
=======
This project is licensed under the terms of the Mozilla Public License, version 2.0. See *license.txt* for more information.