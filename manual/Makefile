#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

PANDOC := $(shell command -v pandoc 2>/dev/null)

PANDOC_CSS := ./assets/style.css

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
	'$(PANDOC)' '$<' \
		--latex-engine=xelatex \
		--output='$@'

%.html: %.md
	'$(PANDOC)' '$<' \
		--latex-engine=xelatex \
		--css='$(PANDOC_CSS)' \
		--self-contained \
		--output='$@'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -f $(addprefix ', $(addsuffix ', $(TARGET_PDF) $(TARGET_HTML)))
