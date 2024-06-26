![image](https://github.com/hyeji111544/zeropie-back/assets/154953972/325a935b-47fc-4526-8f2f-e8e85a46208b)<div align="center">
  <img src="https://github.com/green-lotte2/community-site-back-team1/assets/154953972/801ce44e-8d14-4346-99d7-943c6e2ff55c" alt="zeroPie2" width="400"/>
</div>


## 1. 프로젝트 주제
- **제로파이란?** : 협업과 커뮤니케이션의 어려움을 `제로`로 만드는 솔루션
- **목표** : 협업의 효율성을 극대화하기 위해 양방향 통신 기술을 활용한 그룹웨어 도입

## 2. 프로젝트 기간
- 24.05.20 ~ 24.06.21

## 3. 사용 기술
- **프로그래밍 언어**: Java, JavaScript
- **프레임워크**: Spring Boot, React
- **ORM**: Spring Data JPA
- **보안**: Spring Security, OAuth2 Client
- **데이터베이스**: MySQL
- **배포**: AWS(EC2, CodeDeploy, S3), GitHub Actions 

### 4. 주요기능
- 채팅, 캘린더, 칸반보드, 페이지, 게시판

### 5. 팀 구성(4인)
- 윤혜지 (팀장) : 관리자, 프로젝트 기능 구현 및 개발 일정 관리
- 박임재 : CSS 디자인, 메인 페이지, 페이지 기능 구현
- 오아람 : 회원, 고객센터, 채팅 구현
- 김광은 : 게시판, 캘린더 구현

### 6. 시연 영상

[![시연영상](http://img.youtube.com/vi/z9AovY82XFQ/0.jpg)](https://youtu.be/z9AovY82XFQ?si=7DkK2PQnGHTBG7v2)



## 프로젝트 진행하며
<details>
<summary>상세보기</summary>

### 1. 프론트와 백 서버 각각 배포하기.
백 서버는 AWS 구축 후 배포 하였으나 React 프로젝트 배포를 위해 공부한 내용입니다.
[이동](https://rune-toothpaste-826.notion.site/git-page-0ff3de28d5ba42b8bbf0d63bf7de0909?pvs=4)

netlify 와 git page 로 각각 배포 완료 하였으나
AWS 는 `http` , Netlify 는 `https` 를 사용하는 문제가 있어 해결방법을 찾던 중
프론트 역시 AWS 에서 배포하여 `http` 로 맞추기로 결정하였습니다.

### 2. React 배포 후 새로고침 시 404 에러 발생
배포가 완료된 뒤 프론트와 백을 연결하여 배포 테스트를 하던 중 페이지 뒤로가기, 새로고침 시 404 에러가 발생하는 것을
알게 되었습니다. 이는 React 의 SPA 의 특성때문이었는데 해결하는 과정은 다음과 같습니다. 
[이동](https://rune-toothpaste-826.notion.site/React-404-a94a27d8961e48df92a5ab75e138dfcc?pvs=4)

### 3. 칸반보드 만들기
drag and drop 라이브러리를 활용하여 깃허브 프로젝트 처럼 칸반보드를 구현해야 했습니다.
빠른 개발을 위해 [kanban-board](https://github.com/aman162000/kanban-board?tab=readme-ov-file) 의 코드를 활용하여 개발 했습니다.

이를 db에 저장하기 위해 공부한 내용은 다음과 같습니다.
[이동](https://rune-toothpaste-826.notion.site/56de294d670349239aba6e8534173790?pvs=4)


</details>

## 프로젝트 버전 업데이트 내역
<details>
<summary>상세보기</summary>
  
### 0.0.1-SNAPSHOT / 24.05.24
- 게시판 글 목록, 상세 뷰, 글쓰기, 글 수정 작업
- 사원 회원가입 기능 작업
- 프로젝트 페이지 CSS 작업 및 웹 배포
  
### 0.0.2-SNAPSHOT / 24.05.27
- 관리자 페이지 유저 목록 작업
- 게시물 검색기능 작업
- 사원 회원가입 및 로그인 기능구현

### 0.0.3-SNAPSHOT / 24.05.28
- 관리자 페이지 유저 검색 기능구현
- 관리자 페이지 게시물 카테고리 수정 및 조회 기능 구현
- 글 뷰 페이지 기능 구현
- 회원가입 약관, 아이디/비밀번호 찾기 기능구현

### 0.0.4-SNAPSHOT / 24.05.29
- 관리자 페이지 유저 정보 수정
- 관리자 페이지 게시판 삭제 기능
- 글 작성, 삭제 기능
- 고객센터 리스트 출력 및 검색기능

### 0.0.5-SNAPSHOT / 24.05.30
- 관리자 페이지 글 조회, 체크박스 이용한 글 삭제기능
- 게시글 수정 기능
- 고객센터 검색 구현 완료 및 글 쓰기 구현

### 0.0.6-SNAPSHOT / 24.05.31
- 사이드바 게시물 카테고리 db 와 연결
- 회원가입 메일 버튼 스피너 기능 추가
- QnA 게시판 제목 출력 및 기본 정렬 변경

### 0.0.7-SNAPSHOT / 24.06.03
- 유저 리스트 엑셀 다운로드 기능
- 조직도 아이콘 동적 출력
- 게시글 파일첨부 기능 구현중
- CS 내용 출력 및 페이지네이션

### 0.0.8-SNAPSHOT / 24.06.04
- 관리자 페이지 유저목록 출력 수 조절 기능
- CS 뷰 페이지 기능 구현 완료
- 게시판 정렬 타입 기능 구현

### 0.0.9-SNAPSHOT / 24.06.05
- 사이드바 카테고리 캐싱 처리
- 채팅 백앤드 기능 작업
- 그룹 플랜 내용 출력

### 0.1.0-SNAPSHOT / 24.06.06
- 그룹 플랜 DB 구현
- 게시판 이미지 업로드 및 출력
- 캘린더 view ui 적용
- 관리자 config 출력 수정

### 0.1.1-SNAPSHOT / 24.06.10
- 채팅 기능 백엔드 구현중
- 캘린더 뷰 토스트 ui 로 변경
- 캘린더 생성 기능 구현(협업자 검색 기능)
- 게시글 파일 업로드, 다운로드, 삭제 기능 구현

### 0.1.2-SNAPSHOT / 24.06.11
- 다중 채팅 기능 (그룹채팅 가능)
- 캘린더 생성, 협업자 등록 기능구현
- 리액트 인증, 인가설정 완료
- 게시물 파일 관련 기능 구현 완료
- 게시물 댓글 작성, 삭제 기능 구현
- 프로젝트 협업자 등록 모달 수정

### 0.1.3-SNAPSHOT / 24.06.12
- 회원가입 시 캘린더 할당
- 캘린더 생성 / 협업자 등록
- 채팅 입장 시 입장 멘트 출력
- 칸반보드 협업자 등록 기능
- 페이지 웹 소켓 연결(동시편집 가능)

### 0.1.4-SNAPSHOT / 24.06.13
- 채팅 입장 시 입장 멘트 출력 마무리
- 협업자 캘린더 조회 및 이벤트 삭제, 추가
- 페이지 db 저장, 불러오기 
- 칸반보드 db 조회 불러오기

### 0.1.5-SNAPSHOT / 24.06.14
- 채팅 db 저장
- 캘린더 수정 기능 구현
- 칸반 보드 출력 index 추가

### 0.1.6-SNAPSHOT / 24.06.17

- 게시판 글, 댓글 수정 시 작성자/로그인 정보 체크
- 댓글 수정/삭제 기능
- 캘린더 메인페이지 랜더링
- 게시판 글 수정시 이미지 인코딩 및 썸네일 처리
- 문서 페이지 공동 작업자 초대 기능, 페이지 삭제 기능 구현
- 메인 페이지 생일, 회원, 공지사항 정보 출력
- CS 관리자만 답변 가능하게 변경
- CS 비밀글 체크 기능/ 보기 구현
- 채팅 초대 및 플랜별 초대인원 제한 기능
- 프로젝트 협업자 초대 및, 프로젝트 나가기
- 관리자 config 직책 설정 저장/변경 기능

### 0.1.7-SNAPSHOT / 24.06.18

- 게시판 글 삭제시 업로드 이미지 삭제기능.
- 캘린더 초대한 사람 방 나가기 / 캘린더 생성자 방 삭제가
- OAuth2 카카오 로그인 기능
- 게시판 조회 인가설정 

### 0.1.8-SNAPSHOT / 24.06.19

- 카카오 배포환경 로그인 기능 
- 관리자 페이지 회원/고객문의 현황 출력
- index TodoList 기능 구현
- 조직도에서 채팅 하기 기능 구현

### 0.1.9-SNAPSHOT / 24.06.20
- 회원가입 인증코드 스타일 수정
- 그룹 플랜 수정
### 0.2.0-SNAPSHOT / 24.06.21
- 회원 검색 기능 수정

### 1.0.0-RELEASE / 24.06.21
- 최종 버전 릴리즈

</details>
