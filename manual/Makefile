#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -eu -c

PANDOC := $(shell command -v pandoc 2>/dev/null)
SOFFICE := $(shell command -v soffice 2>/dev/null)

PANDOC_REFERENCE_DOCX := ./assets/reference.docx

TARGET_PDF := $(addsuffix .pdf, $(basename $(wildcard *.md)))
TARGET_DOCX := $(addsuffix .docx, $(basename $(wildcard *.md)))

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "build" target
##################################################

.PHONY: build
build: $(TARGET_PDF) $(TARGET_DOCX)

%.pdf: %.docx
	'$(SOFFICE)' --headless --convert-to pdf '$<'

#%.pdf: %.md
#	'$(PANDOC)' '$<' \
#		--latex-engine=xelatex \
#		--output='$@'

%.docx: %.md
	'$(PANDOC)' '$<' \
		--latex-engine=xelatex \
		--reference-doc='$(PANDOC_REFERENCE_DOCX)' \
		--table-of-contents \
		--output='$@'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -f $(addprefix ', $(addsuffix ', $(TARGET_PDF) $(TARGET_DOCX)))
