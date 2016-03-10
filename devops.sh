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
	mvn test #> /dev/null

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
	# cd $cheminProcesseurs
	# processeurs=`ls *.java`

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
posInit=`pwd`

# balises du pom dans lequels est indique quel processeur est utilise
BALISE_DEB="<processor>"
BALISE_FIN="<\/processor>"
# prefixe du rapport en cas de mutant mort-nee.
MORT_NEE="MORT_NEE"
# le fichier ou est sauvegarde le rapport mvn de compile en cas de probleme
MVN_INSTALL_TXT="mvn_install.txt" 

# repertoire ou est enregistrer les rapports de testes des mutants
REP_TEST="${posInit}/tests_reports/"
# repertoire dans lequel maven produit les rapports de testes des mutants
STATUS="target/surefire-reports/" 
# repertoires du projet contenant les mutants
mutationsProject="ProjetDevOps/DevopsMutation/"
mutationsProject=${posInit}'/'$mutationsProject
cheminProcesseurs="src/main/java/DevopsTest/mutators/"
cheminProcesseurs=${mutationsProject}$cheminProcesseurs
pom="pom.xml"
# prefixe de package utilise dans le pom pour chaque processeur
package="DevopsTest.mutators."

# gestion du repertoire d'entre
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

rms=`ls ${REP_TEST}*.* 2>/dev/null`
if [ -n "$rms" ];
then
	echo "on supprime les reports qui etaient contenu dans $REP_TEST" 
	rm $rms
fi

MUTT_PROBA_DEBUT="private static final double MUTATION_PROBABILITY = "
MUTT_PROBA_FIN=";"
selector="0.152"

read -p "pourcentage de mutants: " selector
# on change la valeur du pourcentage selectionne
cd $cheminProcesseurs
processeurs=`ls *.java`
for proc in $processeurs
do
	sed "s/${MUTT_PROBA_DEBUT}.*/$MUTT_PROBA_DEBUT$selector${MUTT_PROBA_FIN}/" $proc > $proc.tmp
	mv $proc.tmp $proc
done

# on met a jour le projet avec processeurs
cd $mutationsProject
mvn install > $MVN_INSTALL_TXT

# puis on trait le resultat de la compile du projet
resultBuild=`cat $MVN_INSTALL_TXT | grep "BUILD FAILURE"`
if [ -n $resultBuild ];
then
	rm $MVN_INSTALL_TXT
else
	echo -e "\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
	echo $resultBuild "   avait-vous bien rentre un nombre ?"
	echo -e "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"
	cat $MVN_INSTALL_TXT
	echo "\n Ce rapport a ete sauvegarde dans $mutationsProject$MVN_INSTALL_TXT \n"
	exit 0
fi

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