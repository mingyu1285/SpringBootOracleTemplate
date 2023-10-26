<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.dto.NoticeDTO" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="kopo.poly.dto.UserInfoDTO" %>
<%@ page import="kopo.poly.util.EncryptUtil" %>
<%
    // NoticeController 함수에서 model 객체에 저장된 값 불러오기
    UserInfoDTO rDTO = (UserInfoDTO) request.getAttribute("rDTO");
    String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); // 로그인된 회원 아이디



%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>회원정보 조회</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script>
        <%--const session_user_id = "<%=CmmUtil.nvl((String)session.getAttribute("SESSION_USER_ID"))%>";--%>




    </script>
</head>
<body>
<h2>회원정보</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">회원 아이디
            </div>
            <div class="divTableCell"><%=ssUserId%>
            </div>
        </div>

        <div class="divTableRow">
            <div class="divTableCell">이름
            </div>

            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getUserName())%>
            </div>
        </div>

        <div class="divTableRow">

            <div class="divTableCell">이메일
            </div>

            <div class="divTableCell"><%=EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail()))%>

            </div>
        </div>

        <div class="divTableRow">
            <div class="divTableCell">주소
            </div>

            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getAddr1())%>
            </div>

        </div>
    </div>


</div>
</body>
</html>

