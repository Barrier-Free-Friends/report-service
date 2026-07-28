# Report Service

Barrier Free Friends MSA의 제보 서비스입니다. 시민이 제출한 장애물/불편사항 제보를 생성·관리하고, AI 사진 검증 결과를 반영합니다.

전체 서비스 구성은 [조직 README](https://github.com/Barrier-Free-Friends)를 참고하세요.

## 기술 스택
- Java 21, Spring Boot 3.5.7 (JPA, QueryDSL)
- PostgreSQL, AWS S3 (이미지 업로드)
- Kafka (AI 검증 요청/결과 이벤트)
- OpenFeign, Resilience4j
- Eureka Client, Config Client

## 동작 방식
- 제보 생성 시 이미지를 S3에 업로드하고, `image-ai-service`에 AI 검증 요청 이벤트를 발행
- 검증 결과를 Kafka로 수신해 제보 상태(`ReportStatus`, `VerificationStatus`)에 반영
- 승인된 제보는 `point-service`의 포인트 적립 트리거로 이어짐

## 주요 구조
- `application/command` — ReportCreateService, ReportModifyService, AsyncReportService
- `application/event` — AiVerificationEventPublisher
- `infrastructure/kafka` — AiVerificationEventListener
- `presentation/controller` — ReportController, AsyncReportController

## 실행

```bash
./gradlew bootRun
```

기본 포트: `4100`

필요 환경변수: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, AWS 자격증명

## Docker

```bash
docker build -t report-service .
docker run -p 4100:4100 report-service
```
