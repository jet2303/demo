package com.daily.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daily.demo.entity.daily.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

}
