공통 아키텍처

프로젝트 모듈화 진행

화면마다 모듈화를 진행

동일한 패키지 구조, 네이밍, 개발 아키텍처 적용 및 검토 필요성.

# 모듈명
settings.gradle에 RootProject 를 소문자로 한다.

ex) rootProject.name = "Feed" -> feed

# Compose 작성 방법

[uiState 네이밍](https://developer.android.com/topic/architecture/ui-layer#naming-conventions)
[usecase 네이밍](https://developer.android.com/topic/architecture/domain-layer#conventions)
[repository 네이밍](https://developer.android.com/topic/architecture/data-layer#naming-conventions)

com.sarang.torang.compose.[모듈명] 으로 설정하기

compose가 viewmodel을 파라미터로 가지고 있으면<br><br>

functionality + Screen or Route <br>
ex) FeedScreen or FeedRoute <br><br>


