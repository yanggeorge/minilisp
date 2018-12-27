#!/bin/sh
basepath=$(cd `dirname $0`; pwd)
cd ${basepath}
cd ..
mvn package
