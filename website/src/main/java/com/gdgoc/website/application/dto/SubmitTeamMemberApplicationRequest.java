package com.gdgoc.website.application.dto;

import com.gdgoc.website.application.entity.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

public record SubmitTeamMemberApplicationRequest(

        @NotBlank
        @Pattern(
                regexp = "^\\d{7}$",
                message = "학번은 숫자 7자리여야 합니다."
        )
        String studentNumber,

        @NotBlank
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$",
                message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
        @Schema(example = "010-1234-5678")
        String phoneNumber,

        @NotBlank
        String major,

        @NotNull
        @Min(1)
        Integer completedSemester,

        @NotNull
        Boolean onLeave,

        @NotBlank
        @Size(min = 300, message = "자기소개를 300자 이상 작성해주세요.")
        @Schema(example = "이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.")
        String introduction,

        @NotBlank
        @Size(min = 300, message = "지원동기를 300자 이상 작성해주세요.")
        @Schema(example = "이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.")
        String motivation,

        @NotBlank
        @Size(min = 300, message = "숙명여대 개발자 커뮤니티에 대한 생각을 300자 이상 작성해주세요.")
        @Schema(example = "이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.")
        String communityPerspective,

        @NotBlank
        @Size(min = 300, message = "GDGoC Sookmyung 활동을 통해 얻어가고자 하는 것을 300자 이상 작성해주세요.")
        @Schema(example = "이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.")
        String goal,

        @NotBlank
        String portfolioUrl,

        @NotBlank
        @Size(min = 300, message = "본인이 진행한 프로젝트에 대해 300자 이상 작성해주세요.")
        @Schema(example = "이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.")
        String project,

        @NotBlank
        @Size(min = 300, message = "프로젝트나 스터디를 이끌거나 개발 커뮤니티에 참가(운영)했던 경험을 300자 이상 작성해주세요.")
        @Schema(example = "이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.이 문장은 API 테스트 및 Swagger 예시를 위한 더미 데이터입니다. 실제 지원 내용과는 관련이 없으며, 최소 글자 수 검증과 요청 예시를 확인하기 위해 작성되었습니다. 동일한 형식의 문장을 자연스럽게 이어 작성하여 충분한 길이를 확보하였습니다. 이를 통해 요청 데이터가 정상적으로 전달되는지 확인할 수 있습니다.")
        String leadershipExperience,

        @NotBlank
        String techStack,

        @NotBlank
        String studyField,

        @NotEmpty
        @Schema(example = "[\"STUDY_LEAD\", \"MANAGEMENT\", \"PROJECT_MENTOR\"]")
        Set<TeamMemberRole> roles,

        String eventIdea,

        @NotEmpty
        @Schema(example = "[29, 30]")
        List<Long> interviewTimeSlotIds

) {
}
