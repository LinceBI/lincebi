package com.stratebi.lincebi.filemetadata.schema;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

public class FileMetadataTreeArraySchema {

	private static final JsonSchemaFactory FACTORY = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

	public static final JsonSchema SCHEMA = FACTORY.getSchema((""
		+ "{"
		+ "  `$id`: `http://lincebi.com/schemas/file-metadata/set.json`,"
		+ "  `$schema`: `http://json-schema.org/draft-07/schema#`,"
		+ "  `type`: `array`,"
		+ "  `items`: {"
		+ "    `type`: `object`,"
		+ "    `required`: [`path`],"
		+ "    `additionalProperties`: false,"
		+ "    `properties`: {"
		+ "      `id`: {`type`: `string`, `pattern`: `^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$`},"
		+ "      `path`: {`type`: `string`, `minLength`: 1, `maxLength`: 4096},"
		+ "      `parent`: {`type`: [`string`, `null`], `minLength`: 0, `maxLength`: 4096},"
		+ "      `name`: {`type`: `string`, `minLength`: 0, `maxLength`: 4096},"
		+ "      `title`: {`type`: `string`, `minLength`: 0, `maxLength`: 4096},"
		+ "      `description`: {`type`: `string`, `minLength`: 0, `maxLength`: 4096},"
		+ "      `extension`: {`type`: `string`, `minLength`: 0, `maxLength`: 4096},"
		+ "      `openUrl`: {`type`: `string`, `minLength`: 1, `maxLength`: 4096},"
		+ "      `editUrl`: {`type`: [`string`, `null`], `minLength`: 1, `maxLength`: 4096},"
		+ "      `created`: {`type`: `string`, `pattern`: `^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}Z$`},"
		+ "      `modified`: {`type`: `string`, `pattern`: `^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}Z$`},"
		+ "      `isFolder`: {`type`: `boolean`},"
		+ "      `isHidden`: {`type`: `boolean`},"
		+ "      `isFavorite`: {`type`: `boolean`},"
		+ "      `isRecent`: {`type`: `boolean`},"
		+ "      `isHome`: {`type`: `boolean`},"
		+ "      `isGlobal`: {`type`: `boolean`},"
		+ "      `isReadonly`: {`type`: `boolean`},"
		+ "      `properties`: {"
		+ "        `type`: `object`,"
		+ "        `maxProperties`: 50,"
		+ "        `properties`: {"
		+ "          `file.title`: {`type`: `string`, `minLength`: 0, `maxLength`: 4096},"
		+ "          `file.description`: {`type`: `string`, `minLength`: 0, `maxLength`: 4096}"
		+ "        },"
		+ "        `patternProperties`: {"
		+ "          `.*`: {`type`: `string`, `minLength`: 0, `maxLength`: 1500000}"
		+ "        }"
		+ "      },"
		+ "      `children`: {`$ref`: `#`}"
		+ "    }"
		+ "  }"
		+ "}"
	).replace('`', '"'));

}
