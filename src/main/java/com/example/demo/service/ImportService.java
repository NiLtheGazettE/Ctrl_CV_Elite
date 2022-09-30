package com.example.demo.service;

import java.io.InputStream;
import java.util.List;

import com.example.demo.bean.Transactions;

public interface ImportService {
	public List getBankListByExcel(InputStream in, String fileName) throws Exception;
	public List<Transactions> initData();
	public List<Transactions> loadRules();
	public List<Transactions> loadRule();
}
