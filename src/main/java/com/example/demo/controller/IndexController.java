package com.example.demo.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.bean.Transactions;
import com.example.demo.service.ImportService;

import net.sf.json.JSONArray;



@RestController
@RequestMapping(value = "/index")
public class IndexController {

    @Autowired
    private ImportService importService;
    
	@RequestMapping
	public String index() {
		return "hello world";
	}

	@RequestMapping(value = "/get")
	public HashMap<String, Object> get(@RequestParam String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "hello world");
		map.put("name", name);
		return map;
	}
	
	@RequestMapping(value = "/getTrans")
	public String getTrans(@RequestParam String name) {
		List<Transactions> trans = importService.loadRules();
		//List<Transactions> trans = importService.initData();
		if (trans != null )
		{
			JSONArray js = JSONArray.fromObject(trans);
			String  result  =  js.toString();
			return result;
		}
		else
		{
			return null;
		}

	}
	
	@RequestMapping(value = "/getInitData")
	public String getIInitData(@RequestParam String name) {
		List<Transactions> trans = importService.initData();
		if (trans != null )
		{
			JSONArray js = JSONArray.fromObject(trans);
			String  result  =  js.toString();
			return result;
		}
		else
		{
			return null;
		}

	}
	
	
    @PostMapping(value = "/transfor")
    @ResponseBody
    public String uploadExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        MultipartFile file = multipartRequest.getFile("D:\\Users\\Zhan\\git\\Ctrl_CV_Elite\\demo_data.xlsx");
        if (file.isEmpty()) {
            return "File can't be null";
        }
        InputStream inputStream = file.getInputStream();
        List<List<Object>> list = importService.getBankListByExcel(inputStream, file.getOriginalFilename());
        inputStream.close();

        for (int i = 0; i < list.size(); i++) {
            List<Object> lo = list.get(i);
            //TODO 
            System.out.println(lo);

        }
        return "Transfor Successful";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	@RequestMapping(value = "/getTran")
	public String getTran(@RequestParam String name) {
		List<Transactions> trans = importService.loadRule();
		//List<Transactions> trans = importService.initData();
		if (trans != null )
		{
			JSONArray js = JSONArray.fromObject(trans);
			String  result  =  js.toString();
			return result;
		}
		else
		{
			return null;
		}

	}

}
