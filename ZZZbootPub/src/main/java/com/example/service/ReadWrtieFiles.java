package com.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.dao.ForOtherDAO;

import com.example.model.Basket;
import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.LoadGoods;
import com.example.model.LoadGroups;
import com.example.model.Order;
import com.example.model.Picturesgoods;
import com.example.model.Price;

@Service
public class ReadWrtieFiles  {

	@Value("${path.xmlschema}")
	private String XMLSCHEMAS_LOCATION;
	
	@Autowired
	ForOtherDAO dao;
	
	public enum TypeSprav {GROUPS,GOODS};
	
	
	public int FactoryFileToDatabase(String path, TypeSprav type)
	{
		int ret = 0;
		File file = new File(path);
		String[] sp = file.getName().split("\\.");
		String extension = sp[sp.length - 1];
		
		if (extension.equals("xlsx"))
		{
			if (type==TypeSprav.GROUPS)  ret = ReadExcelGroups(file);
			if (type==TypeSprav.GOODS)   ret = ReadExcelGoods(file);
		}
		if (extension.equals("csv"))
		{
			if (type==TypeSprav.GROUPS)  ret = ReadCSVGroups(file);
			if (type==TypeSprav.GOODS)   ret = ReadCSVGoods(file);
		}
		if (extension.equals("xml"))
		{
			if (type==TypeSprav.GROUPS)  ret = ReadXMLGroupsDOM(file);
			if (type==TypeSprav.GOODS)   ret = ReadXMLGoodsDOM(file);
		}
		
	return ret;
	}
	
	
	
	
	// ������ �� ������ ������ � ���������� � ���� (2 ������ �����)
    public int ReadExcelGroups(File filetmp) {

    	List<LoadGroups> tempgroup = new ArrayList<LoadGroups>();
    	
    	FileInputStream file;
    	
			try {
				file = new FileInputStream(filetmp);
				
				Workbook workbook = new XSSFWorkbook(file);
				
				Sheet sheet = workbook.getSheetAt(0);
				
				int i = 0;
				Cell c;
				while (i <= sheet.getLastRowNum()) {
					LoadGroups group = new LoadGroups();
					Row row =  sheet.getRow(i++);

			        c = row.getCell(0,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c!=null) 	group.name = c.toString();
					c = row.getCell(1,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c!=null) 	group.parent = c.toString();

					if (group.name!=null) tempgroup.add(group);
				}			
				workbook.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
	return 	GroupsToDatabase(tempgroup);	
    }
			
       
    // ������ �� ������ ������ � ���������� �� � ���� �� �������
    public int ReadExcelGoods(File filetmp) {

    	List<LoadGoods> tempgood = new ArrayList<LoadGoods>();
    	
    	FileInputStream file;

			try {
				file = new FileInputStream(filetmp);
				
				Workbook workbook = new XSSFWorkbook(file);
				
				Sheet sheet = workbook.getSheetAt(0);
				
				int i = 0;
				Cell c;
				while (i <= sheet.getLastRowNum()) {
					LoadGoods good = new LoadGoods();
					Row row =  sheet.getRow(i++);
					
					c = row.getCell(0,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c!=null) 
					{good.parent = c.toString();}
					else
					{good.parent ="";}	
					c = row.getCell(1,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c!=null) 	good.name = c.toString();
					c = row.getCell(2,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c!=null) 	good.description = c.toString();
					c = row.getCell(3,MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (c!=null) 
					{good.price = c.toString();}
					else
					{good.price="0";}	
					
					if (good.name!=null ) tempgood.add(good);
				}			
				workbook.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return GoodsToDatabase(tempgood);
    }	
		
    
 
	// ������ �� CSV ������ � ���������� � ���� (2 ������ �����)
    public int ReadCSVGroups(File filetmp)  {

    	List<LoadGroups> tempgroup = new ArrayList<LoadGroups>();
    	
    	ICsvBeanReader csvBeanReader;
		try {
			InputStreamReader inputfile = new InputStreamReader(new FileInputStream(filetmp), "windows-1251");
			csvBeanReader = new CsvBeanReader(inputfile, CsvPreference.STANDARD_PREFERENCE);

		// ��������� ��� ����� ������
        String[] mapping = new String[]{ "name", "parent"};
 
        // �������� �����������
        CellProcessor[] procs =  new CellProcessor[]{
                new NotNull(), // name �� ������ ���� null
                new Optional() // ���������, ��� ��� �������������� ���� role
        };
        
        
        LoadGroups tmp;
        // ������� ���� csv ������ �� �����
        while ((tmp = csvBeanReader.read(LoadGroups.class, mapping, procs)) != null) {
        	tempgroup.add(tmp);
        }

        csvBeanReader.close();
		} catch (IllegalArgumentException e) {
			tempgroup.clear();
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 		
	return 	GroupsToDatabase(tempgroup);	
    }
	
 
 // ������ �� CSV ������ � ���������� � ���� (2 ������ �����)
    public int ReadCSVGoods(File filetmp)  {

    	List<LoadGoods> tempgood = new ArrayList<LoadGoods>();   	
    	ICsvBeanReader csvBeanReader;
		try {
			
	    	InputStreamReader inputfile = new InputStreamReader(new FileInputStream(filetmp), "windows-1251");
			csvBeanReader = new CsvBeanReader(inputfile, CsvPreference.STANDARD_PREFERENCE);

		// ��������� ��� ����� ������
        String[] mapping = new String[]{"parent", "name", "description", "price"};
 
        // �������� �����������
        CellProcessor[] procs =  new CellProcessor[]{
                new Optional(), // name �� ������ ���� null
                new NotNull(), // ���������, ��� ��� �������������� ���� role
                new Optional(), // name �� ������ ���� null
                new Optional() // ���������, ��� ��� �������������� ���� role
        };
        
        
        
        LoadGoods tmp;
        // ������� ���� csv ������ �� �����
        while ((tmp = csvBeanReader.read(LoadGoods.class, mapping, procs)) != null) {
        	tempgood.add(tmp);
        }

        csvBeanReader.close();
		} catch (IllegalArgumentException e) {
			tempgood.clear();
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 		
	return 	GoodsToDatabase(tempgood);	
    }
    
    
    
 // ������ �� XML (DOM) ������ � ���������� � ���� (2 ������ �����)
    public int ReadXMLGroupsDOM(File filetmp)  {

    	if (!validateXMLSchema(XMLSCHEMAS_LOCATION+"groups.xsd",filetmp.getAbsolutePath())) return 0;
    	
    	List<LoadGroups> tempgroup = new ArrayList<LoadGroups>();
    	

    	try {
    	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	    Document doc = dBuilder.parse(filetmp);
    	    doc.getDocumentElement().normalize();

    	    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    	    NodeList nList = doc.getElementsByTagName("group");
    	    System.out.println("----------------------------");

    	    for (int temp = 0; temp < nList.getLength(); temp++) {
    	        Node nNode = nList.item(temp);
    	        System.out.println("\nCurrent Element :" + nNode.getNodeName());
    	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    	            Element eElement = (Element) nNode;
    	            
    	            String name="";
    	            if (eElement.getElementsByTagName("name").getLength()>0)   	name = eElement.getElementsByTagName("name").item(0).getTextContent();
    	            
    	            String parent="";
    	            if (eElement.getElementsByTagName("parent").getLength()>0) 	parent = eElement.getElementsByTagName("parent").item(0).getTextContent();
    	            
    	            LoadGroups newgr = new LoadGroups();
    	            newgr.setName(name);
    	            newgr.setParent(parent);
    	            tempgroup.add(newgr);
    	            
    	        }
    	    }
    	    } catch (Exception e) {
    	    e.printStackTrace();
    	    }
    	
    	 		
	return 	GroupsToDatabase(tempgroup);	
    }
    
    
 // ������ �� XML (DOM) ������ � ���������� � ���� 
    public int ReadXMLGoodsDOM(File filetmp)  {

    	if (!validateXMLSchema(XMLSCHEMAS_LOCATION+"goods.xsd",filetmp.getAbsolutePath())) return 0;
    	
    	List<LoadGoods> tempgoods = new ArrayList<LoadGoods>();
 
    	try {
    	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	    Document doc = dBuilder.parse(filetmp);
    	    doc.getDocumentElement().normalize();

    	    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    	    NodeList nList = doc.getElementsByTagName("good");
    	    System.out.println("----------------------------");

    	    for (int temp = 0; temp < nList.getLength(); temp++) {
    	        Node nNode = nList.item(temp);

    	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    	            Element eElement = (Element) nNode;

    	            String parent="";
    	            if (eElement.getElementsByTagName("parent").getLength()>0) 	parent = eElement.getElementsByTagName("parent").item(0).getTextContent();
    	            
    	            String name="";
    	            if (eElement.getElementsByTagName("name").getLength()>0)   	name = eElement.getElementsByTagName("name").item(0).getTextContent();
    	            
    	            String description="";
    	            if (eElement.getElementsByTagName("description").getLength()>0)   description = eElement.getElementsByTagName("description").item(0).getTextContent();
    	            
    	            String price="";
    	            if (eElement.getElementsByTagName("price").getLength()>0)   price = eElement.getElementsByTagName("price").item(0).getTextContent();
    	            
    	            
    	            
    	            LoadGoods newgr = new LoadGoods();
    	            newgr.setParent(parent);    	            
    	            newgr.setName(name);
    	            newgr.setDescription(description);
    	            newgr.setPrice(price); 
    	            tempgoods.add(newgr);
    	            
    	        }
    	    }
    	    } catch (Exception e) {
    	    e.printStackTrace();
    	    }
    	
    	 		
	return 	GoodsToDatabase(tempgoods);	
    }   
    
     public static boolean validateXMLSchema(String xsdPath, String xmlPath){
        
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
    
    
    public int GroupsToDatabase(List<LoadGroups> tempgroup) {	
		// �������� ��������� ������ �� ����� � ������ ������� ������ 1 ������
		List<Groupsgoods> groups =  dao.GetGroupsAll(); 
    	int Created = 0;
		for (LoadGroups t: tempgroup)
		{
			if (groups.stream().filter(a->a.getName().equalsIgnoreCase(t.name.trim())).findFirst().isPresent()==false && t.parent==null) 
			{
				Groupsgoods g = new Groupsgoods();
				g.setName(t.name);
				dao.addGroup(g);
				Created++;
			}
		}
		

		// ���� ������ ������� ������
		groups =  dao.GetGroupsAll(); 
		List<Groupsgoods> groupitem;
		for (LoadGroups t: tempgroup)
		{
			if (groups.stream().filter(a->a.getName().equalsIgnoreCase(t.name.trim())).findFirst().isPresent()==false && t.parent!=null) 
			{
				groupitem = groups.stream().filter(a->a.getName().equalsIgnoreCase(t.parent.trim())).collect(Collectors.toList()); ;
				if (groupitem.size()>0)
				{
					Groupsgoods g = new Groupsgoods();
					g.setName(t.name);
					g.setParent(groupitem.get(0));
					dao.addGroup(g);
					Created++;
				}
			}
		}			
		
		
	return Created;
}
    
	public int GoodsToDatabase(List<LoadGoods> tempgood) {			

			// ���� ������ � ������. ���� ����� �� ������, � ������ ������� �� �������
			List<Goods> goods =  dao.GetGoods(); 
			List<Goods> goodsitem;
			List<Groupsgoods> groups =  dao.GetGroupsAll(); 
			List<Groupsgoods> groupitem;
	    	int Created = 0;
			
			for (LoadGoods t: tempgood)
			{
				if (goods.stream().filter(a->a.getName().equalsIgnoreCase(t.name.trim())).findFirst().isPresent()==false) 
				{    // ��� ���������� ����� ������ ������� ������, �� ���� �������� ������ ���� �����������
					groupitem = groups.stream().filter(a->(a.getName().equalsIgnoreCase(t.parent.trim()) && a.getParent()!=null)  ).collect(Collectors.toList()); ;
					if (groupitem.size()>0)
					{
						Goods g = new Goods();
						g.setName(t.name);
						g.setGroup_id(groupitem.get(0).getId());
						g.setDescription(t.description);
						dao.addGood(g);
						Created++;
						
						Price price = new Price();
						price.setId_good(g.getId());
						price.setId_type(1);
						price.setPrice(Double.valueOf(t.price));
						dao.addPrice(price);
					}
				}
			}			
			
			
    	return Created;
	}

    
    // ������ ��� �������� � CSV ����
	public List<LoadGroups> GetGroupsTemp() {
		List<Groupsgoods> l = dao.GetGroupsAll();
		List<LoadGroups> tmp = new ArrayList<LoadGroups>();
		LoadGroups group;
		for(Groupsgoods g: l)
		{
			group = new LoadGroups();
			group.name = g.getName();
			group.parent = g.getParent()==null?"":g.getParent().getName();
			tmp.add(group);
		}	
			
		return tmp;
	}
	
	//  ������ ��� �������� �  ����
		public List<LoadGoods> GetGoodsTemp() {
			List<Goods> goods = dao.GetGoods();
			List<LoadGoods> tmp = new ArrayList<LoadGoods>();
			LoadGoods tmpgood;
			for(Goods g: goods)
			{
				tmpgood = new LoadGoods();
				if (g.getGroup_id()>0) tmpgood.parent = dao.GetGroup(g.getGroup_id()).getName();
					tmpgood.setName(g.getName());
					tmpgood.setDescription(g.getDescription()!=null ? g.getDescription() : "");
					
					Double price=0.0;
					List<Price> l = dao.GetPrice(1, g.getId());
					if (l.size()>0) price = l.get(0).getPrice();
					tmpgood.setPrice(Double.toString(price));
	                
					tmp.add(tmpgood);
			     }	
				
			return tmp;
		}
	
		
	
	
	

		
	
	
}
