package com.stratebi.lincebi.filemetadata.schema;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

public class FileMetadataPathArraySchema {

	private static final JsonSchemaFactory FACTORY = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

	public static final JsonSchema SCHEMA = FACTORY.getSchema((""
		+ "{"
		+ "  `$id`: `http://lincebi.com/schemas/file-metadata/get.json`,"
		+ "  `$schema`: `http://json-schema.org/draft-07/schema#`,"
		+ "  `type`: `array`,"
		+ "  `items`: {"
		+ "    `type`: `object`,"
		+ "    `required`: [`fullPath`],"
		+ "    `additionalProperties`: false,"
		+ "    `properties`: {"
		+ "      `fullPath`: {`type`: `string`, `minLength`: 1, `maxLength`: 4096}"
		+ "    }"
		+ "  }"
		+ "}"
	).replace('`', '"'));

}
