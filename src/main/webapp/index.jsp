<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<html>
<body>
<h2>Hello World!</h2>
<form name="form1" action="/manage/product/upload2.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>
<img src="/virtualPath/30d4b94b-cb88-49ce-84e0-d12101955169.jpg">
<form name="form2" action="/manage/product/richTextImgUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc富文本图片上传文件" />
</form>
</form>
</body>
</html>
