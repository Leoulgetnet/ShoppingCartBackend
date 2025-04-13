package All.Controller;
import All.Dto.ImageDto;
import All.Exceptions.ResourceNotFoundException;
import All.Model.Image;
import All.Response.ApiResponse;
import All.Service.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.SQLException;
import java.util.List;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {
    @Autowired
    private ImageService service;


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestBody List<MultipartFile> files,
                                                  @RequestParam Long productId){
        try {
            List<ImageDto> imagesDtos=service.saveImage(files,productId);
            return ResponseEntity.ok(new ApiResponse("Upload Successful",imagesDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload Failed" ,e.getMessage()));}}




@GetMapping("image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("imageId") Long imageId) throws SQLException {
        Image image=service.getImageById(imageId);
        ByteArrayResource resource =
                new ByteArrayResource(image.getImage().getBytes(1,(int)image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + "downloadedFile" + "\"")
                .body(resource);}




    @PutMapping("image/{imageId}/update")
public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestBody MultipartFile file){
Image image=service.getImageById(imageId);
    try {
        if(image!=null){
            service.updateImage(file,imageId);
            return ResponseEntity.ok(new ApiResponse("Update Success",null));}
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Update Failed!",null));
    }

    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Resource Not Found!"
            ,INTERNAL_SERVER_ERROR));

}



    @DeleteMapping("image/{imageId}/delete")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId){
        Image image=service.getImageById(imageId);

        try {
            if(image!=null){
                service.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete Success",null));}
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Deleting Failed!",null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Resource Not Found!",INTERNAL_SERVER_ERROR));
    }/*2:03*/}












