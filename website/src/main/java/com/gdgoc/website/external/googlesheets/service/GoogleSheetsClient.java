package com.gdgoc.website.external.googlesheets.service;

import com.gdgoc.website.external.googlesheets.config.GoogleSheetsProperties;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleSheetsClient {


    private final Sheets sheets;
    private final GoogleSheetsProperties properties;

    public void appendTeamMemberApplication(List<Object> row) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(List.of(row));

        sheets.spreadsheets()
                .values()
                .append(
                        properties.spreadsheetId(),
                        properties.teamMemberRange(),
                        body
                )
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

}
