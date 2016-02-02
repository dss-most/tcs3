package tcs3.model.lab;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobPriorityConverter implements AttributeConverter<JobPriority, Integer> {

	@Override
	public Integer convertToDatabaseColumn(JobPriority jobPriority) {
		switch(jobPriority) {
		case NORMAL: 
			return 1;
		case EXPRESS: 
			return 2;
		default:
			break;
		}
		return null;
	}

	@Override
	public JobPriority convertToEntityAttribute(Integer dbJobPriority) {
		if(dbJobPriority == 1) {
			return JobPriority.NORMAL;
		} else if(dbJobPriority == 2) {
			return JobPriority.EXPRESS;
		} else {
			return JobPriority.UNKNOWN;
		}
	} 

}
