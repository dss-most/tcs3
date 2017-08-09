package tcs3.webUI;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NumToBaht {
    
    /** Creates a new instance of NumToBaht */
    public NumToBaht() {
    }
    
    private static  char fristBaht;
    
    private static  String baht, stang;
    
    private static  int sLen;
    
    private static  StringBuffer word;
    
    private static DecimalFormat standardFormat = new DecimalFormat("#.#");
    private static DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    private static DecimalFormat currencyNoCommaFormat = new DecimalFormat("#.00");
    private static SimpleDateFormat thaiDateFormat = new SimpleDateFormat("d MMM yyyy", new Locale("th", "TH"));
    
// to make sure str meet our format,
// this str should be converted from double number in the format of xxx.xx
// this class won't check format of the number.
    private static void getWord(String str, String currency, String subcurrency){
        StringBuffer txt = new StringBuffer();
        word = new StringBuffer();
        int idx = str.indexOf(".");
        
        if (idx == -1) {
            
            txt.append(str);
            // prevent error from unwanted (stupid) zeros such as 00000001234.25
            while(txt.length() > 1 && txt.charAt(0) == '0')
                txt.deleteCharAt(0);
            
            baht = txt.toString();
            fristBaht = baht.charAt(0);
            
            stang = "";
            
        } else {
            
            txt.append(str.substring(0, idx));
            // prevent error from unwanted (stupid) zeros
            while(txt.length() > 1 && txt.charAt(0) == '0')
                txt.deleteCharAt(0);
            
            baht = txt.toString();
            
            // make sure formatted number is xxx.xx before this
            stang = str.substring(idx + 1, str.length() );
            
            fristBaht = baht.charAt(0);
            
            // if stang is only a digit we need to append zero , case value 1.5 -> 1.50
            if(stang.length() == 1)
                stang += "0";
            
            // remove unwanted zero , case value = x.0x so 01 is หนึ่ง not เอ็ด
            if(stang.charAt(0) == '0')
                stang = "" + stang.charAt(1);
            
        }
        
        //bLen = baht.length();
        
        sLen = stang.length();
        if(fristBaht != '0'){
            int len = (baht.length()-1) / 6;
            for (int i = 0; i <= len; i++) {
                if(i < len){
                    addBaht(baht.substring(0,baht.length() - ((len-i) * 6)));
                    baht = baht.substring(baht.length() - ((len-i) * 6));
                    for (int j = i; j < len; j++) {
                        word.append("ล้าน");
                    }
                }else
                    addBaht(baht);
            }
            
            word.append(currency);
            
        }
        
        if(sLen > 0 && stang.compareTo("0") != 0){
            
            addBaht(stang);
            word.append(subcurrency);
            
        } else {
        	word.append("ถ้วน");
        }
        
    }//end getWord
    
    
    
    private static void addBaht(String str){
        
        int countDown = str.length();
        
        int idxInc = 0;
        
        while(countDown > 0){
            
            char g = str.charAt(idxInc);
            
            if(g != '0'){
                
                increBaht(g,countDown,idxInc);
                
            }//end if
            
            countDown--;
            
            idxInc++;
            
        }//end while
        
    } //end addBaht
    
    
    private static void increBaht(char g, int CounDown, int idx){
        
        switch(g){
            
            case '1':
                
                if (CounDown == 1 && idx > 0)
                    
                    word.append("เอ็ด");
                
                else if (CounDown > 2 || CounDown == 1)
                    
                    word.append("หนึ่ง");
                
                
                break;
                
            case '2':
                
                if (CounDown != 2)
                    
                    word.append("สอง");
                
                else
                    
                    word.append("ยี่");
                
                break;
                
            case '3': word.append("สาม");break;
            
            case '4': word.append("สี่");break;
            
            case '5': word.append("ห้า");break;
            
            case '6': word.append("หก");break;
            
            case '7': word.append("เจ็ด");break;
            
            case '8': word.append("แปด");break;
            
            case '9': word.append("เก้า");break;
            
        }//end switch1
        
        
        
        switch(CounDown){
            
            case 2: word.append("สิบ");break;
            
            case 3: word.append("ร้อย");break;
            
            case 4: word.append("พัน");break;
            
            case 5: word.append("หมื่น");break;
            
            case 6: word.append("แสน");break;
            
        }//end switch2
        
    }//end increWord
    
    public static String formatDate(Date date) {
    	return date==null?" - ":thaiDateFormat.format(date);
    }
    
    public static String toBaht(Double number) {
    	if(number == null ) return "";
    	return toBaht(standardFormat.format(number), "บาท", "สตางค์");
    }
    
    public static String toCurrencyDigit(Double number) {
    	if(number == null ) return "";
    	return currencyFormat.format(number);
    }
    
    public static String toCurrencyDigit(BigDecimal number) {
    	if(number == null) return "0.00";
    	//if(number.equals(0.00f) return "0.00");
    	return currencyFormat.format(number);
    }
    
    public static String toBarcode(String ref1, Double amount) {
    	String amountString = "0";
    	if(amount != null ){ 
    		 amountString = currencyNoCommaFormat.format(amount);
    	} 
    	// now remove "."
    	amountString = amountString.replaceAll("\\.", "");
    	return "|099400016169701\r" + ref1 + "\r\r" + amountString;
    }
    
    public static String toBaht(String num, String currency, String subcurrency){
    	if(num.charAt(0) == '.') {
    		getWord("0" + num, currency, subcurrency);
    	} else {
    		getWord(num, currency, subcurrency);
    	}
        return word.toString().trim();
        
    }//
    
    public static void main(String[] args) {
    	System.out.println(toBaht("100.20","บาท","สตางค์"));
    	System.out.println(toBaht(123541.0));
    	System.out.println(formatDate(new Date()));
    	
    	System.out.println(toBarcode("5500500051", 3500.00));
    }

}
