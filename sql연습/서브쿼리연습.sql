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
                
-- 3-1) 단일행 연산자: =, >, <, >=, <=, <>, !=
-- 3-2) 복수행 연자: in, not in, 비교연산자any, 비교연산지all
-- where 서브쿼리의 결과가 복수 열일 경우 ()안에 이름을 넣어 맞출 수 있다
-- (colunmn1, column2) = 서브쿼리 결과1, 서브쿼리 결과2