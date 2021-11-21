### JSP 사용준비

* pom.xml에 JSP, JSTL 추가(https://github.com/jhs512/at 에서 복붙함)
  * JSTL : JSP 안에서 if문 for문 같은 것들 편하게 쓸 수 있게 해줌

* application.yml에 설정

* Emmet 설치
  * Help -> install new software -> add 에서 위에 Emmet, 밑에 주소 적어주고 add 클릭
    * http://emmet.io/eclipse/updates
* window -> preferences 에 emmet 검색해서 extensions 부분 마지막에 ,jsp,jspf 추가
* cdnjs.com 들어가서 tailwind 준비
  * css 편하게 사용하게 해줌

* jsp 코드 작성

### 공통 상단,하단 따로 만들기

### 공통 CSS 파일 생성

### 로그인 폼 변경

* 구글에 tailwind login form 검색해서 맨위에 있는거 들어가서 Full screen Preview 클릭해서 Ctrl + U 누름
  * 전체 소스 코드를 보고 고치려고
* 필요한 것들 복사해서 사용

### 반응형으로 구현

* 모바일 퍼스트로 먼저 구현해야 함
* 인터넷창에서 F12 클릭해서 위쪽 모바일 모양 클릭 후, 상단의 폰을 iphone 5로 설정함
* 깃에서 복사해서 head.jspf에 모바일 반응형부분 코드 추가
* login.jsp 코드 수정