package com.kh.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.board.dto.GenFile;

@Mapper
public interface GenFileDao {
	void saveMeta(Map<String, Object> param);

	GenFile getGenFile(@Param("relId") int relId, @Param("fileNo") int fileNo);

	GenFile getGenFileById(@Param("id") int id);

	void changeRelId(@Param("id") int id, @Param("relId") int relId);

	void deleteFiles(@Param("relId") int relId);

	List<GenFile> getGenFiles(@Param("relId") int relId);

	void deleteFile(@Param("id") int id);

	List<GenFile> getGenFilesRelIds(@Param("relIds") List<Integer> relIds);

	void changeDeleteFileById(@Param("id") int id);

	void restoreFileById(@Param("id") int id,@Param("fileDir") String fileDir);

	int getGenFilesTotalCountByDel();

	List<GenFile> getForPrintGenFilesByDel(@Param("limitStart") int limitStart, @Param("limitTake") int limitTake);

	List<GenFile> getGenFilesByDel();
}
