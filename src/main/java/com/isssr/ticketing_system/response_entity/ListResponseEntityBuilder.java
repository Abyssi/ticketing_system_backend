package com.isssr.ticketing_system.response_entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListResponseEntityBuilder extends ResponseEntityBuilder<ArrayList> {

    public ListResponseEntityBuilder() {
        this.body = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public ListResponseEntityBuilder add(Object value) {
        this.body.add(value);
        return this;
    }

    public ListResponseEntityBuilder addAll(List<Object> list) {
        for (Object object : list)
            this.add(object);

        return this;
    }

    public ListResponseEntityBuilder addBuilder(ResponseEntityBuilder responseEntityBuilder) {
        return this.add(responseEntityBuilder.getBody());
    }

    public ListResponseEntityBuilder addAllBuilders(List<? extends ResponseEntityBuilder> responseEntityBuilders) {
        return this.addAll(responseEntityBuilders.stream().map(ResponseEntityBuilder::getBody).collect(Collectors.toList()));
    }

}
