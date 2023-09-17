package com.nexters.phochak.post.application.port.out;

import java.net.URL;

public interface GeneratePresignedUrlPort {
    URL generate(final String uploadKey, String objectName);
}
