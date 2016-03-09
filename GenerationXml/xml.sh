#! /bin/bash

javac -d bin src/*.java 

if test $? -eq 0
then 
	if [ -n "$1" ]
	then
		java -cp bin Main $1
		exit 0
	fi
	java -cp bin Main tests_reports/
	exit 0
fi
