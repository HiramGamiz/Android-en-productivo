package com.zgas.tesselar.myzuite.Utilities;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jbarrien on 25/11/2015.
 * Clase encargada de manejar los eventos con el web service.
 */
public class NetServices {

    //Post
    public String Post(String ... sParams) throws IOException {

        URL url = new URL(sParams[0]);//URL(sParams[0].toString());
        URLConnection urlConnection = url.openConnection();
        String sRes = "";

        try{
            HttpURLConnection httpurlconnection = (HttpURLConnection) urlConnection;
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpurlconnection.setRequestProperty("Token", sParams[1]);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.connect();


            //Parametros
            OutputStreamWriter writer = new OutputStreamWriter(httpurlconnection.getOutputStream());

            writer.write(sParams[2]);
            writer.flush();

            if(httpurlconnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                // A Simple JSON Response Read
                InputStream instream = httpurlconnection.getInputStream();
                String result= convertStreamToString(instream);
                Log.w("[CHECK]",result);
                sRes = result;
                // now you have the string representation of the HTML request
                instream.close();
            }else{

                InputStream instream = httpurlconnection.getErrorStream();
                String result= convertStreamToString(instream);
                Log.w("[CHECK]",result);
                sRes = result;
                // now you have the string representation of the HTML request
                instream.close();
            }



        }
        catch(Exception e)
        {
            Log.w("[CHECK]", e.toString());
        }

        return sRes;

    }

    //Get  URL
    public String Get(String ... sParams) throws IOException {


        URL obj = new URL(sParams[0]);
        HttpURLConnection httpurlconnection = (HttpURLConnection) obj.openConnection();
        String sRes = "";


        try {
            // optional default is GET
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpurlconnection.setRequestProperty("Token", sParams[1]);

            int responseCode = httpurlconnection.getResponseCode();

            if(httpurlconnection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                BufferedReader in = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {

                    response.append(inputLine);
                }

                sRes=response.toString();

                in.close();
            }

        }catch (Exception e){
            Log.w("[CHECK]", e.toString());
        }

        return sRes;
    }

    //Put
    public String Put(String ... sParams) throws IOException {

        URL url = new URL(sParams[0]);
        URLConnection urlConnection = url.openConnection();
        String sRes = "";

        try{
            HttpURLConnection httpurlconnection = (HttpURLConnection) urlConnection;
            httpurlconnection.setRequestMethod("PUT");
            httpurlconnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpurlconnection.setRequestProperty("Token", sParams[1]);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.connect();

            //Parametros
            OutputStreamWriter writer = new OutputStreamWriter(httpurlconnection.getOutputStream());

            writer.write(sParams[2]);
            writer.flush();

            if(httpurlconnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                // A Simple JSON Response Read
                InputStream instream = httpurlconnection.getInputStream();
                String result= convertStreamToString(instream);
                Log.w("[CHECK]",result);
                sRes = result;
                // now you have the string representation of the HTML request
                instream.close();
            }else{

                InputStream instream = httpurlconnection.getErrorStream();
                String result= convertStreamToString(instream);
                Log.w("[CHECK]",result);
                sRes = result;
                // now you have the string representation of the HTML request
                instream.close();
            }
        }
        catch(Exception e)
        {
            Log.w("[CHECK]", e.toString());
        }

        return sRes;

    }


    private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }




}


