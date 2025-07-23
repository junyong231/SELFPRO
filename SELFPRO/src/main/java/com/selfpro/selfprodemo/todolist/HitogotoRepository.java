package com.selfpro.selfprodemo.todolist;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HitogotoRepository extends JpaRepository<Hitogoto, Long> {

	Optional<Hitogoto> findByMember_MemberLoginIDAndWriteDate(String memberId, LocalDate yesterday);

	void deleteByWriteDate(LocalDate yesterday);

}
