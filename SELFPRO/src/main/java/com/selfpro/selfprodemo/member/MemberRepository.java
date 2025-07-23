package com.selfpro.selfprodemo.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByMemberLoginID(String username);

	// 해당 ID가 존재하는지 체크하는 메서드
	boolean existsByMemberLoginID(String memberLoginId);

	// 경험치 계산
	@Modifying
	@Query("UPDATE Member m SET m.dailyEXP = m.dailyEXP + :dailyExp WHERE m.memberLoginID = :memberId")
	void updateDailyExp(@Param("memberId") String memberId, @Param("dailyExp") int dailyExp);
	
	//걍함치 음수 방지 ( 0으로 )
	@Modifying
	@Query("UPDATE Member m SET m.dailyEXP = 0 WHERE m.memberLoginID = :memberId")
	void updateDailyExpToZero(@Param("memberId") String memberId);
	
	// 현재 전체 경험치 확인
	@Query("SELECT dailyEXP FROM Member WHERE memberLoginID = :memberId")
	int findDailyExpByMemberId(@Param("memberId") String memberId);

	// 오늘의 결산여부 True로 바꾸기
	@Modifying
	@Query("UPDATE Member m SET m.todayClosingDone = true WHERE m.memberLoginID = :memberId")
	void updateTodayClosingDone(@Param("memberId") String memberId);

	// 결산 자정까지 안누른 사람들 찾기
	List<Member> findByTodayClosingDone(boolean todayClosingDone);

	// DB에서 레벨 계산
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE Member m SET m.memberLevel = (:dailyEXP/100)+1 WHERE memberLoginID = :memberId")
	void updateMemberLevel(@Param("memberId") String memberLoginID, @Param("dailyEXP") int dailyEXP);
	
	//Test 오늘의 결산 여부 False;
	@Modifying
	@Query("UPDATE Member m SET m.todayClosingDone = false WHERE m.memberLoginID = :memberId")
	void updateTodayClosigDoneFalseForTest(@Param("memberId") String memberLoginID);

	//Test Xp + 25
	@Modifying
	@Query("UPDATE Member m SET m.dailyEXP = m.dailyEXP + 25 WHERE m.memberLoginID = :memberId")
	void updateDailyExpPlus25(@Param("memberId") String memberId);

	
}
