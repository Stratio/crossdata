#!/bin/bash

function changedir() {
	cd $1
	echo "Current dir: $PWD"
}

#Get Crossdata version from parent pom
tmp1=`grep -m2 "<version>" ../pom.xml | tail -n1`
tmp2=${tmp1/<version>/}
tmp3=${tmp2/<\/version>/}
XD_VERSION=${tmp3// }
