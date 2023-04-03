package com.gaian.imageuploaderdownloader;

import java.sql.Blob;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "images_set")
@Component
public class ImageModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "uuid2")
    private String id;

    @Lob
    private Blob image;
    
    private String imagetype;

    

    public ImageModel(Blob image, String type) {
        this.image = image;
        this.imagetype = type;
    }

    public String toString(String id ,Blob imageascv, String type) {
        
        return "ImageModel [id=" + id + ", image=" + imageascv + ", type=" + type + "]";
    }

    public ImageModel() {}

    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return this.id;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getType() {
        return imagetype;
    }

    public void setType(String type) {
        this.imagetype = type;
    }

    

    
}
