#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

GIT := $(shell command -v git 2>/dev/null)

DIST_DIR := ./dist

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "start" target
##################################################

.PHONY: start
start:
	(cd ./src/ \
		&& export CATALINA_PID="$$(pwd)/catalina.pid" \
		&& (pkill --pidfile "$${CATALINA_PID}" && sleep 5 ||:) \
		&& rm -rf ./pentaho-solutions/system/karaf/caches/* \
		&& rm -rf ./tomcat/logs/* ./tomcat/temp/* ./tomcat/work/* \
		&& ./start-pentaho.sh && tail -f ./tomcat/logs/catalina.out)

##################################################
## "build" target
##################################################

.PHONY: build
build: $(DIST_DIR)

$(DIST_DIR):
	(cd ./src/ \
		&& OUT='$(shell readlink -m '$@')' \
		&& STASH=$$('$(GIT)' stash create) && STASH=$${STASH:=HEAD} \
		&& rm -rf "$${OUT}" && mkdir -p "$${OUT}" \
		&& '$(GIT)' archive --format=tar "$${STASH}" ./ | tar -xf- -C "$${OUT}" \
		&& '$(GIT)' gc --prune=now)

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -rf '$(DIST_DIR)'
