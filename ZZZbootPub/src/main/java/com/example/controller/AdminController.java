package com.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.model.FileGoods;
import com.example.model.FileGoodsValidator;
import com.example.model.FileGroups;
import com.example.model.FileGroupsValidator;
import com.example.model.Goods;
import com.example.model.Groupsgoods;
import com.example.model.GroupsgoodsEdit;
import com.example.model.LoadGoods;
import com.example.model.LoadGroups;
import com.example.model.Picturesgoods;
import com.example.model.User;
import com.example.service.ForOtherSrv;
import com.example.service.ReadWrtieFiles;
import com.example.service.ReadWrtieFiles.TypeSprav;
import com.example.service.UserService;

@Controller
@PropertySource("classpath:application.properties")
public class AdminController {

	@Autowired
	ForOtherSrv srv;
	@Autowired
	ReadWrtieFiles srvfile;
	@Autowired
	FileGroupsValidator fileGroupsValidator;
	@Autowired
	FileGoodsValidator fileGoodsValidator;
	@Autowired
	UserService userService;
	@Autowired
	private User CurrentUser;

	@Value("${path.temp}")
	private String UPLOAD_LOCATION;
	
	@Value("${path.pictures.short}")
	private String IMAGE_LOCATION; 

	
	
	@InitBinder("fileGroups")
	protected void initBinderFileGroups(WebDataBinder binder) {
		binder.setValidator(fileGroupsValidator);
	}

