package tcs3.model.lab;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ReportDeliveryMethodConverter implements AttributeConverter<ReportDeliveryMethod, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ReportDeliveryMethod reportDeliveryMethod) {
		switch(reportDeliveryMethod) {
		case POST: 
			return 1;
		case SELF: 
			return 2;
		default:
			break;
		}
		return null;
	}

	@Override
	public ReportDeliveryMethod convertToEntityAttribute(Integer dbReportDeliveryMethod) {
		if(dbReportDeliveryMethod == 1) {
			return ReportDeliveryMethod.POST;
		} else if(dbReportDeliveryMethod == 2) {
			return ReportDeliveryMethod.SELF;
		} else {
			return ReportDeliveryMethod.UNKNOWN;
		}
	} 

}
