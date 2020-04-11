package com.filestone.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.filestone.service.FileinfoService;
import com.filestone.service.repository.FileInfoRepository;
import com.filestone.entity.FileInfo;

/**
 * A Service-Implementation class  for the {@link FileInfo} DAO-Layer service
 * @author Hoffman
 *
 */
@Service
@Component
public class FileInfoServiceImpl implements FileinfoService {

	/**
	 * Spring Dependency Injection
	 */
	@Autowired
	private FileInfoRepository fileStub;

	/**
	 * CRUD methods for the {@link FileInfo} entity
	 */
	@Override
	public FileInfo createFileinfo(FileInfo fileinfo) {
		return  fileStub.save(fileinfo);
	}

	@Override
	public List<FileInfo> getAllFileInfoByUserId(UUID userId) {
		return fileStub.getAllFileInfoByUserId(userId, new PageRequest(0, 120));
	}

	@Override
	public void deleteFileInfoById(UUID fileInfoId) {
		List<FileInfo> temp = fileStub.getFileInfoByFIleInfoId(fileInfoId);
		fileStub.delete(temp.get(0));
	}

	@Override
	public List<FileInfo> getFileInfoByFIleInfoId(UUID fileInfoId) {
		return fileStub.getFileInfoByFIleInfoId(fileInfoId);
	}

	@Override
	public long getRowCountByUserId(UUID userId) {
		return fileStub.getRowCountByUserId(userId);
	}

	@Override
	public List<Double> getFilesSizeOnDisk(UUID userId) {
		return fileStub.getFilesSizeOnDisk(userId);
	}

}
