insert into users (email, firstname, lastname, password, role) values ('anonymous@clarin.eu', 'anonymous', '','XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=','ANONYMOUS');
update users set email='admin@clarin.eu' where id = 2;
update users set email='user@clarin.eu' where id = 1;