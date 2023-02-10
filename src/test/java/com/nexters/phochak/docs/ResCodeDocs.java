package com.nexters.phochak.docs;

import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.exception.ResCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ResCodeDocs extends RestDocs {

    @Test
    @DisplayName("ResCode 출력")
    void resCode() throws Exception {

        StringBuilder builder = new StringBuilder();

        builder.append("== 서비스 응답 코드\n\n");
        builder.append("서비스 응답 코드입니다.\n\n");
        builder.append("응답 메시지는 디버깅 편의를 위해 조금씩 변경될 수 있습니다.\n\n");

        builder.append("|===\n");
        builder.append("|코드(resCode) |메시지(resMessage) \n\n");

        Arrays.stream(ResCode.values())
                .forEach(e -> {
                    builder.append(String.format("|%s |%s \n", e.getCode(), e.getMessage()));
                });

        builder.append("|===\n");

        builder.append("=== 서비스 전체에서 발생 가능한 공통 코드\n");
        builder.append("\"P000\", \"정상 처리\"\n\n");
        builder.append("\"P100\", \"서버 에러 발생 (HTTP 200번 응답. 서버에서)\"\n\n");
        builder.append("\"P200\", \"요청 값이 올바르지 않습니다\" - param, body 값 누락 등\n\n");
        builder.append("\"P201\", \"토큰을 찾을 수 없습니다(로그인 되지 않은 사용자입니다)\"\n\n");
        builder.append("\"P202\", \"올바르지 않은 토큰입니다\"\n\n");
        builder.append("\"P203\", \"만료된 토큰입니다\" - 재발급 필요\n\n");

        try (FileOutputStream fos = new FileOutputStream("src/docs/asciidoc/rescode.adoc")) {
            fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
