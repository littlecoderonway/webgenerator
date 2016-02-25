package com.way.generator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库信息，以及对应实体bean信息
 * @author lufee.liu
 *
 */
public class Metadata {
	private String className;
	private String tableName;
	
	private String targetProject;
	
	private List<Field> fieldList;
	
	private FieldColumn idColumn;
	private List<FieldColumn> fcList = new ArrayList<FieldColumn>();
	
	private String mapperFile;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Field> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}
	public String getTargetProject() {
		return targetProject;
	}
	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
	public FieldColumn getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(FieldColumn idColumn) {
		this.idColumn = idColumn;
	}
	public List<FieldColumn> getFcList() {
		return fcList;
	}
	public void setFcList(List<FieldColumn> fcList) {
		this.fcList = fcList;
	}
	
	public void addColumn(FieldColumn column){
		fcList.add(column);
	}
	public String getMapperFile() {
		return mapperFile;
	}
	public void setMapperFile(String mapperFile) {
		this.mapperFile = mapperFile;
	}
	
}
