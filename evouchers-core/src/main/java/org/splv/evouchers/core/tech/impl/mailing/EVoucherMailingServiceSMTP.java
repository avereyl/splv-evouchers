package org.splv.evouchers.core.tech.impl.mailing;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import org.splv.evouchers.core.business.beans.Attachment;
import org.splv.evouchers.core.config.properties.MailingProperties;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.tech.EVoucherMailingService;
import org.splv.evouchers.core.tech.exception.MailingException;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class EVoucherMailingServiceSMTP implements EVoucherMailingService {

	private final JavaMailSender javaMailSender;
	private final EVoucherTemplatingServiceFreemarker templatingService;
    private final MessageSource messageSource;
	private final MailingProperties mailingProperties; 
	
	@Override
	public void sendEVoucherPrint(EVoucher eVoucher, Locale locale, List<Attachment> attachments, Set<String> to, Set<String> cc) {

		Set<InternetAddress> toAddresses = new HashSet<>();
		Set<InternetAddress> ccAddresses = new HashSet<>();
		toAddresses.addAll(EVoucherMailingServiceSMTP.parse(eVoucher.getDonorEmail()));
		to.stream().map(EVoucherMailingServiceSMTP::parse).flatMap(List::stream).forEach(toAddresses::add);
		cc.stream().map(EVoucherMailingServiceSMTP::parse).flatMap(List::stream).forEach(ccAddresses::add);
		
		if (toAddresses.isEmpty()) {
			log.error("Unable to find any mail address.");
			throw new MailingException("Unable to find any mail address.");
		}
		
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			// add recipients
			messageHelper.setTo(toAddresses.toArray(new InternetAddress[0]));
			messageHelper.setCc(ccAddresses.toArray(new InternetAddress[0]));
			// add sender / bcc / replyTo
			messageHelper.setFrom(this.mailingProperties.getFrom());
			messageHelper.addBcc(this.mailingProperties.getArchive());
			// add subject
			messageHelper.setSubject(messageSource.getMessage("mail.subject", new Object[] {}, locale));
			
			// generate content																						
			String content = templatingService.buildEmailContentFromEVoucher(eVoucher, locale);
			messageHelper.setText(content, true);
			
			// add attachments
			addAttachements(attachments, messageHelper);
		};
		try {
			javaMailSender.send(messagePreparator);
			log.info("eVoucher {} sent to {}", eVoucher.getId(), eVoucher.getDonorEmail());
		} catch (MailException e) {
			log.error("Exception while sending email for eVoucher {} !", eVoucher.getId());
			log.error(e.toString());
			throw new MailingException("Unable to send mail!", e);
		}

	}

	private void addAttachements(final List<Attachment> attachments, MimeMessageHelper messageHelper) {
		try {
			for (Attachment attachment : attachments) {
				messageHelper.addAttachment(attachment.getFilename(), new ByteArrayResource(attachment.getData()));
			}
		} catch (MessagingException e) {
			log.error("Unable to add attachment to the mail.");
			throw new MailingException("Unable to add attachment.", e);
		}
	}
	
	private static List<InternetAddress> parse(String addresses) {
		try {
			return Arrays.asList(InternetAddress.parse(addresses));
		} catch (AddressException e) {
			log.error("Unable to parse the mail address.", e);
			throw new MailingException("Unable to parse the mail address.", e);
		}
	}
	
}
