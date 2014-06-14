package org.webdev.kpoint.bl.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.util.encoders.Base64;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.util.ApplicationProperty;

public class Encryption {
		private static final KinekLogger logger = new KinekLogger(Encryption.class);
	
		Cipher ecipher;
        Cipher dcipher;
    
        // 8-byte Salt
        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
        };
    
        // Iteration count
        int iterationCount = 2;
    
        public Encryption(String passPhrase) {
            try {
                // Create the key
                KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
                SecretKey key = SecretKeyFactory.getInstance(
                    "PBEWithMD5AndDES").generateSecret(keySpec);
                ecipher = Cipher.getInstance(key.getAlgorithm());
                dcipher = Cipher.getInstance(key.getAlgorithm());
    
                // Prepare the parameter to the ciphers
                AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
    
                // Create the ciphers
                ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            } catch (Exception ex) {
            	logger.error(new ApplicationException("Error occurred creating the Encryption object", ex));
            }
        }
    
        public String encrypt(String str) {
            try {
                // Encode the string into bytes using utf-8
                byte[] utf8 = str.getBytes("UTF8");
    
                // Encrypt
                byte[] enc = ecipher.doFinal(utf8);
    
                // Encode bytes to base64 to get a string
                return new sun.misc.BASE64Encoder().encode(enc);
            } catch (Exception ex)
            {
            	Hashtable<String,String> logProps = new Hashtable<String,String>();
            	logProps.put("Input", str);
            	logger.error(new ApplicationException("Could not encrypt specified string", ex), logProps);
            }
            return null;
        }
    
        public String decrypt(String str) {
            try {
            	//update string so it contains original characters (even if they were url reserved chars)
            	str = addURLReservedChars(str);
        		
            	// Decode base64 to get bytes
                byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
    
                // Decrypt
                byte[] utf8 = dcipher.doFinal(dec);
    
                // Decode using utf-8
                return new String(utf8, "UTF8");
            }catch (Exception ex)
            {
            	Hashtable<String,String> logProps = new Hashtable<String,String>();
            	logProps.put("EncodedInput", str);
            	logger.error(new ApplicationException("Could not decrypt specified string", ex), logProps);
            }
            return null;
        }
        
        /**
    	 * Creates an encrypted command for a PayPal payment button based on the given
    	 * command text and account settings in application.properties
    	 */
    	public static String getPayPalEncryptionValue(Invoice invoice)
    	{
    		Class<?> temp = ApplicationProperty.class;
    		InputStream certPath = temp.getResourceAsStream(ExternalSettingsManager.getInvoiceLocalPaypalCertificateName());
    		InputStream privateKeyStream = temp.getResourceAsStream(ExternalSettingsManager.getInvoicePrivateKeyName());		
    		InputStream paypalCertPath = temp.getResourceAsStream(ExternalSettingsManager.getInvoicePaypalCertificateName());
    		String keyPass = ExternalSettingsManager.getInvoiceKeyPassword();		
    		
    		GregorianCalendar gc = new GregorianCalendar();
    		gc.setTime(invoice.getStartDate());
    		String itemName = ApplicationProperty.getInstance().getProperty("invoice.itemName") + " for " +
    			gc.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + ", " + gc.get(Calendar.YEAR);
    		
    		String cmdText =
    			"cert_id=" + ExternalSettingsManager.getInvoicePaypalCertificateId() + "\n" +
    			"cmd=" + ApplicationProperty.getInstance().getProperty("invoice.paypal.sendCommand") + "\n" +
    			"invoice=" + invoice.getInvoiceNumber() + "\n" +
    			"no_shipping=1" + "\n" +
    			"no_note=1" + "\n" + 
    			"item_name=" + itemName + "\n" +
    			"business=" + ExternalSettingsManager.getInvoiceMerchantEmail() + "\n" +
    			"amount=" + invoice.getAmountDue() + "\n" +
    			"currency_code=" + invoice.getCurrency();

    		try
    		{
    			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    			CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");

    			// Read the Private Key
    			KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
    			//ks.load( new FileInputStream(keyPath), keyPass.toCharArray() );
    			ks.load(privateKeyStream, keyPass.toCharArray() );
    			
    			String keyAlias = null;
    			Enumeration<String> aliases = ks.aliases();
    			while (aliases.hasMoreElements()) {
    				keyAlias = aliases.nextElement();
    			}

    			PrivateKey privateKey = (PrivateKey) ks.getKey( keyAlias, keyPass.toCharArray() );

    			// Read the Certificate
    			X509Certificate certificate = (X509Certificate) cf.generateCertificate( certPath );

    			// Read the PayPal Cert
    			X509Certificate payPalCert = (X509Certificate) cf.generateCertificate( paypalCertPath );

    			// Create the Data
    			byte[] data = cmdText.getBytes();

    			// Sign the Data with my signing only key pair
    			CMSSignedDataGenerator signedGenerator = new CMSSignedDataGenerator();

    			signedGenerator.addSigner( privateKey, certificate, CMSSignedDataGenerator.DIGEST_SHA1 );

    			ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
    			certList.add(certificate);
    			CertStore certStore = CertStore.getInstance( "Collection", new CollectionCertStoreParameters(certList) );
    			signedGenerator.addCertificatesAndCRLs(certStore);

    			CMSProcessableByteArray cmsByteArray = new CMSProcessableByteArray(data);
    			ByteArrayOutputStream baos = new ByteArrayOutputStream();
    			cmsByteArray.write(baos);
    			

    			CMSSignedData signedData = signedGenerator.generate(cmsByteArray, true, "BC");

    			byte[] signed = signedData.getEncoded();

    			CMSEnvelopedDataGenerator envGenerator = new CMSEnvelopedDataGenerator();
    			envGenerator.addKeyTransRecipient(payPalCert);
    			CMSEnvelopedData envData = envGenerator.generate( new CMSProcessableByteArray(signed),
    					CMSEnvelopedDataGenerator.DES_EDE3_CBC, "BC" );

    			byte[] pkcs7Bytes = envData.getEncoded();

    			String encryptedData = new String( DERtoPEM(pkcs7Bytes, "PKCS7") ); 
    			return encryptedData;
    			
    		}
    		catch(Exception ex)
    		{
               	logger.error(new ApplicationException("Could not create encrypted Paypal button", ex));
    		}
    		return null;
    	}
    	
    	/**
    	 * Converts a certificate from DER to PEM format
    	 */
    	public static byte[] DERtoPEM(byte[] bytes, String headfoot) 
    	{
    		ByteArrayOutputStream pemStream = new ByteArrayOutputStream();
    		PrintWriter writer = new PrintWriter(pemStream);
    		
    		byte[] stringBytes = Base64.encode(bytes);
    		
    		
    		
    		String encoded = new String(stringBytes);

    		if (headfoot != null) {
    			writer.print("-----BEGIN " + headfoot + "-----\n");
    		}

    		// write 64 chars per line till done
    		int i = 0;
    		while ((i + 1) * 64 < encoded.length()) {
    			writer.print(encoded.substring(i * 64, (i + 1) * 64));
    			writer.print("\n");
    			i++;
    		}
    		if (encoded.length() % 64 != 0) {
    			writer.print(encoded.substring(i * 64)); // write remainder
    			writer.print("\n");
    		}
    		if (headfoot != null) {
    			writer.print("-----END " + headfoot + "-----\n");
    		}
    		writer.flush();
    		return pemStream.toByteArray();
    	}
    	
    	/**
    	 * Removes reserved characters from a url string and
    	 * replaces them with their hex value
    	 * @param str The string to replace
    	 * @return The replaced string
    	 */
    	public static String removeURLReservedChars(String str) {
    		str = str.replace("$", "%24");
    		str = str.replace("&", "%26");
    		str = str.replace("+", "%2B");
    		str = str.replace(",", "%2C");
    		str = str.replace("/", "%2F");
    		str = str.replace(":", "%3A");
    		str = str.replace(";", "%3B");
    		str = str.replace("=", "%3D");
    		str = str.replace("?", "%3F");
    		str = str.replace("@", "%40");
    		return str;
    	}
    	
    	/**
    	 * Adds reserved characters back into a url string
    	 * @param str The string to replace
    	 * @return The replaced string
    	 */
    	public static String addURLReservedChars(String str) {
    		str = str.replace("%24", "$");
    		str = str.replace("%26", "&");
    		str = str.replace("%2B", "+");
    		str = str.replace("%2C", ",");
    		str = str.replace("%2F", "/");
    		str = str.replace("%3A", ":");
    		str = str.replace("%3B", ";");
    		str = str.replace("%3D", "=");
    		str = str.replace("%3F", "?");
    		str = str.replace("%40", "@");
    		return str;
    	}
    }
