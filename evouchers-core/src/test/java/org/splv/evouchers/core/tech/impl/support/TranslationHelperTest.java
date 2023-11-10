package org.splv.evouchers.core.tech.impl.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;

class TranslationHelperTest {

	private static TranslationHelper translationHelperFR = null;
	private static TranslationHelper translationHelperEN = null;
	private static TranslationHelper translationHelperFRwithPrefix = null;
	private static TranslationHelper translationHelperENwithPrefix = null;

	@BeforeAll
	static void init() {
		StaticMessageSource messageSource = new StaticMessageSource();

		Map<String, String> messagesMapFR = new HashMap<>();
		Map<String, String> messagesMapEN = new HashMap<>();

		messagesMapFR.put("greetings", "Salut");
		messagesMapEN.put("greetings", "Hello");
		messagesMapFR.put("prefix.greetings", "Salut");
		messagesMapEN.put("prefix.greetings", "Hello");
		messagesMapFR.put("prefix.greetings-with-value", "Salut {0}!");
		messagesMapEN.put("prefix.greetings-with-value", "Hello {0}!");

		messageSource.addMessages(messagesMapFR, Locale.FRENCH);
		messageSource.addMessages(messagesMapEN, Locale.ENGLISH);
		translationHelperFR = new TranslationHelper(messageSource, Locale.FRENCH);
		translationHelperEN = new TranslationHelper(messageSource, Locale.ENGLISH);
		translationHelperFRwithPrefix = new TranslationHelper(messageSource, Locale.FRENCH, "prefix");
		translationHelperENwithPrefix = new TranslationHelper(messageSource, Locale.ENGLISH, "prefix");
	}

	@Test
	void helperTest() {
		
		assertEquals(Locale.FRENCH, translationHelperFR.getLocale());
		assertEquals(Locale.ENGLISH, translationHelperEN.getLocale());
		assertEquals("Salut", translationHelperFR.getMessage("greetings"));
		assertEquals("Hello", translationHelperEN.getMessage("greetings"));
		assertEquals("Salut", translationHelperFRwithPrefix.getMessage("greetings"));
		assertEquals("Hello", translationHelperENwithPrefix.getMessage("greetings"));
		assertEquals("Salut toi!", translationHelperFRwithPrefix.getMessage("greetings-with-value", "toi"));
		assertEquals("Hello you!", translationHelperENwithPrefix.getMessage("greetings-with-value", "you"));
	}

}
