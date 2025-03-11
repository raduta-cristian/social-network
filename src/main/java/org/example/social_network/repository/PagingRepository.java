package org.example.social_network.repository;

import org.example.social_network.domain.Entity;
import org.example.social_network.domain.dto.FilterDTO;
import org.example.social_network.utils.paging.Page;
import org.example.social_network.utils.paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    public Page<E> findAllOnPage(Pageable pageable, FilterDTO filter);
}
