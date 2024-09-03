// let level = 0;
// console.log(level);
// let score = 0;
//
// // 난이도를 설정하고 UI를 업데이트하는 함수
// function setDifficulty() {
//     // 모든 옵션 버튼의 배경색을 기본값으로 설정
//     let buttons = document.getElementsByClassName("option-button");
//     for (let i = 0; i < buttons.length; i++) {
//         buttons[i].style.backgroundColor = "#f0f0f0"; // 기본 배경색
//         buttons[i].style.color = "#000"; // 기본 글자 색상
//     }
//
//     // 클릭된 버튼의 배경색과 글자 색상을 변경하여 강조
//     event.target.style.backgroundColor = "#1a73e8"; // 강조 배경색
//     event.target.style.color = "#fff"; // 강조 글자 색상
//
//     level = $("input[name='difficulty']:checked").val();
//
//     if (level === '상') {
//         return 0;
//     }
//     if (level === '중') {
//         return 50;
//     }
//     if (level === '하') {
//         return 100;
//     }
// }

function setLevel() {
    score = (setDifficulty()) / 2;
    console.log(score);

    return score;
}

// // 완료 버튼을 눌렀을 때 실행되는 함수
// function submitForm() {
//     // 선택된 난이도 값을 가져옴
//     const difficulty = setLevel();
//     if (difficulty) {
//         alert("완료되었습니다");
//         window.location.href = "/views/main";
//     } else {
//         // 난이도가 선택되지 않은 경우 경고 메시지를 표시
//         alert("난이도를 선택해 주세요.");
//     }
// }