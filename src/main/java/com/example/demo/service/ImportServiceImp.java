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

	public List<Transactions> loadRules() {
		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource("com/example/demo/controller/sample.drl"),
				ResourceType.DRL);

		KnowledgeBuilderErrors knowledgeBuilderErrors = knowledgeBuilder.getErrors();
		if (knowledgeBuilderErrors.size() > 0) {
			for (KnowledgeBuilderError error : knowledgeBuilderErrors) {
				System.out.println(error);
			}

			return null;
		}

		KieBase kieBase = knowledgeBuilder.newKieBase();
		kieSession = kieBase.newKieSession();

		kieSession.addEventListener(new EventListener());
		System.out.println(
				" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^ controller.drl  was read - ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^ ");

		// kieSession.setGlobal("me", this);
		// kieSession.setGlobal("arrOfCheckTrans", new ArrayList());
		System.out.println(
				" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^   drl was read - ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n^^^^^^^^^^^^^^\n^^^^^^^^^^^ ");

		List<Transactions> lists = initData();
		if (lists != null) {

			for (Iterator<Transactions> i = lists.iterator(); i.hasNext();) {
				Transactions tran = i.next();
				synchronized (kieSession) {
					FactHandle factHandle = kieSession.getFactHandle(tran);
					if (factHandle == null) {
						kieSession.insert(tran);
						System.out.println("insert 1 data into drools");
					} else {
						kieSession.update(factHandle, tran);
					}
				}
			}
		}

		kieSession.fireAllRules();

		return lists;

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
		ArrayList<Transactions> lists = new ArrayList<Transactions>();
		Transactions tran = new Transactions();
		tran.setId(1);
		tran.setName("Equity");
		tran.setType("Equity");
		tran.setAmount(0);
		tran.setAmount1(81);
		tran.setAmount2(64);
		tran.setAmount3(47);
		tran.setIsArchived("N");
		tran.setInsertedBy("user1");
		// tran.setInsertedAt(new Date());
		tran.setUpdatedBy("user2");
		// tran.setUpdatedAt(new Date());
		lists.add(tran);

		Transactions tran1 = new Transactions();
		tran1.setId(2);
		tran1.setName("Removed");
		tran1.setType("Equity");
		tran1.setAmount(0);
		tran1.setAmount1(92);
		tran1.setAmount2(17);
		tran1.setAmount3(84);
		tran1.setIsArchived("N");
		tran1.setInsertedBy("user1");
		tran1.setUpdatedBy("user2");
		lists.add(tran1);
		Transactions tran2 = new Transactions();
		tran2.setId(3);
		tran2.setName("Equity");
		tran2.setType(null);
		tran2.setAmount(0);
		tran2.setAmount1(78);
		tran2.setAmount2(90);
		tran2.setAmount3(4);
		tran2.setIsArchived("N");
		tran2.setInsertedBy("user1");
		tran2.setUpdatedBy("user2");
		lists.add(tran2);

		Transactions tran3 = new Transactions();
		tran3.setId(4);
		tran3.setName("Equity");
		tran3.setType("Equity");
		tran3.setAmount(0);
		tran3.setAmount1(0);
		tran3.setAmount2(73);
		tran3.setAmount3(32);
		tran3.setIsArchived("N");
		tran3.setInsertedBy("user1");
		tran3.setUpdatedBy("user2");
		lists.add(tran3);

		Transactions tran4 = new Transactions();
		tran4.setId(5);
		tran4.setName("Equity");
		tran4.setType("Equity");
		tran4.setAmount(0);
		tran4.setAmount1(59);
		tran4.setAmount2(78);
		tran4.setAmount3(27);
		tran4.setIsArchived("Y");
		tran4.setInsertedBy("user1");
		tran4.setUpdatedBy("user2");
		lists.add(tran4);

		return lists;
	}

	public List<Transactions> loadRule() {
		ArrayList<Transactions> results = new ArrayList<Transactions>();
		Transactions tran = new Transactions();
		tran.setId(1);
		tran.setName("Equity");
		tran.setType("Equity");
		tran.setAmount(0);
		tran.setAmount1(81);
		tran.setAmount2(64);
		tran.setAmount3(47);
		tran.setIsArchived("N");
		tran.setInsertedBy("user1");
		// tran.setInsertedAt(new Date());
		tran.setUpdatedBy("user2");
		// tran.setUpdatedAt(new Date());
		tran.setAmount(tran.getAmount1() + tran.getAmount2() + tran.getAmount3());
		results.add(tran);

		Transactions tran4 = new Transactions();
		tran4.setId(5);
		tran4.setName("Equity");
		tran4.setType("Equity");
		tran4.setAmount(0);
		tran4.setAmount1(59);
		tran4.setAmount2(78);
		tran4.setAmount3(27);
		tran4.setIsArchived("Y");
		tran4.setInsertedBy("user1");
		tran4.setUpdatedBy("user2");

		tran4.setAmount(tran4.getAmount1() + tran4.getAmount2() + tran4.getAmount3());
		results.add(tran4);
		return results;
	}

}
