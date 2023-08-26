# Ver 1.0.0 리팩토링 일지

### 1. 자바 및 스프링 버전 업그레이드
record, lambda 최신 문법 등의 사용에 제약이 있었습니다. 
- Java 11 -> Java 17
- Spring Boot 2.7.7 -> Spring Boot 3.1.2

### 2. 패키지 구조 변경
기존 방식들의 패키지 관리가 어려워졌습니다.
- 기존 layerd 아키텍쳐에서 최상단을 feature 로 분리
- 이후 기능별 리팩토링 시에 port-adapter 구조로 전환

### 3. Git - Flow 브랜치 전략 관리
- 배포용 브랜치인 release 브랜치를 추가했습니다.
- 기존 개발 단계에서 develop 브랜치를 향하던 CD 파이프라인을 release로 옮겼습니다.
- 이제 다음 브랜치 전략을 준수해봅시다 !
[우린 Git-flow를 사용하고 있어요](https://techblog.woowahan.com/2553/)

### 4. 기능별 코드 및 테스트 리팩토링
빠른 런칭을 위해서 구현에만 급급했어서, 이번 리팩토링을 진행하면서 지속 가능한 코드로 리팩토링합니다.

<a href="#리팩토링-룰">기능 리팩토링은 아래의 '리팩토링 룰'을 따릅니다.</a>

<a href="#테스트코드-룰">테스트 리팩토링은 아래의 '테스트코드 룰'을 따릅니다.</a>

### 기능별 리팩토링 PR Log
- [추가예정. PR 링크]

### 리팩토링 룰
1. TDD 와 함께하는 리팩토링
    - 먼저 기존 로직에 대해 성공하는 최소한의 테스트를 새로 작성.
    - 작성한 테스트를 기반으로 기존 코드의 YAGNI, KISS 검증 및 리팩토링
    - <a href="#테스트코드-룰">그 외 테스트를 작성하면서 '테스트코드 룰'을 따릅니다.</a>
2. Request DTO / Response DTO 를 java 17 record 문법 사용
3. 검증 및 예외
    - Request Record 검증(400): Bean Validation 
    - 도메인 생성 검증(500): jakarta.validation
    - 기존 서비스 커스텀 예외코드 재정리
4. 패키지 구조를 변경에 따라, 기존 public 매서드들에 대하여 package-private 등 최대한 낮은 수준의 접근제어자 고려하여 캡슐화
5. Lambda 적극 활용
6. 적극적인 매서드 extract을 통해 재사용성 및 가독성 고려
7. 커밋 단위 짧게 가져가서 빠른 롤백, 커핏을 따라가는 코드 리뷰 고려 
8. 그 외 좋은 코드를 작성하기 위한 방법들 적용


### 테스트코드 룰
기존 테스트들은 사실상 재사용 불가능하며 가독성이 좋지 않은 테스트라서, 모든 테스트를 다음 규칙으로 재작성합니다.

단, 리팩토링 이후에 기존 테스트도 잘 돌아가야하며, 최종 확인 이후 PR 직전에 삭제합니다.
   - 시나리오 기반 테스트로 중복 최소화하고 가독성을 가져가기
   - Rest Docs Test, JUnit(AssertJ) 활용
   - 다음 테스트코드 작성 기준을 준수하기
     - Presentation Layer: Rest Docs, Integration Test, Only Happy Test
     - Application Layer: Unit Test, Happy Test, Fail Test
     - Domain Layer: Unit Test, Happy Test, Fail Test
     - Query: 직접 정의한 쿼리 Happy Test

### Reference
[백명석님 유튜브](https://www.youtube.com/@codetemplate/videos)

[]https://www.youtube.com/@ejoongseok/videos


