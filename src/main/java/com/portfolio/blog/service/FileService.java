package com.portfolio.blog.service;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.Post;
import com.portfolio.blog.repository.file.FileRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final EntityManager entityManager;
    public static final String separator = java.io.File.separator;
    public static final String fullPath = System.getProperty("user.dir")
            +separator+"src" + separator + "main" + separator + "resources" + separator + "static"
            +separator+"files"+separator;

    @Transactional
    public void saveWithPost(MultipartFile file, Post post) throws IOException {

        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1); ;
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        log.info("file.getOriginalFilename = {}", file.getOriginalFilename());
        log.info("fullPath = {}", fullPath);
        log.info("type = {}", type);
        log.info("fileName = {}", fileName);

        java.io.File saveFile = new java.io.File(fullPath, fileName);
        file.transferTo(saveFile);

        File newFile = File.builder()
                .fileName(fileName)
                .originFileName(file.getOriginalFilename())
                .filePath("/files/"+fileName)
                .fileType(type)
                .fileSize(file.getSize())
                .post(post)
                .build();

        fileRepository.save(newFile);
    }

    @Transactional
    public void saveWithProfile(MultipartFile file, Member member) throws IOException {

        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1); ;
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        log.info("file.getOriginalFilename = {}", file.getOriginalFilename());
        log.info("fullPath = {}", fullPath);
        log.info("type = {}", type);
        log.info("fileName = {}", fileName);

        java.io.File saveFile = new java.io.File(fullPath, fileName);
        file.transferTo(saveFile);

        File newFile = File.builder()
                .fileName(fileName)
                .originFileName(file.getOriginalFilename())
                .filePath("/files/"+fileName)
                .fileType(type)
                .fileSize(file.getSize())
                .member(member)
                .build();

        fileRepository.save(newFile);
    }

    @Transactional(readOnly = true)
    public File findById(Long id){
        return fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 파일입니다."));
    }

    @Transactional
    public boolean delete(File file){
        entityManager.flush();
        entityManager.clear();

        java.io.File filePath = new java.io.File(fullPath + file.getFileName());

        if(filePath.exists()){
            if(filePath.delete()){
                fileRepository.delete(file); //기존파일정보 DB에서 삭제
                log.info("파일삭제 : 성공 = {}", file.getFileName());
                return true;
            }else {
                log.info("파일삭제 : 실패 = {}", file.getFileName());
                return false;
            }
        }else {
            log.info("파일삭제 : 경로를 찾지못하였습니다. = {}", filePath);
            return false;
        }
    }

}
