package com.hendisantika.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UploadController {

    //Save the uploaded file to this folder
//    private static String UPLOADED_FOLDER = "/tmp/";
    private static String UPLOADED_FOLDER = "/home/vance_pofeng/"; //System.getProperty("java.io.tmpdir");

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
    
    @GetMapping(value = "/download/{filename}")
    void get_image(@PathVariable("filename") String filename, HttpServletResponse response) {
    	
    	String mimeType = "application/octet-stream";
    	File path = null;
    	InputStream in  = null;
    	try {
    		path = new File(UPLOADED_FOLDER, filename);
			response.setContentType(mimeType);
    		in = new FileInputStream(path);
    		byte[] buffer = new byte[8192];
    		int n=-1;
    		OutputStream bos = response.getOutputStream();
    		
    		while( (n=in.read(buffer))>0 ) {
    			bos.write(buffer, 0, n);
    		}
    		
    		//byte[] out = bos.toByteArray();
    		//response.setContentType(img.getImg_content_tpye());
    	    //InputStream in = servletContext.getResourceAsStream("/images/no_image.jpg");
    	    //IOUtils.copy(in, response.getOutputStream());
    	}catch(Exception e) {
    		//log.error("error:{}", ExceptionUtil.toString(e));
    		e.printStackTrace();
    	}finally {
    		if(in!=null) {
    			try {
    				in.close();
    			}catch(Exception ew) {
    				ew.printStackTrace();
    			}
    		}
    	}
    }

}