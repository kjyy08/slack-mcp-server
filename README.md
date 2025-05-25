# Slack MCP 서버

## 프로젝트 개요

**Spring AI**를 활용하여 슬랙 메시지 전송, 채널 내역 조회 등의 도구를 지원합니다.

---

## 주요 도구

### 구현된 도구

#### 1. 메시지 전송 (postSlackMessage)

* Slack 채널에 메시지를 전송합니다. 마크다운 형식을 일부 지원합니다.

#### 2. 채널 히스토리 조회 (slackChannelHistory)

* Slack 채널의 메시지 내역을 조회합니다. 페이징 처리를 위한 커서와 조회할 메시지 수를 지정할 수 있습니다.

#### 3. 파일 업로드 (uploadFileToSlack)

* 절대 경로로 지정된 파일을 Slack 채널에 업로드합니다.

#### 4. Base64 파일 업로드 (uploadFileToSlackByBase64)

* Base64로 인코딩된 파일 데이터를 Slack 채널에 업로드합니다.
    * 단, 파일 크기가 큰 경우 토큰 사용량이 증가할 수 있어 주의가 필요합니다.
      > 절대 경로 지정 방식을 권장합니다.

#### 5. 예약 메시지 (slackScheduleMessage)

* 특정 시간에 메시지를 전송하도록 예약하는 기능입니다.

### 구현 예정 도구

#### 1. 리액션 추가/삭제 (addReaction/removeReaction)

* 메시지에 이모지 리액션을 추가하거나 삭제하는 기능입니다.

---

## 설정 방법

### 빌드 및 실행

1. 프로젝트 빌드

    ```bash
    # 프로젝트 빌드
    ./gradlew build
    ```

2. **Claude Desktop**으로 실행  
   `claude_desktop_config.json`을 다음과 같이 작성합니다.
   ```json
   {
     "mcpServers": {
       "slack-mcp": {
         "command": "java",
         "args": [
           "-Dfile.encoding\u003dUTF-8",
           "-jar",
           "JAR_PATH"
         ],
         "env": {
           "SLACK_BOT_TOKEN": "xoxb-your-bot-token",
           "SLACK_CHANNEL_ID": "your-channel-id"
         }
       }
     }
   }
   ```
    - `JAR_PATH`: 빌드한 jar 파일의 경로
    - `SLACK_BOT_TOKEN`: Slack Bot 토큰
    - `SLACK_CHANNEL_ID`: 메시지를 보낼 Slack 채널 ID

---

## 프로젝트 구조

```
src/main/java/dev/luigi/slack/mcp/server/
├── SlackMcpServerApplication.java  # 메인 애플리케이션 클래스
├── dto/                           # 데이터 전송 객체
│   ├── common/                    # 공통 DTO
│   ├── request/                   # 요청 DTO
│   └── response/                  # 응답 DTO
└── service/                       # 서비스 클래스
    ├── SlackService.java          # Slack 서비스 인터페이스
    ├── SlackServiceImpl.java      # Slack 서비스 구현체
    ├── file/                      # 파일 관련 서비스
    ├── history/                   # 채널 히스토리 관련 서비스
    ├── message/                   # 메시지 관련 서비스
    ├── reaction/                  # 리액션 관련 서비스
    └── schedule/                  # 예약 메시지 관련 서비스
```

