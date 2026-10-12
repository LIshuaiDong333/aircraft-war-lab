# Aircraft War Lab

A Java Swing aircraft shooting game with difficulty modes, enemies, bullets, power-ups, sound, and ranking support.

- `src/edu/hitsz`: game sources and Swing UI forms.
- `src/images`, `src/videos`: reviewed game graphics and WAV sound assets.
- `test`: supplied Java unit tests.
- `uml`: PlantUML diagrams.
- `AircraftWar-base.iml`: original IntelliJ module configuration.

The module configuration uses Java 11 and references Commons Lang 3.8.1 and JUnit 5.8.1. Obtain the dependencies separately; bundled JAR files are excluded. Import the module into a compatible Java IDE and run `edu.hitsz.application.Main` from the project root so the original relative asset paths resolve. Personal leaderboard files are excluded; new scores are stored locally when the game runs.

## Publication scope

This repository is a reviewed export of local coursework. Word/PDF reports, archive files, personal records, credentials, local IDE settings, generated binaries, and original Git history are excluded. Original source bytes and asset files are retained except for the documented database privacy substitutions. The root `.gitignore` uses an explicit upload allowlist; review new files before adding them.
