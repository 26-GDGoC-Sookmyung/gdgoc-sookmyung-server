package com.gdgoc.website.external.googlesheets.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleSheetsClient {

    @Value("${google.sheets.spreadsheet-id}")
    private String spreadsheetId;

    private final Sheets sheets;

    private void upsert(String sheetName, Long applicationId, List<Object> row) throws IOException {
        // A열의 application ID만 조회
        ValueRange ids = sheets.spreadsheets()
                .values()
                .get(spreadsheetId, sheetName + "!A:A")
                .setValueRenderOption("UNFORMATTED_VALUE")
                .execute();

        List<List<Object>> values = ids.getValues();
        int existingRow = findApplicationRow(values, applicationId);

        int targetRow = existingRow > 0
                ? existingRow  // 동일한 applicationId가 시트에 있으면 업데이트
                : findNextRow(values);

        ValueRange body = new ValueRange()
                .setValues(List.of(row));

        sheets.spreadsheets()
                .values()
                .update(
                        spreadsheetId,
                        sheetName + "!A" + targetRow,
                        body
                )
                .setValueInputOption("USER_ENTERED")
                .execute();

        log.info(
                "Google Sheets upsert 완료 - sheetName: {}, applicationId: {}, targetRow: {}",
                sheetName,
                applicationId,
                targetRow
        );
    }

    /** A열 데이터에서 application ID가 저장된 행 번호 조회 (없을 경우 -1) */
    private int findApplicationRow(List<List<Object>> values, Long applicationId) {
        if (values == null) {
            return -1;
        }

        for (int index = 0; index < values.size(); index++) {
            List<Object> sheetRow = values.get(index);

            if (sheetRow == null || sheetRow.isEmpty()) {
                continue;
            }

            if (isSameApplicationId(sheetRow.get(0), applicationId)) {
                return index + 1;
            }
        }

        return -1;
    }

    /** Google Sheets에서 읽은 셀 값과 application ID가 같은지 비교 */
    private boolean isSameApplicationId(Object value, Long applicationId) {
        if (value == null) {
            return false;
        }

        if (value instanceof Number number) {
            return number.longValue() == applicationId;
        }

        try {
            return Long.parseLong(value.toString().trim()) == applicationId;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** 다음에 기록할 행 번호를 계산 */
    private int findNextRow(List<List<Object>> values) {
        // 1행은 헤더이므로 데이터는 최소 2행부터 기록한다.
        return values == null
                ? 2
                : Math.max(2, values.size() + 1);
    }

    public void appendMemberApplication(Long applicationId, List<Object> row) throws IOException {
        upsert("Member", applicationId, row);
    }

    public void appendTeamMemberApplication(Long applicationId, List<Object> row) throws IOException {
        upsert("TeamMember", applicationId, row);
    }

}
