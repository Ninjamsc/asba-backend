package com.technoserv.bio.kernel;

import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.request.Base64Photo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by Adrey on 24.11.2016.
 */
@Service
@Profile("test")
public class MockPhotoServiceClient extends PhotoPersistServiceRestClient {

    private final static String foto = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAABlBMVEUAAAD///+l2Z/dAAAAM0lEQVR4nGP4/5/h/1+G/58ZDrAz3D/McH8yw83NDDeNGe4Ug9C9zwz3gVLMDA/A6P9/AFGGFyjOXZtQAAAAAElFTkSuQmCC";

    @Override
    public Base64Photo getPhoto(String photoUrl) {
        return new Base64Photo(foto);
    }

    @Override
    public String putPhoto(String file_content, String file_name) {
        return foto;
    }

}
