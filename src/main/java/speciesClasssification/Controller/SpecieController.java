package speciesClasssification.Controller;

/**
 * Created by Tiago on 8/31/2017.
 */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import speciesClasssification.Model.SpecieResponse;

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
    @RequestMapping(value = "/getSpecies", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<String> getSpecies( @RequestBody  String g)
            throws Exception {
        return new ResponseEntity<String>(g, HttpStatus.OK);
    }
}
