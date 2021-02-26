package com.stratebi.lincebi.filemetadata.exception;

public class FileMetadataReadException extends Exception {

	private static final long serialVersionUID = -5057916271984844260L;

	public FileMetadataReadException(String userName) {
		super(String.format("User \"%s\" does not have read permission", userName));
	}

	public FileMetadataReadException(String userName, String path) {
		super(String.format("User \"%s\" does not have read permission for: %s", userName, path));
	}

}
