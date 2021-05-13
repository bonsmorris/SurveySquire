package com.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.claim.entity.Point;
import com.claim.entity.UploadFiles;

public interface PointRepository extends JpaRepository<Point, Long> {

	int deleteByFile(UploadFiles file);
	List<Point> findAllByFile(UploadFiles file);
}
