package com.claim.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.claim.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByEmail(String email);
	
	@Query("FROM User WHERE email=?1")
	Optional<User> findEmail(String email);
	
	Optional<User> findByEmailAndPassword(String email, String password);
}
