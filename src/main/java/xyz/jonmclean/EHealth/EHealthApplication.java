package xyz.jonmclean.EHealth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class EHealthApplication {
	
	@Autowired
	public Environment environment;
	
	public final String awsAccessPropName = "awsAccessKeyProperty";
	public final String awsSecretPropName = "awsSecretKeyProperty";
	
	public final String googleSecretPropName = "googleApiSecretProperty";
	
	@Value("${base.url}")
	public static String baseUrl;
	
	public static void main(String[] args) {
		SpringApplication.run(EHealthApplication.class, args);
	}
	
	@Bean
	public AmazonS3 getS3() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(environment.getProperty(awsAccessPropName), environment.getProperty(awsSecretPropName)))).withRegion(Regions.AP_SOUTHEAST_2).build();
	}
	
	@Bean 
	public GoogleCredentials getFirebaseCredentials() throws IOException {
		return ServiceAccountCredentials.fromStream(new ByteArrayInputStream(environment.getProperty(googleSecretPropName).getBytes(StandardCharsets.UTF_8)));
	}
	
	@Bean
	public FirebaseApp getFirebaseApp(GoogleCredentials creds) throws IOException {
		FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(creds).build();
		return FirebaseApp.initializeApp(options);
	}
	
	@Bean
	public Firestore getFirestore(GoogleCredentials creds ) {
		return FirestoreOptions
				.getDefaultInstance()
				.toBuilder()
				.setCredentialsProvider(FixedCredentialsProvider.create(creds))
				.setProjectId("ehealth-458e4")
				.build()
				.getService();
	}
}
