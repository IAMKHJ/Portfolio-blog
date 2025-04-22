package com.portfolio.blog.repository.file;

import com.portfolio.blog.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
