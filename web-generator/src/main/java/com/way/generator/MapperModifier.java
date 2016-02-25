package com.way.generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MapperModifier {

	public static void modify(Metadata metaData) throws IOException {

		Properties prop = new Properties();
		InputStream in = MapperModifier.class.getClass().getResourceAsStream(
				"constant.properties");
		prop.load(in);

		String searchCriteriaSql = prop.getProperty("searchCriteriaSql");
		StringBuilder sb = new StringBuilder();
		FieldColumn idColumn = metaData.getIdColumn();
		String idName = idColumn.getColumnName();
		sb.append("<if test=\"").append(idName).append("!=null\">")
				.append(" and ").append(idName).append("=#{")
				.append(idColumn.getFieldName()).append("}").append("</if>");
		for (FieldColumn column : metaData.getFcList()) {
			String columnName = column.getColumnName();
			sb.append("<if test=\"").append(columnName).append("!=null\">")
					.append(" and ").append(columnName).append("=#{")
					.append(column.getFieldName()).append("}").append("</if>");
		}
		searchCriteriaSql = searchCriteriaSql.replace("{condition}", sb);

		String listCriteriaSql = prop.getProperty("listCriteriaSql");
		listCriteriaSql = listCriteriaSql.replace("{modelName}",
				metaData.getClassName());
		listCriteriaSql = listCriteriaSql.replace("{tableName}",
				metaData.getTableName());
		listCriteriaSql = listCriteriaSql.replace("{id}", metaData
				.getIdColumn().getColumnName());

		String countCriteriaSql = prop.getProperty("countCriteriaSql");
		countCriteriaSql = countCriteriaSql.replace("{tableName}",
				metaData.getTableName());

		File mapperFile = new File(metaData.getMapperFile());

		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(mapperFile));
			byte[] buff = new byte[(int) mapperFile.length()];
			bis.read(buff);
			FileOutputStream fos = new FileOutputStream(mapperFile);
			String[] lines = (new String(buff)).split("\n");
			for (String line : lines) {
				if ("</mapper>\n".equals(line)) {
					fos.write((searchCriteriaSql+'\n').getBytes());
					fos.write((listCriteriaSql+'\n').getBytes());
					fos.write((countCriteriaSql+'\n').getBytes());
					fos.write(line.getBytes());
				}else{
					fos.write((line + "\n").getBytes());
				}
			}
			fos.flush();
			fos.close();
			bis.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
