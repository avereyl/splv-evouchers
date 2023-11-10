package org.splv.evouchers.core.process.beans.in;

import java.io.Serializable;

import org.splv.evouchers.core.Constants;

import lombok.Data;

@Data
public class EVoucherSearchBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
    private String query;
    
    private Double latitude;
    private Double longitude;
    
    private int nbHits = 10;
}
