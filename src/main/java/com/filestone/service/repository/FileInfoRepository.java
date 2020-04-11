package com.filestone.service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.filestone.entity.FileInfo;

/**
 * A Jpa-Repository-Interface for the {@link FileInfo} DAO-Layer service
 * @author Hoffman
 *
 */
@Repository
@RepositoryRestResource(path = "/FileInfo", collectionResourceRel = "FileInfo")
public interface FileInfoRepository extends JpaRepository<FileInfo, Long>{

	@RestResource(exported = false)
	@Query("SELECT f FROM FileInfo AS f WHERE f.userId= :userId")
	public List<FileInfo> getAllFileInfoByUserId(@Param("userId") UUID userId ,Pageable pageable);
	
	
	@RestResource(exported = false)
	@Query("SELECT f FROM FileInfo AS f WHERE f.id= :id")
	public List<FileInfo> getFileInfoByFIleInfoId(@Param("id") UUID id);
	
	
	@RestResource(exported = false)
	@Query("SELECT COUNT(f) FROM FileInfo AS f WHERE f.userId= :userId")
	public long getRowCountByUserId(@Param("userId") UUID userId);
	
	@RestResource(exported = false)
	@Query("SELECT f.size FROM FileInfo AS f WHERE f.userId= :userId")
	public List<Double> getFilesSizeOnDisk(@Param("userId") UUID userId);
		
}



	
	
