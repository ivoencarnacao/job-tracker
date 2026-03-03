package dev.ivoencarnacao.jobtracker.shared.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import dev.ivoencarnacao.jobtracker.shared.config.SecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	// --- Status, View Resolution, Content Type ---

	@Test
	void landingPageReturns200WithCorrectViewAndContentType() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	void registerPageReturns200WithCorrectViewAndContentType() throws Exception {
		this.mockMvc.perform(get("/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("identity/register"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	void loginPageReturns200WithCorrectViewAndContentType() throws Exception {
		this.mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("identity/login"))
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	// --- Security ---

	@Test
	void unknownUrlRequiresAuthentication() throws Exception {
		this.mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
	}

	// --- Layout Dialect Integration ---

	@Test
	void landingPageRendersLayoutFragments() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("id=\"main-content\"")))
			.andExpect(content().string(containsString("Skip to main content")))
			.andExpect(content().string(containsString("<footer")));
	}

	// --- SEO Meta Tags ---

	@Test
	void landingPageContainsSeoMetaTags() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("<title>")))
			.andExpect(content().string(containsString("name=\"description\"")))
			.andExpect(content().string(containsString("property=\"og:title\"")))
			.andExpect(content().string(containsString("property=\"og:description\"")));
	}

	// --- Vite Assets ---

	@Test
	void landingPageIncludesStylesheet() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("rel=\"stylesheet\"")));
	}

}
