package tcs3.model.lab;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RequestStatusConverter implements AttributeConverter<RequestStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(RequestStatus status) {
		return status.getCode();
	}

	@Override
	public RequestStatus convertToEntityAttribute(Integer dbStatus) {
		return RequestStatus.valueOf(dbStatus);
	} 

}
