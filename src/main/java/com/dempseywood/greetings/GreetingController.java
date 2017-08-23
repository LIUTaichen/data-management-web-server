package com.dempseywood.greetings;

import com.dempseywood.navixy.AuthResult;
import com.dempseywood.navixy.reading.Response;
import com.dempseywood.navixy.tracker.Reading;
import com.dempseywood.navixy.tracker.ReadingRepository;
import com.dempseywood.webservice.equipmentstatus.EquipmentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Controller
public class GreetingController {

    @Autowired
    private ReadingRepository readingRepository;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping("/thyme")
    public String welcome(Map<String, Object> model) {
        model.put("message", "hello world from dw");
        return "welcome";
    }



    @Transactional
    private void saveReading(List<Reading> readingList, Integer trackerId){
        for(Reading reading: readingList){
            reading.setTrackerId(9);
            readingRepository.save(reading);
        }

    }
    @RequestMapping("/loadreading")
    public String load(@RequestParam String from, @RequestParam String to, @RequestParam Integer trackerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("login", "jason.liu@dempseywood.co.nz");
        map.add("password", "qianer1006");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AuthResult> response = restTemplate.postForEntity("http://ec2-52-65-181-0.ap-southeast-2.compute.amazonaws.com/api/user/auth", request, AuthResult.class);
        AuthResult result = response.getBody();
        if (result.isSuccess()) {

            map = new LinkedMultiValueMap<String, String>();
            map.add("hash", result.getHash());
            map.add("tracker_id", trackerId.toString());
            map.add("from", from);
            map.add("to", to);
            request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            ResponseEntity<Response> readResponse = restTemplate.postForEntity("http://ec2-52-65-181-0.ap-southeast-2.compute.amazonaws.com/api/track/read", request, Response.class);

            System.out.println(readResponse.getBody().getList().size());
            saveReading(readResponse.getBody().getList(), trackerId);
        }
        return "ok";
    }


}
