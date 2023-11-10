package org.splv.evouchers.core.process.beans.converter;

import java.time.ZonedDateTime;

import org.splv.evouchers.core.domain.DocumentMetadata;
import org.splv.evouchers.core.domain.DocumentType;
import org.splv.evouchers.core.process.beans.out.EVoucherDocumentMetadataObject;
import org.springframework.core.convert.converter.Converter;

public class EVoucherDocumentMetadataObjectConverter implements Converter<DocumentMetadata, EVoucherDocumentMetadataObject> {

	@Override
	public EVoucherDocumentMetadataObject convert(DocumentMetadata source) {
		EVoucherDocumentMetadataObject object = new EVoucherDocumentMetadataObject();
		object.setId(source.getId() == null ? 0 : source.getId());
		object.setVersion(source.getVersion() == null ? 0 : source.getVersion());
		object.setCreatedBy(source.getCreatedBy().orElse(""));
		object.setCreatedDate(source.getCreatedDate().orElseGet(ZonedDateTime::now));
		object.setLastModifiedBy(source.getLastModifiedBy().orElse(""));
		object.setLastModifiedDate(source.getCreatedDate().orElseGet(ZonedDateTime::now));
		object.setDocumentType(source.getDocumentType() == null ? DocumentType.UNKNOWN : source.getDocumentType());
		return object;
	}

}
