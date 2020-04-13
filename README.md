# Prog project 
Multi threaded Email application written in Java for an university exam. 

## Instructions
1. Clone the repository
2. Use `gradlew` wrapper script to build and run (eg. `./gradlew appStart`)

## Notes
I know that this codebase needs a giant refactor, plus most of the JavaFX code is not well written (I had no prior experience with the library). 
You can get some inspiration if you are here and you need some help with this project but keep in mind that some of the asynch methods could have beeen written in a better way. (_some method locks can be removed_)
Maybe when I'll have some spare time I'm going to refactor the whole application (but this time with [Reactor](https://github.com/reactor/reactor-core) and [Netty](https://netty.io/))