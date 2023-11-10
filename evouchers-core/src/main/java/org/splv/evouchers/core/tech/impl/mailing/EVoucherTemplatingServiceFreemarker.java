package org.splv.evouchers.core.tech.impl.mailing;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.tech.exception.TemplatingException;
import org.splv.evouchers.core.tech.impl.support.PrintingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EVoucherTemplatingServiceFreemarker {

	private final String mailFromAddress;
	private final Configuration freemarkerConfiguration;
	

	public EVoucherTemplatingServiceFreemarker(@Value("${app.mailing.from}") final String mailFromAddress, final Configuration freemarkerConfiguration) {
		super();
		this.mailFromAddress = mailFromAddress;
		this.freemarkerConfiguration = freemarkerConfiguration;
	}


	public String buildEmailContentFromEVoucher(final EVoucher eVoucher, final Locale locale) {

		String content = "";
		Template contentTemplate;
		String templateName = "mail_%s.ftlh".formatted(locale.getLanguage().toLowerCase());
		try {
			contentTemplate = freemarkerConfiguration.getTemplate(templateName);
			Map<String, Object> model = new HashMap<>();
			
	        model.put("contributorName", EVoucherTemplatingServiceFreemarker.formatContributorName(eVoucher));
	        model.put("formattedAmount", PrintingHelper.formatAmount(eVoucher.getAmount().floatValue(), locale));
	        model.put("answerAddress", mailFromAddress);
	        	
			content = FreeMarkerTemplateUtils.processTemplateIntoString(contentTemplate, model);
		} catch (TemplateNotFoundException e) {
			log.error("Unable to find the template.", e);
			throw new TemplatingException(templateName, "Failed to load template " + templateName + " for locale " + locale, e);
		} catch (TemplateException | IOException e) {
			log.error("Unable to render the content of the given resource.", e);
			throw new TemplatingException(templateName, "Failed to translate resource to expected format.", e);
		}
		return content;
	}

	/**
	 * In case of name on several lines then we only keep the first not empty line.
	 * 
	 * @param eVoucher
	 * @return
	 */
	public static String formatContributorName(EVoucher eVoucher) {
		// @formatter:off
		return Arrays.asList(eVoucher.getDisplayName().split("\n"))
				.stream()
				.filter(StringUtils::hasLength)
				.findFirst()
				.orElseThrow(() -> new TemplatingException("Undefined", "Unable to format contributor name."));
		// @formatter:on
	}
	
}
