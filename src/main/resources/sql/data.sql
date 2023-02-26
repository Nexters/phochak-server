DROP PROCEDURE IF EXISTS loopInsert;

CREATE PROCEDURE loopInsert()
    BEGIN
        DECLARE i INT DEFAULT 1;
        DECLARE j INT DEFAULT 1;

        WHILE i <= 100 DO

                INSERT INTO post (created_at, updated_at, post_category, view, shorts_id, user_id)
                VALUES (NOW(), NOW(),
                       (CASE WHEN (mod(i, 3) = 0) THEN 'TOUR'
                           WHEN (mod(i, 3) = 1) THEN 'CAFE'
                            ELSE 'RESTAURANT' END),
                       FLOOR(RAND() * 1000), i, FLOOR(RAND() * 30))
                     ,(NOW(), NOW(),
                       (CASE WHEN (mod(i, 3) = 1) THEN 'TOUR'
                             WHEN (mod(i, 3) = 2) THEN 'CAFE'
                             ELSE 'RESTAURANT' END),
                       FLOOR(RAND() * 1000), i, FLOOR(RAND() * 40)) ;

                INSERT INTO shorts (shorts_state_enum, shorts_url, thumbnail_url, upload_key)
                VALUES ((CASE WHEN (FLOOR(RAND() * 10)) < 9.5 THEN 'OK' ELSE 'FAIL' END),
                        concat('shorts_url', 2*i), concat('thumbnail_url', 2*i), concat('upload_key', 2*i)),
                       ((CASE WHEN (FLOOR(RAND() * 10)) < 9.5 THEN 'OK' ELSE 'FAIL' END),
                        concat('shorts_url', 2*i+1), concat('thumbnail_url', 2*i+1), concat('upload_key', 2*i+1));

                INSERT INTO hashtag (tag, post_id)
                VALUES (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200)),
                       (concat('해시태그', i), FLOOR(RAND() * 200));

                    IF mod(i, 10) IN (0, 1, 2, 3) THEN
                    INSERT INTO user (created_at, updated_at, nickname, profile_img_url, provider, provider_id)
                    VALUES (NOW(), NOW(), concat('여행자', i), concat('profile', i), 'KAKAO', concat('pid', i));
                end if;

                SET i = i + 1;
            END WHILE;

        WHILE j <= 100 DO
            INSERT INTO likes (created_at, updated_at, post_id, user_id)
            VALUES (NOW(), NOW(), FLOOR(RAND() * 200), FLOOR(RAND() * 40)),
                   (NOW(), NOW(), FLOOR(RAND() * 200), FLOOR(RAND() * 40));

            end while;
END;

CALL loopInsert;
