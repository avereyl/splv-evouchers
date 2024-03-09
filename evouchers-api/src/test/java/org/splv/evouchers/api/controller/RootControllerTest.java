package org.splv.evouchers.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.api.config.MetadataConfig;
import org.splv.evouchers.api.config.metadata.MetadataProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RootController.class)
@Import(MetadataConfig.class)
class RootControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MetadataProperties metadataProperties;

	@Test
	void shouldReturnValidHealthStatus() throws Exception {
		this.mockMvc.perform(get("/health"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is(1)));
	}
	
	@Test
	void shouldReturnApiInfoTitle() throws Exception {
		this.mockMvc.perform(get("/info/title"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("SPLV-eVouchers-API")));
	}
	
	@Test
	void shouldReturnApiVersion() throws Exception {
		this.mockMvc.perform(get("/info/version"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(metadataProperties.getVersion())));
	}
	
	@Test
	void shouldReturnApiDescription() throws Exception {
		this.mockMvc.perform(get("/info/description"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(metadataProperties.getDescription())));
	}
	
	@Test
	void shouldReturnApiContact() throws Exception {
		this.mockMvc.perform(get("/info/contact"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(metadataProperties.getContact().getName())))
		.andExpect(jsonPath("$.url", is(metadataProperties.getContact().getUrl())))
		.andExpect(jsonPath("$.email", is(metadataProperties.getContact().getEmail())))
		;
	}
}
