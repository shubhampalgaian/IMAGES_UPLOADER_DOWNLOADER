package com.gaian.imageuploaderdownloader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ImageController {

    @Autowired
    ImageService service;
    @Autowired
    ImageRepository repository;

    // view All images
    @GetMapping("/")
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("index");
        List<ImageModel> imageList = service.viewAll();
        mv.addObject("imageList", imageList);
        return mv;
    }

    // display image
    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") String id) throws IOException, SQLException
    {
        ImageModel image = service.viewById(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    // upload image - get
    @GetMapping("/upload")
    public ModelAndView addImage(){
        return new ModelAndView("addImage");
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, SQLException {

        // System.out.println("in uploader method");
        long maxFileSize = 4 * 1024 * 1024; // 4 MB in bytes
        System.out.println(file.getSize()+" size "+ maxFileSize+" maxfilesize" );


        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("image size is more than 4mb");
        }
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        ImageModel imageData = new ImageModel();
        imageData.setImage(blob);
        imageData.setType(file.getContentType());
        service.create(imageData);

        redirectAttributes.addFlashAttribute("message", "Image uploaded successfully!");

        return "redirect:/";
    }

    //download
    @GetMapping("/download")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") String id, HttpServletResponse response) throws IOException, SQLException {
        ImageModel image = service.viewById(id);
        byte[] imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());
        
        // set headers for the response
        response.setContentType("image/jpeg");
       response.setContentType(image.getType());
        response.setContentLength(imageBytes.length);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=image_" + id + image.getType();
        response.setHeader(headerKey, headerValue);
        
        // write image to response output stream
        response.getOutputStream().write(imageBytes);
        response.getOutputStream().flush();
        
        return null;
    }
}
