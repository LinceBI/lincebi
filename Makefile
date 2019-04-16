#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

DOCKER := $(shell command -v docker 2>/dev/null)
DOCKER_COMPOSE := $(shell command -v docker-compose 2>/dev/null)
JQ := $(shell command -v jq 2>/dev/null)
MVN := $(shell command -v mvn 2>/dev/null)
NPM := $(shell command -v npm 2>/dev/null)
TMUX := $(shell command -v tmux 2>/dev/null)

PACKAGE_NAME ?= $(shell '$(JQ)' -r '.name' ./package.json | sed -r 's|^@([^/]+)/|\1-|')
PACKAGE_VERSION ?= $(shell '$(JQ)' -r '.version' ./lerna.json)
PACKAGE_VERSION_EXTRA := $(if $(PACKAGE_VERSION_EXTRA),.$(PACKAGE_VERSION_EXTRA),)

MAVEN_GROUP ?= UNDEFINED
MAVEN_REPOSITORY_ID ?= UNDEFINED
MAVEN_REPOSITORY_URL ?= UNDEFINED

DIST_DIR := ./dist
DIST_TARBALL := $(DIST_DIR)/$(PACKAGE_NAME).tgz

BISERVER_DIST_DIR := ./biserver/dist
PACKAGE_LOGIN_DIST_DIR := ./packages/login/dist
PACKAGE_HOME_DIST_DIR := ./packages/home/dist

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "start-*" targets
##################################################

.PHONY: start-biserver
start-biserver:
	'$(TMUX)' \
		new-session 'cd ./biserver/; $(MAKE) start' ';' \
		split-window 'cd ./docker/; $(DOCKER_COMPOSE) up' ';' \
		select-layout even-horizontal

.PHONY: start-devserver
start-devserver:
	'$(NPM)' run serve

##################################################
## "build" target
##################################################

.PHONY: build
build: $(DIST_TARBALL)

$(BISERVER_DIST_DIR):
	cd ./biserver && '$(MAKE)' build

$(PACKAGE_LOGIN_DIST_DIR) $(PACKAGE_HOME_DIST_DIR):
	'$(NPM)' run build

$(DIST_TARBALL): $(BISERVER_DIST_DIR) $(PACKAGE_LOGIN_DIST_DIR) $(PACKAGE_HOME_DIST_DIR)
	mkdir -p '$(DIST_DIR)'
	tar -czf '$@' \
		--transform='s|^$(BISERVER_DIST_DIR)||' \
		--exclude='$(BISERVER_DIST_DIR)/tomcat/webapps/pentaho/Login' \
		--transform='s|^$(PACKAGE_LOGIN_DIST_DIR)|tomcat/webapps/pentaho/Login|' \
		--exclude='$(BISERVER_DIST_DIR)/tomcat/webapps/pentaho/Home' \
		--transform='s|^$(PACKAGE_HOME_DIST_DIR)|tomcat/webapps/pentaho/Home|' \
		'$(BISERVER_DIST_DIR)' '$(PACKAGE_LOGIN_DIST_DIR)' '$(PACKAGE_HOME_DIST_DIR)'

##################################################
## "deploy" target
##################################################

.PHONY: deploy
deploy: $(DIST_TARBALL)
	mvn deploy:deploy-file \
		-Dpackaging=tar.gz \
		-Dfile='$(DIST_TARBALL)' \
		-DgroupId='$(MAVEN_GROUP)' \
		-DartifactId='$(PACKAGE_NAME)' \
		-Dversion='$(PACKAGE_VERSION)$(PACKAGE_VERSION_EXTRA)' \
		-DrepositoryId=$(MAVEN_REPOSITORY_ID) \
		-Durl='$(MAVEN_REPOSITORY_URL)'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -rf '$(DIST_DIR)' '$(PACKAGE_LOGIN_DIST_DIR)' '$(PACKAGE_HOME_DIST_DIR)'
