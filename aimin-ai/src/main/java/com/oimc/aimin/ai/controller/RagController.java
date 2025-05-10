package com.oimc.aimin.ai.controller;

import com.aliyuncs.exceptions.ClientException;
import com.oimc.aimin.ai.service.RagService;
import com.oimc.aimin.base.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/*
 *
 * @author 渣哥
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rag")
public class RagController {
    
    private final VectorStore vectorStore;
    private final RagService ragService;

    /**
     * 上传并读取文件内容，保存到向量存储中
     * @param file 上传的文件（支持pdf/word/txt）
     * @return 处理结果
     */
    @PostMapping("/upload")
    public Result<String> uploadAndLoadData(@RequestParam("file") MultipartFile file) throws IOException, ClientException {
        boolean success = ragService.uploadAndLoadData(file);
        return success ? Result.success() : Result.error("处理失败");
    }

    /**
     * 测试接口，用于测试向量存储是否正常工作，之后删除
     * @param query 查询关键字
     */
    @GetMapping("/search")
    public void search(@RequestParam String query) {
        // 调用向量存储进行搜索
        List<Document> documents = vectorStore.similaritySearch(query);
        assert documents != null;
        System.out.println("搜索到文档数量:" +documents.size());
        for(Document document : documents) {
            System.out.println(document.getText());
        }
    }
}


