#! /bin/bash


javac -d bin src/*.java 

if test $? -eq 0
then 
	java -cp bin Main ProjetDevOps/DevopsEntree
fi
