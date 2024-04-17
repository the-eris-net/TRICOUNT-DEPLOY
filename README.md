# 13. 서버 애플리케이션 AWS에 배포하기

(과제 설명)

앞서 진행했던 스프링 부트 과제인 tricount 클론 코딩 정산 어플리케이션을 AWS에 배포해보는 과제입니다.

(사용 기술)

- AWS IAM
- AWS EC2
- AWS S3
- AWS CodeDeploy
- Github Action
- Docker

(구현 요구사항)

- **섹션 1. AWS - IAM** 수강했던 내용을 참고하여 AWS IAM 서비스로 사용자계정을 생성합니다.
- AWS EC2 인스턴스를 생성하여 CodeDeploy 에이전트, AWS CLI, java를 설치합니다.
- AWS S3 버킷을 생성합니다.
- 어플리케이션 프로젝트 안에 도커파일을 작성합니다.
- CI 구축: Github에 본인이 진행하였던 과제를 업로드하고, Github Action 을 사용하여 Docker 이미지를 빌드하여 S3에 푸시합니다.
    - 어플리케이션 프로젝트 루트 디렉토리에 .github/workflows 디렉토리를 만들고, 그 하위에 워크플로우를 작성합니다.
    - springboot 어플리케이션 빌드, 도커이미지 빌드, AWS Access Key 세팅, s3에 이미지 업로드, CodeDeploy 실행 스텝으로 진행하는 워크플로우를 작성합니다.
        - CodeDeploy 에이전트, AWS CLI, java가 미리 설치되어있기 때문에, 이 3개에 대한 설치 스텝은 워크플로우에서 제외합니다.
    - 워크플로우 실행 트리거는 자동이 아닌 수동 트리거로 세팅합니다.
    - **따라하며 배우는 도커 커리큘럼** 및 **AWS 운영을 위한 이해**(섹션 10. AWS - Code Commit & Code Deploy & Code Pipeline)수강했던 것을 참고합니다.
- CD 구축: CodeDeploy를 이용하여 EC2 인스턴스에 어플리케이션을 배포합니다.
    - 이때 작성하는 appspec.yml은 배포하는 스프링부트 프로젝트 안에 저장합니다.
    - appspec.yml에서 s3의 이미지를 가져올때 AWS CLI를 사용합니다.

---
## ec2에서 사용한 작업
```shell
sudo su
yum install -y ruby
yum install -y wget
wget https://aws-codedeploy-ap-northeast-2.s3.amazonaws.com/latest/install
chmod +x install
./install auto
service codedeploy-agent status
yum install -y docker
service docker start
```