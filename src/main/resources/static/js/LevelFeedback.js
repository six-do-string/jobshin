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