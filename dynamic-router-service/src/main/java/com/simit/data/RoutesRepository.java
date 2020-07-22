package com.simit.data;

import com.simit.entity.Routes;

public interface RoutesRepository {

    Iterable<Routes> findAll();

    Routes save(Routes routes);
}
