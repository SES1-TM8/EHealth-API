package xyz.jonmclean.EHealth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@SpringBootApplication
public class EHealthApplication {
	
	@Autowired
	public Environment environment;
	
	public final String awsAccessPropName = "awsAccessKeyProperty";
	public final String awsSecretPropName = "awsSecretKeyProperty";
	
	@Value("${base.url}")
	public static String baseUrl;
	
	public static void main(String[] args) {
		SpringApplication.run(EHealthApplication.class, args);
	}
	
	@Bean
	public AmazonS3 getS3() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(environment.getProperty(awsAccessPropName), environment.getProperty(awsSecretPropName)))).withRegion(Regions.AP_SOUTHEAST_2).build();
	}
}
