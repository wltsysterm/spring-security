<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<body>
<h2>Hello Security! </h2>
<br>
<form action="/api/index" method="post">
    用户：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit" value="提交">

</form>
</body>
</html>