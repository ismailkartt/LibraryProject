package com.tm3library.service;

import com.tm3library.domain.Author;
import com.tm3library.domain.Publisher;
import com.tm3library.dto.request.PublisherRequest;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.dto.response.PublisherResponse;
import com.tm3library.exception.BadRequestException;
import com.tm3library.exception.ResourceNotFoundException;
import com.tm3library.exception.message.ErrorMessage;
import com.tm3library.mapper.PublisherMapper;
import com.tm3library.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;


    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    // Publisher oluşturma !!!!!!
    public PublisherResponse createPublisher(PublisherRequest publisherRequest) {

        Publisher publisher = publisherMapper.publisherRequestToPublisher(publisherRequest);

        publisherRepository.save(publisher);

        return  publisherMapper.publisherToPublisherResponse(publisher);

    }

    // Publisher bulma !!!
    public PublisherResponse getPublisherById(Long id) {

        Publisher publisher= findPublisherById(id);

        return publisherMapper.publisherToPublisherResponse(publisher);
    }

    // Yardımcı method !!!
    public Publisher findPublisherById(Long id){

        Publisher publisher= publisherRepository.
                findById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.PUBLISHER_NOT_FOUND_EXCEPTION,id)));

        return publisher;
    }


    public Page<PublisherResponse> findAllWithPage(Pageable pageable) {

        Page<Publisher> publisherPage = publisherRepository.findAll(pageable);

        return publisherPage.map(publisher -> publisherMapper.publisherToPublisherResponse(publisher));

    }

    //--------------------------------------------------------------------------------------------
    //!!! Update !!!
    public void updatePublisher(Long id, PublisherRequest publisherRequest) {

        Publisher publisher = findPublisherById(id);

        if(publisher.isBuiltIn()){
            throw new ResourceNotFoundException(String.format(ErrorMessage.PUBLISHER_NOT_FOUND_EXCEPTION,id));
        }

        publisher.setName(publisherRequest.getName());
        publisherRepository.save(publisher);

    }


    //!!! Delete !!!
    public PublisherResponse removePublisherById(Long id) {

        Publisher publisher = findPublisherById(id);
        PublisherResponse publisherResponse= publisherMapper.publisherToPublisherResponse(publisher);

        // Built-in
        if(publisher.isBuiltIn()){
            throw new ResourceNotFoundException(String.format(ErrorMessage.PUBLISHER_CANNOT_DELETE_EXCEPTION,id));
        }


        // Publisher kitabı var mı kontrol et !!!
        if (!(publisher.getBookList().size()==0)){
            throw new BadRequestException(String.format(ErrorMessage.PUBLISHER_CANNOT_DELETE_EXCEPTION,id));
        }


        publisherRepository.delete(publisher);

        return publisherResponse;
    }

    public List<Publisher> getAllpublishers() {

        return publisherRepository.getAllBy();
    }
}
