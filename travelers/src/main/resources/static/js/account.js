// 이메일 도메인 정보
var emailDomainArr = ["", "@gmail.com", "@naver.com", "@kakao.com", "@ssafy.com"];

// contextPath
function getContextPath() {
  var hostIndex = location.href.indexOf(location.host) + location.host.length;
  var contextPath = location.href.substring(hostIndex, location.href.indexOf("/", hostIndex + 1));
  return contextPath;
}

// 로그인 수행
function login() {
  let loginModal = document.getElementById("loginModal");

  let loginInfo = {
    email: loginModal.querySelector("[name='email']").value,
    password: loginModal.querySelector("[name='password']").value,
    saveid: loginModal.querySelector("#id-checkbox").value,
  };

  let config = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(loginInfo),
  };

  fetch(`/user/login`, config)
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      console.log(data);
      location.href = "/";
    })
    .catch((error) => alert(error.msg));
}

function logout() {
  let config = {
    method: "GET",
  };

  fetch(`/user`, config)
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      alert(data.msg);
      location.href = "/";
    })
    .catch((error) => console.log(error.msg));
}

/*
 * // 로그아웃 function logout() { loginInfo.isLogin = false; // 로그아웃 storage에 반영
 * sessionStorage.setItem("loginInfo", JSON.stringify(loginInfo));
 * window.open("/views/index.html", "_self"); }
 */

// 회원가입 입력 창 초기화
function reset() {
  let registerModal = document.getElementById("registerModal");
  let inputs = registerModal.getElementsByTagName("input");

  // 모든 입력 초기화
  for (element of inputs) {
    element.value = "";
  }

  let emailDomain = registerModal.getElementsByTagName("select");
  for (element of emailDomain) {
    element.value = 0;
  }
}

// 회원가입
function register() {
  let registerModal = document.getElementById("registerModal");

  // 비동기로 이메일 중복 체크, 비밀번호 동일한지 체크 필요
  // 계정 정보 입력
  let registerInfo = {};
  registerInfo.email = registerModal.querySelector("[name='emailid']").value;
  if (!registerInfo.email) {
    alert("이메일을 입력해주세요!");
    return;
  }

  let emailDomain = document.getElementById("emali-domain").value;
  if (emailDomain == 0) {
    alert("이메일 도메인을 선택해주세요!");
    return;
  }
  registerInfo.email += "@" + emailDomain;

  registerInfo.password = registerModal.querySelector("[name='password']").value;
  if (!registerInfo.password) {
    alert("비밀번호를 입력해주세요!");
    return;
  }

  let passwordConfirm = registerModal.querySelector("[name='password-confirm']").value;
  if (registerInfo.password !== passwordConfirm) {
    alert("비밀번호가 다릅니다.");
    return;
  }

  registerInfo.name = registerModal.querySelector("[name='name']").value;
  if (!registerInfo.name) {
    alert("이름을 입력해주세요!");
    return;
  }

  registerInfo.id = registerModal.querySelector("[name='id']").value;
  if (!registerInfo.id) {
    alert("아이디를 입력해주세요!");
    return;
  }

  /*
   * // 모든 정보가 채워져 있다면 회원가입 alert("회원가입이 완료되었습니다!"); // 정보 저장
   * accountInfo.push(account); localStorage.setItem("accountInfo",
   * JSON.stringify(accountInfo)); location.reload();
   */

  let config = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(registerInfo),
  };

  fetch(`/user`, config)
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      alert(data.msg);
      location.href = "/";
    })
    .catch((error) => alert(error.msg));
}

// 아이디 찾기
function findId() {
  let email = prompt("가입 시 입력한 이메일을 입력해주세요.");

  let account = accountInfo.find((obj) => obj.email === email);
  if (account) {
    alert(`해당 이메일을 가진 계정의 아이디는 '${account.id}'입니다.`);
  } else {
    alert("해당 이메일을 가진 계정은 존재하지 않습니다.");
  }
}

// 비밀번호 찾기
function findPassword() {
  let id = prompt("가입 시 입력한 아이디를 입력해주세요.");
  let email = prompt("가입 시 입력한 이메일을 입력해주세요.");

  let account = accountInfo.find((obj) => obj.email === email && obj.id === id);

  if (account) {
    let password = prompt("새로 변경할 비밀번호를 입력해주세요.");
    account.password = password;
    // 정보 저장
    localStorage.setItem("accountInfo", JSON.stringify(accountInfo));
    alert(`비밀번호가 변경되었습니다!`);
    location.reload();
  } else {
    alert("해당 계정 정보를 가진 계정은 존재하지 않습니다.");
  }
}

// 회원 정보 수정 창 띄우기
function update() {
  let defaultPage = document.getElementById("mypage-default");
  let activePage = document.getElementById("mypage-active");

  if (defaultPage.style.display == "none") {
    defaultPage.style.display = "";
    activePage.style.display = "none";
  } else {
    defaultPage.style.display = "none";
    activePage.style.display = "";
  }
}

// 회원 정보 수정
function modify() {
  // 비동기로 아이디 중복 체크 필요
  // 계정 정보 입력
  let modifyInfo = {};
  modifyInfo.id = document.getElementById("modify-id").value;
  if (!modifyInfo.id) {
    alert("아이디를 입력해주세요!");
    return;
  }

  modifyInfo.name = document.getElementById("modify-name").value;
  if (!modifyInfo.name) {
    alert("이름을 입력해주세요!");
    return;
  }

  let config = {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(modifyInfo),
  };

  fetch(`/user`, config)
    .then((response) => {
      if (response.ok === true) {
        return response.json();
      }
      throw new Error(response.json());
    })
    .then((data) => {
      alert(data.msg);
      location.href = "/mypage";
    })
    .catch((error) => alert(error.msg));
}

// 회원 탈퇴
function withdraw() {
  if (confirm("정말 탈퇴하시겠습니까?")) {
    let config = {
      method: "DELETE",
    };

    fetch(`/user`, config)
      .then((response) => {
        if (response.ok === true) {
          return response.json();
        }
        throw new Error(response.json());
      })
      .then((data) => {
        alert(data.msg);
        location.href = "/";
      })
      .catch((error) => alert(error.msg));
  }
}
