package com.chong.girl.control;

import com.chong.girl.bean.Girl;
import com.chong.girl.dao.GirlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/girl")
public class GirlController {

    private final GirlRepository repository;

    @Autowired
    public GirlController(GirlRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = {"/girls","/"})
    public List<Girl> getGirls() {

        return repository.findAll();
    }

    @GetMapping(value = "/addGirl")
    public Girl addGirl(@RequestParam Integer age, @RequestParam String name) {

        Girl girl = new Girl();
        girl.setAge(age);
        girl.setName(name);
        return repository.save(girl);
    }

    @GetMapping(value = "/deleteGirl/{id}")
    public void deleteGirl(@PathVariable Long id) {
        repository.delete(id);
    }

    @GetMapping(value = "/updateGirl/{id}/{age}/{name}}")
    public Girl updateGirl(@PathVariable(value = "id", required = true) Long id,
                           @PathVariable(value = "age", required = false) int age,
                           @PathVariable(value = "name", required = false) String name
    ) {
        Girl girl = new Girl();
        girl.setId(id);
        girl.setName(name);
        girl.setAge(age);
        return repository.save(girl);
    }

    @GetMapping(value = "/findGirlById/{id}")
    public Girl findGirlById(@PathVariable Long id) {
        return repository.findOne(id);
    }
    @GetMapping(value = "/findGirlByAge/{age}")
    public List<Girl> findGirlByAge(@PathVariable Integer age){
        return repository.findGirlByAge(age);
    }

}
