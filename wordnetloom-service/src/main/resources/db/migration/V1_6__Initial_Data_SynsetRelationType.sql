SET FOREIGN_KEY_CHECKS = 0;

# hiponimia
insert into localised(id) values(396);
insert into localised(id) values(397);
insert into localised(id) values(398);
insert into localised(id) values(399);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(10, 396, 397, 398, 399, NULL, 11, 1);
insert into localised_strings(id, strings_KEY, strings) values(396, 'pl', 'hiponimia');
insert into localised_strings(id, strings_KEY, strings) values(397, 'pl', 'hipo');
insert into localised_strings(id, strings_KEY, strings) values(398, 'pl', 'Hipo-/hiperonimia to podstawowa relacja kształtujaca hierarchiczną strukturę słownictwa. Jest to relacja, którą można określić jako implikację jednostronną. Hiperonimia jest relacją między jednostkami należącymi do tej samej klasy fleksyjnej. Warunek: Jednostki leksykalne synsetu X stanowią rodzaj jednostek synsetu Y, istnieje jeszcze co najmniej jeden synset Z, którego jednostki są rodzajem jednostek leksykalnych synsetu Y.');
insert into localised_strings(id, strings_KEY, strings) values(399, 'pl', '<x#> jest hiponimem <y#>');
insert into localised_strings(id, strings_KEY, strings) values(396, 'en', 'hiponimia');
insert into localised_strings(id, strings_KEY, strings) values(397, 'en', 'hipo');
insert into localised_strings(id, strings_KEY, strings) values(398, 'en', 'Hipo-/hiperonimia to podstawowa relacja kształtujaca hierarchiczną strukturę słownictwa. Jest to relacja, którą można określić jako implikację jednostronną. Hiperonimia jest relacją między jednostkami należącymi do tej samej klasy fleksyjnej. Warunek: Jednostki leksykalne synsetu X stanowią rodzaj jednostek synsetu Y, istnieje jeszcze co najmniej jeden synset Z, którego jednostki są rodzajem jednostek leksykalnych synsetu Y.');
insert into localised_strings(id, strings_KEY, strings) values(399, 'en', '<x#> jest hiponimem <y#>');

# hiperonimia
insert into localised(id) values(400);
insert into localised(id) values(401);
insert into localised(id) values(402);
insert into localised(id) values(403);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(11, 400, 401, 402, 403, NULL, 10, 1);
insert into localised_strings(id, strings_KEY, strings) values(400, 'pl', 'hiperonimia');
insert into localised_strings(id, strings_KEY, strings) values(401, 'pl', 'hipero');
insert into localised_strings(id, strings_KEY, strings) values(402, 'pl', 'Hipo-/hiperonimia to podstawowa relacja kształtująca hierarchiczną strukturę słownictwa.\nJest to relacja, którą można określić jako implikację jednostronną. Hiperonimia jest relacją między jednostkami należącymi do tej samej klasy fleksyjnej.');
insert into localised_strings(id, strings_KEY, strings) values(403, 'pl', '<x#> jest hiperonimem <y#>');
insert into localised_strings(id, strings_KEY, strings) values(400, 'en', 'hiperonimia');
insert into localised_strings(id, strings_KEY, strings) values(401, 'en', 'hipero');
insert into localised_strings(id, strings_KEY, strings) values(402, 'en', 'Hipo-/hiperonimia to podstawowa relacja kształtująca hierarchiczną strukturę słownictwa.\nJest to relacja, którą można określić jako implikację jednostronną. Hiperonimia jest relacją między jednostkami należącymi do tej samej klasy fleksyjnej.');
insert into localised_strings(id, strings_KEY, strings) values(403, 'en', '<x#> jest hiperonimem <y#>');

