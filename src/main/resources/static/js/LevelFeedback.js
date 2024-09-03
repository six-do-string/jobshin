// 난이도를 설정하고 UI를 업데이트하는 함수
function setDifficulty(level) {
    // 모든 옵션 버튼의 배경색을 기본값으로 설정
    let buttons = document.getElementsByClassName("option-button");
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].style.backgroundColor = "#f0f0f0"; // 기본 배경색
        buttons[i].style.color = "#000"; // 기본 글자 색상
    }

    // 클릭된 버튼의 배경색과 글자 색상을 변경하여 강조
    event.target.style.backgroundColor = "#1a73e8"; // 강조 배경색
    event.target.style.color = "#fff"; // 강조 글자 색상
}

// 완료 버튼을 눌렀을 때 실행되는 함수
function submitForm() {
    // 선택된 난이도 값을 가져옴
    const difficulty = document.getElementById("difficulty").value;
    if (difficulty) {
        // 난이도가 선택된 경우 완료 메시지를 표시
        alert("완료되었습니다");

        // 선택 완료 후 지정된 URL로 페이지를 이동
        window.location.href = "/views/main"; // 이동할 페이지의 URL로 변경 필요
    } else {
        // 난이도가 선택되지 않은 경우 경고 메시지를 표시
        alert("난이도를 선택해 주세요.");
    }
}