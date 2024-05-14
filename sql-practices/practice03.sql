-- 문제 1. 
-- 현재 급여가 많은 직원부터 직원의 사번, 이름, 그리고 연봉을 출력 하시오.
select a.salary, b.emp_no, b.first_name
from salaries a join employees b on a.emp_no = b.emp_no
where a.to_date = '9999-01-01'
order by a.salary desc;

-- 문제2.
-- 전체 사원의 사번, 이름, 현재 직책을 이름 순서로 출력하세요.
select a.emp_no, a.first_name, b.title
from employees a join titles b on a.emp_no = b.emp_no
where b.to_date = '9999-01-01'
order by a.first_name;

-- 문제3.
-- 전체 사원의 사번, 이름, 현재 부서를 이름 순서로 출력하세요..
select a.emp_no, a.first_name, c.dept_name
from employees a join dept_emp b on a.emp_no = b.emp_no join departments c on b.dept_no = c.dept_no
where b.to_date = '9999-01-01' 
order by a.first_name;

-- 문제4.
-- 전체 사원의 사번, 이름, 연봉, 직책, 부서를 모두 이름 순서로 출력합니다.
select a.emp_no, a.first_name, d.salary, e.title, c.dept_name
from employees a join dept_emp b on a.emp_no = b.emp_no join departments c on b.dept_no = c.dept_no join salaries d on a.emp_no=d.emp_no join titles e on a.emp_no = e.emp_no
where b.to_date = '9999-01-01'
and d.to_date = '9999-01-01'
and e.to_date = '9999-01-01'
order by a.first_name;

-- 문제5.
-- ‘Technique Leader’의 직책으로 과거에 근무한 적이 있는 모든 사원의 사번과 이름을 출력하세요. 
-- (현재 ‘Technique Leader’의 직책(으로 근무하는 사원은 고려하지 않습니다.) 이름은 first_name과 last_name을 합쳐 출력 합니다.
select a.emp_no, concat(a.first_name,' ',a.last_name)
from employees a join titles b on a.emp_no = b.emp_no
where b.to_date != '9999-01-01' and b.title = 'Technique Leader';

-- 문제6.
-- 직원 이름(first_name) 중에서 S(대문자)로 시작하는 직원들의 이름, 부서명, 직책을 조회하세요.
select a.last_name, c.dept_name, d.title
from employees a join dept_emp b on a.emp_no = b.emp_no join departments c on b.dept_no = c.dept_no join titles d on a.emp_no = d.emp_no
where a.last_name like 'S%';

-- 문제7.
-- 현재, 직책이 Engineer인 사원 중에서 현재 급여가 40000 이상인 사원을 급여가 큰 순서대로 출력하세요.
select a.emp_no, a.first_name, c.salary
from employees a join titles b on a.emp_no = b.emp_no join salaries c on a.emp_no = c.emp_no
where b.to_date = '9999-01-01' and b.title = 'Engineer' and c.to_date = '9999-01-01' and c.salary >= 40000
order by c.salary desc;

-- 문제8.
-- 현재 평균 급여가 50000이 넘는 직책을 직책, 평균급여로 평균급여가 큰 순서대로 출력하시오.
select b.title, avg(c.salary)
from employees a join titles b on a.emp_no = b.emp_no join salaries c on a.emp_no = c.emp_no
where b.to_date = '9999-01-01' and c.to_date = '9999-01-01'
group by title
having avg(c.salary) >= 50000
order by avg(c.salary) desc;

-- 문제9.
-- 현재, 부서별 평균 연봉을 연봉이 큰 부서 순서대로 출력하세요.
SELECT 
    c.dept_name, AVG(d.salary)
FROM
    employees a
        JOIN
    dept_emp b ON a.emp_no = b.emp_no
        JOIN
    departments c ON b.dept_no = c.dept_no
        JOIN
    salaries d ON a.emp_no = d.emp_no
WHERE
    b.to_date = '9999-01-01'
        AND d.to_date = '9999-01-01'
GROUP BY c.dept_name
ORDER BY AVG(d.salary) DESC;

-- 문제10.
-- 현재, 직책별 평균 연봉을 연봉이 큰 직책 순서대로 출력하세요.
select b.title, avg(c.salary)
from employees a join titles b on a.emp_no = b.emp_no join salaries c on a.emp_no = c.emp_no
where b.to_date = '9999-01-01' and c.to_date = '9999-01-01'
group by b.title
order by avg(c.salary) desc;


