package xyz.jonmclean.EHealth.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import net.coobird.thumbnailator.Thumbnails;
import xyz.jonmclean.EHealth.EHealthApplication;
import xyz.jonmclean.EHealth.image.models.Image;
import xyz.jonmclean.EHealth.image.models.ImageVariant;
import xyz.jonmclean.EHealth.image.models.S3Upload;
import xyz.jonmclean.EHealth.image.models.S3UploadRequest;
import xyz.jonmclean.EHealth.image.repositories.ImageRepository;
import xyz.jonmclean.EHealth.image.repositories.ImageVariantRepository;
import xyz.jonmclean.EHealth.image.repositories.S3UploadRequestRepository;
import xyz.jonmclean.EHealth.models.InternalImageCallback;

@Component
public class ImageService {
	
	public ImageRepository imageRepo;
	public ImageVariantRepository variantRepo;
	public S3UploadRequestRepository uploadRepo;
	
	public AmazonS3 s3;

	public ImageService(ImageRepository imageRepo, ImageVariantRepository variantRepo,
			S3UploadRequestRepository uploadRepo, AmazonS3 s3) {
		this.imageRepo = imageRepo;
		this.variantRepo = variantRepo;
		this.uploadRepo = uploadRepo;
		this.s3 = s3;
	}
	
	public S3Upload getUpload(String service, String mimeType, Long ownerId, String callbackPath, long appointmentInfoId) { // TODO: Verify MIME type is an image
		long expire = System.currentTimeMillis() + (1000 * 60 * 30);
		String key = System.currentTimeMillis() + UUID.randomUUID().toString() + "." + mimeType.split("/")[1];
		
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest("ehealthcare-uts", key)
				.withMethod(HttpMethod.PUT)
				.withContentType(mimeType)
				.withExpiration(new Date(expire));
		
		URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
		
		S3UploadRequest uploadRequest = new S3UploadRequest();
		uploadRequest.setOwnerId(ownerId);
		uploadRequest.setId(key);
		uploadRequest.setExpire(new Date(expire));
		uploadRequest.setService(service);
		uploadRequest.setMimeType(mimeType);
		uploadRequest.setAppointmentInfoId(appointmentInfoId);
		
		uploadRepo.save(uploadRequest);
		
		return new S3Upload(url.toString(), EHealthApplication.baseUrl + "/" + callbackPath + "/?id=" + key);
	}
	
	public InternalImageCallback process(String key) throws IOException {
		Optional<S3UploadRequest> optionalUploadRequest = uploadRepo.findById(key);
		
		if(!optionalUploadRequest.isPresent()) return null; // TODO: Throw error
		
		S3UploadRequest request = optionalUploadRequest.get();
		
		BufferedImage buffered = getObject("ehealthcare-uts", key);
		
		Image newImage = new Image();
		newImage.setOwnerId(request.getOwnerId());
		
		ImageVariant variant = new ImageVariant();
		variant.setUrl("https://ehealthcare-uts.s3-ap-southeast-2.amazonaws.com/" + key);
		variant.setHeight(buffered.getHeight());
		variant.setWidth(buffered.getWidth());
		
		variantRepo.save(variant);
		
		newImage.getImageVariants().add(variant);
		newImage.getImageVariants().add(scale(1000, 1000, buffered, key, "ehealthcare-uts", request.getMimeType()));
		newImage.getImageVariants().add(scale(500, 500, buffered, key, "ehealthcare-uts", request.getMimeType()));
		newImage.getImageVariants().add(scale(100, 100, buffered, key, "ehealthcare-uts", request.getMimeType()));
		
		imageRepo.save(newImage);
		
		return new InternalImageCallback(request.appointmentInfoId, newImage.getImageId());
	}
	
	public BufferedImage getObject(String bucket, String key) throws IOException {
		S3Object obj = s3.getObject(bucket, key);
		return ImageIO.read(obj.getObjectContent());
	}
	
	public ImageVariant scale(int width, int height, BufferedImage im, String filename, String bucket, String mimeType) throws IOException {
		BufferedImage sim = Thumbnails.of(im)
			.size(width, height)
			.asBufferedImage();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(sim, mimeType.split("/")[1], os);
		
		byte[] content = os.toByteArray();
		
		System.out.println(content.length);
		
		ObjectMetadata om = new ObjectMetadata();
		om.setContentType(mimeType);
		om.setContentLength(content.length);
		
		s3.putObject(new PutObjectRequest(bucket, width + "-" + height + "/" + filename, new ByteArrayInputStream(content), om));
		
		return variantRepo.save(new ImageVariant(sim.getWidth(), sim.getHeight(), "https://" + bucket + ".s3-ap-southeast-2.amazonaws.com/" + width + "-" + height + "/" + filename));
	}
	
	public Image getImage(long imageId) {
		Optional<Image> imO = imageRepo.findById(imageId);
		
		if(!imO.isPresent()){
			return null;
		}
		
		Image im = imO.get();
		
		for(ImageVariant iv: im.getImageVariants()) {
			String bucket = "ehealthcare-uts";
			String key = iv.getUrl().substring(bucket.length() + 41);
			
			GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, key)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(new Date(System.currentTimeMillis() + (30 * 1000 * 60)));
			
			iv.setUrl(s3.generatePresignedUrl(generatePresignedUrlRequest).toString());
		}
		
		return im;
	}
}
