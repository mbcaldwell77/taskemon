use taskemon_test;

set sql_safe_updates = 0;
call set_known_good_state();
set sql_safe_updates = 1;

select * from checklist;
select * from task;

-- select * from task, checklist;								-- we selecting task and list, items on list are printing 5 times each

-- select 														-- when selecting this way items on list are printing multiple times
-- 	checklist.checklist_name, 
--     task.task_name,
--     task.due_date,
--     task.status,
--     checklist.total_completion
-- from task
-- inner join checklist
-- order by checklist_name asc;


select * from pokemon;
select * from pokemon_user;
select * from user;
select * from user_auth;
select * from auth;

delete from task where task_id = 34;

select * from checklist where checklist_id = 4;
