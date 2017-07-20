package tcs3.model.fin;

import javax.persistence.AttributeConverter;

public class InvoiceLangugeConverter implements AttributeConverter<InvoiceLanguage, String> {

	@Override
	public String convertToDatabaseColumn(InvoiceLanguage invoiceLanguage) {
		switch(invoiceLanguage) {
		case TH: 
			return "T";
		case EN: 
			return "E";
		default:
			break;
		}
		
		return null;
	}

	@Override
	public InvoiceLanguage convertToEntityAttribute(String dbInvoiceLanguage) {
		if(dbInvoiceLanguage.equalsIgnoreCase("T") || 
				dbInvoiceLanguage.equalsIgnoreCase("TH") ) {
			return InvoiceLanguage.TH;
		} else if(dbInvoiceLanguage.equalsIgnoreCase("E") || 
				dbInvoiceLanguage.equalsIgnoreCase("EN") ) {
			return InvoiceLanguage.EN;
		} else {
			return InvoiceLanguage.UNKNOWN;
		}
	}

}
