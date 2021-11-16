package com.kh.board.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.google.common.base.Joiner;
import com.kh.board.dao.GenFileDao;
import com.kh.board.dto.GenFile;
import com.kh.board.dto.ResultData;
import com.kh.board.util.Util;

@Service
public class GenFileService {
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	@Autowired
	private GenFileDao genFileDao;

	public ResultData saveMeta(int relId, int fileNo, String originFileName, String fileExtTypeCode,
			String fileExtType2Code, String fileExt, int fileSize, String fileDir) {

		Map<String, Object> param = Util.mapOf("relId", relId, "fileNo", fileNo, "originFileName", originFileName,
				"fileExtTypeCode", fileExtTypeCode, "fileExtType2Code", fileExtType2Code, "fileExt", fileExt,
				"fileSize", fileSize, "fileDir", fileDir);
		genFileDao.saveMeta(param);

		int id = Util.getAsInt(param.get("id"), 0);
		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData save(MultipartFile multipartFile) {
		String fileInputName = multipartFile.getName();
		String[] fileInputNameBits = fileInputName.split("__");

		if (fileInputNameBits[0].equals("file") == false) {
			return new ResultData("F-1", "파라미터 명이 올바르지 않습니다.");
		}

		int fileSize = (int) multipartFile.getSize();

		if (fileSize <= 0) {
			return new ResultData("F-2", "파일이 업로드 되지 않았습니다.");
		}
		int relId = Integer.parseInt(fileInputNameBits[1]);
		int fileNo = Integer.parseInt(fileInputNameBits[2]);
		String originFileName = multipartFile.getOriginalFilename();
		String fileExtTypeCode = Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename());
		String fileExtType2Code = Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename());
		String fileExt = Util.getFileExtFromFileName(multipartFile.getOriginalFilename()).toLowerCase();

		if (fileExt.equals("jpeg")) {
			fileExt = "jpg";
		} else if (fileExt.equals("htm")) {
			fileExt = "html";
		}

		String fileDir = Util.getNowYearMonthDateStr();

		if (relId > 0) {
			GenFile oldGenFile = getGenFile(relId, fileNo);

			if (oldGenFile != null) {
				deleteGenFile(oldGenFile);
			}
		}

		ResultData saveMetaRd = saveMeta(relId, fileNo, originFileName, fileExtTypeCode, fileExtType2Code, fileExt,
				fileSize, fileDir);
		int newGenFileId = (int) saveMetaRd.getBody().get("id");

		// 새 파일이 저장될 폴더(io파일) 객체 생성
		String targetDirPath = genFileDirPath + "/" + fileDir;
		java.io.File targetDir = new java.io.File(targetDirPath);

		// 새 파일이 저장될 폴더가 존재하지 않는다면 생성
		if (targetDir.exists() == false) {
			targetDir.mkdirs();
		}

		String targetFileName = newGenFileId + "." + fileExt;
		String targetFilePath = targetDirPath + "/" + targetFileName;

		// 파일 생성(업로드된 파일을 지정된 경로롤 옮김)
		try {
			multipartFile.transferTo(new File(targetFilePath));
		} catch (IllegalStateException | IOException e) {
			return new ResultData("F-3", "파일저장에 실패하였습니다.");
		}

