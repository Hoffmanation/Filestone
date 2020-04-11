package com.filestone.service;

import java.util.List;
import java.util.UUID;

import com.filestone.entity.FileInfo;

/**
 * An Interface  for the {@link FileInfo} DAO-Layer service
 * @author Hoffman
 *
 */
public interface FileinfoService {

	public FileInfo createFileinfo(FileInfo fileinfo);

	public List<FileInfo> getAllFileInfoByUserId(UUID userId);

	public List<FileInfo> getFileInfoByFIleInfoId(UUID fileInfoId);

	public void deleteFileInfoById(UUID fileInfoId);

	public long getRowCountByUserId(UUID userId);

	public List<Double> getFilesSizeOnDisk(UUID userId);

}
