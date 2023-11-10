package org.splv.evouchers.core.tech;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import org.splv.evouchers.core.domain.EVoucher;

public interface EVoucherPrintingService {

	ByteArrayOutputStream printEVoucher(final EVoucher eVoucher, final Locale locale);
	ByteArrayOutputStream printCertificate(final EVoucher eVoucher, final Locale locale);
	
}
