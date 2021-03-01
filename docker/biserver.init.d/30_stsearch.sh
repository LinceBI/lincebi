#!/bin/sh

set -eu
export LC_ALL=C

RELEASE='1.6.5'; SNAPSHOT=''
URL="https://repo.stratebi.com/repository/lincebi-mvn/com/stratebi/lincebi/stsearch/${RELEASE:?}${SNAPSHOT:+-SNAPSHOT}/stsearch-${RELEASE:?}${SNAPSHOT:+-}${SNAPSHOT?}.zip"

curl -fsSL "${URL:?}" > "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/stsearch.zip
(cd "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/ && unzip -qo ./stsearch.zip && rm -f ./stsearch.zip)
