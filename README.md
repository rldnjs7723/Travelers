# Travelers 화면 구성

## 메인 화면

<img width="916" alt="1 메인화면" src="https://github.com/rldnjs7723/Travelers/assets/20474034/18e266d9-7eb0-4b02-91cb-9b532007f289">

메인화면입니다.  
Vue.js 프레임워크를 이용하여 Single Page Application 형태로 구현을 했으며,  
화면 중앙의 카드를 클릭하면 해당 기능을 이용할 수 있는 페이지로 이동합니다.  
다만 현재 화면에서는 로그인되지 않아 상단 헤더바에 페이지별 이동 버튼이 비활성화 되어 있는데,  
로그인하지 않은 상태에서 카드를 클릭하면 로그인 페이지로 전환됩니다.

## 회원 관리

<img width="917" alt="4 로그인" src="https://github.com/rldnjs7723/Travelers/assets/20474034/a6080355-97a9-42bb-9c8d-58b4f7d56e6b">

로그인 화면입니다.  
로그인 시 아이디는 이메일을 입력하도록 하였고, LocalStorage를 통해 아이디 저장 기능을 구현했습니다.

<img width="917" alt="2 회원가입" src="https://github.com/rldnjs7723/Travelers/assets/20474034/fbab17a3-0f82-431e-92d2-b400bf4dada9">

회원가입 화면입니다.  
Vue Bootstrap에서 제공해주는 Form을 사용하여 입력 조건에 맞지 않는 값이 들어오면 빨간색으로 표시하고, 문구를 출력하도록 했습니다.  
이메일, 별명, 연락처의 경우 중복된 값이 DB에 있다면 올바르지 않은 입력으로 처리했으며,  
비밀번호는 최소 8자의 문자로 구성하도록 하였습니다.

<img width="917" alt="3 회원가입" src="https://github.com/rldnjs7723/Travelers/assets/20474034/2dcedc61-af81-48ed-9d97-a4f1a9fe6cda">

모든 입력이 제대로 주어졌다면 위 사진과 같이 초록색으로 표시합니다.

![16 아이디비밀번호찾기](https://github.com/rldnjs7723/Travelers/assets/20474034/81764678-74c6-4e49-8baf-0097fe1172fb)

아이디, 비밀번호 찾기 화면입니다.  
아직 연락처로 본인 인증하는 기능을 추가하지 못하여, 회원가입 할 때 사용했던 연락처를 통해 아이디를 찾을 수 있도록 하였습니다.
비밀번호 찾기 기능은 이메일과 연락처를 통해 임시 비밀번호를 발급 받도록 하였습니다.

<img width="917" alt="15 회원정보수정" src="https://github.com/rldnjs7723/Travelers/assets/20474034/3f774a09-206a-4eb7-96ab-29e80a167def">

마이페이지 화면입니다.  
회원 정보 수정 부분에서는 프로필 사진과 비밀번호, 연락처를 수정할 수 있도록 구현했습니다.

![17 북마크관리](https://github.com/rldnjs7723/Travelers/assets/20474034/c7faf809-6547-4149-a20c-b5b9f075a08e)

북마크 관리 화면입니다.  
관심 있는 관광지나 게시글들을 한 화면에서 조회할 수 있는 페이지입니다.

## 관광지 정보 조회

<img width="917" alt="5 지역별여행지" src="https://github.com/rldnjs7723/Travelers/assets/20474034/c14befd4-b430-4281-8f2a-7d5b2af51ac7">

지역별여행지 화면입니다.  
시도 / 구군 / 검색어 / 관광지 유형에 따라 원하는 관광지를 검색할 수 있습니다.

<img width="916" alt="6 여행지정보모달" src="https://github.com/rldnjs7723/Travelers/assets/20474034/e5e0f560-2147-49e8-b06f-7022175ebd93">

검색 결과로 출력된 카드를 클릭하면 위 사진과 같이 모달로 상세 정보를 확인할 수 있습니다.  
우측 상단의 북마크 아이콘을 클릭하면 북마크에 등록할 수 있습니다.

## 여행 계획 설계

<img width="916" alt="7 나의여행계획" src="https://github.com/rldnjs7723/Travelers/assets/20474034/329a1f09-8da2-432f-a773-b23dd216a1fd">

나의여행계획 화면입니다.
관광지 정보 검색을 통해 북마크에 등록한 여러 관광지를 통해 여행 경로를 설계할 수 있습니다.  
우측의 북마크 탭에서 각 일차로 드래그앤 드랍을 수행하면 관광지 정보를 추가할 수 있습니다.
좌측에는 설정한 여행 경로를 한 눈에 볼 수 있도록 지도에 표시했습니다.

<img width="917" alt="8 여행계획공유" src="https://github.com/rldnjs7723/Travelers/assets/20474034/ae68d0d1-c3b2-412a-a7f7-dd21dc53b34c">

하단으로 스크롤을 내리면 여행 계획의 제목, 세부적인 메모, 시작 날짜를 입력하여 다른 사람들에게 게시글 형태로 공유할 수 있습니다.

## 핫플레이스 리뷰

<img width="917" alt="9 핫플레이스리뷰" src="https://github.com/rldnjs7723/Travelers/assets/20474034/a4f9bc30-3862-47db-9784-5009f934f296">

핫플자랑하기 화면입니다.  
북마크에 등록한 관광지에 대해 별점을 매기고, 관련된 사진을 업로드 할 수 있게 만들었습니다.

## 게시판

<img width="917" alt="11 게시글목록" src="https://github.com/rldnjs7723/Travelers/assets/20474034/38cecdf4-b23e-46e2-b30d-fb5889f96e36">

여행정보공유 화면입니다.  
앞서 작성한 여행 계획, 핫플레이스 리뷰에 대한 게시글을 한 번에 볼 수 있도록 구현했습니다.

<img width="916" alt="10 자유게시판" src="https://github.com/rldnjs7723/Travelers/assets/20474034/3fd7993a-265e-4b7a-ac23-6b4cc423f9ea">

일반 게시글 작성 화면입니다.

<img width="917" alt="12 일반게시글목록" src="https://github.com/rldnjs7723/Travelers/assets/20474034/b5a0f032-4376-4ee9-98d8-c4ad772ca6f9">

검색 창 밑의 두 번째 아이콘을 클릭하면 위 사진과 같이 일반 게시글만을 볼 수 있습니다.

<img width="916" alt="13 핫플레이스목록" src="https://github.com/rldnjs7723/Travelers/assets/20474034/98a3a7fd-e4ba-4553-828d-7a996a8ab14e">

검색 창 밑의 세 번째 아이콘을 클릭하면 위 사진과 같이 핫플 후기 게시글만을 볼 수 있습니다.

<img width="917" alt="14 여행계획목록" src="https://github.com/rldnjs7723/Travelers/assets/20474034/7070a6e7-d45a-4975-bf80-988208964f34">

검색 창 밑의 네 번째 아이콘을 클릭하면 위 사진과 같이 여행 계획 게시글만을 볼 수 있습니다.
