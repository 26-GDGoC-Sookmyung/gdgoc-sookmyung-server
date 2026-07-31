package com.gdgoc.website.external.googlesheets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Google Sheets 관련 설정 값을 바인딩하는 설정 클래스
 */
@ConfigurationProperties(prefix = "google.sheets")
public record GoogleSheetsProperties(

        String spreadsheetId,

        String memberRange,

        String teamMemberRange

) {
}
