package com.stratebi.lincebi.filemetadata.schema;

import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;

public class FileMetadataPathArraySchema {

	private static final SchemaRegistry FACTORY = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7);

	public static final Schema SCHEMA = FACTORY.getSchema((""
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
