package com.shuogesha.copyFast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils; 

/**
 * 模块生成器 
 */
public class ModuleGenerator { 
	public static final String SPT = File.separator;

	public static final String ENCODING = "UTF-8";

	private Properties prop = new Properties();
	
	private String packName;
	private String fileName;
	private File beanFile;
	private File daoImplFile;
	private File mapperImplFile;
	private File daoFile;
	private File serviceFile;
	private File serviceImplFile;
	private File actionFile; 

	private File beanTpl;
	private File daoImplTpl;
	private File mapperImplTpl;
	private File daoTpl;
	private File serviceTpl;
	private File serviceImplTpl;
	private File actionTpl; 

	public ModuleGenerator(String packName, String fileName) {
		this.packName = packName;
		this.fileName = fileName;
	}

	@SuppressWarnings("unchecked")
	private void loadProperties() {
		try {
			System.out.println("packName=" + packName);
			System.out.println("fileName=" + fileName);
			FileInputStream fileInput = new FileInputStream(getFilePath(
					packName, fileName));
			prop.load(fileInput);
			String entityUp = prop.getProperty("Entity");
			System.out.println("entityUp:" + entityUp);
			if (entityUp == null || entityUp.trim().equals("")) {
				System.out.println("Entity not specified, exit!");
				return;
			}
			String entityLow = entityUp.substring(0, 1).toLowerCase()
					+ entityUp.substring(1);
			System.out.println("entityLow:" + entityLow);
			prop.put("entity", entityLow);
			if (true) {
				Set ps = prop.keySet();
				for (Object o : ps) {
					System.out.println(o + "=" + prop.get(o));
				}
			}
			String config_table = prop.getProperty("config_table");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareFile() {
		String entityFilePath = getFilePath(prop.getProperty("entity_p"),
				prop.getProperty("Entity") + ".java");
		beanFile = new File(entityFilePath);
		System.out.println("beanFile:" + beanFile.getAbsolutePath()); 
//		String mapperPath = "src/resources/mapper/"
//				+ prop.getProperty("Entity") + "Mapper.xml"; 
//		mapperImplFile = new File(mapperPath);
//		System.out.println("mapperImplFile:" + mapperImplFile.getAbsolutePath());
		
 		
		String daoFilePath = getFilePath(prop.getProperty("dao_p"), prop
				.getProperty("Entity")
				+ "Dao.java");
		daoFile = new File(daoFilePath);
		System.out.println("daoFile:" + daoFile.getAbsolutePath()); 
		String serviceFilePath = getFilePath(prop.getProperty("service_p"),
				prop.getProperty("Entity") + "Service.java");
		serviceFile = new File(serviceFilePath);
		System.out.println("serviceFile:" + serviceFile.getAbsolutePath());

		String serviceImplFilePath = getFilePath(prop
				.getProperty("service_impl_p"), prop.getProperty("Entity")
				+ "ServiceImpl.java");
		serviceImplFile = new File(serviceImplFilePath);
		System.out.println("serviceImplFile:" + serviceImplFile.getAbsolutePath());
 		if ("true".equals(prop.getProperty("is_action"))) {
			String actionFilePath = getFilePath(prop.getProperty("action_p"), prop
					.getProperty("Entity")
					+ "Act.java");
			actionFile = new File(actionFilePath);
			System.out.println("actionFile:" + actionFile.getAbsolutePath());
		} 
	}

	private void prepareTemplate() {
		String tplPack = prop.getProperty("template_dir");
		System.out.println("tplPack:" + tplPack);
		beanTpl = new File(getFilePath(tplPack, "entity.txt"));
		daoImplTpl = new File(getFilePath(tplPack, "dao_impl.txt"));
		mapperImplTpl = new File(getFilePath(tplPack, "mapper.txt"));
		daoTpl = new File(getFilePath(tplPack, "dao.txt"));
		serviceImplTpl = new File(getFilePath(tplPack, "service_impl.txt"));
		serviceTpl = new File(getFilePath(tplPack, "service.txt"));
		actionTpl = new File(getFilePath(tplPack, "action.txt")); 
	}

	private static void stringToFile(File file, String s) throws IOException {
		FileUtils.writeStringToFile(file, s, ENCODING);
	}

	private void writeFile() {
		try {
			stringToFile(beanFile, readTpl(beanTpl));
			
			if ("true".equals(prop.getProperty("is_dao"))) {
				//stringToFile(daoImplFile, readTpl(daoImplTpl));
				stringToFile(mapperImplFile, readTpl(mapperImplTpl));
				stringToFile(daoFile, readTpl(daoTpl));
			}
			if ("true".equals(prop.getProperty("is_service"))) {
				stringToFile(serviceImplFile, readTpl(serviceImplTpl));
				stringToFile(serviceFile, readTpl(serviceTpl));
			}
			if ("true".equals(prop.getProperty("is_action"))) {
				stringToFile(actionFile, readTpl(actionTpl));
			} 
		} catch (IOException e) {
			System.out.println("write file faild! " + e.getMessage());
		}
	}

	private String readTpl(File tpl) {
		String content = null;
		try {
			content = FileUtils.readFileToString(tpl, ENCODING);
			Set<Object> ps = prop.keySet();
			for (Object o : ps) {
				String key = (String) o;
				String value = prop.getProperty(key);
				content = content.replaceAll("\\#\\{" + key + "\\}", value);
			}
		} catch (IOException e) {
			System.out.println("read file faild. " + e.getMessage());
		}
		return content;

	}

	private String getFilePath(String packageName, String name) {
		System.out.println("replace:" + packageName);
		String path = packageName.replaceAll("\\.", "/");
		System.out.println("after relpace:" + path);
		return "src/" + path + "/" + name;
	}

	public void generate() {
		loadProperties();
		prepareFile();
		prepareTemplate();
		writeFile();
	}

	public static void main(String[] args) {
		String packName = "com.shuogesha.copyFast";
		String fileName = "shuogesha.properties";
		new ModuleGenerator(packName, fileName).generate();
	}
}
