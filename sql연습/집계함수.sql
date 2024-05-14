














-- 예제: 사원별 평균 월급은?
select emp_no, avg(salary)
from salaries
group by emp_no;




-- 5) having
-- 집계 결과 (결과 테이블) 에서 row를 선택해야 하는 경우
-- 이미 where 절은 실행이 되었기 때문에 having 절에 이 조건을 주어야 한다.
-- 예제) 평균월급이 60000 달러 이상인 사원의 사번과 평균월급을 출력 하세요

select emp_no, avg(salary) as avg_salary
from salaries
group by emp_no
having avg_salary >= 60000;

-- 6) order by
-- 	  order by 는 항상 맨 마지막 출력 (projection)전에 한다
select emp_no, avg(salary) as avg_salary
from salaries
group by emp_no
having avg_salary >= 60000
order by avg_salary asc;

-- 주의) 사번이 10060인 사원의 사번, 평균 우러급, 급여총합을 출력하세요.
-- 문법적으로 오류
-- 의미적으로는 맞다 (where 절 때문에)
select emp_no, avg(salary), sum(salary)
from salaries
where emp_no = 10060;

-- 문법적으로 옳다
select emp_no, avg(salary), sum(salary)
from salaries
group by emp_no
having emp_no = 10060;
