#!/bin/bash
echo "Démarrage du test par mutation"
PATH_DEVOPS=$(pwd)
PATH_SOURCE=$PATH_DEVOPS/DevopsEntree
PATH_MUTANT=$PATH_DEVOPS/DevopsMutation/src/main/java/DevopsTest/mutators/

ID="DevopsTest.mutators."


cd "$PATH_MUTANT"
ls *.java > "$PATH_DEVOPS"/processor.txt
cd  "$PATH_DEVOPS"
while read ligne  
do  
   PROCESSOR=$(basename $ligne  '.java')
echo -e "\n\n########################################################################################"
echo "########################################################################################"
echo " APPLICATION DU MUTANT $PROCESSOR"
echo "########################################################################################"
echo -e "########################################################################################\n\n"


   SED="s/<processor>.*/\<processor\>$ID$PROCESSOR\<\/processor\>/"
   sed -i $SED  "$PATH_SOURCE"/pom.xml
   cd "$PATH_SOURCE"
   mvn3 clean >/dev/null
   mvn3 test >/dev/null

echo -e "\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo " Copie du fichier rapport dans $PATH_SOURCE/target/surefire-reports/TEST-$PROCESSOR.xml"
echo -e "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"
   cp "$PATH_SOURCE"/target/surefire-reports/TEST-Devops.sources.AppTest.xml "$PATH_SOURCE"/target/surefire-reports/TEST-$PROCESSOR.xml
done < processor.txt


