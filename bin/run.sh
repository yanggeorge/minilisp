#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
java -jar ${basepath}/../target/minilisp-1.0-SNAPSHOT-jar-with-dependencies.jar

