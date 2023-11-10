package org.splv.evouchers.core.tech;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.splv.evouchers.core.business.beans.Attachment;
import org.splv.evouchers.core.domain.EVoucher;

public interface EVoucherMailingService {

	void sendEVoucherPrint(EVoucher eVoucher, Locale locale, List<Attachment> attachements, Set<String> to, Set<String> cc);
	
}
