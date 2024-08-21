# 📒🧾 expenseTracker 💵📊

### 도메인 주소
expensetracker.kro.kr

## 📑 목차 
- [소개](#소개)
- [프로젝트 정보](#프로젝트-정보)
- [시스템 설계](#시스템-설계)
- [사용기술](#사용기술)
- [기능](#기능)

## 소개
expenseTracker 프로젝트는 사용자가 가계부 내역을 작성하고 통계를 통해 자신의 소비 패턴을 분석할 수 있는 웹 서비스입니다.
<br>
이 서비스를 통해 사용자는 자신의 지출 습관을 명확하게 파악할 수 있습니다.

## 프로젝트 정보
#### 제작기간
> 2024년 5월 2일 ~ 2024년 5월 20일

#### 개발자
> [이지영](https://github.com/jylee102)

## 시스템 설계

<details>
  <summary>ERD</summary>
	
  ![erd](https://github.com/user-attachments/assets/20b99dab-fb2b-4822-8c8c-4d9db8a9329f)

  - 자세한 정보는 [ERD](https://www.erdcloud.com/d/dJ3cw2xkX7gTxAeCH) 링크를 참조하세요.
    
</details>

## 사용기술
> #### Back-end 
> Java / Spring Boot <br> Spring Data JPA <br> Spring Security
 
> #### Front-end
> HTML / CSS / JavaScript

> #### DB
> MySQL

> #### Library
```
<!-- jQuery UI Sortable(드래그 앤 드롭, 순서 변경) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
```
```
<!-- calendar.js: 달력 -->
    <script th:src="@{//cdnjs.cloudflare.com/ajax/libs/moment.js/2.5.1/moment.min.js}"></script>
    <script th:src="@{/js/calendar.js}"></script>
```
```
<!-- chart.js: 통계 -->
    <script th:src="@{/vendors/chart.js/Chart.min.js}"></script>
```
```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    //Querydsl add(spring boot 3)
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
    annotationProcessor "jakarta.annotation:jakarta.annotation-api"
    annotationProcessor "jakarta.persistence:jakarta.persistence-api"

    //thymeleaf layout
    implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect'

    // spring security
    implementation 'org.springframework.boot:spring-boot-starter-security'
    testImplementation 'org.springframework.security:spring-security-test' //시큐리티 테스트 코드에서 사용
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6' //타임리프에서 시큐리티 객체 사용

    // modelmapper
    implementation group: 'org.modelmapper', name: 'modelmapper', version: '2.4.2'

    // gson(List를 JSON 문자열로 변환하기 위해)
    implementation 'com.google.code.gson:gson:2.8.8'

    // email 기능
    implementation 'org.springframework.boot:spring-boot-starter-mail'
}
```

## 기능

<details>
  <summary>
    <b>수입 및 지출 기록</b>
  </summary>
  
  <br>
  <ul>
    <li>
      메인 화면<br>
      <img src="https://github.com/user-attachments/assets/3126a1de-edd6-46d6-8381-5b145a850212" alt="메인화면" style="width: 500px;">
    </li>
    <li>
      등록 화면<br>
      <img src="https://github.com/user-attachments/assets/587f1aee-b5b6-48eb-90e1-60c1cd1610fc" alt="거래 추가" style="width: 500px;">
    </li>
  </ul>

</details>

<br>

<details>
  <summary>
    <b>달력과 통계</b>
  </summary>
  
  <br>
  <ul>
    <li>
      달력<br>
      <img src="https://github.com/user-attachments/assets/a4b4d582-43b9-4bf2-8df4-9f808a96790d" alt="달력" style="width: 500px;">
    </li>
    <li>
      통계<br>
      <img src="https://github.com/user-attachments/assets/5fdafde4-fa16-4dca-973d-ba6015b2ac5e" alt="통계" style="width: 500px;">
    </li>
  </ul>

</details>

<br>

<details>
  <summary>
    <b>예산 설정 및 카테고리 관리</b>
  </summary>
  
  <br>
  <ul>
    <li>
      예산 설정<br>
      <img src="https://github.com/user-attachments/assets/81421156-cf57-44cf-9d70-33118658862c" alt="자산설정" style="width: 500px;">
    </li>
    <li>
      카테고리 관리<br>
      <img src="https://github.com/user-attachments/assets/c1ce0dbf-f102-4465-bd60-6dd5d3ce4608" alt="카테고리 설정" style="width: 500px;">
    </li>
  </ul>

</details>
