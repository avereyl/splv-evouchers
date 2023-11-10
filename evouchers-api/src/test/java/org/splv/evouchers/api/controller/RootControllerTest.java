package org.splv.evouchers.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.api.config.MetadataConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RootController.class)
@Import(MetadataConfig.class)
class RootControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/health"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is(1)));
	}
}
