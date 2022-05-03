# RBV-SEV 공유 가계부 개발

**소개 :** 기존의 가계부 기능 + 공유기능을 추가하여 쉽게 수입, 지출 상황을 공유, 관리할수 있는 기능을 제공.

## ERD 

URL : https://www.erdcloud.com/d/Zug9szTq7AZruYPQG

## API

URL : https://documenter.getpostman.com/view/8156330/UyrHeYui


## 사용 기술
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data_JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [QueryDsl](https://http://querydsl.com/)

### server
- [Dorker](https://www.docker.com/) **v20.10.14**
- [docker-compose](https://www.docker.com/) 

### DB
- [MariaDB](https://mariadb.org/) **10.6.7**

### 폴더 구조
java.com.rbc.red
├── api
|   ├──controller
|   ├──dto
|   ├──entity
|   ├──exxeption
|   ├──repository
|   └──service
├── common (외부 응답 Dto)
├── config (스웨거, Cors, Security, JWT 설정)
├── oauth (Oauth2 기능)
├── utils (쿠키, 헤더)
└── BookApplication.java

