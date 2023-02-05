package com.shravan.api.users.client;

import com.shravan.api.users.model.AlbumResponseModel;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ms", fallbackFactory = AlbumsFallBackFactory.class)
public interface AlbumsClient {

    @GetMapping("/users/{id}/albums")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}
@Component
class AlbumsFallBackFactory implements FallbackFactory<AlbumsClient>{

    @Override
    public AlbumsClient create(Throwable throwable) {
        return new AlbumsFallback(throwable);
    }
}

class AlbumsFallback implements AlbumsClient {

    Logger logger = LoggerFactory.getLogger(AlbumsFallback.class);
    private final Throwable throwable;

    public AlbumsFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404){

            logger.error("404 error"+throwable.getLocalizedMessage());

        }
        else {
            logger.error("Other error " + throwable.getLocalizedMessage());
        }




        return new ArrayList<>();
    }
}
