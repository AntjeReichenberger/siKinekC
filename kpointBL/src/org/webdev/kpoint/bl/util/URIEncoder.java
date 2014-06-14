package org.webdev.kpoint.bl.util;

public class URIEncoder {
	 
    private static String mark = "-_.!~*'()\"";
 
    public static String encode(String argString) {
        StringBuilder uri = new StringBuilder(); // Encoded URL
 
        char[] chars = argString.toCharArray();
        for(int i = 0; i<chars.length; i++) {
            char c = chars[i];
            if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') || mark.indexOf(c) != -1) {
                uri.append(c);
            }
            else {
                uri.append("%");
                uri.append(Integer.toHexString((int)c));
            }
        }
        return uri.toString();
    }
}
