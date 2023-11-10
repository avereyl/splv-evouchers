package org.splv.evouchers.core.tech.impl.printing;

import static com.google.zxing.BarcodeFormat.QR_CODE;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.tech.EVoucherSigningService;
import org.splv.evouchers.core.tech.exception.QRCodeGenerationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class QRCodeGenerator {

	
	/**
	 * Setting preferred size to l let's ZXing choose the best size.
	 */
	private static final int QR_CODE_SIZE = 1;

	private static final Map<EncodeHintType, Object> ENCODING_HINTS = new EnumMap<>(EncodeHintType.class);
	static {
		ENCODING_HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		// MARGIN (aka "quiet zone" for QR code) is set to 0 to be handled in the exporters
		ENCODING_HINTS.put(EncodeHintType.MARGIN, 0);
	}

	private final QRCodeSVGExporter svgExporter;
	private final EVoucherSigningService signingService;
	
	private BitMatrix generateCode(String codeContent) {
		QRCodeWriter qrcodeWriter = new QRCodeWriter();
		try {
			return qrcodeWriter.encode(codeContent, QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, ENCODING_HINTS);
		} catch (WriterException e) {
			log.error("Unable to write the QR code matrix from the given content.", e);
			throw new QRCodeGenerationException("QR code matrix generation failed.", e);
		}
	}

	private BitMatrix generateCode(EVoucher eVoucher) {
		return generateCode(String.format(Constants.BARCODE_DATA_FORMAT, signingService.signEVoucher(eVoucher)));
	}

	
	public String generateQRCodeAsSVG(EVoucher eVoucher) {
		Document document = svgExporter.export(generateCode(eVoucher), Optional.empty());
		return svgExporter.svgDocumentToString(document);
	}
	
	

}
