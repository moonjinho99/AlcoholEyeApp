# alcoholEye
산학클러스터 프로젝트 : 음주 측정 앱

서버 Repository: [alcoholEye](https://github.com/jhchon/alcoholEye)

## 📺 개발환경
- <img src="https://img.shields.io/badge/IDE-%23121011?style=for-the-badge">![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?&style=for-the-badge&logo=Android%20Studio&logoColor=white)
- <img src="https://img.shields.io/badge/Language-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/18-515151?style=for-the-badge">

<br/><br/>
<hr>

## 🔍 프로젝트 개요

- 시민의 안전을 보장하고 음주운전을 사전에 방지하기 위해, 보이스링크 화사에서 개발한 어플리케이션과 유사한 시스템을 구현하는 활동을 진행함

- 이 시스템은 버스 운행 전 음주 측정을 통해 운전자의 음주 여부를 확인하고, 이를 통한 안전 운전 촉진에 목적을 두고 있음

<br/><br/>
<hr>

## 💻 역할

➡ 안드로이드 코드 작성

<br/><br/>
<hr>

## ⭐ 주요기능

➡ 회원가입

<img src="https://github.com/moonjinho99/AlcoholEyeApp/assets/117807455/2d60cd73-b358-4692-a122-12625ea75a24" width="200" height="400">

<br/>

- 패스워드는 BCrypt HASH 함수로 암호화 하여 회원가입
  
- 회원정보를 JSON 형식의 데이터로 서버에 전송하기 위해 Retorfit 라이브러리를 사용

<br/><br/>
<hr>

➡ 로그인 후 검사화면

<img src="https://github.com/moonjinho99/AlcoholEyeApp/assets/117807455/556fbcfb-126f-448f-a66a-b3a5e18ed374" width="200" height="400"> <img src="https://github.com/moonjinho99/AlcoholEyeApp/assets/117807455/0291b914-84ea-47ac-8d58-c627c38cb249" width="200" height="400"> <img src="https://github.com/moonjinho99/AlcoholEyeApp/assets/117807455/3774b3bb-1528-4baa-994f-f56bf0e734b1" width="200" height="400">

<br/>

- 사진을 웹 서버로 전송하여 회원의 사진과 일치하는 검사

- 얼굴인식은 서버에서 FaceAPI.js를 이용하여 구현

<br/><br/>
<hr>

## 🔍 데이터 통신 방법

![image](https://github.com/moonjinho99/AlcoholEyeApp/assets/117807455/2f47b523-4b07-42ac-a8a9-ab33a947a19e)

➡ JSON 형식으로 데이터를 주고 받음

<br/><br/>
<hr>

## 💡 이번 프로젝트를 통해 배운점

➡ JSON 형식으로 통신을 해봄으로써 Retrofit 사용법에 익숙해짐

➡ 어노테이션(@) 기법을 사용함으로써 가독성이 높고 효율적인 코드를 작성하는 실력 향상

<br/><br/>
<hr>

