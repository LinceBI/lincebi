#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

PANDOOC := $(shell command -v pandoc 2>/dev/null)

SOURCE := ./manual.md
TARGET := ./manual.pdf

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "build" target
##################################################

.PHONY: build
build: $(TARGET)

$(TARGET):
	'$(PANDOOC)' '$(SOURCE)' \
		--latex-engine=xelatex \
		-o '$(TARGET)'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -f '$(TARGET)'
