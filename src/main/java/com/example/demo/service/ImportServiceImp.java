package com.example.demo.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Transactions;

@Service
public class ImportServiceImp implements ImportService {

	private KieSession kieSession;

	public List getBankListByExcel(InputStream in, String fileName) throws Exception {
		List list = new ArrayList<>();

		Workbook work = this.getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("Create Excel sheet is null！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = (Sheet) work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			for (int j = ((org.apache.poi.ss.usermodel.Sheet) sheet)
					.getFirstRowNum(); j <= ((org.apache.poi.ss.usermodel.Sheet) sheet).getLastRowNum(); j++) {
				row = ((org.apache.poi.ss.usermodel.Sheet) sheet).getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					continue;
				}

				List<Object> li = new ArrayList<>();
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					li.add(cell);
				}
				list.add(li);
			}
		}
		work.close();
		return list;
	}

	public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook workbook = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (".xls".equals(fileType)) {
			workbook = new HSSFWorkbook(inStr);
		} else if (".xlsx".equals(fileType)) {
			workbook = new XSSFWorkbook(inStr);
		} else {
			throw new Exception("please upload excel file!");
		}
		return workbook;
	}

	public void rules() {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("com/example/demo/controller/sample.drl"),
				ResourceType.DRL);

		KnowledgeBuilderErrors knowledgeBuilderErrors = knowledgeBuilder.getErrors();
		if (knowledgeBuilderErrors.size() > 0) {
			for (KnowledgeBuilderError error : knowledgeBuilderErrors) {
				System.out.println(error);
			}

			return;
		}

		KieBase kieBase = knowledgeBuilder.newKieBase();
		kieSession = kieBase.newKieSession();

		kieSession.addEventListener(new EventListener());
		System.out.println(
				" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^ controller.drl  was read - ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^ ");

		kieSession.setGlobal("me", this);
		kieSession.setGlobal("arrOfCheckTrans", new ArrayList());
		System.out.println(
				" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^   drl was read - ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^ ");
		
		List<Transactions> lists = initData();
		if ( lists != null){

			for (Iterator<Transactions> i = lists.iterator(); i.hasNext();)
			{
				Transactions tran = i.next();
				FactHandle factHandle = kieSession.getFactHandle(tran);
		        if (factHandle == null) {
		            kieSession.insert(tran);
		        } else  {
		            kieSession.update(factHandle, tran);
		        }
			}
		}
		
		kieSession.fireAllRules();

	}

	public class EventListener implements RuleRuntimeEventListener {
		public void objectInserted(ObjectInsertedEvent arg0) {
			Object obj = arg0.getObject();
			System.out.println("new " + obj.getClass().getName());
			if (obj instanceof Transactions) {
				Transactions transaction = (Transactions) obj;
				System.out.println("new " + transaction);

			}
		}

		public void objectDeleted(ObjectDeletedEvent arg0) {
		}

		public void objectUpdated(ObjectUpdatedEvent arg0) {
			if (arg0.getObject() instanceof Transactions)
				return;

			try {
				System.out.println("saving " + arg0.getObject().toString());
				// CloudDbUtils.updateDb(arg0.getObject());
			} catch (Exception e) {
				System.err.println("objectModified " + e);
			}
		}

	}

	public List<Transactions> initData() {
		Transactions tran = new Transactions();
		tran.setId(1);
		tran.setName("Equity");
		tran.setType("Equity");
		tran.setAmount(0);
		tran.setAmount1(25623);
		tran.setAmount2(5484);
		tran.setAmount3(252636);
		tran.setIsArchived("N");
		tran.setInsertedBy("user1");
		tran.setInsertedAt(new Date());
		tran.setUpdatedBy("user2");
		tran.setUpdatedAt(new Date());
		List<Transactions> lists = null;
		lists.add(tran);

		Transactions tran1 = new Transactions();
		Transactions tran2 = new Transactions();
		Transactions tran3 = new Transactions();
		Transactions tran4 = new Transactions();
		Transactions tran5 = new Transactions();
		Transactions tran6 = new Transactions();
		Transactions tran7 = new Transactions();
		Transactions tran8 = new Transactions();
		Transactions tran9 = new Transactions();
		lists.add(tran1);
		tran1.setName(null);

		lists.add(tran2);
		tran2.setAmount1(0);

		lists.add(tran3);
		tran3.setIsArchived("Y");
		lists.add(tran4);
		lists.add(tran5);

		lists.add(tran6);
		lists.add(tran7);
		lists.add(tran8);
		lists.add(tran9);

		return lists;
	}

}
