package com.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.claim.entity.UploadFiles;
import com.claim.entity.User;

@Repository
public interface UploadFilesRepository extends JpaRepository<UploadFiles, Long> {

	List<UploadFiles> findByUser(User user);
	
}
