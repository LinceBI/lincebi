#!/bin/sh

set -eu
export LC_ALL=C

RELEASE='9.2-21.09.13'
URL="https://repo.stratebi.com/repository/lincebi-raw/pentaho-language-packs/languagePack_es-${RELEASE:?}.zip"

curl -fsSL "${URL:?}" > "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/language-pack-es.zip
(cd "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/ && unzip -qo ./language-pack-es.zip && rm -f ./language-pack-es.zip)

/usr/share/biserver/bin/kitchen.sh -level=Error -file="${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/languagePack_es/endpoints/kettle/admin/installpack.kjb
