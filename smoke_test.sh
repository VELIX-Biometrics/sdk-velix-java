#!/usr/bin/env bash
set -e
mvn -q -DskipTests compile
mvn -q dependency:build-classpath -Dmdep.outputFile=/tmp/velix-java-cp.txt
CP="target/classes:$(cat /tmp/velix-java-cp.txt)"
mkdir -p /tmp/velix-java-smoke
javac -cp "$CP" -d /tmp/velix-java-smoke smoke/SmokeTest.java
java -cp "/tmp/velix-java-smoke:$CP" SmokeTest
