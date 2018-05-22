package com.isssr.ticketing_system.response_entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ListObjectResponseEntityBuilder<T extends Collection> extends ListResponseEntityBuilder {
    public ListObjectResponseEntityBuilder(T object) {
        this(object, "base");
    }

    @SuppressWarnings("unchecked")
    public ListObjectResponseEntityBuilder(T collection, String responseType) {
        if (collection != null)
            this.addAllBuilders((List<? extends ResponseEntityBuilder>) collection
                    .stream()
                    .map(object -> object instanceof Collection
                            ? new ListObjectResponseEntityBuilder<>((Collection) object, responseType)
                            : new ObjectResponseEntityBuilder<>(object, responseType))
                    .collect(Collectors.toList()));
    }
}
