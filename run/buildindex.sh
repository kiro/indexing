#!/bin/bash
export LC_ALL='C'
java -jar -Xmx256m jar/map.jar < $1 | sort | java -jar -Xmx256m jar/reduce.jar index