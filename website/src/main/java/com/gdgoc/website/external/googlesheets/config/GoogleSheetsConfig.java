package com.gdgoc.website.external.googlesheets.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class GoogleSheetsConfig {

    private static final String APPLICATION_NAME = "gdgoc-website";
    private static final String CREDENTIALS_PATH  = "google/service-account.json";

    @Bean
    public Sheets sheets(@Value("${google.sheets.credentials-location}") Resource resource) throws IOException, GeneralSecurityException {
        GoogleCredentials credentials;

        try (InputStream inputStream = resource.getInputStream()) {
            credentials = GoogleCredentials
                    .fromStream(inputStream)
                    .createScoped(List.of(SheetsScopes.SPREADSHEETS));
        }

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        )
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
