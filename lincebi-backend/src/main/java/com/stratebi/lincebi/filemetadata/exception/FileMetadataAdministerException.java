package com.stratebi.lincebi.filemetadata.exception;

public class FileMetadataAdministerException extends Exception {

	private static final long serialVersionUID = 5851262847205513792L;

	public FileMetadataAdministerException(String userName) {
		super(String.format("User \"%s\" does not have administrator permission", userName));
	}

}
