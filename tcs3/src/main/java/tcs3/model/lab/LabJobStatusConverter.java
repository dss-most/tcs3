package tcs3.model.lab;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LabJobStatusConverter implements AttributeConverter<LabJobStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(LabJobStatus status) {
		return status.getCode();
	}

	@Override
	public LabJobStatus convertToEntityAttribute(Integer dbStatus) {
		return LabJobStatus.valueOf(dbStatus);
	} 

}
