### 데이터베이스 생성
DROP DATABASE IF EXISTS board;
CREATE DATABASE board;
USE board;

### 게시물 테이블 생성
CREATE TABLE article(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	boardId INT(10) UNSIGNED NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL,
	delDate DATETIME DEFAULT NULL,
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	title CHAR(100) NOT NULL,
	`body` TEXT NOT NULL
);

### 테스트 데이터 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
boardId = FLOOR(RAND() * 2) + 1,
memberId = 1,
title = "제목1 입니다.",
`body` = "내용1 입니다.";

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
boardId = FLOOR(RAND() * 2) + 1,
memberId = 1,
title = "제목2 입니다.",
`body` = "내용2 입니다.";

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
boardId = FLOOR(RAND() * 2) + 1,
title = "제목3 입니다.",
memberId = 1,
`body` = "내용3 입니다.";

### 회원 테이블 생성
CREATE TABLE `member`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	loginId CHAR(30) NOT NULL,
	loginPw VARCHAR(100) NOT NULL,
	`authLevel` SMALLINT(2) UNSIGNED DEFAULT 3 NOT NULL COMMENT '(3=일반,7=관리자)',
	authKey CHAR(80) NOT NULL,
	`name` CHAR(30) NOT NULL,
	`nickname` CHAR(30) NOT NULL,
	`cellphoneNo` CHAR(20) NOT NULL,
	`email` CHAR(100) NOT NULL
);

### 로그인 ID로 검색했을 때 (인덱스 종류를 UNIQUE로 설정(같은 아이디 사용불가, 로그인 아이디로 검색시 속도향상))
ALTER TABLE `member` ADD UNIQUE INDEX (`loginId`);

### 회원, 테스트 데이터 생성
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user1",
loginPw = "user1",
authLevel = 7,
authKey = 'authKey1__39285f22-3157-11ec-805d-2cf05da835df__0.9446445982387704',
`name` = "user1",
nickname = "user1",
cellphoneNo = "01012341234",
email = "jangka512@gmail.com";

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user2",
loginPw = "user2",
authKey = 'authKey1__39286ba6-3157-11ec-805d-2cf05da835df__0.2614991490370586',
`name` = "user2",
nickname = "user2",
cellphoneNo = "01012341234",
email = "jangka500@gmail.com";

### 게시판별 리스팅
CREATE TABLE board(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	`code` CHAR(20) UNIQUE NOT NULL,
	`name` CHAR(20) UNIQUE NOT NULL
);

### 공지사항 게시판 추가
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'notice',
`name` = '공지사항';

### 자유게시판 추가
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'free',
`name` = '자유게시판';

DROP TABLE genFile;
### genFile 테이블 생성
CREATE TABLE genFile(
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,	# 번호
	regDate DATETIME DEFAULT NULL,			# 작성날짜
	updateDate DATETIME DEFAULT NULL,		# 갱신날짜
	delDate DATETIME DEFAULT NULL,			# 삭제날짜
	delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,	# 삭제 상태(0:미삭제, 1:삭제) -> 이런식으로 
	relId INT(10) UNSIGNED NOT NULL,		# 관련 데이터 번호
	originFileName VARCHAR(100) NOT NULL,		#업로드 당시의 파일 이름
	fileExt CHAR(10) NOT NULL,			# 확장자
	fileSize CHAR(10) NOT NULL,			# 파일의 사이즈
	fileExtTypeCode CHAR(10) NOT NULL,		# 파일규격코드(img, video)
	fileExtType2Code CHAR(10) NOT NULL,		# 파일규격2코드(jpg, mp4)
	fileNo SMALLINT(2) UNSIGNED NOT NULL,		# 파일번호(1)
	fileDir CHAR(20) NOT NULL,			# 파일이 저장되는 폴더명
	PRIMARY KEY (id),
	KEY relId(relId, fileNo)
);

### 테스트 게시물 추가
INSERT INTO article
(regDate, updateDate, boardId, memberId, title, `body`)
SELECT NOW(), NOW(),
	FLOOR(RAND() * 2) + 1,
	FLOOR(RAND() * 2) + 1,
	CONCAT('제목_', FLOOR(RAND()*1000)+1), 
	CONCAT('내용_', FLOOR(RAND()*1000)+1)
FROM article;

SELECT COUNT(*) FROM article;	# atricle Table의 갯수 확인