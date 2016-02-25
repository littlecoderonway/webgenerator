package com.way.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MetadataGenerator {
	
	public Metadata parseConfigXmlFile(InputStream fileName){
		Metadata metadata = new Metadata();
		 try {
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            
	            //解析MBG配置文件
	            Document document = db.parse(fileName);
	            Node modelGenerNode = document.getElementsByTagName("javaModelGenerator").item(0);
	            String targetProject = modelGenerNode.getAttributes().getNamedItem("targetProject").getNodeValue();
	            
	            metadata.setTargetProject(targetProject);
	            
	            Node tableNode = document.getElementsByTagName("table").item(0);
	            NamedNodeMap map = tableNode.getAttributes();
	            String tableName = map.getNamedItem("tableName").getNodeValue();
	            String domainObjectName = map.getNamedItem("domainObjectName").getNodeValue();
	            
	            metadata.setClassName(domainObjectName);
	            metadata.setTableName(tableName);
	            
	            //解析生成mapper.xml文件
	            String filePath = metadata.getTargetProject()+File.separator+Constant.DAO_DOCUMENT+File.separator+metadata.getClassName()+"Mapper.xml";
	            metadata.setMapperFile(filePath);
	            Document mapperDocument = db.parse(new File(filePath));
	            Node mapNode = mapperDocument.getElementsByTagName("resultMap").item(0);
	            NodeList columnList = mapNode.getChildNodes();
	            for (int j = 0; j < columnList.getLength(); j++) {
	            	Node column = columnList.item(j);
	            	NamedNodeMap namedNodeMap = column.getAttributes();
	            	FieldColumn fc = new FieldColumn();
	            	fc.setFieldName(namedNodeMap.getNamedItem("property").getNodeValue());
	            	fc.setType(namedNodeMap.getNamedItem("jdbcType").getNodeValue());
	            	fc.setJavaType(toJavaType(fc.getType()));
	            	if (namedNodeMap.getNamedItem("id")!=null) {
						fc.setColumnName(namedNodeMap.getNamedItem("id").getNodeValue());
						metadata.setIdColumn(fc);
					}else{
						fc.setColumnName(namedNodeMap.getNamedItem("column").getNodeValue());
						metadata.addColumn(fc);
					}
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (ParserConfigurationException e) {
	            e.printStackTrace();
	        } catch (SAXException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 return metadata;
	}

	
	public static void main(String[]args) throws FileNotFoundException{
		MetadataGenerator mataGenerator = new MetadataGenerator();
		mataGenerator.parseConfigXmlFile(new FileInputStream(new File("D:\\mocktrading_trunk\\web-generator\\src\\main\\resources\\generatorConfig.xml")));
	}
	
	private String toJavaType(String jdbcType){
		switch (jdbcType) {
		case "VARCHAR":
			return "String";
		case "INTEGER":
		case "TINYINT":
			return "Integer";
		case "TIMESTAMP":
			return "Date";
		case "DECIMAL":
			return "BigDecimal";
		default:
			return "String";
		}
	}
	
}
