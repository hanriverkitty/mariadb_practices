-- 문제1.
-- 현재 평균 연봉보다 많은 월급을 받는 직원은 몇 명이나 있습니까?
select count(*)
from salaries a, employees b
where a.emp_no = b.emp_no
and a.to_date='9999-01-01'
and a.salary > (
				select avg(a.salary)
				from salaries a, employees b
				where a.emp_no = b.emp_no
				and a.to_date='9999-01-01');

-- 문제2. 
-- 현재, 각 부서별로 최고의 급여를 받는 사원의 사번, 이름, 부서, 연봉을 조회하세요. 단 조회결과는 연봉의 내림차순으로 정렬되어 나타나야 합니다. 
select a.emp_no, a.first_name, b.dept_name, d.salary
from employees a, departments b, dept_emp c, salaries d
where a.emp_no=c.emp_no
and a.emp_no = d.emp_no
and c.dept_no = b.dept_no
and c.to_date='9999-01-01'
and d.to_date='9999-01-01'
and (b.dept_no, d.salary) in (
	select a.dept_no, max(b.salary)
	from dept_emp a, salaries b
	where a.emp_no = b.emp_no
	and a.to_date='9999-01-01'
	and b.to_date='9999-01-01'
	group by a.dept_no)
    order by d.salary desc;

-- 문제3.
-- 현재, 자신의 부서 평균 급여보다 연봉(salary)이 많은 사원의 사번, 이름과 연봉을 조회하세요 
select a.emp_no, a.first_name, b.salary
from employees a, salaries b, dept_emp c, 
(select a.dept_no, avg(b.salary) as avg_sal
	from dept_emp a, salaries b
	where a.emp_no = b.emp_no
	and a.to_date='9999-01-01'
	and b.to_date='9999-01-01'
	group by a.dept_no) as d
where a.emp_no = c.emp_no
and b.emp_no = a.emp_no
and b.to_date='9999-01-01'
and c.to_date='9999-01-01'
and c.dept_no = d.dept_no
and b.salary > d.avg_sal;
    

-- 문제4.
-- 현재, 사원들의 사번, 이름, 매니저 이름, 부서 이름으로 출력해 보세요.
select a.emp_no, CONCAT(a.first_name,' ',a.last_name) as name, d.first_name, c.dept_name
from employees a, dept_emp b, departments c, (
								select d.first_name, b.dept_no as dept_no
								from dept_manager a, dept_emp b, employees d
								where a.emp_no = b.emp_no
								and a.to_date = '9999-01-01'
                                and b.to_date = '9999-01-01'
								and a.emp_no = d.emp_no) d
where a.emp_no = b.emp_no
and b.dept_no = c.dept_no
and b.to_date='9999-01-01'
and d.dept_no=b.dept_no;


-- 문제5.
-- 현재, 평균연봉이 가장 높은 부서의 사원들의 사번, 이름, 직책, 연봉을 조회하고 연봉 순으로 출력하세요.
select b.emp_no, b.first_name, a.title, d.salary
from titles a, employees b, dept_emp c, salaries d
where a.emp_no = b.emp_no
and b.emp_no = d.emp_no
and a.to_date='9999-01-01'
and b.emp_no=c.emp_no
and c.to_date='9999-01-01'
and d.to_date = '9999-01-01'
and c.dept_no = (
				select  c.dept_no
				from salaries a, employees b, dept_emp c
				where a.emp_no = b.emp_no
				and c.emp_no = b.emp_no
				and a.to_date='9999-01-01'
				and c.to_date='9999-01-01'
				group by c.dept_no
				order by avg(a.salary) desc
				limit 0,1)
order by d.salary desc;

-- 문제6.
-- 평균 연봉이 가장 높은 부서는? 
-- 영업 20000
select b.dept_name, c.avg_sal
from dept_emp a, departments b,
(select b.dept_no, avg(a.salary) as avg_sal
from salaries a, dept_emp b, employees c
where c.emp_no = b.emp_no
and a.emp_no = c.emp_no
and a.to_date='9999-01-01'
and b.to_date='9999-01-01'
group by b.dept_no) c
where a.dept_no = b.dept_no
and a.dept_no = c.dept_no
order by c.avg_sal desc
limit 1;


-- 문제7.
-- 평균 연봉이 가장 높은 직책?
-- 개발자 20000

select b.title, avg(a.salary) as avg_sal
from salaries a, titles b, employees c
where c.emp_no = b.emp_no
and a.emp_no = b.emp_no
and a.to_date='9999-01-01'
and b.to_date='9999-01-01'
group by b.title
order by avg_sal desc
limit 0,1;


-- 문제8.
-- 현재 자신의 매니저보다 높은 연봉을 받고 있는 직원은?
-- 부서이름, 사원이름, 연봉, 매니저 이름, 매니저 연봉 순으로 출력합니다.
select c.dept_name, a.first_name, f.salary, d.first_name, d.manager_sal
from employees a, dept_emp b, departments c, (
								select d.first_name, b.dept_no as dept_no, e.salary as manager_sal
								from dept_manager a, dept_emp b, employees d, salaries e
								where a.emp_no = b.emp_no
								and a.to_date = '9999-01-01'
								and a.emp_no = d.emp_no
                                and e.to_date='9999-01-01'
                                and a.emp_no = e.emp_no) d,
                                salaries f
where a.emp_no = b.emp_no
and b.dept_no = c.dept_no
and b.to_date='9999-01-01'
and d.dept_no=b.dept_no
and a.emp_no = f.emp_no
and f.to_date='9999-01-01'
and f.salary > d.manager_sal;