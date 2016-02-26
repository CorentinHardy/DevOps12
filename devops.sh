#!/bin/bash

function doPom(){
	# exemple de production: <processor>DevopsTest.mutators.UnaryOperatorMutator</processor>
	echo -e "\n\n########################################################################################"
	echo "########################################################################################"
	echo " APPLICATION DU MUTANT $param"
	echo "########################################################################################"
	echo -e "########################################################################################\n\n"

	cd $projetEntre
	sed "s/$BALISE_DEB.*/$BALISE_DEB${package}$param$BALISE_FIN/" $pom > pom.tmp
	mv pom.tmp $pom
	mvn test > /dev/null

	cd $STATUS
	reports=`ls *.xml`
	for report in $reports
	do
		report=$(basename $report '.xml')
		report_export=$REP_TEST${report}'-'${selector}'-'${param}
echo -e "\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo " Deplacement du fichier rapport dans ${report_export}.xml"
echo -e "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"
		mv ${report}".xml" ${report_export}".xml"
	done

	# fin a suppr XXX
	# echo `cat $pom | grep "$BALISE_DEB"`
}

# quelques constantes
STATUS="target/surefire-reports/" 
BALISE_DEB="<processor>"
BALISE_FIN="<\/processor>"
posInit=`pwd`
REP_TEST="${posInit}/TEST/"

mutationsProject="ProjetDevOps/DevopsMutation/"
mutationsProject=${posInit}'/'$mutationsProject
cheminProcesseurs="src/main/java/DevopsTest/mutators/"
cheminProcesseurs=${mutationsProject}$cheminProcesseurs
pom="pom.xml"
package="DevopsTest.mutators."

selector="all"

# entre XXX
projetEntre="ProjetDevOps/DevopsEntree/"
projetEntre=${posInit}'/'$projetEntre

# on effectue un bakup du pom
cp $projetEntre$pom $projetEntre${pom}.BAK
# il faudrait faire une verification: TODO
# `cat $projetEntre$pom | grep "$BALISE_DEB"` a t il un resultat ? 
# si non, il faut rajouter un truc pour que sa marche apres quand meme 

# on clean le contenu du REP_TEST
ls ${REP_TEST}*.xml | rm -i

# on met a jour le projet avec processeurs
cd $mutationsProject
# mvn install > /dev/null

# on stoque les noms des processeurs pour la boucle
cd $cheminProcesseurs
processeurs=`ls *.java`

# on va dans le projet que l'on nous donne en entree
cd $projetEntre
mvn clean > /dev/null

for param in $processeurs
do
	param=${param%.java}
	doPom "$BALISE_DEB${package}$param$BALISE_FIN"
done

echo -e "\n\n########################################################################################"
echo "########################################################################################"
echo " Generation de l'affichage"
echo "########################################################################################"
echo -e "########################################################################################\n\n"

# execution de l'affichage
cd $posInit/GenerationXml
./xml.sh

cd $posInit
mv $projetEntre${pom}.BAK $projetEntre$pom 