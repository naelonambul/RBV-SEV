package com.rbc.red.api.repository;

import com.rbc.red.api.entity.Asset;
import com.rbc.red.api.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByGroup(Group group);
}
