package com.way.generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FrontGenerator {
	@SuppressWarnings("resource")
	public static void GenerateControllorAndJsp(Metadata metadata) throws IOException{
		
		String className = metadata.getClassName();
		String paramClassName = className.substring(0, 1).toLowerCase()+className.substring(1);
		
		String controllorFileName = metadata.getTargetProject() + "web"
				+ File.separator + metadata.getClassName() + "Controllor.java";
		File controllorFile = new File(controllorFileName);
		if (!controllorFile.exists()) {
			controllorFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(controllorFile);
		
		//生成controllor文件，拷贝文件，替换内容
		FrontGenerator.class.getClassLoader();
		File file = new File(ClassLoader.getSystemResource("TemplateController.java").getFile());
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[]buffer = new byte[(int) file.length()];
		reader.read(buffer);
		
		String templateString = new String(buffer);
		templateString.replace("{paramClassName}",paramClassName);
		templateString.replace("{className}",className);
		
		StringBuilder queryParamWithType = new StringBuilder();
		StringBuilder queryParam = new StringBuilder();
		StringBuilder addParam = new StringBuilder();
		
		for(FieldColumn column:metadata.getFcList()){
			queryParamWithType.append(column.getJavaType()+" "+column.getFieldName()+",");
			queryParam.append(column.getFieldName()+",");
			if("String".equals(column.getJavaType())){
				addParam.append("if(").append(column.getFieldName()).append("!=null){param.put(\"").append(column.getFieldName()).append("\",").append(column.getFieldName()).append(");}");
			}
		}
		queryParam.deleteCharAt(queryParam.length());
		templateString.replace("{queryParamsWithType}", queryParamWithType);
		templateString.replace("{queryParams}", queryParam);
		templateString.replace("{addParam}", addParam);
		fos.write(templateString.getBytes());
		fos.close();
		
		
		//生成jsp文件
		String jspFileName = metadata.getTargetProject() + "web"
				+ File.separator + metadata.getClassName() + "List.jsp";
		File jspFile = new File(jspFileName);
		if (!jspFile.exists()) {
			jspFile.createNewFile();
		}
		FileOutputStream fosJsp = new FileOutputStream(jspFile);
		
		File templateFile = new File(ClassLoader.getSystemResource("templateList.jsp").getFile());
		BufferedInputStream jspreader = new BufferedInputStream(new FileInputStream(templateFile));
		buffer = new byte[(int) templateFile.length()];
		jspreader.read(buffer);
		
		String templateJspString = new String(buffer);
		templateJspString.replace("{paramClassName}",paramClassName);
		templateJspString.replace("{className}",className);
		
		StringBuilder tableString = new StringBuilder();
		StringBuilder colListString = new StringBuilder();
		int colNum = 0;
		for(FieldColumn column:metadata.getFcList()){
			String columnName = column.getFieldName();
			String fieldName = column.getFieldName();
			
			colListString.append("{name : '"+columnName+"',index : '"+fieldName+"',width : 90,align : \"center\"}," );
			
			if(colNum == 0)
			{
				tableString.append("<tr>");
			}
			String tdString = "<td><label for=\"columnName\">columnName</label></td><td><input type=\"text\" name=\"columnName\" id=\"columnName\" value=\"\" class=\"text ui-widget-content ui-corner-all\"></td>";
			tdString = tdString.replaceAll("columnName", columnName);
			tableString.append(tdString);
			colNum++;
			if(colNum==2){
				tableString.append("</tr>");
				colNum = 0;
			}
		}
		if(colNum==1){
			tableString.append("</tr>");
		}
		
		templateJspString.replace("{searchTable}",tableString);
		templateJspString.replace("{addTable}",tableString);
		
		templateString.replace("{colList}", queryParamWithType);
		templateString.replace("{colNameList}", colListString);
		fosJsp.write(templateJspString.getBytes());
		fosJsp.close();
	}
}
