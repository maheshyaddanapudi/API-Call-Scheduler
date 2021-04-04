package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.downstream.rest.oauth2;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.downstream.token.AccessTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DownstreamOAuthLoginService {

    private Logger LOG = LoggerFactory.getLogger(DownstreamOAuthLoginService.class.getSimpleName());

    @Autowired
    private RestTemplate restTemplate;

    public AccessTokenDTO performOAuth2Authentication(String clientId, String clientSecret, String username, String password, String tokenUrl){
        LOG.info("Performing Downstream OAuth2 Authentication ....");
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
            headers.add("Accept", MediaType.APPLICATION_JSON.toString()); //Optional in case server sends back JSON data

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
            requestBody.add("client_id", clientId); //"api_call_scheduler_user_client");
            requestBody.add("client_secret", clientSecret); //"7d804065-4496-4632-8bef-089bd7051a26");
            requestBody.add("grant_type", "password");
            requestBody.add("username", username); //"maheshy");
            requestBody.add("password", password); //"Password");

            HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

            ResponseEntity<AccessTokenDTO> response =
                    restTemplate.exchange(tokenUrl, HttpMethod.POST, formEntity, AccessTokenDTO.class);

            LOG.info("Obtained Access Token : Authentication Successful ....");

            return response.getBody();
        }
        catch (
                HttpClientErrorException hcee)
        {
            LOG.error("Error Obtaining Access Token : Authentication Failed .... {}", hcee.getResponseBodyAsString());

            AccessTokenDTO response = new AccessTokenDTO();

            response.setResponseErrorMsg(hcee.getResponseBodyAsString());
            response.setResponseStatusCode(hcee.getRawStatusCode());

            return  response;
        }
        catch (Exception e)
        {
            LOG.error("Error Obtaining Access Token : Authentication Failed .... {}", e.getMessage());

            AccessTokenDTO response = new AccessTokenDTO();

            response.setResponseErrorMsg(e.getMessage());
            response.setResponseStatusCode(500);

            return  response;
        }
    }
}
