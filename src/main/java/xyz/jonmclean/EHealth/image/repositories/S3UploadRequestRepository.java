package xyz.jonmclean.EHealth.image.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.jonmclean.EHealth.image.models.S3UploadRequest;

@Repository
public interface S3UploadRequestRepository extends CrudRepository<S3UploadRequest, String>{}

