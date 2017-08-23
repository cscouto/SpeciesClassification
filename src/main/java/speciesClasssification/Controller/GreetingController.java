package speciesClasssification.Controller;

/**
 * Created by Tiago on 8/8/2017.
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import speciesClasssification.Model.Greeting;

@RestController
 public class GreetingController {
    @RequestMapping(value = "/greeting")
    public ResponseEntity<Greeting> greeting()
    {
        Greeting g = new Greeting(1, "Ola");
        return new ResponseEntity<Greeting>(g, HttpStatus.OK);
    }

    @RequestMapping(value = "/greetings", method = RequestMethod.POST, headers="Accept=application/json")
    public ResponseEntity<String> greetings( @RequestBody  String g)
   throws Exception {
        return new ResponseEntity<String>(g, HttpStatus.OK);
    }
}