# meronimia
insert into localised(id) values(404);
insert into localised(id) values(405);
insert into localised(id) values(406);
insert into localised(id) values(407);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(14, 404, 405, 406, 407, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(404, 'pl', 'meronimia');
insert into localised_strings(id, strings_KEY, strings) values(405, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(406, 'pl', 'Mero-/holonimia jest relacją cząstkowości, często wzajemnie odwrotną, zachodzi tylko wśród rzeczowników.');
insert into localised_strings(id, strings_KEY, strings) values(407, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(404, 'en', 'meronimia');
insert into localised_strings(id, strings_KEY, strings) values(405, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(406, 'en', 'Mero-/holonimia jest relacją cząstkowości, często wzajemnie odwrotną, zachodzi tylko wśród rzeczowników.');
insert into localised_strings(id, strings_KEY, strings) values(407, 'en', '');

# holonimia
insert into localised(id) values(408);
insert into localised(id) values(409);
insert into localised(id) values(410);
insert into localised(id) values(411);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(15, 408, 409, 410, 411, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(408, 'pl', 'holonimia');
insert into localised_strings(id, strings_KEY, strings) values(409, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(410, 'pl', 'Mero-/holonimia jest relacją cząstkowości, często wzajemnie odwrotną, zachodzi tylko wśród rzeczowników.');
insert into localised_strings(id, strings_KEY, strings) values(411, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(408, 'en', 'holonimia');
insert into localised_strings(id, strings_KEY, strings) values(409, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(410, 'en', 'Mero-/holonimia jest relacją cząstkowości, często wzajemnie odwrotną, zachodzi tylko wśród rzeczowników.');
insert into localised_strings(id, strings_KEY, strings) values(411, 'en', '');

# określnik
insert into localised(id) values(412);
insert into localised(id) values(413);
insert into localised(id) values(414);
insert into localised(id) values(415);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(146, 412, 413, 414, 415, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(412, 'pl', 'określnik');
insert into localised_strings(id, strings_KEY, strings) values(413, 'pl', 'okr');
insert into localised_strings(id, strings_KEY, strings) values(414, 'pl', 'Relacją tą łączymy przymiotnik z rzeczownikiem, jeśli może on określać byty oznaczane przez ten rzeczownik i większość jego hiponimów. Np. przymiotnikiem \"kary\" możemy określać różnego rodzaju konie, stąd relacja \"kary\" --> \"koń\".');
insert into localised_strings(id, strings_KEY, strings) values(415, 'pl', '<x#> określa wyraz <y#>');
insert into localised_strings(id, strings_KEY, strings) values(412, 'en', 'określnik');
insert into localised_strings(id, strings_KEY, strings) values(413, 'en', 'okr');
insert into localised_strings(id, strings_KEY, strings) values(414, 'en', 'Relacją tą łączymy przymiotnik z rzeczownikiem, jeśli może on określać byty oznaczane przez ten rzeczownik i większość jego hiponimów. Np. przymiotnikiem \"kary\" możemy określać różnego rodzaju konie, stąd relacja \"kary\" --> \"koń\".');
insert into localised_strings(id, strings_KEY, strings) values(415, 'en', '<x#> określa wyraz <y#>');

# gradacyjność
insert into localised(id) values(416);
insert into localised(id) values(417);
insert into localised(id) values(418);
insert into localised(id) values(419);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(147, 416, 417, 418, 419, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(416, 'pl', 'gradacyjność');
insert into localised_strings(id, strings_KEY, strings) values(417, 'pl', 'grad');
insert into localised_strings(id, strings_KEY, strings) values(418, 'pl', 'Gradacyjnością łączymy różne stopnie intensywności tej samej cechy. Relacją tą łączymy tylko najbliższe stopnie natężenia cechy.');
insert into localised_strings(id, strings_KEY, strings) values(419, 'pl', '<x#> wyraża większe natężenie cechy P niż <y#>');
insert into localised_strings(id, strings_KEY, strings) values(416, 'en', 'gradacyjność');
insert into localised_strings(id, strings_KEY, strings) values(417, 'en', 'grad');
insert into localised_strings(id, strings_KEY, strings) values(418, 'en', 'Gradacyjnością łączymy różne stopnie intensywności tej samej cechy. Relacją tą łączymy tylko najbliższe stopnie natężenia cechy.');
insert into localised_strings(id, strings_KEY, strings) values(419, 'en', '<x#> wyraża większe natężenie cechy P niż <y#>');


# Princeton relations
/*
'171', '1', NULL, '173', 'Hypernym', 'Opis', 'czasownik pwn,rzeczownik pwn', '1', '', 'Hyper', '@', '62'
'172', '1', NULL, NULL, 'Instance_Hypernym', 'Opis', 'rzeczownik pwn', '0', '', 'InstHyper', '@i', '63'
'173', '1', NULL, '171', 'Hyponym', 'Opis', 'czasownik pwn,rzeczownik pwn', '1', '', 'Hypo', '~', '64'
'174', '1', NULL, '172', 'Instance_Hyponym', 'Opis', 'rzeczownik pwn', '1', '', 'InstHypo', '~i', '65'
'175', '1', NULL, NULL, 'Member_holonym', 'Opis', 'rzeczownik pwn', '0', '', 'Holo:member', '#m', '67'
'176', '1', NULL, NULL, 'Substance_holonym', 'Opis', 'rzeczownik pwn', '0', '', 'Holo:subst', '#s', '68'
'177', '1', NULL, NULL, 'Part_holonym', 'Opis', 'rzeczownik pwn', '0', '', 'Holo:part', '#p', '69'
'178', '1', NULL, NULL, 'Member_meronym', 'Opis', 'rzeczownik pwn', '0', '', 'Mero:member', '%m', '70'
'179', '1', NULL, NULL, 'Substance_meronym', 'Opis', 'rzeczownik pwn', '0', '', 'Mero:subst', '%s', '71'
'180', '1', NULL, NULL, 'Part_meronym', 'Opis', 'rzeczownik pwn', '0', '', 'Mero:part', '%p', '72'
'181', '1', NULL, NULL, 'Attribute', 'Opis', 'rzeczownik pwn,przymiotnik pwn', '0', '', 'Attr', '=', '73'
'183', '1', NULL, '184', 'Domain_of_synset_-_TOPIC', 'Opis', 'czasownik pwn,rzeczownik pwn', '1', '', 'Domain', ';c', '75'
'184', '1', NULL, NULL, 'Member_of_this_domain_-_TOPIC', 'Opis', 'czasownik pwn,rzeczownik pwn', '0', '', 'Topic', '-c', '76'
'185', '1', NULL, '186', 'Domain_of_synset_-_REGION', 'Opis', 'czasownik pwn,rzeczownik pwn', '1', '', 'Domain', ';r', '77'
'186', '1', NULL, NULL, 'Member_of_this_domain_-_REGION', 'Opis', 'czasownik pwn,rzeczownik pwn', '0', '', 'Region', '-r', '78'
'187', '1', NULL, '188', 'Domain_of_synset_-_USAGE', 'Opis', 'czasownik pwn,rzeczownik pwn', '1', '', 'Domain', ';u', '79'
'188', '1', NULL, NULL, 'Member_of_this_domain_-_USAGE', 'Opis', 'czasownik pwn,rzeczownik pwn', '0', '', 'Usage', '-u', '80'
'189', '1', NULL, NULL, 'Entailment', 'Opis', 'czasownik pwn', '0', '', 'Entailed', '*', '81'
'190', '1', NULL, NULL, 'Cause', 'Opis', 'czasownik pwn', '0', '', 'Caused', '>', '82'
'192', '1', NULL, NULL, 'Verb_Group', 'Opis', 'czasownik pwn', '0', '', 'VG', '$', '85'
'193', '1', NULL, '193', 'Similar_to', 'Opis', 'przymiotnik pwn', '1', '', 'Sim', '&', '86'
*/

SET FOREIGN_KEY_CHECKS = 1;