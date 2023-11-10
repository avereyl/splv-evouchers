package org.splv.evouchers.core.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

import org.hibernate.search.mapper.pojo.bridge.builtin.annotation.Latitude;
import org.hibernate.search.mapper.pojo.bridge.builtin.annotation.Longitude;
import org.splv.evouchers.core.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Coordinates implements Serializable {

	private static final long serialVersionUID = Constants.SERIAL_VERSION;
	
	@Latitude 
	private Double latitude;
	@Longitude
	private Double longitude;
}
