package com.stratebi.lincebi.filemetadata.exception;

public class FileMetadataWriteException extends Exception {

	private static final long serialVersionUID = 2629782002260471323L;

	public FileMetadataWriteException(String userName) {
		super(String.format("User \"%s\" does not have write permission", userName));
	}

	public FileMetadataWriteException(String userName, String path) {
		super(String.format("User \"%s\" does not have write permission for: %s", userName, path));
	}

	public FileMetadataWriteException(String userName, String path, String property) {
		super(String.format("User \"%s\" cannot set property \"%s\" for: %s", userName, property, path));
	}

}
