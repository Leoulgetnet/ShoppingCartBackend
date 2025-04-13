package All.Service.Image;

import All.Dto.ImageDto;
import All.Exceptions.ResourceNotFoundException;
import All.Model.Image;
import All.Model.Product;
import All.Repository.ImageRepository;
import All.Service.Product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    @Autowired
ImageRepository repo;
    @Autowired
private final ProductService productService;
    @Override
    public Image getImageById(Long id) {
        return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Image Not Found"));}

    @Override
    public void deleteImageById(Long id) {
           repo.findById(id).ifPresentOrElse(repo::delete,
        ()->new ResourceNotFoundException("Product Not Found with this"+id));}


    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product=productService.getProductById(productId);
        List<ImageDto> savedImageDto=new ArrayList<>();
        for(MultipartFile file:files){
            try{
                Image image=new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFiletype(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                Path buildDownloadUrl= Paths.get(System
                                .getProperty("user.home"), "Downloads")
                                         .toAbsolutePath().normalize();
                String buildDownloadUrlString=String.valueOf(buildDownloadUrl);
                String downloadUrlString=buildDownloadUrlString+image.getId();
                image.setDownloadUrl(downloadUrlString);
                Image savedImage=repo.save(image);
                savedImage.setDownloadUrl(buildDownloadUrlString+savedImage.getId());
                repo.save(savedImage);
                ImageDto imageDto1=new ImageDto();
                imageDto1.setId(savedImage.getId());
                imageDto1.setFilename(savedImage.getFilename());
                imageDto1.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto1); }
            catch (SerialException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }}
        return savedImageDto;}





    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFilename(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            /*here focus more database accepts blob,but not the file it self so
             * first we have to change the file to bytes then to blob
             * new SerialBlob(file.getBytes())
             * */
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }}

    @Override
    public List<Image> getImageByProductId(Long id) {
        return repo.findImageByProductId(id);
    }
}






/*
*
private final BiFunction<String , MultipartFile,String> photofunction=(id, image) -> {
         try {
Path FileStorageLocation= Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();

Paths.get:-retrieves the current working directory(Directory as file)-where the program is running-folder
if we want to retrive the location of downloads folder or any folder we can use
System.getProperty("user.home"),retrieves the part of C:\Users\GECHO then if we use
Paths.get(System.getProperty("user.home"), "Downloads").toAbsolutePath().normalize();
*
*
*
if(!Files.exists(FileStorageLocation)){
        Files.createDirectories(FileStorageLocation);}
        Files.copy(image.getInputStream(),
        FileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())),
REPLACE_EXISTING);
        return ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/contacts/image/"+id+fileExtension.apply(image.getOriginalFilename()))
        .toString();}
                 catch(Exception e){
        throw new RuntimeException("Unable To Save Image");
         }};}
* */



/*private Blob image;
public ImageEntity saveImage(String name, MultipartFile file)
         ImageEntity.setImage(new SerialBlob(file.getBytes())); //saving image


//returning Blob
  Optional<ImageEntity> imageEntity = imageRepository.findById(id);
imageEntity.getImage();//retrieving image ,but returned as Blob

//changing the blob to byte
Blob blob = getImageBlobById(id);
        return blob.getBytes(1, (int) blob.length());    //returned as byte

 * */