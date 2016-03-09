#!/bin/bash

# s'occupe de produire le pom avec le bon processeur, compile avec mvn test 
# puis deplace les rapports xml dans le reppertoire REP_TEST
function doPom(){
	# exemple de production: <processor>DevopsTest.mutators.UnaryOperatorMutator</processor>
	echo -e "\n\n########################################################################################"
	echo "########################################################################################"
	echo " APPLICATION DU MUTANT $param"
	echo "########################################################################################"
	echo -e "########################################################################################\n\n"

	cd $projetEntre
	# modifie le pom pour notre processeur
	sed "s/$BALISE_DEB.*/$BALISE_DEB${package}$param$BALISE_FIN/" $pom > pom.tmp
	mv pom.tmp $pom
	# compile le projet avec ce pom, et genere les sources avec spoon comme definit precedement
	mvn test > /dev/null

	# productions ou deplacement des reports
	if [ -d "$STATUS" ]; then
  	cd $STATUS
	reports=`ls *.xml 2>/dev/null`
	fi
	if [ -n $reports ]
	then
		# il n'y a aucun reports, on considere que c'est un mort-nee
		report_export=$REP_TEST$MORT_NEE'-'${selector}'-'${param}".xml"
		echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuite/>" > $report_export
echo -e "\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo "ce mutant est mort-née, on produit le rapport $report_export "
echo -e "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"
	else
		# on deplace les rapports xml dans le repertoire REP_TEST
		for report in $reports
		do
			report=$(basename $report '.xml')
			report_export=$REP_TEST${report}'-'${selector}'-'${param}".xml"
echo -e "\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo " Deplacement du fichier rapport dans ${report_export}"
echo -e "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"
			mv ${report}".xml" ${report_export}
		done
	fi
	
}

# traite tout les processeurs avec le selecteur $selector
function doProcs(){
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
}


# quelques constantes
STATUS="target/surefire-reports/" 
BALISE_DEB="<processor>"
BALISE_FIN="<\/processor>"
posInit=`pwd`
REP_TEST="${posInit}/tests_reports/"
MORT_NEE="TEST-MORT_NEE"

mutationsProject="ProjetDevOps/DevopsMutation/"
mutationsProject=${posInit}'/'$mutationsProject
cheminProcesseurs="src/main/java/DevopsTest/mutators/"
cheminProcesseurs=${mutationsProject}$cheminProcesseurs
pom="pom.xml"
package="DevopsTest.mutators."

selector="all"

# entre XXX
projetEntre="ProjetDevOps/DevopsEntree/"
if [ -d "$1" ]
then
	if [ -e "$1$pom" ]
	then
		projetEntre=$1
	fi
fi
projetEntre=${posInit}'/'$projetEntre
echo -e "\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo "on prend en entree "$projetEntre
echo -e "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"

# on effectue un bakup du pom
cp $projetEntre$pom $projetEntre${pom}.BAK
# il faudrait faire une verification: TODO
# `cat $projetEntre$pom | grep "$BALISE_DEB"` a t il un resultat ? 
# si non, il faut rajouter un truc pour que sa marche apres quand meme 

# on clean le contenu du REP_TEST
if [ ! -d $REP_TEST ]; then
	echo "on cree le repertoire $REP_TEST" 
	mkdir $REP_TEST
fi

rms=`ls ${REP_TEST}*.xml 2>/dev/null`
if [ -n "$rms" ];
then
	echo "on supprime les reports qui etaient contenu dans $REP_TEST" 
	rm $rms
fi

# on met a jour le projet avec processeurs
cd $mutationsProject
mvn install > /dev/null

doProcs

echo -e "\n\n########################################################################################"
echo "########################################################################################"
echo " Generation de l'affichage"
echo "########################################################################################"
echo -e "########################################################################################\n\n"

# execution de l'affichage
cd $posInit/GenerationXml
# on appelle le script pour lancer la generation de l'affichage avec le projet deonné en entrée.
./xml.sh $REP_TEST

cd $posInit
mv $projetEntre${pom}.BAK $projetEntre$pom 