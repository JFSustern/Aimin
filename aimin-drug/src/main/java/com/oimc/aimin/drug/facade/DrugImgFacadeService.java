package com.oimc.aimin.drug.facade;

import com.aliyuncs.exceptions.ClientException;
import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.base.response.FileUploadResult;
import com.oimc.aimin.base.response.Result;
import com.oimc.aimin.drug.model.vo.DrugImgVO;
import com.oimc.aimin.drug.service.DrugImgService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import com.oimc.aimin.file.base.Constants;
import com.oimc.aimin.file.base.FileService;
import com.oimc.aimin.file.oss.OssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 *
 * @author 渣哥
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DrugImgFacadeService {

    private final FileService fileService;

    private final ObjectConvertor objectConvertor;

    private final DrugImgService drugImgService;

    private final RedisTemplate<String, Object> redisTemplate;



    public Result<?> uploadDrugImg(List<MultipartFile> files) {
        List<FileUploadResult> fileUploadResults = fileService.batchUploadImages(files, Constants.DRUG_FILE_MODULE);
        if (fileUploadResults.isEmpty()) {
            return Result.error("图片上传失败");
        }
        List<DrugImg> list = objectConvertor.toDrugImg(fileUploadResults);
        for(DrugImg drugImg : list) {
            cacheImgUploadResult(drugImg.getPath(), drugImg);
        }
        List<DrugImgVO> drugImgVOS = objectConvertor.toVO(list);
        drugImgVOS.forEach(img -> {
            img.setExternalPath(fileService.externalPath(img.getPath()));
        });

        return Result.success(drugImgVOS);
    }

    /**
     * 删除药品图片
     * 
     * @param imageId 图片ID
     * @return 删除结果，true表示成功，false表示失败
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDrugImage(Integer imageId) throws ClientException {

        // 获取图片信息
        DrugImg drugImg = drugImgService.cacheGetById(imageId);
        if (drugImg == null) {
            return false;
        }
            

        // 删除OSS中的文件
        boolean ossDeleted = fileService.delete(drugImg.getPath());
        if (ossDeleted) {
            drugImgService.cacheDelete(imageId);
            return true;
        }
        return false;
    }

    public void cacheImgUploadResult(String path, DrugImg drugImg) {
        redisTemplate.opsForValue().set(path, drugImg,15, TimeUnit.MINUTES);
    }

    public DrugImg getCacheImgUploadResult(String path) {
        Object o = redisTemplate.opsForValue().get(path);
        if (o != null) {
            return (DrugImg) o;
        }
        return null;
    }

    public void clearCacheImgUploadResult(String path) {
        redisTemplate.delete(path);
    }

}
