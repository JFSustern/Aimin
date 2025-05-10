package com.oimc.aimin.ai.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * RAG（检索增强生成）服务接口
 */
public interface RagService {
    /**
     * 上传并读取文件内容，保存到向量存储中
     * @param file 上传的文件（支持pdf/word/txt）
     * @return 是否处理成功
     */
    boolean uploadAndLoadData(MultipartFile file) throws IOException, ClientException;

    /**
     * 根据文件类型读取并切分文档
     * @param inputStream 文件输入流
     * @param fileType 文件类型
     * @return 切分后的文档列表
     */
    List<Document> readAndSplitDocument(InputStream inputStream, String fileType) throws IOException;

    /**
     * 根据文件类型获取文档
     * @param fileType 文件类型
     * @param fileBytes 文件字节数组
     * @return 文档列表
     */
    List<Document> getDocuments(String fileType, byte[] fileBytes);

    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 扩展名
     */
    String getFileExtension(String filename);
    
    /**
     * 验证文件类型是否支持
     * @param fileType 文件类型
     * @return 是否支持
     */
    boolean isValidFileType(String fileType);
} 