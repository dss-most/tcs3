package tcs3.model.lab;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ReportLanguageConverter implements AttributeConverter<ReportLanguage, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ReportLanguage reportLanguge) {
		switch(reportLanguge) {
		case TH: 
			return 1;
		case EN: 
			return 2;
		default:
			break;
		}
		return null;
	}

	@Override
	public ReportLanguage convertToEntityAttribute(Integer dbReportLanguage) {
		if(dbReportLanguage == 1) {
			return ReportLanguage.TH;
		} else if(dbReportLanguage == 2) {
			return ReportLanguage.EN;
		} else {
			return ReportLanguage.UNKNOWN;
		}
	} 

}
