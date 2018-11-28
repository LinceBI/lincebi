#!/usr/bin/make -f

MKFILE_RELPATH := $(shell printf -- '%s' '$(MAKEFILE_LIST)' | sed 's|^\ ||')
MKFILE_ABSPATH := $(shell readlink -f -- '$(MKFILE_RELPATH)')
MKFILE_DIR := $(shell dirname -- '$(MKFILE_ABSPATH)')

DIST_DIR := $(MKFILE_DIR)/dist

DEPLOY_USER ?= user
DEPLOY_PASS ?= password
DEPLOY_URL_BASE ?= https://repo.stratebi.com
DEPLOY_URL ?= $(DEPLOY_URL_BASE)/repository/stratebi-raw/customizations/sttools-customization-root.tgz

.PHONY: all
all: format build

.PHONY: format
format: format-encoding format-xml format-json format-js format-css

.PHONY: format-encoding
format-encoding:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			iconv -f "$$(uchardet "{}")" -t UTF-8 -o "{}.tmp" "{}" 2>/dev/null && mv -f "{}.tmp" "{}"; \
			dos2unix "{}"; \
		fi' ';'

.PHONY: format-xml
format-xml:
	@XMLLINT_INDENT="$$(printf '\t')" \
	find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.xml' -or -iname '*.wcdf' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			xmllint --format --noblanks --output "{}" "{}"; \
			printf "%s\n" "{}"; \
		fi' ';'

.PHONY: format-json
format-json:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.json' -or -iname '*.cdfde' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			prettier --write --parser json "{}"; \
		fi' ';'

.PHONY: format-js
format-js:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.js' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			prettier --write --parser babylon "{}"; \
		fi' ';'

.PHONY: format-css
format-css:
	@find '$(MKFILE_DIR)/ROOT/' -type f \
		'(' -not -iregex '.*\.min\.[a-z0-9]+' ')' \
		'(' -iname '*.css' ')' \
		-exec sh -c 'if ! git check-ignore -q "{}" && grep -qI "" "{}"; then \
			prettier --write --parser css "{}"; \
		fi' ';'

.PHONY: build
build:
	@mkdir -p '$(DIST_DIR)'
	@(cd '$(MKFILE_DIR)/ROOT/' \
		&& STASH=$$(git stash create) && STASH=$${STASH:=HEAD} \
		&& git archive \
			--verbose \
			--format=tar.gz \
			--output '$(DIST_DIR)/sttools-customization-root.tgz' \
			"$${STASH}" ./ \
		&& git gc --prune=now)

.PHONY: deploy
deploy:
	@curl \
		--verbose \
		--user '$(DEPLOY_USER):$(DEPLOY_PASS)' \
		--header 'Content-Type: application/gzip' \
		--upload-file '$(DIST_DIR)/sttools-customization-root.tgz' \
		'$(DEPLOY_URL)' 2>&1 >/dev/null | grep -v '> Authorization: '

.PHONY: clean
clean:
	rm -rf '$(DIST_DIR)'
