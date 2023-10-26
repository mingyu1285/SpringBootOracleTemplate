<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html lang="en">
<head>


    <meta charset="UTF-8">
    <title> 비밀번호 찾기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">


         let emailAuthNumber = "";

        $(document).ready(function () {

            let f = document.getElementById("f");

            $("#btnLogin").on("click", function () {
                location.href = "/user/login";
            })
            $("#btnSearchPassword").on("click", function () {

                if(f.userName.value === "") {
                    alert("아이디를 입력하세요.");
                    f.userId.focus();
                    return;
                }
                if(f.userName.value === "") {
                    alert("이름을 입력하세요.");
                    f.userName.focus();
                    return;
                }
                if(f.email.value === "") {
                    alert("메일을 입력하세요.");
                    f.email.focus();
                    return;
                }
                f.method = "post";
                f.action = "/user/searchPasswordProc"
                f.submit();
            })
            $("#btnEmail").on("click", function () { // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
                sendAuthMail(f);

            })

        })

        function doSubmit(f) {
            if (f.authNumber.value === "") {
                alert("이메일 인증번호를 입력하세요.");
                f.authNumber.focus();
                return;
            }
            if (f.authNumber.value !== emailAuthNumber) {
                alert("이메일 인증번호가 일치하지 않습니다.");
                f.authNumber.focus();
                return;
            }

        }
        function sendAuthMail(f) {
            console.log("authNumber : " + emailAuthNumber)
            if (f.email.value === "") {
                alert("이메일을 입력하세요.");
                f.email.focus();
                return;
            }

            // Ajax 호출해서 회원가입하기
            $.ajax({
                    url: "/user/getEmailExistsT",
                    type: "post", // 전송방식은 Post
                    dataType: "JSON", // 전송 결과는 JSON으로 받기
                    data: $("#f").serialize(), // form 태그 내 input 등 객체를 자동으로 전송할 형태로 변경하기
                    success: function (json) { // 호출이 성공했다면..
                        // 회원 정보에 저장되어있는 이메일이랑 일치해야함.


                            alert("이메일로 인증번호가 발송되었습니다. \n받은 메일의 인증번호를 입력하기 바랍니다.");
                            emailAuthNumber = json.authNumber;


                    }
                }
            )
        }


    </script>
</head>
<body>

<h2>비밀번호 찾기</h2>
<hr/>
<br/>
<form id="f">
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">아이디
            </div>
            <div class="divTableCell">
                <input type="text" name="userId" id="userId" style="width:95%"/>
            </div>
        </div>

        <div class="divTableRow">
            <div class="divTableCell">이름
            </div>
            <div class="divTableCell">
                <input Type="text" name="userName" id="userName" style="width:95%"/>

            </div>

        </div>

        <div class="divTableRow">
            <div class="divTableCell">이메일
            </div>
            <div class="divTableCell">
                <input Type="email" name="email" id="email" style="width:80%"/>
                <button id="btnEmail" type="button">인증번호 발송</button>
                <input type="text" name="authNumber" style="width:40%" placeholder="메일로 발송된 인증번호"/>
                <button id="text" type="button">인증번호 확인</button>

            </div>

        </div>
    </div>
</div>
    <div>
        <button id="btnSearchPassword" type="button"> 비밀번호 찾기 </button>
        <button id="btnLogin" type="button"> 로그인 </button>
    </div>
</form>
</body>

</html>