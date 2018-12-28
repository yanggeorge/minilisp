#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
echo "(println '(hello word))" | java -jar ${basepath}/../target/minilisp-1.0-SNAPSHOT-jar-with-dependencies.jar

