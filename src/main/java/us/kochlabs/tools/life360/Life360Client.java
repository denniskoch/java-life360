package us.kochlabs.tools.life360;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import us.kochlabs.tools.life360.auth.Bearer;
import us.kochlabs.tools.life360.circles.Circle;
import us.kochlabs.tools.life360.circles.Circles;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Life360 Client class
 */
public class Life360Client {

    static final String AUTH_CONSTANT = "cFJFcXVnYWJSZXRyZTRFc3RldGhlcnVmcmVQdW1hbUV4dWNyRUh1YzptM2ZydXBSZXRSZXN3ZXJFQ2hBUHJFOTZxYWtFZHI0Vg==";
    static final String API_BASE_URL = "https://api.life360.com/v3/";
    static final String TOKEN_PATH = "oauth2/token.json";
    static final String CIRCLES_PATH = "circles.json";
    static final String CIRCLE_PATH = "circles/";

    private String username;

    private String password;

    private String bearerToken;

    private HttpClient httpClient;

    private ObjectMapper objectMapper;

    /**
     * 
     * @param username Life360 username
     * @param password Life 360 password
     */
    public Life360Client(String username, String password) {

        this.username = username;
        this.password = password;
        this.bearerToken = null;
        this.httpClient = HttpClientBuilder.create().build();
        this.objectMapper = new ObjectMapper();

    }

    /**
     * Authenticates to Life360's API and obtains the bearer token for all future API calls.
     * 
     * @return The result of the logon process
     */
    public boolean authenticate() {

        String uri = API_BASE_URL + TOKEN_PATH;
        HttpPost httpPost = new HttpPost(uri);

        String authorization = "Basic " + AUTH_CONSTANT;
        httpPost.addHeader("Authorization",authorization);

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("grant_type", "password"));
        postParameters.add(new BasicNameValuePair("username", username));
        postParameters.add(new BasicNameValuePair("password", password));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");

            if (response.getStatusLine().getStatusCode() == 200) {

                Bearer bearer = objectMapper.readValue(responseString, Bearer.class);
                this.bearerToken = "Bearer " + bearer.access_token;

                return true;
            }

        }
        catch (ClientProtocolException cpe) {

            System.out.println(cpe.getMessage());

        }
        catch (ParseException pe) {

            System.out.println(pe.getMessage());

        }
        catch (JsonProcessingException jpe) {

            System.out.println(jpe.getMessage());

        }
        catch (IOException ioe) {

            System.out.println(ioe.getMessage());
        }

        return false;

    }

    /**
     * Performs the HTTP request with given URI, constructs headers for Life360 authentication
     * 
     * @param uri URI to send the the request to
     * @return JSON respons string from the request
     */
    public String apiHttpGet(String uri) {

        try {

            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader("Authorization", this.bearerToken);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200) {

                String responseString = EntityUtils.toString(responseEntity, "UTF-8");
                return responseString;

            }
            else {

                return null;

            }

        }
        catch (ClientProtocolException cpe) {

            System.out.println(cpe.getMessage());

        }
        catch (IOException ioe) {

            System.out.println(ioe.getMessage());
        }

        return null;

    }

    /**
     * Get all Life360 Circles for the account
     * 
     * @return All circles the subscription is a member/owner of
     */
    public Circles getAllCircles() {

        String uri = API_BASE_URL + CIRCLES_PATH;
        String responseString = apiHttpGet(uri);
        Circles circles = null;

        if (responseString != null) {

            //System.out.println(responseString);

            try {
                circles = objectMapper.readValue(responseString, Circles.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return circles;

    }

    /**
     * Retrieve a Life360 from the API
     * 
     * @param circleId
     * @return Requested Life360 circle
     */
    public Circle getCircleById(String circleId) {

        String uri = API_BASE_URL + CIRCLE_PATH + circleId;
        String responseString = apiHttpGet(uri);
        Circle circle = null;

        if (responseString != null) {

            //System.out.println(responseString);

            try {

                circle = objectMapper.readValue(responseString, Circle.class);

            } catch (Exception e) {

                System.out.println(e.getMessage());

            }
        }

        return circle;

    }
}
