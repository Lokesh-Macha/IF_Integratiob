package com.hp.om.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestS4 {

	public static void main(String[] args) {
		
		String targetURL = "https://service-delivery-pr-30.dindc.g4ihos.itcs.hpecorp.net/api/simulate-sales-orders";
		String request = getS4Request();
		String response = null;
		try {
			response = sendPOSTReqGetRes(targetURL, request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
	
	private static String getS4Request(){
		JSONObject s4Request = new JSONObject();
		s4Request.put("dealId", "");
		s4Request.put("country", "CA");
		s4Request.put("priceList", "02");
		s4Request.put("priceDate", "23072018");
		s4Request.put("dealGeo", "");
		s4Request.put("discountGeo", "");
		s4Request.put("purchaseAgreement", "");
		s4Request.put("currency", "CAD");
		s4Request.put("businessRelationshipType", "");
		s4Request.put("listPriceIndicator", "X");
		s4Request.put("systemId", "CPQ");
		s4Request.put("incoTerms", "");
		s4Request.put("profileId", "");
		s4Request.put("offerId", "");
		
		JSONArray salesOrderItems = new JSONArray();
		JSONObject salesOrderItems1 = new JSONObject();
		salesOrderItems1.put("requestedQuantity", "1");
		salesOrderItems1.put("characteristicName", "");
		salesOrderItems1.put("requestedPartNo", "100017");
		salesOrderItems1.put("characteristicValue", "");
		salesOrderItems1.put("variantKey", "");
		salesOrderItems.put(salesOrderItems1);
		s4Request.put("salesOrderItems", salesOrderItems);
		
		JSONArray variants = new JSONArray();
		JSONObject variants1 = new JSONObject();
		variants1.put("variantKey", "");
		variants1.put("characteristicValue", "");
		variants1.put("materialNumber", "");
		variants1.put("characteristicName", "");
		variants.put(variants1);
		s4Request.put("variants", variants);
		

		JSONArray partners = new JSONArray();
		JSONObject partners1 = new JSONObject();
		partners1.put("shipToPartyNo", "");
		partners1.put("soldToPartyNo", "");
		partners1.put("soldToPartyRole", "");
		partners1.put("shipToPartyRole", "");
		partners.put(partners1);
		s4Request.put("partners", partners);
		
		return s4Request.toString();
	}
	
	
	private static String sendPOSTReqGetRes(String targetUrl,String postData) throws MalformedURLException, IOException{
		
				String truststorefilename = System.getProperty("CONFIG_DIR");
		truststorefilename = "C:\\Users\\lo354884\\git\\ngq-partner\\maven\\q2cngom\\src\\main\\resources\\config\\q2c-om\\dev\\truststore.jks";
		System.setProperty("javax.net.ssl.trustStore", truststorefilename);
		System.setProperty("javax.net.ssl.trustStorePassword", "truststore");
		URL url;
		HttpsURLConnection urlCon;
	    url  = new URL(targetUrl);
	    byte[] sendPostData= postData.getBytes( StandardCharsets.UTF_8 );
	    int    postDataLength = sendPostData.length;
	    StringBuffer response = new StringBuffer();
		try {
			KeyStore clientKeyStore = KeyStore.getInstance("JKS");
			InputStream readStream = new FileInputStream("C:\\Users\\lo354884\\git\\ngq-partner\\maven\\q2cngom\\src\\main\\resources\\config\\q2c-om\\dev\\truststore.jks");
			clientKeyStore.load(readStream, "truststore".toCharArray());
			/*KeyStore clientTrustStore = KeyStore.getInstance("JKS");
			InputStream readStream2 = new FileInputStream("C:\\Users\\lo354884\\git\\ngq-partner\\maven\\q2cngom\\src\\main\\resources\\config\\q2c-om\\dev\\truststore.jks");
			clientTrustStore.load(readStream2, "truststore".toCharArray());*/
			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyFactory.init(clientKeyStore, "wipro@123".toCharArray());
			/*TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(clientTrustStore);
			TrustManager[] tm = trustFactory.getTrustManagers();*/
			KeyManager[] km = keyFactory.getKeyManagers();
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(km, null, null);
			SSLSocketFactory sslSf = sslContext.getSocketFactory();
			URLConnection urlConnection = url.openConnection();
			 urlCon = (HttpsURLConnection) urlConnection;
			    urlCon.setDoOutput( true );
			    urlCon.setInstanceFollowRedirects(false);
			    urlCon.setRequestMethod( "POST" );
			    urlCon.setRequestProperty( "Content-Type", "application/json"); 
			    urlCon.setRequestProperty( "charset", "utf-8");
			    urlCon.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
			    urlCon.setSSLSocketFactory(sslSf);
			    //urlCon.setRequestProperty ("Authorization", "Basic " + new String(Base64.encode(userName+":"+password)));
			    //urlCon.setConnectTimeout(6000);
			    try( DataOutputStream wr = new DataOutputStream( urlCon.getOutputStream())) {
					   wr.write( sendPostData );
				}
			    urlCon.connect();
			    
			    
			    InputStream is = urlCon.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
			    urlCon.getResponseCode();
			    urlCon.disconnect();
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    return response.toString();
	}

	
}
