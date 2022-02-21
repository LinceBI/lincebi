#!/bin/sh

set -eu
export LC_ALL=C

VERSION='9.2-21.09.13'
URL="https://repo.stratebi.com/repository/lincebi-raw/pentaho-language-packs/languagePack_pt_PT-${VERSION:?}.zip"

curl -fsSL "${URL:?}" > "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/language-pack-pt.zip
(cd "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/ && unzip -qo ./language-pack-pt.zip && rm -f ./language-pack-pt.zip)
