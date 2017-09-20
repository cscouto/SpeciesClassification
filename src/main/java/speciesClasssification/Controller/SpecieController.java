package speciesClasssification.Controller;

/**
 * Created by Tiago on 8/31/2017.
 */

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import speciesClasssification.Model.*;
import java.io.*;

@CrossOrigin
@RestController
public class SpecieController {
    @RequestMapping(value = "/getSpecies")
    public ResponseEntity<SpecieResponse> getSpecies()
    {
        SpecieResponse response = new SpecieResponse();
        SpecieManager mg = new SpecieManager();
        response.list = mg.getSpecies();
        return new ResponseEntity<SpecieResponse>(response, HttpStatus.OK);
    }
    @RequestMapping(value = "/getSpecie", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<SpecieClassificationResponse> getSpecies(@RequestBody  SpecieRequest specieRequest)
            throws Exception {
        SpecieManager mg = new SpecieManager();
        SpecieClassificationResponse response = new SpecieClassificationResponse();
        if (specieRequest.image != null) {
            InputStream is = new ByteArrayInputStream(Base64.decodeBase64(specieRequest.image));
            response.list = mg.identifySpecie(is);
        }

        return new ResponseEntity<SpecieClassificationResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/trainNetWork")
    public ResponseEntity<MessageResponse> trainNetwork()
            throws Exception {
        SpecieManager mg = new SpecieManager();
        MessageResponse response = new MessageResponse();
       response.message =  mg.trainNetworkResponse();

        return new ResponseEntity<MessageResponse>(response, HttpStatus.OK);
    }
}
