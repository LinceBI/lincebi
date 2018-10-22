#!/usr/bin/make -f

MKFILE_RELPATH := $(shell printf -- '%s' '$(MAKEFILE_LIST)' | sed 's|^\ ||')
MKFILE_ABSPATH := $(shell readlink -f -- '$(MKFILE_RELPATH)')
MKFILE_DIR := $(shell dirname -- '$(MKFILE_ABSPATH)')

DIST_DIR := $(MKFILE_DIR)/dist

DEPLOY_USER := user
DEPLOY_PASS := password
DEPLOY_URL := http://devel.local.stratebi.com:8081/repository/stratebi-raw/customizations/sttools-customization-root.zip

.PHONY: all \
	format format-encoding format-xml format-json format-js format-css \
	build \
	deploy \
	clean

all: format build

format: format-encoding format-xml format-json format-js format-css

format-encoding:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			iconv -f "$$(uchardet "{}")" -t UTF-8 -o "{}.tmp" "{}" 2>/dev/null && mv -f "{}.tmp" "{}"; \
			dos2unix "{}"; \
		fi' ';'

format-xml:
	@XMLLINT_INDENT="$$(printf '\t')" \
	find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.xml' -or -iname '*.wcdf' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			xmllint --format --noblanks --output "{}" "{}"; \
			printf "%s\n" "{}"; \
		fi' ';'

format-json:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.json' -or -iname '*.cdfde' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			prettier --write --parser json "{}"; \
		fi' ';'

format-js:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.js' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			prettier --write --parser babylon "{}"; \
		fi' ';'

format-css:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.css' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			prettier --write --parser css "{}"; \
		fi' ';'

build:
	@mkdir -p '$(DIST_DIR)'
	@(cd '$(MKFILE_DIR)/ROOT/' \
		&& STASH=$$(git stash create) && STASH=$${STASH:=HEAD} \
		&& git archive \
			--verbose \
			--format=zip \
			--output '$(DIST_DIR)/sttools-customization-root.zip' \
			"$${STASH}" ./ \
		&& git gc --prune=now)

deploy:
	@curl \
		--verbose \
		--user '$(DEPLOY_USER):$(DEPLOY_PASS)' \
		--header 'Content-Type: application/zip' \
		--upload-file '$(DIST_DIR)/sttools-customization-root.zip' \
		'$(DEPLOY_URL)' 2>&1 >/dev/null | grep -v '> Authorization: '

clean:
	rm -rf '$(DIST_DIR)'
