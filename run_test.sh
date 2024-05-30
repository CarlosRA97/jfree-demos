#!/usr/bin/zsh

./gradlew :createFatJar

jamvm -version
jamvm -cp build/libs/jfree-demos.jar $1