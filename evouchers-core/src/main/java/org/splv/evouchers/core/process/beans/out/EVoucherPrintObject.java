package org.splv.evouchers.core.process.beans.out;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.DocumentType;
import org.springframework.util.MimeType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EVoucherPrintObject {

	public static final EVoucherPrintObject EMPTY = new EVoucherPrintObject();

	private long id;
	private long version;
	private byte[] data;
	
	private DocumentType documentType;
	private String filename = "";
	private MimeType mimeType = Constants.DEFAULT_EVOUCHER_PRINT_MIME_TYPE;
}
