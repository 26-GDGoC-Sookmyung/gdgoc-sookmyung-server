package com.gdgoc.website.auth.service;

import com.gdgoc.website.global.config.MailProperties;
import com.gdgoc.website.global.exception.BaseException;
import com.gdgoc.website.global.exception.ErrorCode;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final Resend resend;
    private final MailProperties mailProperties;

    /**
     * [외부 공개 메서드] 회원가입/인증을 위한 6자리 코드 메일 발송
     * HTML 템플릿 생성과 제목 설정을 책임집니다.
     *
     * @param toEmail 수신자 이메일
     * @param verificationCode 6자리 인증코드
     */
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "[GDGoC Sookmyung] 이메일 인증코드 안내";

        String htmlContent = buildVerificationHtml(verificationCode);
        sendEmail(toEmail, subject, htmlContent);
    }

    private String buildVerificationHtml(String verificationCode) {
        return """
                <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e0e0e0; border-radius: 8px;'>
                    <h2 style='color: #4285f4; margin-bottom: 16px;'>GDGoC Sookmyung 이메일 인증</h2>
                    <p style='font-size: 15px; color: #333333; line-height: 1.6;'>
                        안녕하세요, GDGoC Sookmyung 입니다.<br>
                        아래의 6자리 인증코드를 웹페이지 입력창에 입력해 주세요.
                    </p>
                    <div style='margin: 24px 0; padding: 16px; background-color: #f4f5f7; text-align: center; border-radius: 6px;'>
                        <span style='font-size: 28px; font-weight: bold; letter-spacing: 8px; color: #1a73e8;'>
                            %s
                        </span>
                    </div>
                    <p style='font-size: 13px; color: #777777;'>
                        * 본 인증코드는 3분 동안만 유효합니다.<br>
                        * 본인이 요청하지 않은 경우 이 메일을 무시해 주세요.
                    </p>
                </div>
                """.formatted(verificationCode);
    }

    public void sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            CreateEmailOptions sendEmailOptions = CreateEmailOptions.builder()
                .from(mailProperties.getFormattedFrom())
                .to(toEmail)
                .subject(subject)
                .html(htmlContent)
                .build();

            CreateEmailResponse response = resend.emails().send(sendEmailOptions);
            log.info("Resend 메일 발송 성공! - 수신자: {}, Resend ID: {}", toEmail, response.getId());

        } catch (ResendException e) {
            log.error("Resend API 이메일 발송 실패 - 수신자: {}, 에러: {}", toEmail, e.getMessage(), e);
            throw new BaseException(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "이메일 발송 서버 통신에 실패했습니다. 잠시 후 다시 시도해주세요."
            );
        } catch (Exception e) {
            log.error("이메일 발송 중 예기치 못한 에러 발생 - 수신자: {}, 에러: {}", toEmail, e.getMessage(), e);
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, "이메일 발송 중 오류가 발생했습니다.");
        }
    }
}