package com.way.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;

public class WebGenerator {
	
	public static void main(String [] args){
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        InputStream configFile = null;
		try {
			configFile = WebGenerator.class.getClassLoader().getResourceAsStream("generatorConfig.xml");
			config = cp.parseConfiguration(configFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (XMLParserException e1) {
			e1.printStackTrace();
		}
            
        ProjectShellCallback shellCallback = new ProjectShellCallback(true);
        
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null);
        } catch (Exception e) {
        	e.printStackTrace();
            //assertEquals(2, e.getErrors().size());
        }
        
        //获取metadata
        Metadata metadata = new MetadataGenerator().parseConfigXmlFile(configFile);
        
        //修改mapper.xml文件
        
	}
}
