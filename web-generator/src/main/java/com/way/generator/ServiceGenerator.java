package com.way.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ServiceGenerator {
	
	public static void generateDao(Metadata metadata) throws IOException {
		// String serviceInterfaceFileName =
		// metadata.getClassName()+"AdminDao.java";
		String serviceInterfaceFileName = metadata.getTargetProject() + "service"
				+ File.separator + metadata.getClassName() + "Service.java";
		String serviceImplFileName = metadata.getTargetProject() + "service"
				+ File.separator + metadata.getClassName()
				+ "ServiceImpl.java";

		String className = metadata.getClassName();
		String paramClassName = className.substring(0, 1).toLowerCase()+className.substring(1);
		String daoName = className+"Dao";
		
		//service接口文件
		File serviceInterfaceFile = new File(serviceInterfaceFileName);
		if (!serviceInterfaceFile.exists()) {
			serviceInterfaceFile.createNewFile();
		}
		PrintWriter myFile = new PrintWriter(new FileWriter(serviceInterfaceFile));
		//类声明
		myFile.println("public interface "+className+"Service "+"{");
		//方法
		myFile.println("\tpublic int countByCriteria(Map<String, Object> param) ;");
		myFile.println("\tpublic List<"+className+"> listByCriteria(Map<String, Object> param);");
		myFile.println("\tpublic boolean saveOrUpdate("+className+" "+paramClassName+", String admin) ;");
		myFile.println("\tpublic boolean delete"+className+"(int "+paramClassName+"Id) ;");
		myFile.println("}");
		myFile.close();
		
		//service实现文件
		File serviceImplFile = new File(serviceImplFileName);
		if (!serviceImplFile.exists()) {
			serviceImplFile.createNewFile();
		}
		PrintWriter serviceWriter = new PrintWriter(new FileWriter(serviceImplFile));
		serviceWriter.println("@Service");
		//类声明
		serviceWriter.println("public class "+className+"ServiceImpl implements "+className+"Service "+"{");
		//mybatis session注入
		serviceWriter.println("@Autowired");
		serviceWriter.println("private "+className+"AdminDao "+paramClassName+"Dao;");
		
		//方法
		printOverrideMark(serviceWriter);
		serviceWriter.println("	public int countByCriteria(Map<String, Object> param) {return "+daoName+".countByCriteria(param);}");
		
		printOverrideMark(serviceWriter);
		serviceWriter.println("	public List<"+className+"> listByCriteria(Map<String, Object> param) {return "+daoName+".listByCriteria(param);}");
		
		printOverrideMark(serviceWriter);
		serviceWriter.println("	public boolean saveOrUpdate"+className+"("+className+" "+paramClassName+") {if ("+paramClassName+".getId() == null||"+paramClassName+".getId() == 0) {return "+paramClassName+"Dao.save"+className+"("+paramClassName+");}else{return "+paramClassName+"Dao.update"+className+"("+paramClassName+");}}");
		
		printOverrideMark(serviceWriter);
		serviceWriter.println("	public boolean delete"+className+"(int "+paramClassName+"Id) {return "+daoName+".delete"+className+"("+paramClassName+"Id);}");
		
		serviceWriter.println("}");
		serviceWriter.close();
		
		
	}
	
	private static void printOverrideMark(PrintWriter serviceWriter){
		serviceWriter.println("@Override");
	}
}
