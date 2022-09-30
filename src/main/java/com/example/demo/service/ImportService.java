package com.example.demo.service;

import java.io.InputStream;
import java.util.List;

public interface ImportService {
	public List getBankListByExcel(InputStream in, String fileName) throws Exception;
}
