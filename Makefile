#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

DOCKER := $(shell command -v docker 2>/dev/null)
GIT := $(shell command -v git 2>/dev/null)
JQ := $(shell command -v jq 2>/dev/null)
MVN := $(shell command -v mvn 2>/dev/null)
NPM := $(shell command -v npm 2>/dev/null)

PACKAGE_NAME ?= $(shell '$(JQ)' -r '.name' ./package.json)
PACKAGE_VERSION ?= $(shell '$(JQ)' -r '.version' ./package.json)
PACKAGE_VERSION_EXTRA := $(if $(PACKAGE_VERSION_EXTRA),.$(PACKAGE_VERSION_EXTRA),)

MAVEN_GROUP ?= com.stratebi.pentaho
MAVEN_SNAPSHOT_REPOSITORY_ID ?= stratebi-snapshot
MAVEN_SNAPSHOT_REPOSITORY_URL ?= https://repo.stratebi.com/repository/stratebi-mvn-snapshot
MAVEN_RELEASE_REPOSITORY_ID ?= stratebi-releases
MAVEN_RELEASE_REPOSITORY_URL ?= https://repo.stratebi.com/repository/stratebi-mvn-releases

DIST_DIR := ./dist
DIST_BISERVER_DIR := $(DIST_DIR)/biserver
DIST_DEV_DIR := $(DIST_DIR)/dev
DIST_PROD_DIR := $(DIST_DIR)/prod
DIST_DEV_PACKAGE := $(DIST_DIR)/$(PACKAGE_NAME)-dev.tgz
DIST_PROD_PACKAGE := $(DIST_DIR)/$(PACKAGE_NAME)-prod.tgz

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "start-*" targets
##################################################

.PHONY: start-caddy
start-caddy:
	'$(DOCKER)' run --interactive --tty --rm --network host \
		--mount type=bind,src='$(MKFILE_DIR)'/Caddyfile,dst=/etc/caddy/Caddyfile,ro \
		hectormolinero/caddy:latest

.PHONY: start-webpack
start-webpack:
	'$(NPM)' run serve

.PHONY: start-pentaho
start-pentaho:
	(cd ./biserver/ \
		&& export CATALINA_PID="$$(readlink -f .)/catalina.pid" \
		&& (pkill --pidfile "$${CATALINA_PID}" && sleep 5 ||:) \
		&& rm -rf ./pentaho-solutions/system/karaf/caches/* \
		&& rm -rf ./tomcat/logs/* ./tomcat/temp/* ./tomcat/work/* \
		&& ./start-pentaho.sh && tail -f ./tomcat/logs/catalina.out)

##################################################
## "build-*" target
##################################################

.PHONY: build
build: build-dev

.PHONY: build-dev
build-dev: $(DIST_DEV_PACKAGE)

.PHONY: build-prod
build-prod: $(DIST_PROD_PACKAGE)

$(DIST_BISERVER_DIR):
	(cd ./biserver/ \
		&& OUT='$(shell readlink -m '$@')' \
		&& STASH=$$('$(GIT)' stash create) && STASH=$${STASH:=HEAD} \
		&& rm -rf "$${OUT}" && mkdir -p "$${OUT}" \
		&& '$(GIT)' archive --format=tar "$${STASH}" ./ | tar -xf- -C "$${OUT}" \
		&& '$(GIT)' gc --prune=now)

$(DIST_DEV_DIR):
	@'$(NPM)' run lint
	@'$(NPM)' run build:dev

$(DIST_PROD_DIR):
	@'$(NPM)' run lint
	@'$(NPM)' run build:prod

$(DIST_DEV_PACKAGE): $(DIST_BISERVER_DIR) $(DIST_DEV_DIR)
	tar -czf '$@' \
		--exclude=biserver/tomcat/webapps/pentaho/customization \
		--transform='s|^biserver||;s|^dev|tomcat/webapps/pentaho/customization|' \
		--directory='$(DIST_DIR)' biserver/ dev/

$(DIST_PROD_PACKAGE): $(DIST_BISERVER_DIR) $(DIST_PROD_DIR)
	tar -czf '$@' \
		--exclude=biserver/tomcat/webapps/pentaho/customization \
		--transform='s|^biserver||;s|^prod|tomcat/webapps/pentaho/customization|' \
		--directory='$(DIST_DIR)' biserver/ prod/

##################################################
## "deploy-*" target
##################################################

.PHONY: deploy
deploy: deploy-dev

.PHONY: deploy-dev
deploy-dev: $(DIST_DEV_PACKAGE)
	mvn deploy:deploy-file \
		-Dpackaging=tar.gz \
		-Dfile='$(DIST_DEV_PACKAGE)' \
		-DgroupId='$(MAVEN_GROUP)' \
		-DartifactId='$(PACKAGE_NAME)' \
		-Dversion='$(PACKAGE_VERSION)$(PACKAGE_VERSION_EXTRA)-SNAPSHOT' \
		-DrepositoryId=$(MAVEN_SNAPSHOT_REPOSITORY_ID) \
		-Durl='$(MAVEN_SNAPSHOT_REPOSITORY_URL)'

.PHONY: deploy-prod
deploy-prod: $(DIST_PROD_PACKAGE)
	mvn deploy:deploy-file \
		-Dpackaging=tar.gz \
		-Dfile='$(DIST_PROD_PACKAGE)' \
		-DgroupId='$(MAVEN_GROUP)' \
		-DartifactId='$(PACKAGE_NAME)' \
		-Dversion='$(PACKAGE_VERSION)$(PACKAGE_VERSION_EXTRA)' \
		-DrepositoryId='$(MAVEN_RELEASE_REPOSITORY_ID)' \
		-Durl='$(MAVEN_RELEASE_REPOSITORY_URL)'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -rf $(addprefix ', $(addsuffix ', $(DIST_BISERVER_DIR) $(DIST_DEV_DIR) $(DIST_PROD_DIR) $(DIST_DEV_PACKAGE) $(DIST_PROD_PACKAGE)))
	if [ -d '$(DIST_DIR)' ] && [ -z "$$(ls -A '$(DIST_DIR)')" ]; then rmdir '$(DIST_DIR)'; fi
