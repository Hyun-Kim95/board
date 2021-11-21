# crontab 방식

* SpringBoot에서 Application.java의 @EnableScheduling 을 붙히고

  사용하고자 하는 함수에 @Scheduled(cron = "0 0 12 * * ?") 와 같이 붙히면 됨

* 주기설정 방법

```
*           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7) 
각 별 위치에 따라 주기를 다르게 설정 할 수 있음
괄호 안의 숫자 범위 내로 별 대신 입력 할 수도 있음
요일에서 0과 7은 일요일, 1부터 월요일
```

* 주기설정하는 방식

![image](https://user-images.githubusercontent.com/75933619/142228979-a2cb388d-6b1a-4154-a0dd-5afc45fc6dab.png)