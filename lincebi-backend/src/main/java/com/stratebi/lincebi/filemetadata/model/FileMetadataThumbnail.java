package com.stratebi.lincebi.filemetadata.model;

public class FileMetadataThumbnail {

	private final byte[] bytes;
	private final String mimeType;

	public FileMetadataThumbnail(byte[] bytes, String mimeType) {
		this.bytes = bytes;
		this.mimeType = mimeType;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public String getMimeType() {
		return this.mimeType;
	}

}
