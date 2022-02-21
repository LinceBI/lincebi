#!/bin/sh

set -eu
export LC_ALL=C

# shellcheck disable=SC1091
. /usr/share/biserver/bin/set-utils.sh

for langpack_dir in "${BISERVER_HOME:?}"/"${SOLUTIONS_DIRNAME:?}"/system/languagePack_*/; do
	[ -d "${langpack_dir:?}" ] || continue

	for webapps_dir in "${langpack_dir:?}"/data/*/tomcat/webapps/; do
		[ -d "${webapps_dir:?}" ] || continue

		# Rename Pentaho webapp directory if target directory does not exist
		if [ -e "${webapps_dir:?}"/pentaho/ ] && [ ! -e "${webapps_dir:?}"/"${WEBAPP_PENTAHO_DIRNAME:?}"/ ]; then
			mv "${webapps_dir:?}"/pentaho/ "${webapps_dir:?}"/"${WEBAPP_PENTAHO_DIRNAME:?}"/
		fi
	done

	# Replace some hardcoded values
	sed -ri 's|(<value>(tomcat/webapps/)?)pentaho(</value>)|\1'"${WEBAPP_PENTAHO_DIRNAME:?}"'\3|g' \
		"${langpack_dir:?}"/endpoints/kettle/admin/installpack.kjb

	# Execute ETL
	/usr/share/biserver/bin/kitchen.sh \
		-level=Error \
		-file="${langpack_dir:?}"/endpoints/kettle/admin/installpack.kjb \
		-param=cpk.webapp.dir="${CATALINA_BASE:?}"/webapps/"${WEBAPP_PENTAHO_DIRNAME:?}"
done
