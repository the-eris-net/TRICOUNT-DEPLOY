select s.id   as settlement_id,
       s.name as settlement_name
from settlement_participant sp
         join settlement s on s.id = sp.settlement_id;

select s.id       as settlement_id,
       s.name     as settlement_name,
       m.id       as member_id,
       m.login_id as member_login_id,
       m.name     as member_name
from (select * from settlement_participant where member_id = 3) spm
         join settlement_participant sp on spm.settlement_id = sp.settlement_id
         join settlement s on s.id = sp.settlement_id
         join member m on sp.member_id = m.id;

select s.id                as settlement_id,
       s.name              as settlement_name,
       m.id                as member_id,
       m.login_id          as member_login_id,
       m.name              as member_name,
       e.id                as expense_id,
       e.name              as expense_name,
       e.amount            as expense_amount,
       e.expense_date_time as expense_expense_date_time
from settlement s
         inner join expense e on s.id = e.settlement_id
         inner join member m on e.payer_member_id = m.id
where s.id = 1;

select e.id                as expense_id,
       e.name              as expense_name,
       e.amount            as expense_amount,
       e.settlement_id     as expense_settlement_id,
       e.expense_date_time as expense_expense_date_time,
       m.id                as member_id,
       m.login_id          as member_login_id,
       m.name              as member_name
from expense e
inner join member m on e.payer_member_id = m.id
where e.payer_member_id = 2;