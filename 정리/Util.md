# Util

* util 패키지에 Util 클래스를 만들어서 다른곳에서도 사용할 함수를 저장해 놓음
  * 공용함수
  * public static 으로 선언
* String getNowDateStr()
  * 현재 날짜,시간 정보 리턴
* Map<String, Object> mapOf(Object... args)
  * 값이 들어간 map 리턴(id, title, body 등등)
  * 갯수 상관없이 받을 수 있음(Object... args)
  * key 와 value 값 순서대로 들어가야 함
    * 홀수번째 인자는 무조건 String, 전체 값은 짝수개 들어오는지 확인
  * LinkedHashMap<>()
    * 순서에 맞게 정보 넣기
      * HashMap은 순서대로 들어가지 않음
* getAsInt(object, defaultValue)
  * 어떤 값이 들어와도 정수로 변환
  * 실패하면 defaultValue로 변환

* msgAndBack(String msg)
  * history.back() : 뒤로가기 버튼 누른것과 같은 효과
* msgAndReplace(String msg, String url)
  * location.replace() : 이동 후 바로 이전 페이지로 돌아갈 수 없음

* 이동 방법
  * <a href="b.html"\>B로 이동</a\>
  * <button onclick="location.href='b.html';"\>B로 이동</button\>
  * <button onclick="location.replace('b.html');"\>B로 이동</button\>
    * k.html 에서 a.html로 왔다가 이 버튼으로 b.html로 이동하면 뒤로가기 했을 때, k로 돌아감

