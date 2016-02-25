package com.way.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 生成dao以及daoimpl文件
 * 
 * @author lufee.liu
 *
 */
public class DaoGenerator {
	
	private static String MYBATIS_NAMESPACE = "mocktrading.admin.";
	
	public static void generateDao(Metadata metadata) throws IOException {
		// String daoInterfaceFileName =
		// metadata.getClassName()+"AdminDao.java";
		String daoInterfaceFileName = metadata.getTargetProject() + "dao"
				+ File.separator + metadata.getClassName() + "AdminDao.java";
		String daoImplFileName = metadata.getTargetProject() + "dao"
				+ File.separator + metadata.getClassName()
				+ "AdminDaoImpl.java";

		String className = metadata.getClassName();
		String paramClassName = className.substring(0, 1).toLowerCase()+className.substring(1);
		
		//dao接口文件
		File daoInterfaceFile = new File(daoInterfaceFileName);
		if (!daoInterfaceFile.exists()) {
			daoInterfaceFile.createNewFile();
		}
		PrintWriter myFile = new PrintWriter(new FileWriter(daoInterfaceFile));
		//类声明
		myFile.println("public interface "+className+"AdminDao "+"{");
		//方法
		myFile.println("\tpublic int countByCriteria(Map<String, Object> param) ;");
		myFile.println("\tpublic List<"+className+"> listByCriteria(Map<String, Object> param);");
		myFile.println("\tpublic boolean save"+className+"("+className+" "+paramClassName+") ;");
		myFile.println("\tpublic boolean update"+className+"("+className+" "+paramClassName+") ;");
		myFile.println("\tpublic boolean delete"+className+"(int "+paramClassName+"Id) ;");
		myFile.println("}");
		myFile.close();
		
		//dao实现文件
		File daoImplFile = new File(daoImplFileName);
		if (!daoImplFile.exists()) {
			daoImplFile.createNewFile();
		}
		PrintWriter daoWriter = new PrintWriter(new FileWriter(daoImplFile));
		daoWriter.println("@Repository");
		//类声明
		daoWriter.println("public class "+className+"AdminDaoImpl implements "+className+"AdminDao "+"{");
		//mybatis 变量
		daoWriter.println("private static final String ADD = "+MYBATIS_NAMESPACE+paramClassName+".insert");
		daoWriter.println("private static final String UPDATE = "+MYBATIS_NAMESPACE+paramClassName+".update");
		daoWriter.println("private static final String COUNT_BY_CRITERIA = "+MYBATIS_NAMESPACE+paramClassName+".countByCriteria");
		daoWriter.println("private static final String LIST_BY_CRITERIA = "+MYBATIS_NAMESPACE+paramClassName+".listByCriteria");
		daoWriter.println("private static final String DELETE = "+MYBATIS_NAMESPACE+paramClassName+".DELETE");
		//mybatis session注入
		daoWriter.println("@Autowired");
		daoWriter.println("private SqlSessionTemplate sqlSessionTemplate;");
		
		//方法
		printOverrideMark(daoWriter);
		daoWriter.println("	public int countByCriteria(Map<String, Object> param) {return sqlSessionTemplate.selectOne(COUNT_BY_CRITERIA, param);}");
		
		printOverrideMark(daoWriter);
		daoWriter.println("	public List<"+className+"> listByCriteria(Map<String, Object> param) {return sqlSessionTemplate.selectList(LIST_BY_CRITERIA, param);}");
		
		printOverrideMark(daoWriter);
		daoWriter.println("	public boolean save"+className+"("+className+" "+paramClassName+") {return sqlSessionTemplate.insert(ADD, "+paramClassName+")==1;}");
		
		printOverrideMark(daoWriter);
		daoWriter.println("	public boolean update"+className+"("+className+" "+paramClassName+") {return sqlSessionTemplate.update(UPDATE, "+paramClassName+")==1;}");
		
		printOverrideMark(daoWriter);
		daoWriter.println("	public boolean delete"+className+"(int "+paramClassName+"Id) {return sqlSessionTemplate.delete(DELETE, "+paramClassName+"Id)==1;}");
		
		daoWriter.println("}");
		daoWriter.close();
		
		
	}
	
	private static void printOverrideMark(PrintWriter daoWriter){
		daoWriter.println("@Override");
	}
}
