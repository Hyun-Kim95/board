### genFile

* generate file(만든 파일)
  * java에 이미 file이란 이름이 내장되어 있어서 genFile로 이름 지음
* 내용
  * regDate : 등록 날짜
  * delDate : 삭제된 날짜
  * delStatus : 삭제 여부
    * 바로 삭제하지않고 남겨놓기 위해서
  * relId : 게시물의 Id
  * originFileName : 업로드 당시 파일 명
    * 주키(id) 로 바꿔서 보여줌(중복제거용)
  * fileExtType2Code 
    * 규격화된 확장자
    * fileExt 는 실제 확장자
    * 거의 같게 들어감
  * fileNo : 몇번 첨부파일인지 확인
  * fileDir : 첨부파일이 저장된 폴더
    * 갯수가 많아질 때를 대비해서 월별로 저장되게 만듬

* getMediaHtml()
  * 첨부파일을 원래 크기대로 보여주는 함수

