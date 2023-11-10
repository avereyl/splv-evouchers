package org.splv.evouchers.api.controller;

import static org.splv.evouchers.api.Constants.DEFAULT_API_CONTENT_TYPE_V1;

import java.util.Map;

import org.splv.evouchers.api.config.metadata.MetadataContact;
import org.splv.evouchers.api.config.metadata.MetadataProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RootController {
	
	private final MetadataProperties metadataProperties;

	@GetMapping(value = "/health")
	public Map<String, Object> health() {
		return Map.of("status", 1L, "message", "Up and running!");
	}
	
	@Operation(summary = "Get API title", description = "This method returns an (unique) identifying, functional descriptive name of the API.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description = "Title of the API.", content = @Content(schema = @Schema(implementation = String.class)))
	})
	@GetMapping(value = "/info/title", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getTitle() {
		return ResponseEntity.ok(metadataProperties.getTitle());
	}
	
	@Operation(summary = "Get API version", description = "This method the version of the API following semver.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description = "Version of the API.", content = @Content(schema = @Schema(implementation = String.class)))
	})
	@GetMapping(value ="/info/version", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getVersion() {
		return ResponseEntity.ok(metadataProperties.getVersion());
	}
	
	@Operation(summary = "Get API description", description = "This method returns a description for the API.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description = "Description of the API.", content = @Content(schema = @Schema(implementation = String.class)))
	})
	@GetMapping(value ="/info/description", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getDescription() {
		return ResponseEntity.ok(metadataProperties.getDescription());
	}
	
	
	@Operation(summary = "Get API responsible contact", description = "This method returns contact information of team responsible for the API.")
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200", description = "Team resaponsible for the API.", content = @Content(schema = @Schema(implementation = MetadataContact.class)))
	})
	@GetMapping(value ="/info/contact", produces = { DEFAULT_API_CONTENT_TYPE_V1 })
	public ResponseEntity<MetadataContact> getContact() {
		return ResponseEntity.ok(metadataProperties.getContact());
	}
	
	
}
