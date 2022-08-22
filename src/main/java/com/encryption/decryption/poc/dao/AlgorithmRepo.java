package com.encryption.decryption.poc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encryption.decryption.poc.model.Algorithm;

@Repository
public interface AlgorithmRepo extends JpaRepository<Algorithm,Long> {


}
