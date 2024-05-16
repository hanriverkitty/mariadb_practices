--
-- subquery
-- 

--
-- 1) selcet 절, insert into t1 values(...)
--

-- insert into board values (null,(select max(group_no) + 1 from board), ...,..)
select (select 1+2 from dual) from dual;


--
-- 2) from 절의 서브쿼리
--
select a.* from (select 1+2 from dual) a;  -- 서브쿼리 from은 Alias 를 써줘야 한다
select now() as n, sysdate() as s, 3+1 as r from dual;

select n,s,r
from (select now() as n, sysdate() as s , 3+1 as r from dual) a;

--
-- 3) where 절의 서브쿼리
--

-- 예제) 현재, Fai Bale 이 근무하는 부서에서 근무하는 다른 직원의 사번, 이름을 출력하세요.
select b.dept_no
from employees a, dept_emp b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
and concat(a.first_name, ' ' , a.last_name) = 'Fai Bale';

-- 'd004'

select a.emp_no, a.first_name
from employees a, dept_emp b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
and b.dept_no = 'd004';


SELECT 
    a.emp_no, a.first_name
FROM
    employees a,
    dept_emp b
WHERE
    a.emp_no = b.emp_no
        AND b.to_date = '9999-01-01'
        AND b.dept_no = (SELECT 
            b.dept_no
        FROM
            employees a,
            dept_emp b
        WHERE
            a.emp_no = b.emp_no
                AND b.to_date = '9999-01-01'
                AND CONCAT(a.first_name, ' ', a.last_name) = 'Fai Bale');

-- where 서브쿼리의 결과가 복수 열일 경우 ()안에 이름을 넣어 맞출 수 있다
-- (colunmn1, column2) = 서브쿼리 결과1, 서브쿼리 결과2                
-- 3-1) 단일행 연산자: =, >, <, >=, <=, <>, !=

-- 실습문제1
-- 현재, 전체 사원의 평균 연봉보다 적은 급여를 받는 사원의 이름과 급여를 출력 하세요.
select a.first_name, b.salary
from employees a, salaries b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
and b.salary < (select avg(salary)
				from salaries
				where to_date = '9999-01-01')
order by b.salary desc;

-- 실습문제2
-- 현재, 직책별 평균급여 중에 가장 작은 직책의 직책이름과 그 평균 급여를 출력해 보세요.
-- 집계함수는 다른 컬럼이 오면 안된다
-- 틀린 방법
-- select avg_t.title,min(avg_sal)
-- from (
-- 	select a.title, avg(b.salary) as avg_sal
-- 	from titles a, salaries b
-- 	where a.emp_no = b.emp_no
-- 	and a.to_date = '9999-01-01' 
-- 	and b.to_date = '9999-01-01'
-- 	group by a.title) as avg_t;
    
-- 3) sol1: where 절 subquery
select a.title, avg(b.salary)
from titles a, salaries b
where a.emp_no = b.emp_no
and a.to_date = '9999-01-01' 
and b.to_date = '9999-01-01'
group by a.title 
having avg(b.salary) = (select min(avg_sal)
							from (
								select a.title, avg(b.salary) as avg_sal
								from titles a, salaries b
								where a.emp_no = b.emp_no
								and a.to_date = '9999-01-01' 
								and b.to_date = '9999-01-01'
								group by a.title) as avg_t);

-- 4) sol2: top-k(limit)
select a.title, avg(b.salary)
from titles a, salaries b
where a.emp_no = b.emp_no
and a.to_date = '9999-01-01' 
and b.to_date = '9999-01-01'
group by a.title
order by avg(b.salary) asc
limit 0,1;
-- limit 앞은 인덱스 (0부터 시작) , 뒤는 갯수

-- 3-2) 복수행 연자: in, not in, 비교연산자any, 비교연산지all

-- any 사용법
-- 1. =any : in
-- 2. >any, >=any: 최솟값
-- 3. <any, <=any: 최댓값
-- 4. <>any, !=any: not in

-- all 사용법
-- 1. =all: (x) 논리적으로 될 수 가 없다
-- 2. >all, >=all: 최댓값
-- 3. <all, <=all: 최솟값
-- 4. <>all, !=all

-- 실습문제3
-- 현재 급여가 50000 이상인 직원의 이름과 급여를 출력하세요.
-- 둘리 60000
-- 또치 80000

-- sol1) join
select a.first_name, b.salary
from employees a, salaries b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
and b.salary > 50000
order by b.salary;

-- sol2) subquery: where(in)

-- select emp_no, salary
-- from salaries
-- where to_date = '9999-01-01'
-- and salary > 50000;

select a.first_name, b.salary
from employees a, salaries b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
and (a.emp_no,b.salary) in (select emp_no, salary
							from salaries
							where to_date = '9999-01-01'
							and salary > 50000)
order by b.salary;

-- sol3) subquery: where(=any)
select a.first_name, b.salary
from employees a, salaries b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
and (a.emp_no,b.salary) =any (select emp_no, salary
							from salaries
							where to_date = '9999-01-01'
							and salary > 50000)
order by b.salary;

-- 실습문제4:
-- 현재, 각 부서별로 최고 급여를 받고 있는 직원의 이름과 월급을 부서 별로 출력하세요.
select b.dept_name, d.first_name, c.salary
from dept_emp a, departments b, salaries c, employees d
where a.dept_no = b.dept_no
and a.emp_no =c.emp_no
and c.emp_no = d.emp_no
and a.to_date='9999-01-01'
and c.to_date = '9999-01-01'
and (b.dept_no, c.salary) in
(select  a.dept_no, max(salary)
from  dept_emp a, employees b, salaries c
where a.to_date = '9999-01-01'
and a.emp_no = b.emp_no
and c.emp_no = b.emp_no
and c.to_date = '9999-01-01'
group by a.dept_no);


-- 기본 검색틀
select a.dept_no, max(b.salary)
from dept_emp a, salaries b
where a.emp_no = b.emp_no
and a.to_date='9999-01-01'
and b.to_date = '9999-01-01'
group by a.dept_no;


-- sol1) where 절 subquery(in)
select a.dept_name, b.dept_no, c.first_name, d.salary
from departments a, dept_emp b, employees c, salaries d
where a.dept_no = b.dept_no
and b.emp_no = c.emp_no
and c.emp_no = d.emp_no
and b.to_date = '9999-01-01'
and d.to_date = '9999-01-01'
and (b.dept_no, d.salary) in (select a.dept_no, max(b.salary)
								from dept_emp a, salaries b
								where a.emp_no = b.emp_no
								and a.to_date='9999-01-01'
								and b.to_date = '9999-01-01'
								group by a.dept_no);


-- sol2: from절 subquery & join
select a.dept_name, c.first_name, d.salary
from departments a, dept_emp b, employees c, salaries d,
	 (select a.dept_no, max(b.salary) as max_salary
		from dept_emp a, salaries b
		where a.emp_no = b.emp_no
		and a.to_date='9999-01-01'
		and b.to_date = '9999-01-01'
		group by a.dept_no) as e
where a.dept_no = b.dept_no
and b.emp_no = c.emp_no
and c.emp_no = d.emp_no
and b.dept_no = e.dept_no
and b.to_date = '9999-01-01'
and d.to_date = '9999-01-01'
and d.salary = e.max_salary;