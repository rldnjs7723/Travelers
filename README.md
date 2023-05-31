# EnjoyTrip_Vue\_광주\_04\_남기원\_전윤철

## User (회원 관리)

![1](https://github.com/rldnjs7723/CodingTest/assets/20474034/63583a27-b7f8-4810-a31f-4f9e8a0d1453)

메인화면입니다. 토큰을 통한 로그인이 완전하게 구현되지 않았기 때문에, 상시 로그인 된 상태를 유지하도록 설정했습니다.

![8](https://github.com/rldnjs7723/CodingTest/assets/20474034/e0307c63-5489-498b-b81f-125ab3abd616)
![7](https://github.com/rldnjs7723/CodingTest/assets/20474034/374db10c-7b74-4bdc-887e-2d39f8617d81)

회원가입은 /user/regist으로 이동할 수 있으며, 아직 제대로 구현되지 않았습니다.  
로그인은 /user/login으로 이동할 수 있으며, 이전 프로젝트에서 회원가입 했던 아이디라면 로그인이 가능합니다. 다만, 토큰이 만료됐을 때를 제대로 처리하지 못했기 때문에 Spring Security의 필터를 해제해놓은 상태입니다.

![2](https://github.com/rldnjs7723/CodingTest/assets/20474034/0cf676a1-2512-4638-a1f8-7f7979c01d42)

마이페이지 화면입니다. 우측 상단의 사람 아이콘을 눌렀을 시 마이페이지 링크를 누르면 이동할 수 있습니다.

![3](https://github.com/rldnjs7723/CodingTest/assets/20474034/196eb87a-9b55-4f60-8364-3c764f08af00)

수정 화면입니다. 완전히 구현되지 않았습니다.

## Attraction (관광지 정보 조회)

![4](https://github.com/rldnjs7723/CodingTest/assets/20474034/861fc3dd-e35a-4ae7-8d6c-1978cb2b75bf)

관광지 정보 조회 화면입니다. 이전과 동일하게 시도 / 구군을 선택한 뒤 검색하면 됩니다.  
관광지 중 하나를 골라 우측 상단의 여행 경로 추가 버튼을 누르면 여행 계획 경로에 추가됩니다.

## Article (여행 계획 공유, Hotplace)

![5](https://github.com/rldnjs7723/CodingTest/assets/20474034/dcd31f3a-c757-4329-ab20-6718d48395f5)

여행 계획 화면입니다. 선택된 관광지 순서대로의 경로에 따른 거리를 보여주며, 삭제 버튼을 누르면 해당 관광지를 제거합니다.

![6](https://github.com/rldnjs7723/CodingTest/assets/20474034/9af78d1d-da81-4b64-90c3-131eb6534ab8)

하단의 제목, 내용, 시작일, 종료일을 입력한 뒤 여행 계획을 게시판에 공유할 수 있습니다.

![7](https://github.com/ycjeon0129/algorithm-problem-solving/assets/79627716/aab8f857-0e8b-4cb7-b029-f315043a09b4)

등록한 여행 계획을 조회하는 화면입니다. 출발일자, 도착일자, 내용, 계획에 추가된 관광지의 contentId 배열이 표시됩니다.

![8](https://github.com/ycjeon0129/algorithm-problem-solving/assets/79627716/c835e4e9-5460-4e5f-b034-0d19d47c51b5)

Hotplace 등록 화면입니다. 관광지 유형, 방문 날짜를 표현한 게시글 형태로 작성할 수 있습니다.

![9](https://github.com/ycjeon0129/algorithm-problem-solving/assets/79627716/6a567756-b23a-4b90-8a1a-b4e99bfdc975)

등록한 Hotplace를 조회하는 화면입니다. `제목(관광지 유형 번호)` 형태로 Hotplace 정보를 표시합니다.

![10](https://github.com/ycjeon0129/algorithm-problem-solving/assets/79627716/c6ad3978-0f14-4bd4-ac4c-878f85bb7827)

게시판 키워드 검색 화면입니다. 입력한 검색어를 포함한 게시글의 목록을 표시합니다. 
