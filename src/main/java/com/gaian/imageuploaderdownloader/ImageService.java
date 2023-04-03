package com.gaian.imageuploaderdownloader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    ImageRepository repository;

    public ImageModel create(ImageModel modelData) {

        return repository.save(modelData);
    }

    public List<ImageModel> viewAll() {
        return (List<ImageModel>) repository.findAll();
    }

    
    public ImageModel viewById(String id) {
        return repository.findById(id).get();
    }
    
}
