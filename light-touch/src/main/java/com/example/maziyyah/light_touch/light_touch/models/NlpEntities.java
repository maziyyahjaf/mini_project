package com.example.maziyyah.light_touch.light_touch.models;

import java.util.List;

public class NlpEntities {

    private List<String> feelings;
    private List<String> people;
    private List<String> topics;

    

    public NlpEntities() {
    }

    public NlpEntities(List<String> feelings, List<String> people, List<String> topics) {
        this.feelings = feelings;
        this.people = people;
        this.topics = topics;
    }

    public List<String> getFeelings() {
        return feelings;
    }

    public void setFeelings(List<String> feelings) {
        this.feelings = feelings;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    
    
    
    
}
