#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

NPM := $(shell command -v npm 2>/dev/null)

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
	'$(NPM)' run serve

##################################################
## "build" target
##################################################

.PHONY: build
build: $(DIST_DIR)

$(DIST_DIR):
	'$(NPM)' run lint
	'$(NPM)' run build

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -rf '$(DIST_DIR)'
