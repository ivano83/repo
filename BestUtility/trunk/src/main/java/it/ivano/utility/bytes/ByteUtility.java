package it.ivano.utility.bytes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ByteUtility {

	
	public static String convertByte(Long byteSize) {
		
		String res = "";
		if(byteSize<1000)
			res = byteSize + " Byte";
		else {
			BigDecimal resByte = new BigDecimal(byteSize / 1024, new MathContext(3, RoundingMode.HALF_UP));
			resByte.setScale(3);
			if(resByte.floatValue()>1000) {
				BigDecimal newValue = new BigDecimal(resByte.floatValue() / 1024, new MathContext(3, RoundingMode.HALF_UP));
				newValue.setScale(3);
				resByte = newValue;
				res = resByte + " MB";
				if(resByte.floatValue()>1000) {
					newValue = new BigDecimal(resByte.floatValue() / 1024, new MathContext(3, RoundingMode.HALF_UP));
					newValue.setScale(3);
					resByte = newValue;
					res = resByte + " TB";
				}
			}
			else {
				res = resByte + " KB";
			}
		}
		
		return res+" ("+byteSize+")";
	}
	
	public static void main(String[] args) {
		System.out.println(convertByte(2493493L));
		System.out.println(convertByte(2493L));
		System.out.println(convertByte(2493453932L));
		System.out.println(convertByte(4534L));
		System.out.println(convertByte(667342000L));
		System.out.println(convertByte(500L));
	}
}
