// DOM이 완전히 로드되면 실행
// document.addEventListener("DOMContentLoaded", function () {
//     // 모든 .toggle-button 요소를 선택
//     const toggleButtons = document.querySelectorAll(".toggle-button");
//
//     // 각 버튼에 클릭 이벤트 리스너 추가
//     toggleButtons.forEach(button => {
//         button.addEventListener("click", function () {
//             // 클릭된 버튼과 관련된 .question-content 요소 선택
//             const content = this.closest(".question-item").querySelector(".question-content");
//             // 콘텐츠가 숨겨져 있으면 보이게, 보이면 숨기기
//             if (content.style.display === "none" || content.style.display === "") {
//                 content.style.display = "block";
//                 this.textContent = "▼"; // 버튼 텍스트 변경
//             } else {
//                 content.style.display = "none";
//                 this.textContent = "▲"; // 버튼 텍스트 변경
//             }
//         });
//     });
// });