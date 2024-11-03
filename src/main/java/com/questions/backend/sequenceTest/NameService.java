package com.questions.backend.sequenceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NameService {

    @Autowired
    private NameRepository nameRepository;

    public void insert() {
        nameRepository.insert();
    }

    public void demoOops() {
        nameRepository.insert();
    }

}
