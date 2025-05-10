package com.oimc.aimin.ai.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.oimc.aimin.ai.service.RagService;
import com.oimc.aimin.base.exception.BusinessException;
import com.oimc.aimin.file.base.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private final VectorStore vectorStore;
    private final FileService fileService;

    @Override
    public boolean uploadAndLoadData(MultipartFile file) throws IOException, ClientException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        String fileType = getFileExtension(file.getOriginalFilename());
        if (!isValidFileType(fileType)) {
            throw new BusinessException("不支持的文件类型，仅支持PDF/WORD/TXT格式");
        }

        try (InputStream ossInputStream = file.getInputStream()) {
            // 1. 上传文件到OSS
            boolean success = fileService.upload("rag", ossInputStream);
            if (!success) {
                throw new BusinessException("文件上传失败");
            }
        }

        // 重新获取新的输入流用于文档处理
        try (InputStream docInputStream = file.getInputStream()) {
            // 2. 读取文件内容并保存到向量存储
            List<Document> splitDocuments = readAndSplitDocument(docInputStream, fileType);
            if (splitDocuments.isEmpty()) {
                throw new BusinessException("文件内容解析失败");
            }

            // 3. 保存到向量存储
            vectorStore.add(splitDocuments);

            log.info("文件已上传并保存到向量存储中，文件名：{}，切分文档数：{}",
                    file.getOriginalFilename(), splitDocuments.size());
            return true;
        }
    }

    @Override
    public List<Document> readAndSplitDocument(InputStream inputStream, String fileType) throws IOException {
        // 将InputStream转换为可重复读取的字节数组
        byte[] fileBytes = inputStream.readAllBytes();
        List<Document> docs = getDocuments(fileType, fileBytes);
        if (docs.stream().allMatch(doc -> doc.getText().isBlank())) {
            throw new BusinessException("文档内容解析失败或为空");
        }

        // 3. 优化文本分割参数（增加元数据）
        return new TokenTextSplitter(
                1024,    // 块大小：适合大多数embedding模型
                200,     // 重叠长度：保持上下文连贯性
                10,      // 最小块数：保证基本分割
                2000,    // 最大块数：防止内存溢出
                true     // 启用递归分割
        ).apply(docs).stream()
                .peek(doc -> doc.getMetadata().put("file_type", fileType))
                .toList();
    }

    @Override
    public List<Document> getDocuments(String fileType, byte[] fileBytes) {
        Resource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));

        // 1. 修正文档读取器初始化逻辑
        DocumentReader reader = switch (fileType.toLowerCase()) {
            case "pdf" -> new PagePdfDocumentReader(resource);
            case "doc", "docx" -> new TikaDocumentReader(resource);
            case "txt" -> new TextReader(resource);
            default -> throw new BusinessException("不支持的文档类型: " + fileType);
        };

        return reader.get();
    }

    @Override
    public String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    @Override
    public boolean isValidFileType(String fileType) {
        if (fileType == null || fileType.isEmpty()) {
            return false;
        }
        
        String type = fileType.toLowerCase();
        return type.equals("pdf") || type.equals("doc") || type.equals("docx") || type.equals("txt");
    }
} 