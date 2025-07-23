package com.selfpro.selfprodemo.todolist;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<TodoList, Long> {

	// 넣는 날짜에 따른 목표 불러오기
	List<TodoList> findByMemberMemberLoginIDAndWriteDate(String memberLoginID, LocalDate writeDate);

	// 삭제
	void deleteByMemberMemberLoginIDAndWriteDate(String memberLoginID, LocalDate writeDate);

	// 결산으로 isCompleted 불리언 바꿔주기
	@Modifying
	@Query("UPDATE TodoList t SET t.isCompleted = :isComplete WHERE t.todoId = :todoId")
	void closingUpdate(@Param("isComplete") Boolean completeStatus, @Param("todoId") Long todoIdValue);

	int countByIsCompletedAndWriteDate(Boolean completeStatus, LocalDate writeDate);

	// 자정 결산 시 :: 멤버 아이디와 작성일을 기준으로 놓친 과제 수 카운팅
	int countByMemberMemberLoginIDAndWriteDate(String memberLoginID, LocalDate writeDate);

	// Test 결산 여부 N으로 바꾸면 과제들도 다 체크해제 시키기 위해서
	@Modifying
	@Query(value = "UPDATE TodoList t SET t.isCompleted = false WHERE t.member.memberLoginID = :memberId AND t.writeDate = :yesterday")
	void updateYesterdayTaskFalse(@Param("memberId") String memberId, @Param("yesterday") LocalDate yesterday);

}
