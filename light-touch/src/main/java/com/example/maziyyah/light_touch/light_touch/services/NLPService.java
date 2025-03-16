package com.example.maziyyah.light_touch.light_touch.services;

import java.io.StringReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.maziyyah.light_touch.light_touch.models.NlpEntities;
import com.example.maziyyah.light_touch.light_touch.utils.Utils;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class NLPService {

    @Value("${nlp.api.url}")
    private String nlpApiURl;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(NLPService.class);

    public NlpEntities analyzeText(String text) {

        NlpEntities nlpEntities = new NlpEntities();
        JsonObject textJson = Utils.sendTextToNLPService(text);
        
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "application/json");

        RequestEntity<String> request = RequestEntity
                                        .post(URI.create(nlpApiURl))
                                        .body(textJson.toString());


        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            logger.info("response: {}", response);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
                JsonObject root = jsonReader.readObject();
                // parse the response and transform to the model?
                
            }
            



        } catch(HttpClientErrorException ex) {

        }

        return null;
    }
    
}
