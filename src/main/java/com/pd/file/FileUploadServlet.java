package com.pd.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @author: youpd@asiainfo.com
 * @create: 2018-02-01 15:58
 */
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(request, resp);
        String logoPathDir = "/upload";
        String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir);


        try {

            //0.5 检查是否支持文件上传  ,检查请求头Content-Type : multipart/form-data
            if (!ServletFileUpload.isMultipartContent(request)) {
                throw new RuntimeException("不要得瑟，没用");
            }

            //1 工厂
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            // 1.1 设置是否生产临时文件临界值。大于2M生产临时文件。保证：上传数据完整性。
            fileItemFactory.setSizeThreshold(1024 * 1024 * 2);  //2MB
            // 1.2 设置临时文件存放位置
            // * 临时文件扩展名  *.tmp  ，临时文件可以任意删除。
            String tempDir = this.getServletContext().getRealPath("/temp");
            fileItemFactory.setRepository(new File(tempDir));

            //2 核心类
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            // 2.1 如果使用无参构造  ServletFileUpload() ，手动设置工厂
            //servletFileUpload.setFileItemFactory(fileItemFactory);
            // 2.2 单个上传文件大小
            //servletFileUpload.setFileSizeMax(1024*1024 * 2);  //2M
            // 2.3 整个上传文件总大小
            //servletFileUpload.setSizeMax(1024*1024*10);        //10M
            // 2.4 设置上传文件名的乱码
            // * 首先使用  setHeaderEncoding 设置编码
            // * 如果没有设置将使用请求编码 request.setCharacterEncoding("UTF-8")
            // * 以上都没有设置，将使用平台默认编码
            servletFileUpload.setHeaderEncoding("UTF-8");
            // 2.5 上传文件进度，提供监听器进行监听。
//            servletFileUpload.setProgressListener(new MyProgressListener());


            //3 解析request  ,List存放 FileItem （表单元素的封装对象，一个<input>对应一个对象）
            List<FileItem> list = servletFileUpload.parseRequest(request);

            //4 遍历集合获得数据
            for (FileItem fileItem : list) {
                // 判断
                if (fileItem.isFormField()) {
                    // 5 是否为表单字段（普通表单元素）
                    //5.1.表单字段名称
                    String fieldName = fileItem.getFieldName();
                    System.out.println(fieldName);
                    //5.2.表单字段值 ， 解决普通表单内容的乱码
                    String fieldValue = fileItem.getString("UTF-8");
                    System.out.println(fieldValue);
                } else {
                    //6 上传字段（上传表单元素）
                    //6.1.表单字段名称  fileItem.getFieldName();
                    //6.2.上传文件名
                    String fileName = fileItem.getName();
                    // * 兼容浏览器， IE ： C:\Users\liangtong\Desktop\abc.txt  ; 其他浏览器 ： abc.txt
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                    // * 文件重名
                    fileName = UUID.randomUUID().toString().replace("-", "") + fileName;
                    // * 单个文件夹文件个数过多？

//                    String subDir = logoRealPathDir+"/"+fileName;

                    System.out.println(fileName);
                    //6.3.上传内容
                    InputStream is = fileItem.getInputStream();
                    String parentDir = this.getServletContext().getRealPath("/WEB-INF/upload");
                    File file = new File(parentDir + "/", fileName);

                    // 将指定流 写入 到 指定文件中  -- mkdirs() 自动创建目录
                    FileUtils.copyInputStreamToFile(is, file);

                    //7删除临时文件
                    fileItem.delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(e);

        }
    }
}