		return new ResultData("S-1", "파일이 생성되었습니다.", "id", newGenFileId, "fileRealPath", targetFilePath, "fileName",
				targetFileName, "fileInputName", fileInputName);
	}

	public List<GenFile> getGenFiles(int relId) {
		return genFileDao.getGenFiles(relId);
	}

	public GenFile getGenFile(int relId, int fileNo) {
		return genFileDao.getGenFile(relId, fileNo);
	}

	public ResultData saveFiles(Map<String, Object> param, MultipartRequest multipartRequest) {
		// 업로드 시작
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		Map<String, ResultData> filesResultData = new HashMap<>();
		List<Integer> genFileIds = new ArrayList<>();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				ResultData fileResultData = save(multipartFile);
				int genFileId = (int) fileResultData.getBody().get("id");
				genFileIds.add(genFileId);

				filesResultData.put(fileInputName, fileResultData);
			}
		}

		String genFileIdsStr = Joiner.on(",").join(genFileIds);

		// 삭제 시작
		int deleteCount = 0;

		for (String inputName : param.keySet()) {
			String[] inputNameBits = inputName.split("__");

			if (inputNameBits[0].equals("deleteFile")) {
				int relId = Integer.parseInt(inputNameBits[1]);
				int fileNo = Integer.parseInt(inputNameBits[2]);

				GenFile oldGenFile = getGenFile(relId, fileNo);

				if (oldGenFile != null) {
					changeDeleteFileById(oldGenFile);
					deleteCount++;
				}
			}
		}

		return new ResultData("S-1", "파일을 업로드하였습니다.", "filesResultData", filesResultData, "genFileIdsStr",
				genFileIdsStr, "deleteCount", deleteCount);
	}

	public void changeRelId(int id, int relId) {
		genFileDao.changeRelId(id, relId);
	}

	public void changeDeleteGenFilesByRelId(int relId) {
		List<GenFile> genFiles = genFileDao.getGenFiles(relId);

		for (GenFile genFile : genFiles) {
			changeDeleteFileById(genFile);
		}
	}

	public void changeDeleteFileById(GenFile gen) {
		// 삭제 처리
		genFileDao.changeDeleteFileById(gen.getId());
		String fileDir = "deleteFolder";
		// 삭제파일 이동
		moveFile(fileDir, gen.getId()+"."+gen.getFileExt(), genFileDirPath + "/" + Util.getNowYearMonthDateStr(),
				genFileDirPath);
	}
	// 삭제파일 모아둘 폴더
	public void moveFile(String folderName, String fileName, String beforeFilePath, String afterFilePath) {
		String path = afterFilePath + "/" + folderName;
		String filePath = path + "/" + fileName;
		java.io.File dir = new java.io.File(path);
		if (!dir.exists()) {
			// 폴더 없으면 폴더 생성
			dir.mkdirs();
		}
		try {
			File file = new File(beforeFilePath+"/"+fileName);
			// 옮기고자 하는 경로로 파일 이동
			file.renameTo(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteGenFile(GenFile genFile) {
		String filePath = genFile.getFilePath(genFileDirPath);
		Util.deleteFile(filePath);

		genFileDao.deleteFile(genFile.getId());
	}

	public GenFile getGenFile(int id) {
		return genFileDao.getGenFileById(id);
	}

	public Map<Integer, Map<String, GenFile>> getFilesMapKeyRelIdAndFileNo(List<Integer> relIds) {
		List<GenFile> genFiles = genFileDao.getGenFilesRelIds(relIds);
		Map<Integer, Map<String, GenFile>> rs = new LinkedHashMap<>();

		for (GenFile genFile : genFiles) {
			if (rs.containsKey(genFile.getRelId()) == false) {
				rs.put(genFile.getRelId(), new LinkedHashMap<>());
			}

			rs.get(genFile.getRelId()).put(genFile.getFileNo() + "", genFile);
		}

		return rs;
	}

	public void changeInputFileRelIds(Map<String, Object> param, int id) {
		String genFileIdsStr = Util.ifEmpty((String) param.get("genFileIdsStr"), null);

		if (genFileIdsStr != null) {
			List<Integer> genFileIds = Util.getListDividedBy(genFileIdsStr, ",");

			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
			// 그것을 뒤늦게라도 이렇게 고처야 한다.
			for (int genFileId : genFileIds) {
				changeRelId(genFileId, id);
			}
		}
	}

	public void deleteGenFiles(int id) {
		List<GenFile> genFiles = genFileDao.getGenFiles(id);

		for (GenFile genFile : genFiles) {
			deleteGenFile(genFile);
		}
	}

	public void restoreGenFilesByRelId(int relId) {
		List<GenFile> genFiles = genFileDao.getGenFiles(relId);

		for (GenFile gen : genFiles) {
			String fileDir = Util.getNowYearMonthDateStr();
			genFileDao.restoreFileById(gen.getId(), fileDir);
			moveFile(fileDir, gen.getId()+"."+gen.getFileExt(), genFileDirPath + "/" + "deleteFolder",
					genFileDirPath);
		}
	}

	public int getGenFilesTotalCountByDel() {
		return genFileDao.getGenFilesTotalCountByDel();
	}

	public List<GenFile> getForPrintGenFilesByDel(int page, int itemsInAPage) {
		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		List<GenFile> genFiles = genFileDao.getForPrintGenFilesByDel(limitStart, limitTake);
		
		return genFiles;
	}
}
