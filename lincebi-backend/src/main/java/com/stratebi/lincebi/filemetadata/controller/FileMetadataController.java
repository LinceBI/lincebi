package com.stratebi.lincebi.filemetadata.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import org.codehaus.enunciate.Facet;
import org.ehcache.Cache;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratebi.lincebi.filemetadata.cache.FileMetadataCache;
import com.stratebi.lincebi.filemetadata.model.FileMetadataPath;
import com.stratebi.lincebi.filemetadata.model.FileMetadataTree;
import com.stratebi.lincebi.filemetadata.schema.FileMetadataPathArraySchema;
import com.stratebi.lincebi.filemetadata.schema.FileMetadataTreeArraySchema;
import com.stratebi.lincebi.filemetadata.service.FileMetadataService;
import com.stratebi.lincebi.util.CacheUtils;

@Path("/lincebi/api/file-metadata")
@Facet(name = "Unsupported")
public class FileMetadataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMetadataController.class);

	private static final Cache<String, String> CACHE = FileMetadataCache.getInstance().cache;

	private static final String DEFAULT_LOCALE_QUERY_PARAM = RepositoryFile.DEFAULT_LOCALE;
	private static final String DEFAULT_SHOW_HIDDEN_QUERY_PARAM = "auto";
	private static final String DEFAULT_DEPTH_QUERY_PARAM = "1";

	@POST
	@Path("/get")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response getController(
		@DefaultValue(FileMetadataController.DEFAULT_LOCALE_QUERY_PARAM) @QueryParam("locale") String locale,
		@DefaultValue(FileMetadataController.DEFAULT_SHOW_HIDDEN_QUERY_PARAM) @QueryParam("showHidden") String showHidden,
		@DefaultValue(FileMetadataController.DEFAULT_DEPTH_QUERY_PARAM) @QueryParam("depth") int depth,
		String input
	) {
		try {
			String response;
			String cacheKey = CacheUtils.getCacheKey(locale, showHidden, depth, input);

			if (FileMetadataController.CACHE.containsKey(cacheKey)) {
				response = FileMetadataController.CACHE.get(cacheKey);
			} else {
				ObjectMapper mapper = new ObjectMapper();

				JsonNode jsonInput = mapper.readTree(input);
				Set<ValidationMessage> errors = FileMetadataPathArraySchema.SCHEMA.validate(jsonInput);
				if (errors.size() > 0) {
					FileMetadataController.LOGGER.error("Invalid JSON schema");
					Response.serverError().build();
				}

				FileMetadataService fileMetadataService = new FileMetadataService();
				List<FileMetadataPath> fileMetadataPathList = Arrays.asList(mapper.treeToValue(jsonInput, FileMetadataPath[].class));
				List<FileMetadataTree> fileMetadataTreeList = new ArrayList<FileMetadataTree>();

				for (FileMetadataPath fileMetadataPath : fileMetadataPathList) {
					FileMetadataTree fileMetadataTree = fileMetadataService.getFileMetadata(fileMetadataPath, locale, showHidden, depth);
					if (fileMetadataTree != null) fileMetadataTreeList.add(fileMetadataTree);
				}

				response = mapper.writeValueAsString(fileMetadataTreeList);
				FileMetadataController.CACHE.put(cacheKey, response);
			}

			return Response.ok(response).build();
		} catch (Exception ex) {
			FileMetadataController.LOGGER.error(ex.getMessage());
		}

		return Response.serverError().build();
	}

	@POST
	@Path("/set")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response setController(
		@DefaultValue(FileMetadataController.DEFAULT_LOCALE_QUERY_PARAM) @QueryParam("locale") String locale,
		String input
	) {
		try {
			String response;

			ObjectMapper mapper = new ObjectMapper();

			JsonNode jsonInput = mapper.readTree(input);
			Set<ValidationMessage> errors = FileMetadataTreeArraySchema.SCHEMA.validate(jsonInput);
			if (errors.size() > 0) {
				FileMetadataController.LOGGER.error("Invalid JSON schema");
				Response.serverError().build();
			}

			FileMetadataService fileMetadataService = new FileMetadataService();
			List<FileMetadataTree> fileMetadataTreeList = Arrays.asList(mapper.treeToValue(jsonInput, FileMetadataTree[].class));
			List<FileMetadataPath> fileMetadataPathList = new ArrayList<FileMetadataPath>();

			for (FileMetadataTree fileMetadataTree : fileMetadataTreeList) {
				FileMetadataPath fileMetadataPath = fileMetadataService.setFileMetadata(fileMetadataTree, locale);
				if (fileMetadataPath != null) fileMetadataPathList.add(fileMetadataPath);
			}

			response = mapper.writeValueAsString(fileMetadataPathList);
			FileMetadataController.CACHE.clear();

			return Response.ok(response).build();
		} catch (Exception ex) {
			FileMetadataController.LOGGER.error(ex.getMessage());
		}

		return Response.serverError().build();
	}

}