	@InitBinder("files")
	protected void initBinderFileGoods(WebDataBinder binder) {
		binder.setValidator(fileGoodsValidator);
	}

	
	// @RequestMapping(value = "/", method = RequestMethod.GET)
	// public String start(Model model){
	//
	//
	//
	// return "redirect:/admingoods/1";
	// }

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/admingoods/{id}", method = RequestMethod.GET)
	public String listAllGoodsbyGroup(@PathVariable("id") int id, Model model) {
		// ��������� ���������� ������� ��������
		model.addAttribute("currentuser", CurrentUser);
		List<Groupsgoods> groups = srv.GetGroups();
		model.addAttribute("groups", groups);
		List<Goods> goods = srv.GetGoodsbyGroup(id);
		model.addAttribute("goods", goods);
		model.addAttribute("currentgroup", id);
		GroupsgoodsEdit ed = new GroupsgoodsEdit();
		ed.setGrlev1(srv.GetGroups());
		model.addAttribute("edit_group", ed);
		///

		return "Admin/GoodsInGroupAdmin";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/SaveGroup", method = RequestMethod.POST)
	public String SaveGroup(@ModelAttribute(name = "edit_group") GroupsgoodsEdit g, Model model) {

		if (g.isNewgood()) {
			Groupsgoods parent = null;
			if (g.getNewparent() != 0) {
				parent = srv.GetGroup(g.getNewparent());
			}

			Groupsgoods newgr = new Groupsgoods();
			newgr.setName(g.getNew_name());
			newgr.setParent(parent);
			srv.SaveGroup(newgr);
		}

		if (!g.isNewgood()) {
			if (g.getId_group() != 0) {
				Groupsgoods current = srv.GetGroup(g.getId_group());
				current.setName(g.getNew_name());
				srv.SaveGroup(current);
			}
		}

		return "redirect:/admingoods/1";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/DeleteGroup/{id}", method = RequestMethod.GET)
	public String DeleteGroup(@PathVariable("id") int id, Model model) {
		String error = "";
		if (id != 0) {
			if (srv.ValidatieDeleteGroup(id)) {
				srv.deleteGroup(id);
			} else {
				error = "В группе существует другие группы или товары!";
			}
		}

		// ��������� ���������� ������� ��������
		model.addAttribute("currentuser", CurrentUser);
		List<Groupsgoods> groups = srv.GetGroups();
		model.addAttribute("groups", groups);
		List<Goods> goods = srv.GetGoodsbyGroup(1);
		model.addAttribute("goods", goods);
		model.addAttribute("currentgroup", 1);
		GroupsgoodsEdit ed = new GroupsgoodsEdit();
		ed.setGrlev1(srv.GetGroups());
		ed.setError(error);
		model.addAttribute("edit_group", ed);
		///

		return "Admin/GoodsInGroupAdmin";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/newgood/{id}", method = RequestMethod.GET)
	public String NewGood(@PathVariable("id") int id, Model model) {

		//если группа первого уровня то нельзя
		Groupsgoods gr = srv.GetGroup(id);
		if (gr==null||gr.getParent()==null) return "redirect:/admingoods/" + id;
		
		model.addAttribute("currentuser", CurrentUser);
		List<Groupsgoods> groups = srv.GetGroups();
		model.addAttribute("groups", groups);

		Goods good = new Goods();
		good.setGroup_id(id);

		model.addAttribute("product", good);
		model.addAttribute("saveandback", "0");
		model.addAttribute("edit_group", new GroupsgoodsEdit());

		return "Admin/ProductAdmin";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "admingoods/deletegood", method = RequestMethod.GET)
	public String deleteGood(@RequestParam Integer id_group, @RequestParam Integer id_good,
			HttpServletRequest request) {
		srv.DeleteGood(id_good);
		return "redirect:/admingoods/" + id_group;
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/adminproduct/{id}", method = RequestMethod.GET)
	public String GetProduct(@PathVariable("id") int id, Model model) {

		model.addAttribute("currentuser", CurrentUser);
		List<Groupsgoods> groups = srv.GetGroups();
		model.addAttribute("groups", groups);
		Goods good = srv.GetProduct(id);
		model.addAttribute("product", good);
		model.addAttribute("saveandback", "0");
		model.addAttribute("edit_group", new GroupsgoodsEdit());

		return "Admin/ProductAdmin";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/SaveProduct", method = RequestMethod.POST)
	public String SetProduct(Model model, @Valid @ModelAttribute(name = "product") Goods good, MultipartRequest multipartRequest, BindingResult result,  RedirectAttributes redirectAttributes,
			 @ModelAttribute(name = "saveandback") String saveandback) {
		
		if (result.hasErrors()) {
			List<Groupsgoods> groups = srv.GetGroups();
			model.addAttribute("groups", groups);
			model.addAttribute("saveandback", "0");
			model.addAttribute("product", good);
			model.addAttribute("edit_group", new GroupsgoodsEdit());

			return "Admin/ProductAdmin";
		}

		// ���� ����� ����� �� ����������
		if (good.getId() == 0) {
			good.setPictures(new ArrayList<Picturesgoods>());
			srv.AddOrUpdateGood(good);
		}

		// �������� ����� �� ���� �� ����� ������������
		Goods fullgood = srv.GetProduct(good.getId());
		List<MultipartFile> images = multipartRequest.getFiles("files");

		// ��� ������� ������
		String url = "";
		Picturesgoods s;
		for (MultipartFile file : images) {
			if (file.getSize() == 0)
				break;
			try {
				// ���� ����� ��� ����� ��� �� �������
				File folder = new File(IMAGE_LOCATION + good.getId());
				if (!folder.exists())
					folder.mkdir();

				// ���� ����� ������ � ���� ���������� �� �� ���������
				url = "/" + good.getId() + "/" + file.getOriginalFilename();
				for (Picturesgoods gp : fullgood.getPictures()) {
					if (gp.getUrl().equalsIgnoreCase(url)) {
						url = "yes";
						break;
					}
				}

				if (!url.equals("yes")) {
					s = new Picturesgoods();
					s.setGood(fullgood);
					s.setUrl(url);
					s.setPrimary(false);
					fullgood.getPictures().add(s);

					FileCopyUtils.copy(file.getBytes(),
							new File(IMAGE_LOCATION + good.getId() + "/" + file.getOriginalFilename()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (good.getPictures() != null) {
			// �������� ���������� �� �������� ����
			for (Picturesgoods g : good.getPictures()) {
				if (g.isForDelete()) {
					fullgood.getPictures().removeIf(i -> i.getId() == g.getId());
					File file = new File(IMAGE_LOCATION + g.getUrl());
					file.delete();
				}
			}

			// ��������� primary ����
			for (Picturesgoods g : fullgood.getPictures()) {
				g.setPrimary(false);
				if (g.getId() == good.getId_primPicture())
					g.setPrimary(true);
			}
		}

		// ��������� ��������� �����
		fullgood.setName(good.getName());
		fullgood.setDescription(good.getDescription());
		fullgood.setPrice(good.getPrice());
		srv.AddOrUpdateGood(fullgood);

		// ��������� ����
		srv.SetPrice(good.getId(), good.getPrice());

		if (saveandback.equals("0")) {
			return "redirect:/adminproduct/" + good.getId();
		} else {
			return "redirect:/admingoods/" + good.getGroup_id();
		}

	}

	// ��������� ���������� �������� ��������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/UploadFiles", method = RequestMethod.GET)
	public String getSingleUploadPage(ModelMap model) {

		model.addAttribute("currentuser", CurrentUser);
		FileGroups modelGroups = new FileGroups();
		FileGoods modelGoods = new FileGoods();
		model.addAttribute("fileGroups", modelGroups);
		model.addAttribute("fileGoods", modelGoods);
		return "Admin/LoadtoDatabase";
	}

	// �������� ����� �����
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/GroupsUpload", method = RequestMethod.POST)
	public String GroupsUpload(@Valid FileGroups fileGroups, BindingResult result, ModelMap model) throws IOException {

		model.addAttribute("currentuser", CurrentUser);
		FileGoods modelGoods = new FileGoods();
		model.addAttribute("fileGoods", modelGoods);

		if (result.hasErrors()) {
			System.out.println("validation errors");
			return "Admin/LoadtoDatabase";
		} else {
			System.out.println("Fetching file");

			MultipartFile multipartFile = fileGroups.getFile();
			FileCopyUtils.copy(fileGroups.getFile().getBytes(),
					new File(UPLOAD_LOCATION + fileGroups.getFile().getOriginalFilename()));
			// �������� �������
			int Created = srvfile.FactoryFileToDatabase(UPLOAD_LOCATION + fileGroups.getFile().getOriginalFilename(),
					TypeSprav.GROUPS);

			fileGroups.setConclusion("Создано " + Created + " групп.");

			return "Admin/LoadtoDatabase";
		}
	}

	// �������� ����� �������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/GoodsUpload", method = RequestMethod.POST)
	public String singleFileUpload(@Valid FileGoods fileGoods, BindingResult result, ModelMap model)
			throws IOException {

		model.addAttribute("currentuser", CurrentUser);
		FileGroups modelGroups = new FileGroups();
		model.addAttribute("fileGroups", modelGroups);

		if (result.hasErrors()) {
			System.out.println("validation errors");
			return "Admin/LoadtoDatabase";
		} else {
			System.out.println("Fetching file");

			MultipartFile multipartFile = fileGoods.getFile();
			FileCopyUtils.copy(fileGoods.getFile().getBytes(),
					new File(UPLOAD_LOCATION + fileGoods.getFile().getOriginalFilename()));

			int Created = srvfile.FactoryFileToDatabase(UPLOAD_LOCATION + fileGoods.getFile().getOriginalFilename(),
					TypeSprav.GOODS);
			fileGoods.setConclusion("Создано " + Created + " товаров.");

			return "Admin/LoadtoDatabase";
		}
	}

	// �������� ������� �� ���������� ����� CSV ������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/downloadCSVGroups")
	public void downloadCSVGroups(HttpServletResponse response) throws IOException {

		String csvFileName = "groups.csv";

		response.setContentType("text/csv;charset=cp1251");

		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);

		List<LoadGroups> groups = srvfile.GetGroupsTemp();

		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] header = { "name", "parent" };

		csvWriter.writeHeader(header);

		for (LoadGroups group : groups) {
			csvWriter.write(group, header);
		}

		csvWriter.close();
	}

	// �������� ������� �� ���������� ����� CSV ������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/downloadCSVGoods")
	public void downloadCSVGoods(HttpServletResponse response) throws IOException {

		String csvFileName = "goods.csv";

		response.setContentType("text/csv;charset=cp1251");

		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);

		List<LoadGoods> goods = srvfile.GetGoodsTemp();

		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] header = { "parent", "name", "description", "price" };

		csvWriter.writeHeader(header);

		for (LoadGoods good : goods) {
			csvWriter.write(good, header);
		}

		csvWriter.close();
	}

	// �������� ������� �� ���������� ����� EXCEL ������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/downloadExcelGroups")
	public void downloadExcelGroups(HttpServletResponse response) throws IOException {
		String name = "groups.xlsx";
		String headerValue = "attachment; filename=" + name;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", headerValue);

		// �������� ������ excel ����� � ������
		XSSFWorkbook workbook = new XSSFWorkbook();
		// �������� ����� � ��������� "������ ����"
		XSSFSheet sheet = workbook.createSheet("Группы");
		// ��������� ������ ������-�� �������
		List<LoadGroups> groups = srvfile.GetGroupsTemp();

		// ������� ��� �����
		int rowNum = 0;
		// ��������� ���� �������
		for (LoadGroups dataModel : groups) {
			Row row = sheet.createRow(rowNum);
			rowNum++;
			row.createCell(0).setCellValue(dataModel.getName());
			row.createCell(1).setCellValue(dataModel.getParent());
		}

		workbook.write(response.getOutputStream());
		// ���������� ��������� � ������ Excel �������� � ����
		// try (FileOutputStream out = new FileOutputStream(new
		// File(UPLOAD_LOCATION + "�������� �����.xls"))) {
		// workbook.write(out);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	// �������� ������� �� ���������� ����� EXCEL ������
	@RequestMapping(value = "/downloadExcelGoods")
	public void downloadExcelGoods(HttpServletResponse response) throws IOException {
		String name = "goods.xlsx";
		String headerValue = "attachment; filename=" + name;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", headerValue);

		// �������� ������ excel ����� � ������
		XSSFWorkbook workbook = new XSSFWorkbook();
		// �������� ����� � ��������� "������ ����"
		XSSFSheet sheet = workbook.createSheet("Товары");
		// ��������� ������ ������-�� �������
		List<LoadGoods> goods = srvfile.GetGoodsTemp();

		// ������� ��� �����
		int rowNum = 0;
		// ��������� ���� �������
		for (LoadGoods dataModel : goods) {
			Row row = sheet.createRow(rowNum);
			rowNum++;
			row.createCell(0).setCellValue(dataModel.getParent());
			row.createCell(1).setCellValue(dataModel.getName());
			row.createCell(2).setCellValue(dataModel.getDescription());
			row.createCell(3).setCellValue(dataModel.getPrice());
		}

		workbook.write(response.getOutputStream());

	}

	// �������� ������� �� ���������� ����� XML ������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/downloadXMLGroups")
	public void downloadXMLGroups(HttpServletResponse response) throws IOException {
		String name = "groups.xml";
		String headerValue = "attachment; filename=" + name;
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", headerValue);

		List<LoadGroups> groups = srvfile.GetGroupsTemp();

		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		Document doc = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();

			Element e_root = doc.createElement("groups");
			doc.appendChild(e_root);

			Text text;
			for (LoadGroups group : groups) {
				Element e_gr = doc.createElement("group");
				e_gr.setAttribute("name", group.getName());

				Element field = doc.createElement("name");
				text = doc.createTextNode(group.getName());
				field.appendChild(text);
				e_gr.appendChild(field);

				field = doc.createElement("parent");
				text = doc.createTextNode(group.getParent());
				field.appendChild(text);
				e_gr.appendChild(field);
				e_root.appendChild(e_gr);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		try {
			DOMSource src = new DOMSource(doc);
			Transformer trf = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(response.getOutputStream());
			trf.transform(src, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �������� ������� �� ���������� ����� XML ������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/downloadXMLGoods")
	public void downloadXMLGoods(HttpServletResponse response) throws IOException {
		String name = "goods.xml";
		String headerValue = "attachment; filename=" + name;
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", headerValue);

		List<LoadGoods> goods = srvfile.GetGoodsTemp();

		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		Document doc = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();

			Element e_root = doc.createElement("goods");
			doc.appendChild(e_root);

			Text text;
			Element field;
			for (LoadGoods good : goods) {
				Element e_gr = doc.createElement("good");
				e_gr.setAttribute("name", good.getName());

				field = doc.createElement("parent");
				text = doc.createTextNode(good.getParent());
				field.appendChild(text);
				e_gr.appendChild(field);

				field = doc.createElement("name");
				text = doc.createTextNode(good.getName());
				field.appendChild(text);
				e_gr.appendChild(field);

				field = doc.createElement("description");
				text = doc.createTextNode(good.getDescription());
				field.appendChild(text);
				e_gr.appendChild(field);

				field = doc.createElement("price");
				text = doc.createTextNode(good.getPrice());
				field.appendChild(text);
				e_gr.appendChild(field);

				e_root.appendChild(e_gr);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		try {
			DOMSource src = new DOMSource(doc);
			Transformer trf = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(response.getOutputStream());
			trf.transform(src, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// отправка клиенту на скачивание файла PDF ГРУППЫ
	@RequestMapping(value = "/downloadPDFGroups")
	public void downloadPDFGroups(HttpServletResponse response) throws IOException {
		String name = "groups.pdf";
		String headerValue = "attachment; filename=" + name;
		response.setContentType("application/pdf;charset=cp1251");
		response.setHeader("Content-Disposition", headerValue);

		List<LoadGroups> groups = srvfile.GetGroupsTemp();

		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // подключаем
																														// файл
																														// шрифта,
																														// который
																														// поддерживает
																														// кириллицу
			Font font = new Font(bf);

			PdfPTable table = new PdfPTable(2); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] columnWidths = { 1f, 1f };
			table.setWidths(columnWidths);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Название группы", font));
			// cell1.setBorderColor(BaseColor.BLUE);
			cell1.setPaddingLeft(10);
			cell1.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

			PdfPCell cell2 = new PdfPCell(new Paragraph("Родитель группы", font));
			// cell2.setBorderColor(BaseColor.GREEN);
			cell2.setPaddingLeft(10);
			cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

			table.addCell(cell1);
			table.addCell(cell2);

			for (LoadGroups group : groups) {
				PdfPCell gr_name = new PdfPCell(new Paragraph(group.getName(), font));
				gr_name.setPaddingLeft(10);
				gr_name.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
				gr_name.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

				PdfPCell gr_parent = new PdfPCell(new Paragraph(group.getParent(), font));
				gr_parent.setPaddingLeft(10);
				gr_parent.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
				gr_parent.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

				table.addCell(gr_name);
				table.addCell(gr_parent);
			}

			Font bigfont = new Font(bf, 15);
			Paragraph title = new Paragraph("Группы товаров", bigfont);
			title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);

			document.add(title);
			document.add(table);
			document.close();
			writer.close();

		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// отправка клиенту на скачивание файла PDF ТОВАРЫ
	@RequestMapping(value = "/downloadPDFGoods")
	public void downloadPDFGoods(HttpServletResponse response) throws IOException {
		String name = "goods.pdf";
		String headerValue = "attachment; filename=" + name;
		response.setContentType("application/pdf;charset=cp1251");
		response.setHeader("Content-Disposition", headerValue);

		List<LoadGoods> goods = srvfile.GetGoodsTemp();

		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // подключаем
																														// файл
																														// шрифта,
																														// который
																														// поддерживает
																														// кириллицу
			Font font = new Font(bf);

			PdfPTable table = new PdfPTable(3); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] columnWidths = { 1f, 2f, 1f };
			table.setWidths(columnWidths);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Название группы", font));
			BaseColor colorheader = BaseColor.GREEN;
			cell1.setBackgroundColor(colorheader);
			cell1.setPaddingLeft(10);
			cell1.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

			PdfPCell cell2 = new PdfPCell(new Paragraph("Наименование", font));
			cell2.setBackgroundColor(colorheader);
			cell2.setPaddingLeft(10);
			cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			cell2.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

			PdfPCell cell3 = new PdfPCell(new Paragraph("Цена", font));
			cell3.setBackgroundColor(colorheader);
			cell3.setPaddingLeft(10);
			cell3.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			cell3.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

			PdfPCell cell4 = new PdfPCell(new Paragraph("Описание", font));
			cell4.setBackgroundColor(colorheader);
			cell4.setPaddingLeft(10);
			cell4.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			cell4.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
			cell4.setColspan(3);

			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);
			table.addCell(cell4);

			for (LoadGoods good : goods) {
				PdfPCell gr_parent = new PdfPCell(new Paragraph(good.getParent(), font));
				gr_parent.setPaddingLeft(10);
				gr_parent.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
				gr_parent.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

				PdfPCell gr_name = new PdfPCell(new Paragraph(good.getName(), font));
				gr_name.setPaddingLeft(10);
				gr_name.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
				gr_name.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

				PdfPCell gr_price = new PdfPCell(new Paragraph(good.price, font));
				gr_price.setPaddingLeft(10);
				gr_price.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
				gr_price.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);

				PdfPCell gr_describe = new PdfPCell(new Paragraph(good.description, font));
				gr_describe.setPaddingLeft(10);
				gr_describe.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
				gr_describe.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
				gr_describe.setColspan(3);

				table.addCell(gr_parent);
				table.addCell(gr_name);
				table.addCell(gr_price);
				table.addCell(gr_describe);

			}

			Font bigfont = new Font(bf, 17);
			Paragraph title = new Paragraph("Товары", bigfont);
			title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);

			document.add(title);
			document.add(table);
			document.close();
			writer.close();

		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
