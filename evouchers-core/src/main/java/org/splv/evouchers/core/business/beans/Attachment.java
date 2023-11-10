package org.splv.evouchers.core.business.beans;

import org.springframework.util.MimeType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Attachment {

	private final byte[] data;
	private final String filename;
	private final MimeType mimeType;
}
