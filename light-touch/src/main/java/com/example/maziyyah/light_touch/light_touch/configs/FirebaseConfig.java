package com.example.maziyyah.light_touch.light_touch.configs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

@Configuration
public class FirebaseConfig {

    @Value("classpath:/private-key.json")
    private Resource privateKey;

    @Bean 
    public FirebaseApp firebaseApp() throws IOException {

        // check if FirebaseApp is already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream credentials = new ByteArrayInputStream(privateKey.getContentAsByteArray());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                                                            .setCredentials(GoogleCredentials.fromStream(credentials))
                                                            .build();
            return FirebaseApp.initializeApp(firebaseOptions);
        } else {
            return FirebaseApp.getInstance();
        }
        
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp foFirebaseApp) {
        return FirebaseAuth.getInstance(foFirebaseApp);
    }
    
}
