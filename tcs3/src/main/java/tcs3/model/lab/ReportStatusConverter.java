package tcs3.model.lab;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ReportStatusConverter implements AttributeConverter<ReportStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ReportStatus status) {
		return status.getCode();
	}

	@Override
	public ReportStatus convertToEntityAttribute(Integer dbStatus) {
		return ReportStatus.valueOf(dbStatus);
	} 

}
