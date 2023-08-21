package com.nexters.phochak.docs;

import com.nexters.phochak.common.exception.ResCode;
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

        builder.append("|===\n\n");


        try (FileOutputStream fos = new FileOutputStream("src/docs/asciidoc/rescode.adoc")) {
            fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
