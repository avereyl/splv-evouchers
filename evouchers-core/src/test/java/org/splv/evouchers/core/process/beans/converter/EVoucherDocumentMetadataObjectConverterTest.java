package org.splv.evouchers.core.process.beans.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.DocumentMetadata;
import org.splv.evouchers.core.domain.DocumentType;
import org.splv.evouchers.core.process.beans.out.EVoucherDocumentMetadataObject;

class EVoucherDocumentMetadataObjectConverterTest {

private EVoucherDocumentMetadataObjectConverter converter = new EVoucherDocumentMetadataObjectConverter();
	
	@Test
	void minimalConversion() {
		//given
		var metadata = new DocumentMetadata();
		//when
		EVoucherDocumentMetadataObject metadataObject = converter.convert(metadata);
		//then
		assertEquals(DocumentType.UNKNOWN, metadataObject.getDocumentType());
	}
	
	@Test
	void fullConversion() {
		//given
		String auditor = Constants.DEFAULT_AUDITOR;
		ZonedDateTime createdDate = ZonedDateTime.now();
		var metadata = new DocumentMetadata();
		metadata.setDocumentType(DocumentType.VOUCHER);
		metadata.setCreatedBy(auditor);
		metadata.setCreatedDate(createdDate);
		metadata.setLastModifiedBy(auditor);
		metadata.setLastModifiedDate(createdDate);
		metadata.setId(1L);
		metadata.setVersion(2L);
		//when
		EVoucherDocumentMetadataObject metadataObject = converter.convert(metadata);
		//then
		assertEquals(createdDate, metadataObject.getCreatedDate());
		assertEquals(auditor, metadataObject.getCreatedBy());
		assertEquals(DocumentType.VOUCHER, metadataObject.getDocumentType());
		
	}
}
