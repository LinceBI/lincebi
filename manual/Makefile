#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

PANDOOC := $(shell command -v pandoc 2>/dev/null)

PANDOC_CSS := ./pandoc.css

TARGET_PDF := $(addsuffix .pdf, $(basename $(wildcard *.md)))
TARGET_HTML := $(addsuffix .html, $(basename $(wildcard *.md)))

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "build" target
##################################################

.PHONY: build
build: $(TARGET_PDF) $(TARGET_HTML)

%.pdf: %.md
	'$(PANDOOC)' '$<' \
		--latex-engine=xelatex \
		-o '$@'
%.html: %.md
	'$(PANDOOC)' '$<' \
		--latex-engine=xelatex \
		--self-contained \
		--css '$(PANDOC_CSS)' \
		-o '$@'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -f $(addprefix ', $(addsuffix ', $(TARGET_PDF) $(TARGET_HTML)))
