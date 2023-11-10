package org.splv.evouchers.core.config.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.voucher")
public class VoucherGenerationProperties {

	
	Integer metadataCurrentVersion = 0;
	Map<Integer, VoucherProperties> properties = new HashMap<>();

	@Getter
	@Setter
	public static class VoucherProperties {
		Integer metadataVersion = 0;
		String contactEmail = "";
		VoucherResponsibleProperties responsible = new VoucherResponsibleProperties();
		boolean facturXenabled = false;
		VoucherFacturXProperties facturX = new VoucherFacturXProperties();
	}
	
	@Getter
	@Setter
	public static class VoucherResponsibleProperties {
		String title = "";
		String name = "";
		String signaturePath = "";
	}

	@Getter
	@Setter
	public static class VoucherFacturXProperties {
		String name = "";
		String address = "";
		String zipCode = "";
		String city = "";
		String countryCode = "";
	}

	public VoucherResponsibleProperties getResponsible() {
		return properties.get(getMetadataCurrentVersion()).getResponsible();
	}
	



}
