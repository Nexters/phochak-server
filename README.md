![스크린샷 2023-05-24 오전 12 51 01](https://github.com/Nexters/phochak-server/assets/76773202/0f366955-87fe-4e15-9392-e3fa9af188ec)
![IMG_2281](https://github.com/Nexters/phochak-server/assets/76773202/70ca8b0f-d364-4dfc-87f5-013651d52e2f)

<div align="center">

# "포착"

**✈️ 포착은 여행에서의 소중했던 추억을 공유하는 쇼츠 컨텐츠 서비스입니다 ✈️**

인스타 릴스, 유튜브 쇼츠, 틱톡.. 딱히 보고 싶지 않은 영상들에 지치지 않으셨나요?
<br/><br/><br/>

 
🏖️ 그렇다면 혹시 여행과 관련된 주제만 골라서 보고싶지 않으신가요? 🏖️

🤩 특별했던 장면을 공유하며 다음을 기약하고, 다른 사람들이 포착한 순간을 시청하며 나만의 여행을 꿈꿔보세요 🤩
  
</div>

<br/><br/>

**🟢 운영중 - Play Store / App Store**

<a href="https://github.com/Nexters/phochak-android"> 🤖 Android Github </a>

<a href="https://github.com/Nexters/phochak-iOS"> 🍎 iOS Github </a>
<a href="http://phochak-lb-813451034.ap-northeast-2.elb.amazonaws.com/docs/index.html"> 📝 Rest API Sheet </a>

## Back-end Members

|[김세영](https://github.com/seyoung755)|[천진우](https://github.com/JinuCheon)|
|:-:|:-:|
|<img src="https://avatars.githubusercontent.com/u/54302155?v=4" alt="김세영" width="150" height="150">|<img src="https://avatars.githubusercontent.com/u/76773202?v=4" alt="천진우" width="150" height="150">||


## Infrastructure

<img src="https://user-images.githubusercontent.com/76773202/231319152-7b8d0164-51b0-4134-9fdc-c6d319c2845f.png" width="100%">


## Branch Rule

![스크린샷 2023-09-20 오후 1 32 12](https://github.com/Nexters/phochak-server/assets/76773202/1ab7ba7a-b44e-4f40-abf7-1e2d1bdc843e)

- Release: 운영 서버에 배포
- Develop: 개발 서버에 배포
- Feature: 기능 개발

## Co-Work Flow
1. Issue 생성
2. feature 개발
3. Pull Request
4. 리뷰
5. Squash Merge 

## Test Rule

<a href="https://github.com/Nexters/phochak-server/blob/develop/src/test/java/com/nexters/phochak/post/PostControllerTest.java"> 🔗 시나리오 테스트 예시 </a>


[9.1 수정]
기존 테스트들은 사실상 재사용 불가능하며 가독성이 좋지 않은 테스트라서, 모든 테스트를 다음 규칙으로 재작성합니다.


단, 리팩토링 이후에 기존 테스트도 잘 돌아가야하며, 최종 확인 이후 PR 직전에 삭제합니다.
- 시나리오 기반 테스트로 중복 최소화하고 가독성을 가져가기
- Rest Docs Test, JUnit(AssertJ) 활용
- 다음 테스트코드 작성 기준을 준수하기
    - Presentation Layer: Rest Docs, Integration Test, Only Happy Test
    - Application Layer: Unit Test, Happy Test, Fail Test
    - Domain Layer: Unit Test, Happy Test, Fail Test
    - Query: 직접 정의한 쿼리 Happy Test

## Documentation Rule
<a href="http://phochak-lb-813451034.ap-northeast-2.elb.amazonaws.com/docs/index.html"> 📝 Rest API Sheet </a>

Rest Docs 를 활용합니다.

컨트롤러 통합 테스트 -> ActionResult 를 DocumentGenerator.java(Util 클래스)에 추상화.
