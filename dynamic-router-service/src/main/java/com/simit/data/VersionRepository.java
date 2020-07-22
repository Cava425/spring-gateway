package com.simit.data;

import com.simit.entity.Version;

public interface VersionRepository {

    Iterable<Version> findAll();

    Version findLatest();

    Version save(Version version);
}
