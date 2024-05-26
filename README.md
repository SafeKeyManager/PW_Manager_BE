# PW_Manager_BE
### 사용자들의 password 갱신 관리
---
## 1. 프로젝트 소개
현대사람들은 자신이 가입한 사이트, service 들에 대해 자세히 기억하지 못한다. 그로인해 오랬동안 사용되지 않았던 service 의 계정정보는 갱신이 이루어지지 않아 보안적으로 취약해진다. 이러한 문제를 해결하기 위해 사용자들에게 설정한 갱신 일자가 지나게 되면 각 유저들에게 어떤사이트의 갱신일이 지났다고 정보 제공
service url : ~~https://schedules.store~~ (서버 닫은 상태)
## 2. 프로젝트 기능
### 1️⃣ 가입한 사이트 관리
유저가 해당 사이트에 가입할 때 가입한 사이트에 대한 정보를 입력 받는다. 원하는 갱신주기도 추가로 입력받아 해당 주기를 바탕으로 유저에게 갱신 알람을 전달한다. 
### 2️⃣ 갱신이 필요한 사이트에 대한 알림 전송
spring batch를 사용해 매일 DB를 스캔해서 특정 사이트가 유저가 원하는 갱신 주기를 벗어났을때 firebase 의 fcm 을 사용해서 사용자에게 해당 사이트가 갱신 주기가 변경되었음을 알린다.
#### 예시
![image](https://github.com/SafeKeyManager/PW_Manager_FE/assets/55120730/66967b5b-c595-45cf-8cfb-877a23088113)
## 3. 기술스택
| 역할            | 종류
| -------------- | ----------------
| framwork       |  <img alt="RED" src ="https://img.shields.io/badge/SPRING Boot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Spring Security-6DB33F.svg?&style=for-the-badge&logo=springsecurity&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Spring Batch-6DB33F.svg?&style=for-the-badge&logo=springbatch&logoColor=white"/> |
| database       | <img alt="RED" src ="https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white"/> |
| deploy         | <img alt="RED" src ="https://img.shields.io/badge/Firebase-gray?style=for-the-badge&logo=Firebase&logoColor=%23FFCA28" /> <img alt="RED" src ="https://img.shields.io/badge/Nginx-009639.svg?&style=for-the-badge&logo=nginx&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Amazon EC2-FF9900.svg?&style=for-the-badge&logo=AmazonEC2&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Amazon Rds-527FFF.svg?&style=for-the-badge&logo=AmazonRds&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Amazon S3-569A31.svg?&style=for-the-badge&logo=AmazonS3&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Amazon Route 53-8C4FFF.svg?&style=for-the-badge&logo=Amazon Route 53&logoColor=white"/> <img alt="RED" src ="https://img.shields.io/badge/Certbot-FF1E0D.svg?&style=for-the-badge&logo=Certbot&logoColor=white"/> |                   
| version control|  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">   |
| cop            | <img alt="RED" src ="https://img.shields.io/badge/Notion-000000.svg?&style=for-the-badge&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white"> |

## 정리한 내용
[정리 notion](https://brawny-tachometer-42f.notion.site/2fc60b1c7a3948bebf3e7c9e7ec1531f?v=fc0e7baa84404bdd914e90bb358a9409&pvs=4)

