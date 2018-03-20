<%--
  Created by IntelliJ IDEA.
  User: pd
  Date: 2018/2/1
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  $END$
  <span style="font-size:14px;"> <form action="FileUploadServlet"  enctype="multipart/form-data" method="post">
         商户信息：<input type="text" name="adAddName"><br/>
         上传文件1：<input type="file" name="file"><br/>
    <!--          上传文件2：<input type="file" name="file2"><br/> -->
    <!--          上传文件3：<input type="file" name="file3"><br/> -->
    <!--          上传文件4：<input type="file" name="file4"><br/> -->
         <input type="submit" value="提交">
     </form>
  </span>
  </body>
</html>
