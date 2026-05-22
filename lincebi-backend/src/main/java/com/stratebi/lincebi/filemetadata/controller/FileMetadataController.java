package com.stratebi.lincebi.filemetadata.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import com.stratebi.lincebi.filemetadata.cache.FileMetadataCache;
import com.stratebi.lincebi.filemetadata.model.FileMetadataPath;
import com.stratebi.lincebi.filemetadata.model.FileMetadataTree;
import com.stratebi.lincebi.filemetadata.schema.FileMetadataPathArraySchema;
import com.stratebi.lincebi.filemetadata.schema.FileMetadataTreeArraySchema;
import com.stratebi.lincebi.filemetadata.service.FileMetadataService;
import com.stratebi.lincebi.util.KeyUtils;
import org.codehaus.enunciate.Facet;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("/lincebi/api/file-metadata")
@Facet(name = "Unsupported")
public class FileMetadataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMetadataController.class);

	private static final String DEFAULT_LOCALE_QUERY_PARAM = RepositoryFile.DEFAULT_LOCALE;
	private static final String DEFAULT_SHOW_HIDDEN_QUERY_PARAM = "auto";
	private static final String DEFAULT_DEPTH_QUERY_PARAM = "1";
	private static final String DEFAULT_REFRESH_QUERY_PARAM = "false";

	@POST
	@Path("/get")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response getController(
		@DefaultValue(FileMetadataController.DEFAULT_LOCALE_QUERY_PARAM) @QueryParam("locale") String locale,
		@DefaultValue(FileMetadataController.DEFAULT_SHOW_HIDDEN_QUERY_PARAM) @QueryParam("showHidden") String showHidden,
		@DefaultValue(FileMetadataController.DEFAULT_DEPTH_QUERY_PARAM) @QueryParam("depth") int depth,
		@DefaultValue(FileMetadataController.DEFAULT_REFRESH_QUERY_PARAM) @QueryParam("refresh") boolean refresh,
		String input
	) {
		try {
			String userName = PentahoSessionHolder.getSession().getName();

			FileMetadataService fileMetadataService = new FileMetadataService();
			String effectiveShowHidden = "auto".equals(showHidden) ? String.valueOf(fileMetadataService.isShowHiddenFiles()) : showHidden;
			String cacheKey = KeyUtils.getKeyName(userName, locale, effectiveShowHidden, depth, input);

			String response = FileMetadataCache.getInstance().getOrCompute(cacheKey, userName, refresh, () -> {
				try {
					ObjectMapper mapper = new ObjectMapper();

					JsonNode jsonInput = mapper.readTree(input);
					Set<ValidationMessage> errors = FileMetadataPathArraySchema.SCHEMA.validate(jsonInput);
					if (errors.size() > 0) {
						FileMetadataController.LOGGER.error("Invalid JSON schema");
						return null;
					}

					FileMetadataPath[] fileMetadataPathList = mapper.treeToValue(jsonInput, FileMetadataPath[].class);
					List<FileMetadataTree> fileMetadataTreeList = new ArrayList<>();

					for (FileMetadataPath fileMetadataPath : fileMetadataPathList) {
						FileMetadataTree fileMetadataTree = fileMetadataService.getFileMetadata(fileMetadataPath, locale, effectiveShowHidden, depth);
						if (fileMetadataTree != null) fileMetadataTreeList.add(fileMetadataTree);
					}

					return mapper.writeValueAsString(fileMetadataTreeList);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});

			if (response == null) return Response.serverError().build();
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
				return Response.serverError().build();
			}

			FileMetadataService fileMetadataService = new FileMetadataService();
			FileMetadataTree[] fileMetadataTreeList = mapper.treeToValue(jsonInput, FileMetadataTree[].class);
			List<FileMetadataPath> fileMetadataPathList = new ArrayList<>();

			boolean needsGlobalClear = false;
			for (FileMetadataTree fileMetadataTree : fileMetadataTreeList) {
				FileMetadataPath fileMetadataPath = fileMetadataService.setFileMetadata(fileMetadataTree, locale);
				if (fileMetadataPath != null) fileMetadataPathList.add(fileMetadataPath);
				boolean perUserOnly = (fileMetadataTree.hasIsFavorite() || fileMetadataTree.hasIsRecent())
					&& !fileMetadataTree.hasTitle()
					&& !fileMetadataTree.hasDescription()
					&& !fileMetadataTree.hasProperties()
					&& !fileMetadataTree.hasIsHome();
				if (!perUserOnly) needsGlobalClear = true;
			}

			response = mapper.writeValueAsString(fileMetadataPathList);
			if (needsGlobalClear) {
				FileMetadataCache.getInstance().clear();
			} else {
				String userName = PentahoSessionHolder.getSession().getName();
				FileMetadataCache.getInstance().clear(userName);
			}

			return Response.ok(response).build();
		} catch (Exception ex) {
			FileMetadataController.LOGGER.error(ex.getMessage());
		}

		return Response.serverError().build();
	}

}
