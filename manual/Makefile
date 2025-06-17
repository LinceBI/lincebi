#!/usr/bin/make -f

SHELL := /bin/sh
.SHELLFLAGS := -euc

PANDOC := $(shell command -v pandoc 2>/dev/null)
SOFFICE := $(shell command -v soffice 2>/dev/null)
EXIFTOOL := $(shell command -v exiftool 2>/dev/null)
QPDF := $(shell command -v qpdf 2>/dev/null)

PANDOC_REFERENCE_ODT := ./assets/reference.odt

TARGET_PDF := $(addsuffix .pdf, $(basename $(wildcard *.md)))
TARGET_ODT := $(addsuffix .odt, $(basename $(wildcard *.md)))

export SOURCE_DATE_EPOCH := 0

##################################################
## "all" target
##################################################

.PHONY: all
all: build

##################################################
## "build" target
##################################################

.PHONY: build
build: $(TARGET_PDF) $(TARGET_ODT)

.ONESHELL:
%.pdf: %.odt
	'$(SOFFICE)' --headless --convert-to 'pdf:writer_pdf_Export:{
		"SelectPdfVersion": { "type": "long", "value": "3" },
		"UseLosslessCompression": { "type": "boolean", "value": "true" },
		"RemovePersonalInformation": { "type": "boolean", "value": "true" }
	}' '$<'
	'$(EXIFTOOL)' -all:all= -overwrite_original '$@'
	'$(QPDF)' --linearize --replace-input '$@'

.ONESHELL:
%.odt: %.md
	'$(PANDOC)' '$<' \
		--reference-doc='$(PANDOC_REFERENCE_ODT)' \
		--output='$@'

##################################################
## "clean" target
##################################################

.PHONY: clean
clean:
	rm -f $(addprefix ', $(addsuffix ', $(TARGET_PDF) $(TARGET_ODT)))
