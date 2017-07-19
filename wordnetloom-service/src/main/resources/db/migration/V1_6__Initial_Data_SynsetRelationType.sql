SET FOREIGN_KEY_CHECKS = 0;

# hiponimia
insert into localised(id) values(396);
insert into localised(id) values(397);
insert into localised(id) values(398);
insert into localised(id) values(399);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
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
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
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
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
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
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
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
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
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
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(147, 416, 417, 418, 419, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(416, 'pl', 'gradacyjność');
insert into localised_strings(id, strings_KEY, strings) values(417, 'pl', 'grad');
insert into localised_strings(id, strings_KEY, strings) values(418, 'pl', 'Gradacyjnością łączymy różne stopnie intensywności tej samej cechy. Relacją tą łączymy tylko najbliższe stopnie natężenia cechy.');
insert into localised_strings(id, strings_KEY, strings) values(419, 'pl', '<x#> wyraża większe natężenie cechy P niż <y#>');
insert into localised_strings(id, strings_KEY, strings) values(416, 'en', 'gradacyjność');
insert into localised_strings(id, strings_KEY, strings) values(417, 'en', 'grad');
insert into localised_strings(id, strings_KEY, strings) values(418, 'en', 'Gradacyjnością łączymy różne stopnie intensywności tej samej cechy. Relacją tą łączymy tylko najbliższe stopnie natężenia cechy.');
insert into localised_strings(id, strings_KEY, strings) values(419, 'en', '<x#> wyraża większe natężenie cechy P niż <y#>');

# wartość cech
insert into localised(id) values(420);
insert into localised(id) values(421);
insert into localised(id) values(422);
insert into localised(id) values(423);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(145, 420, 421, 422, 423, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(420, 'pl', 'wartość cech');
insert into localised_strings(id, strings_KEY, strings) values(421, 'pl', 'war');
insert into localised_strings(id, strings_KEY, strings) values(422, 'pl', 'Relacją wartość cechy łączymy przymiotniki lub przysłówki oznaczające jakiś stopień natężenia cechy wyrażonej rzeczownikiem.');
insert into localised_strings(id, strings_KEY, strings) values(423, 'pl', '<x#> jest wartością cechy <y#>');
insert into localised_strings(id, strings_KEY, strings) values(420, 'en', 'wartość cech');
insert into localised_strings(id, strings_KEY, strings) values(421, 'en', 'war');
insert into localised_strings(id, strings_KEY, strings) values(422, 'en', 'Relacją wartość cechy łączymy przymiotniki lub przysłówki oznaczające jakiś stopień natężenia cechy wyrażonej rzeczownikiem.');
insert into localised_strings(id, strings_KEY, strings) values(423, 'en', '<x#> jest wartością cechy <y#>');

# część
insert into localised(id) values(424);
insert into localised(id) values(425);
insert into localised(id) values(426);
insert into localised(id) values(427);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(20, 424, 425, 426, 427, 14, 25, 0);
insert into localised_strings(id, strings_KEY, strings) values(424, 'pl', 'część');
insert into localised_strings(id, strings_KEY, strings) values(425, 'pl', 'mero:cz');
insert into localised_strings(id, strings_KEY, strings) values(426, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(427, 'pl', '<x#> jest meronimem (typu część) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(424, 'en', 'część');
insert into localised_strings(id, strings_KEY, strings) values(425, 'en', 'mero:cz');
insert into localised_strings(id, strings_KEY, strings) values(426, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(427, 'en', '<x#> jest meronimem (typu część) dla <y#>');

# porcja
insert into localised(id) values(428);
insert into localised(id) values(429);
insert into localised(id) values(430);
insert into localised(id) values(431);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(21, 428, 429, 430, 431, 14, 26, 0);
insert into localised_strings(id, strings_KEY, strings) values(428, 'pl', 'porcja');
insert into localised_strings(id, strings_KEY, strings) values(429, 'pl', 'mero:porc');
insert into localised_strings(id, strings_KEY, strings) values(430, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(431, 'pl', '<x#> jest meronimem (typu porcja) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(428, 'en', 'porcja');
insert into localised_strings(id, strings_KEY, strings) values(429, 'en', 'mero:porc');
insert into localised_strings(id, strings_KEY, strings) values(430, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(431, 'en', '<x#> jest meronimem (typu porcja) dla <y#>');

# miejsce
insert into localised(id) values(432);
insert into localised(id) values(433);
insert into localised(id) values(434);
insert into localised(id) values(435);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(22, 432, 433, 434, 435, 14, 27, 0);
insert into localised_strings(id, strings_KEY, strings) values(432, 'pl', 'miejsce');
insert into localised_strings(id, strings_KEY, strings) values(433, 'pl', 'mero:msc');
insert into localised_strings(id, strings_KEY, strings) values(434, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(435, 'pl', '<x#> jest meronimem (typu miejsce) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(432, 'en', 'miejsce');
insert into localised_strings(id, strings_KEY, strings) values(433, 'en', 'mero:msc');
insert into localised_strings(id, strings_KEY, strings) values(434, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(435, 'en', '<x#> jest meronimem (typu miejsce) dla <y#>');

# element kolekcji
insert into localised(id) values(436);
insert into localised(id) values(437);
insert into localised(id) values(438);
insert into localised(id) values(439);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(23, 436, 437, 438, 439, 14, 28, 0);
insert into localised_strings(id, strings_KEY, strings) values(436, 'pl', 'element kolekcji');
insert into localised_strings(id, strings_KEY, strings) values(437, 'pl', 'mero:el');
insert into localised_strings(id, strings_KEY, strings) values(438, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(439, 'pl', '<x#> jest meronimem (typu element) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(436, 'en', 'element kolekcji');
insert into localised_strings(id, strings_KEY, strings) values(437, 'en', 'mero:el');
insert into localised_strings(id, strings_KEY, strings) values(438, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(439, 'en', '<x#> jest meronimem (typu element) dla <y#>');

#  materiał
insert into localised(id) values(440);
insert into localised(id) values(441);
insert into localised(id) values(442);
insert into localised(id) values(443);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(24, 440, 441, 442, 443, 14, 29, 0);
insert into localised_strings(id, strings_KEY, strings) values(440, 'pl', 'materiał');
insert into localised_strings(id, strings_KEY, strings) values(441, 'pl', 'mero:mat');
insert into localised_strings(id, strings_KEY, strings) values(442, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(443, 'pl', '<x#> jest meronimem (typu materiał) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(440, 'en', 'materiał');
insert into localised_strings(id, strings_KEY, strings) values(441, 'en', 'mero:mat');
insert into localised_strings(id, strings_KEY, strings) values(442, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(443, 'en', '<x#> jest meronimem (typu materiał) dla <y#>');

# część
insert into localised(id) values(444);
insert into localised(id) values(445);
insert into localised(id) values(446);
insert into localised(id) values(447);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(25, 444, 445, 446, 447, 15, 20, 0);
insert into localised_strings(id, strings_KEY, strings) values(444, 'pl', 'część');
insert into localised_strings(id, strings_KEY, strings) values(445, 'pl', 'holo:cz');
insert into localised_strings(id, strings_KEY, strings) values(446, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(447, 'pl', '<x#> jest holonimem (typu część) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(444, 'en', 'część');
insert into localised_strings(id, strings_KEY, strings) values(445, 'en', 'holo:cz');
insert into localised_strings(id, strings_KEY, strings) values(446, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(447, 'en', '<x#> jest holonimem (typu część) dla <y#>');

# porcja
insert into localised(id) values(448);
insert into localised(id) values(449);
insert into localised(id) values(450);
insert into localised(id) values(451);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(26, 448, 449, 450, 451, 15, 21, 0);
insert into localised_strings(id, strings_KEY, strings) values(448, 'pl', 'porcja');
insert into localised_strings(id, strings_KEY, strings) values(449, 'pl', 'holo:porc');
insert into localised_strings(id, strings_KEY, strings) values(450, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(451, 'pl', '<x#> jest holonimem (typu porcja) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(448, 'en', 'porcja');
insert into localised_strings(id, strings_KEY, strings) values(449, 'en', 'holo:porc');
insert into localised_strings(id, strings_KEY, strings) values(450, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(451, 'en', '<x#> jest holonimem (typu porcja) dla <y#>');

# miejsc
insert into localised(id) values(452);
insert into localised(id) values(453);
insert into localised(id) values(454);
insert into localised(id) values(455);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(27, 452, 453, 454, 455, 15, 22, 0);
insert into localised_strings(id, strings_KEY, strings) values(452, 'pl', 'miejsc');
insert into localised_strings(id, strings_KEY, strings) values(453, 'pl', 'holo:msc');
insert into localised_strings(id, strings_KEY, strings) values(454, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(455, 'pl', '<x#> jest holonimem (typu miejsce) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(452, 'en', 'miejsc');
insert into localised_strings(id, strings_KEY, strings) values(453, 'en', 'holo:msc');
insert into localised_strings(id, strings_KEY, strings) values(454, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(455, 'en', '<x#> jest holonimem (typu miejsce) dla <y#>');

# element kolekcji
insert into localised(id) values(456);
insert into localised(id) values(457);
insert into localised(id) values(458);
insert into localised(id) values(459);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(28, 456, 457, 458, 459, 15, 23, 0);
insert into localised_strings(id, strings_KEY, strings) values(456, 'pl', 'element kolekcji');
insert into localised_strings(id, strings_KEY, strings) values(457, 'pl', 'holo:el');
insert into localised_strings(id, strings_KEY, strings) values(458, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(459, 'pl', '<x#> jest holonimem (typu element) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(456, 'en', 'element kolekcji');
insert into localised_strings(id, strings_KEY, strings) values(457, 'en', 'holo:el');
insert into localised_strings(id, strings_KEY, strings) values(458, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(459, 'en', '<x#> jest holonimem (typu element) dla <y#>');

# materiał
insert into localised(id) values(460);
insert into localised(id) values(461);
insert into localised(id) values(462);
insert into localised(id) values(463);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(29, 460, 461, 462, 463, 15, 24, 0);
insert into localised_strings(id, strings_KEY, strings) values(460, 'pl', 'materiał');
insert into localised_strings(id, strings_KEY, strings) values(461, 'pl', 'holo:mat');
insert into localised_strings(id, strings_KEY, strings) values(462, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(463, 'pl', '<x#> jest holonimem (typu materiał) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(460, 'en', 'materiał');
insert into localised_strings(id, strings_KEY, strings) values(461, 'en', 'holo:mat');
insert into localised_strings(id, strings_KEY, strings) values(462, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(463, 'en', '<x#> jest holonimem (typu materiał) dla <y#>');

# element taksonomiczny mero
insert into localised(id) values(464);
insert into localised(id) values(465);
insert into localised(id) values(466);
insert into localised(id) values(467);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(64, 464, 465, 466, 467, 14, 65, 0);
insert into localised_strings(id, strings_KEY, strings) values(464, 'pl', 'element taksonomiczny');
insert into localised_strings(id, strings_KEY, strings) values(465, 'pl', 'mero:taks');
insert into localised_strings(id, strings_KEY, strings) values(466, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(467, 'pl', '<x#> jest meronimem (typu element) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(464, 'en', 'element taksonomiczny');
insert into localised_strings(id, strings_KEY, strings) values(465, 'en', 'mero:taks');
insert into localised_strings(id, strings_KEY, strings) values(466, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(467, 'en', '<x#> jest meronimem (typu element) dla <y#>');

# element taksonomiczny holo
insert into localised(id) values(468);
insert into localised(id) values(469);
insert into localised(id) values(470);
insert into localised(id) values(471);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(65, 468, 469, 470, 471, 15, 64, 0);
insert into localised_strings(id, strings_KEY, strings) values(468, 'pl', 'element taksonomiczny');
insert into localised_strings(id, strings_KEY, strings) values(469, 'pl', 'holo:taks');
insert into localised_strings(id, strings_KEY, strings) values(470, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(471, 'pl', '<x#> jest holonimem (typu element) dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(468, 'en', 'element taksonomiczny');
insert into localised_strings(id, strings_KEY, strings) values(469, 'en', 'holo:taks');
insert into localised_strings(id, strings_KEY, strings) values(470, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(471, 'en', '<x#> jest holonimem (typu element) dla <y#>');

# mieszkaniec
insert into localised(id) values(472);
insert into localised(id) values(473);
insert into localised(id) values(474);
insert into localised(id) values(475);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(58, 472, 473, 474, 475, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(472, 'pl', 'mieszkaniec');
insert into localised_strings(id, strings_KEY, strings) values(473, 'pl', 'mieszk');
insert into localised_strings(id, strings_KEY, strings) values(474, 'pl', 'Relacja wynikająca z derywacji: bycie mieszkańcem/obywatelem jakiegoś kraju, regionu lub miasta. Relacja wiąże synsety, z których przynajmniej dwie jednostki musi wiązać relacja derywacyjna (po jednym z każdego).');
insert into localised_strings(id, strings_KEY, strings) values(475, 'pl', 'wyraz <x#> określa mieszkańca miejscowości/regionu <y#>');
insert into localised_strings(id, strings_KEY, strings) values(472, 'en', 'mieszkaniec');
insert into localised_strings(id, strings_KEY, strings) values(473, 'en', 'mieszk');
insert into localised_strings(id, strings_KEY, strings) values(474, 'en', 'Relacja wynikająca z derywacji: bycie mieszkańcem/obywatelem jakiegoś kraju, regionu lub miasta. Relacja wiąże synsety, z których przynajmniej dwie jednostki musi wiązać relacja derywacyjna (po jednym z każdego).');
insert into localised_strings(id, strings_KEY, strings) values(475, 'en', 'wyraz <x#> określa mieszkańca miejscowości/regionu <y#>');

# bliskoznaczność
insert into localised(id) values(476);
insert into localised(id) values(477);
insert into localised(id) values(478);
insert into localised(id) values(479);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(60, 476, 477, 478, 479, NULL, 60, 1);
insert into localised_strings(id, strings_KEY, strings) values(476, 'pl', 'bliskoznaczność');
insert into localised_strings(id, strings_KEY, strings) values(477, 'pl', 'blzn');
insert into localised_strings(id, strings_KEY, strings) values(478, 'pl', 'Wyrazy bliskoznaczne różnią się w sposób istotny rejestrem, przy czym wszystkie relacje synsetów mają takie same z wyjątkiem relacji hiperonimii (= mogą różnić się hiponimami).');
insert into localised_strings(id, strings_KEY, strings) values(479, 'pl', '<x#> jest bliskoznaczne z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(476, 'en', 'bliskoznaczność');
insert into localised_strings(id, strings_KEY, strings) values(477, 'en', 'blzn');
insert into localised_strings(id, strings_KEY, strings) values(478, 'en', 'Wyrazy bliskoznaczne różnią się w sposób istotny rejestrem, przy czym wszystkie relacje synsetów mają takie same z wyjątkiem relacji hiperonimii (= mogą różnić się hiponimami).');
insert into localised_strings(id, strings_KEY, strings) values(479, 'en', '<x#> jest bliskoznaczne z <y#>');

# kauzacja
insert into localised(id) values(480);
insert into localised(id) values(481);
insert into localised(id) values(482);
insert into localised(id) values(483);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(80, 480, 481, 482, 483, NULL, NULL, 1);
insert into localised_strings(id, strings_KEY, strings) values(480, 'pl', 'kauzacja');
insert into localised_strings(id, strings_KEY, strings) values(481, 'pl', 'kauz');
insert into localised_strings(id, strings_KEY, strings) values(482, 'pl', 'Czasownik kauzujący można sparafrazować za pomocą wyrażenia \'(s)powodować, że...\'. Relacją tą łączymy czasowniki z czasownikami, przymiotnikami i rzeczownikami.');
insert into localised_strings(id, strings_KEY, strings) values(483, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(480, 'en', 'kauzacja');
insert into localised_strings(id, strings_KEY, strings) values(481, 'en', 'kauz');
insert into localised_strings(id, strings_KEY, strings) values(482, 'en', 'Czasownik kauzujący można sparafrazować za pomocą wyrażenia \'(s)powodować, że...\'. Relacją tą łączymy czasowniki z czasownikami, przymiotnikami i rzeczownikami.');
insert into localised_strings(id, strings_KEY, strings) values(483, 'en', '');

# kauzacja NDK-NDK
insert into localised(id) values(484);
insert into localised(id) values(485);
insert into localised(id) values(486);
insert into localised(id) values(487);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(120, 484, 485, 486, 487, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(484, 'pl', 'kauzacja NDK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(485, 'pl', 'kauz_ndk-ndk');
insert into localised_strings(id, strings_KEY, strings) values(486, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(487, 'pl', '<x#> wchodzi w relację kauzacji NDK-NDK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(484, 'en', 'kauzacja NDK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(485, 'en', 'kauz_ndk-ndk');
insert into localised_strings(id, strings_KEY, strings) values(486, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(487, 'en', '<x#> wchodzi w relację kauzacji NDK-NDK z <y#>');

# kauzacja DK-DK
insert into localised(id) values(488);
insert into localised(id) values(489);
insert into localised(id) values(490);
insert into localised(id) values(491);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(121, 488, 489, 490, 491, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(488, 'pl', 'kauzacja DK-DK');
insert into localised_strings(id, strings_KEY, strings) values(489, 'pl', 'kauz_dk-dk');
insert into localised_strings(id, strings_KEY, strings) values(490, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(491, 'pl', '<x#> wchodzi w relację kauzacji DK-DK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(488, 'en', 'kauzacja DK-DK');
insert into localised_strings(id, strings_KEY, strings) values(489, 'en', 'kauz_dk-dk');
insert into localised_strings(id, strings_KEY, strings) values(490, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(491, 'en', '<x#> wchodzi w relację kauzacji DK-DK z <y#>');

# kauzacja stanu DK-NDK
insert into localised(id) values(492);
insert into localised(id) values(493);
insert into localised(id) values(494);
insert into localised(id) values(495);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(122, 492, 493, 494, 495, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(492, 'pl', 'kauzacja stanu DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(493, 'pl', 'kauz_st_dk-ndk');
insert into localised_strings(id, strings_KEY, strings) values(494, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(495, 'pl', '<x#> wchodzi w relację kauzacji stanu DK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(492, 'en', 'kauzacja stanu DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(493, 'en', 'kauz_st_dk-ndk');
insert into localised_strings(id, strings_KEY, strings) values(494, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(495, 'en', '<x#> wchodzi w relację kauzacji stanu DK z <y#>');

# kauzacja procesu DK-Adj
insert into localised(id) values(496);
insert into localised(id) values(497);
insert into localised(id) values(498);
insert into localised(id) values(499);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(124, 496, 497, 498, 499, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(496, 'pl', 'kauzacja procesu DK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(497, 'pl', 'kauz_pr_dk-Adj');
insert into localised_strings(id, strings_KEY, strings) values(498, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(499, 'pl', '<x#> wchodzi w relację kauzacji procesu DK-Adj z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(496, 'en', 'kauzacja procesu DK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(497, 'en', 'kauz_pr_dk-Adj');
insert into localised_strings(id, strings_KEY, strings) values(498, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(499, 'en', '<x#> wchodzi w relację kauzacji procesu DK-Adj z <y#>');

# kauzacja procesu DK-N
insert into localised(id) values(500);
insert into localised(id) values(501);
insert into localised(id) values(502);
insert into localised(id) values(503);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(125, 500, 501, 502, 503, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(500, 'pl', 'kauzacja procesu DK-N');
insert into localised_strings(id, strings_KEY, strings) values(501, 'pl', 'kauz_pr_dk-N');
insert into localised_strings(id, strings_KEY, strings) values(502, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(503, 'pl', '<x#> wchodzi w relację kauzacji procesu DK-N z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(500, 'en', 'kauzacja procesu DK-N');
insert into localised_strings(id, strings_KEY, strings) values(501, 'en', 'kauz_pr_dk-N');
insert into localised_strings(id, strings_KEY, strings) values(502, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(503, 'en', '<x#> wchodzi w relację kauzacji procesu DK-N z <y#>');

# kauzacja procesu NDK-Adj
insert into localised(id) values(504);
insert into localised(id) values(505);
insert into localised(id) values(506);
insert into localised(id) values(507);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(126, 504, 505, 506, 507, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(504, 'pl', 'kauzacja procesu NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(505, 'pl', 'kauz_pr_ndk-Adj');
insert into localised_strings(id, strings_KEY, strings) values(506, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(507, 'pl', '<x#> wchodzi w relację kauzacji procesu NDK-Adj z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(504, 'en', 'kauzacja procesu NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(505, 'en', 'kauz_pr_ndk-Adj');
insert into localised_strings(id, strings_KEY, strings) values(506, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(507, 'en', '<x#> wchodzi w relację kauzacji procesu NDK-Adj z <y#>');

# kauzacja procesu NDK-Adj
insert into localised(id) values(508);
insert into localised(id) values(509);
insert into localised(id) values(510);
insert into localised(id) values(511);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(127, 508, 509, 510, 511, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(508, 'pl', 'kauzacja procesu NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(509, 'pl', 'kauz_pr_ndk-N');
insert into localised_strings(id, strings_KEY, strings) values(510, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(511, 'pl', '<x#> wchodzi w relację kauzacji procesu NDK-N z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(508, 'en', 'kauzacja procesu NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(509, 'en', 'kauz_pr_ndk-N');
insert into localised_strings(id, strings_KEY, strings) values(510, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(511, 'en', '<x#> wchodzi w relację kauzacji procesu NDK-N z <y#>');

# procesywność
insert into localised(id) values(512);
insert into localised(id) values(513);
insert into localised(id) values(514);
insert into localised(id) values(515);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(86, 512, 513, 514, 515, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(512, 'pl', 'procesywność');
insert into localised_strings(id, strings_KEY, strings) values(513, 'pl', 'p_proc');
insert into localised_strings(id, strings_KEY, strings) values(514, 'pl', 'Czasownik procesywny można sparafrazować za pomocą wyrażenia \'stawać się jakimś, czymś\'. Relacja ta łączy czasowniki z przymiotnikami i rzeczownikami.');
insert into localised_strings(id, strings_KEY, strings) values(515, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(512, 'en', 'procesywność');
insert into localised_strings(id, strings_KEY, strings) values(513, 'en', 'p_proc');
insert into localised_strings(id, strings_KEY, strings) values(514, 'en', 'Czasownik procesywny można sparafrazować za pomocą wyrażenia \'stawać się jakimś, czymś\'. Relacja ta łączy czasowniki z przymiotnikami i rzeczownikami.');
insert into localised_strings(id, strings_KEY, strings) values(515, 'en', '');

# procesywność V_DK-Adj
insert into localised(id) values(516);
insert into localised(id) values(517);
insert into localised(id) values(518);
insert into localised(id) values(519);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(118, 516, 517, 518, 519, 86, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(516, 'pl', 'procesywność V_DK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(517, 'pl', 'pr_V_DK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(518, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(519, 'pl', '<x#> wchodzi w relację procesywności DK-Adj z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(516, 'en', 'procesywność V_DK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(517, 'en', 'pr_V_DK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(518, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(519, 'en', '<x#> wchodzi w relację procesywności DK-Adj z <y#>');

# procesywność V_DK-N
insert into localised(id) values(520);
insert into localised(id) values(521);
insert into localised(id) values(522);
insert into localised(id) values(523);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(119, 520, 521, 522, 523, 86, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(520, 'pl', 'procesywność V_DK-N');
insert into localised_strings(id, strings_KEY, strings) values(521, 'pl', 'pr_V_DK-N');
insert into localised_strings(id, strings_KEY, strings) values(522, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(523, 'pl', '<x#> wchodzi w relację procesywności DK-N z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(520, 'en', 'procesywność V_DK-N');
insert into localised_strings(id, strings_KEY, strings) values(521, 'en', 'pr_V_DK-N');
insert into localised_strings(id, strings_KEY, strings) values(522, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(523, 'en', '<x#> wchodzi w relację procesywności DK-N z <y#>');

# procesywność V_NDK-Adj
insert into localised(id) values(524);
insert into localised(id) values(525);
insert into localised(id) values(526);
insert into localised(id) values(527);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(89, 524, 525, 526, 527, 86, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(524, 'pl', 'procesywność V_NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(525, 'pl', 'pr_V_NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(526, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(527, 'pl', '<x#> wchodzi w relację procesywności NDK-Adj z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(524, 'en', 'procesywność V_NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(525, 'en', 'pr_V_NDK-Adj');
insert into localised_strings(id, strings_KEY, strings) values(526, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(527, 'en', '<x#> wchodzi w relację procesywności NDK-Adj z <y#>');

# procesywność V_NDK-N
insert into localised(id) values(528);
insert into localised(id) values(529);
insert into localised(id) values(530);
insert into localised(id) values(531);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(90, 528, 529, 530, 531, 86, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(528, 'pl', 'procesywność V_NDK-N');
insert into localised_strings(id, strings_KEY, strings) values(529, 'pl', 'pr_V_NDK-N');
insert into localised_strings(id, strings_KEY, strings) values(530, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(531, 'pl', '<x#> wchodzi w relację procesywności NDK-N z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(528, 'en', 'procesywność V_NDK-N');
insert into localised_strings(id, strings_KEY, strings) values(529, 'en', 'pr_V_NDK-N');
insert into localised_strings(id, strings_KEY, strings) values(530, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(531, 'en', '<x#> wchodzi w relację procesywności NDK-N z <y#>');

# stanowość
insert into localised(id) values(532);
insert into localised(id) values(533);
insert into localised(id) values(534);
insert into localised(id) values(535);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(91, 532, 533, 534, 535, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(532, 'pl', 'stanowość');
insert into localised_strings(id, strings_KEY, strings) values(533, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(534, 'pl', 'Stanowość to relacja łącząca czasowniki stanowe z przymiotnikami cechującymi ten stan. Taki czasownik stanowy może być sparafrazowany za pomocą wyrażenia \'być jakimś, kimś|czymś\'');
insert into localised_strings(id, strings_KEY, strings) values(535, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(532, 'en', 'stanowość');
insert into localised_strings(id, strings_KEY, strings) values(533, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(534, 'en', 'Stanowość to relacja łącząca czasowniki stanowe z przymiotnikami cechującymi ten stan. Taki czasownik stanowy może być sparafrazowany za pomocą wyrażenia \'być jakimś, kimś|czymś\'');
insert into localised_strings(id, strings_KEY, strings) values(535, 'en', '');

# stanowość V-Adj
insert into localised(id) values(536);
insert into localised(id) values(537);
insert into localised(id) values(538);
insert into localised(id) values(539);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(92, 536, 537, 538, 539, 91, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(536, 'pl', 'stanowość V-Adj');
insert into localised_strings(id, strings_KEY, strings) values(537, 'pl', 'st_V-Adj');
insert into localised_strings(id, strings_KEY, strings) values(538, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(539, 'pl', '<x#> wchodzi w relację stanowości V-Adj z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(536, 'en', 'stanowość V-Adj');
insert into localised_strings(id, strings_KEY, strings) values(537, 'en', 'st_V-Adj');
insert into localised_strings(id, strings_KEY, strings) values(538, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(539, 'en', '<x#> wchodzi w relację stanowości V-Adj z <y#>');

# stanowość V-N
insert into localised(id) values(540);
insert into localised(id) values(541);
insert into localised(id) values(542);
insert into localised(id) values(543);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(93, 540, 541, 542, 543, 91, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(540, 'pl', 'stanowość V-N');
insert into localised_strings(id, strings_KEY, strings) values(541, 'pl', 'st_V-N');
insert into localised_strings(id, strings_KEY, strings) values(542, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(543, 'pl', '<x#> wchodzi w relację stanowości V-N z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(540, 'en', 'stanowość V-N');
insert into localised_strings(id, strings_KEY, strings) values(541, 'en', 'st_V-N');
insert into localised_strings(id, strings_KEY, strings) values(542, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(543, 'en', '<x#> wchodzi w relację stanowości V-N z <y#>');

# typ
insert into localised(id) values(544);
insert into localised(id) values(545);
insert into localised(id) values(546);
insert into localised(id) values(547);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(106, 544, 545, 546, 547, NULL, 107, 1);
insert into localised_strings(id, strings_KEY, strings) values(544, 'pl', 'typ');
insert into localised_strings(id, strings_KEY, strings) values(545, 'pl', 'rel.typu');
insert into localised_strings(id, strings_KEY, strings) values(546, 'pl', 'Relacja pomiędzy nazwą własną a wyrazem pospolitym nie może być określona za pomocą hiponimii. Wprowadzamy dlatego specjalne relacje \"typ\" i \"egzemplarz\". Relacja \"typ\" łączy  nazwę własną i wyraz pospolity, relacja \"egzemplarz\" prowadzi od wyrazu pospolitego, do przedstawiciela danej klasy, tj. do nazwy własnej.');
insert into localised_strings(id, strings_KEY, strings) values(547, 'pl', 'typem dla <x#> jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(544, 'en', 'typ');
insert into localised_strings(id, strings_KEY, strings) values(545, 'en', 'rel.typu');
insert into localised_strings(id, strings_KEY, strings) values(546, 'en', 'Relacja pomiędzy nazwą własną a wyrazem pospolitym nie może być określona za pomocą hiponimii. Wprowadzamy dlatego specjalne relacje \"typ\" i \"egzemplarz\". Relacja \"typ\" łączy  nazwę własną i wyraz pospolity, relacja \"egzemplarz\" prowadzi od wyrazu pospolitego, do przedstawiciela danej klasy, tj. do nazwy własnej.');
insert into localised_strings(id, strings_KEY, strings) values(547, 'en', 'typem dla <x#> jest <y#>');

# egzemplarz
insert into localised(id) values(548);
insert into localised(id) values(549);
insert into localised(id) values(550);
insert into localised(id) values(551);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(107, 548, 549, 550, 551, NULL, 106, 1);
insert into localised_strings(id, strings_KEY, strings) values(548, 'pl', 'egzemplarz');
insert into localised_strings(id, strings_KEY, strings) values(549, 'pl', 'rel.egz.');
insert into localised_strings(id, strings_KEY, strings) values(550, 'pl', 'Relacja pomiędzy nazwą własną a wyrazem pospolitym nie może być określona za pomocą hiponimii. Wprowadzamy dlatego specjalne relacje \"typ\" i \"egzemplarz\". Relacja \"typ\" łączy  nazwę własną i wyraz pospolity, relacja \"egzemplarz\" prowadzi od wyrazu pospolitego, do przedstawiciela danej klasy, tj. do nazwy własnej.');
insert into localised_strings(id, strings_KEY, strings) values(551, 'pl', '<y#> jest egzemplarzem dla <x#>');
insert into localised_strings(id, strings_KEY, strings) values(548, 'en', 'egzemplarz');
insert into localised_strings(id, strings_KEY, strings) values(549, 'en', 'rel.egz.');
insert into localised_strings(id, strings_KEY, strings) values(550, 'en', 'Relacja pomiędzy nazwą własną a wyrazem pospolitym nie może być określona za pomocą hiponimii. Wprowadzamy dlatego specjalne relacje \"typ\" i \"egzemplarz\". Relacja \"typ\" łączy  nazwę własną i wyraz pospolity, relacja \"egzemplarz\" prowadzi od wyrazu pospolitego, do przedstawiciela danej klasy, tj. do nazwy własnej.');
insert into localised_strings(id, strings_KEY, strings) values(551, 'en', '<y#> jest egzemplarzem dla <x#>');

# fuzzynimia_synsetów
insert into localised(id) values(552);
insert into localised(id) values(553);
insert into localised(id) values(554);
insert into localised(id) values(555);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(108, 552, 553, 554, 555, NULL, 108, 1);
insert into localised_strings(id, strings_KEY, strings) values(552, 'pl', 'fuzzynimia_synsetów');
insert into localised_strings(id, strings_KEY, strings) values(553, 'pl', 'fzn');
insert into localised_strings(id, strings_KEY, strings) values(554, 'pl', 'Fuzzynimia jest relacją, którą można by zdefiniować jako \"jest związane z...\" Może łączyć różne części mowy.');
insert into localised_strings(id, strings_KEY, strings) values(555, 'pl', '<x#> jest w relacji fuzzynimii do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(552, 'en', 'fuzzynimia_synsetów');
insert into localised_strings(id, strings_KEY, strings) values(553, 'en', 'fzn');
insert into localised_strings(id, strings_KEY, strings) values(554, 'en', 'Fuzzynimia jest relacją, którą można by zdefiniować jako \"jest związane z...\" Może łączyć różne części mowy.');
insert into localised_strings(id, strings_KEY, strings) values(555, 'en', '<x#> jest w relacji fuzzynimii do <y#>');

# meronimia_czasownikowa
insert into localised(id) values(556);
insert into localised(id) values(557);
insert into localised(id) values(558);
insert into localised(id) values(559);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(112, 556, 557, 558, 559, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(556, 'pl', 'meronimia_czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(557, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(558, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(559, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(556, 'en', 'meronimia_czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(557, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(558, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(559, 'en', '');

# meronimia podsytuacji
insert into localised(id) values(560);
insert into localised(id) values(561);
insert into localised(id) values(562);
insert into localised(id) values(563);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(113, 560, 561, 562, 563, 112, 116, 0);
insert into localised_strings(id, strings_KEY, strings) values(560, 'pl', 'meronimia podsytuacji');
insert into localised_strings(id, strings_KEY, strings) values(561, 'pl', 'mero_podsyt');
insert into localised_strings(id, strings_KEY, strings) values(562, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(563, 'pl', '<x#> jest podsytuacją dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(560, 'en', 'meronimia podsytuacji');
insert into localised_strings(id, strings_KEY, strings) values(561, 'en', 'mero_podsyt');
insert into localised_strings(id, strings_KEY, strings) values(562, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(563, 'en', '<x#> jest podsytuacją dla <y#>');

# meronimia sytuacji towarzyszącej
insert into localised(id) values(564);
insert into localised(id) values(565);
insert into localised(id) values(566);
insert into localised(id) values(567);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(114, 564, 565, 566, 567, 112, 117, 0);
insert into localised_strings(id, strings_KEY, strings) values(564, 'pl', 'meronimia sytuacji towarzyszącej');
insert into localised_strings(id, strings_KEY, strings) values(565, 'pl', 'mero_syt_tow');
insert into localised_strings(id, strings_KEY, strings) values(566, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(567, 'pl', 'sytuacja <x#> towarzyszy sytuacji <y#>');
insert into localised_strings(id, strings_KEY, strings) values(564, 'en', 'meronimia sytuacji towarzyszącej');
insert into localised_strings(id, strings_KEY, strings) values(565, 'en', 'mero_syt_tow');
insert into localised_strings(id, strings_KEY, strings) values(566, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(567, 'en', 'sytuacja <x#> towarzyszy sytuacji <y#>');

# holonimia_czasownikowa
insert into localised(id) values(568);
insert into localised(id) values(569);
insert into localised(id) values(570);
insert into localised(id) values(571);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(115, 568, 569, 570, 571, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(568, 'pl', 'holonimia_czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(569, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(570, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(571, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(568, 'en', 'holonimia_czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(569, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(570, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(571, 'en', '');

# holonimia podsytuacji
insert into localised(id) values(572);
insert into localised(id) values(573);
insert into localised(id) values(574);
insert into localised(id) values(575);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(116, 572, 573, 574, 575, 115, 113, 0);
insert into localised_strings(id, strings_KEY, strings) values(572, 'pl', 'holonimia podsytuacji');
insert into localised_strings(id, strings_KEY, strings) values(573, 'pl', 'hol_podsyt');
insert into localised_strings(id, strings_KEY, strings) values(574, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(575, 'pl', '<x#> jest holonimem podsytuacji dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(572, 'en', 'holonimia podsytuacji');
insert into localised_strings(id, strings_KEY, strings) values(573, 'en', 'hol_podsyt');
insert into localised_strings(id, strings_KEY, strings) values(574, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(575, 'en', '<x#> jest holonimem podsytuacji dla <y#>');

# holonimia sytuacji towarzyszącej
insert into localised(id) values(576);
insert into localised(id) values(577);
insert into localised(id) values(578);
insert into localised(id) values(579);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(117, 576, 577, 578, 579, 115, 114, 0);
insert into localised_strings(id, strings_KEY, strings) values(576, 'pl', 'holonimia sytuacji towarzyszącej');
insert into localised_strings(id, strings_KEY, strings) values(577, 'pl', 'hol_syt_tow');
insert into localised_strings(id, strings_KEY, strings) values(578, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(579, 'pl', '<x#> jest holonimem sytuacji towarzyszącej dla <y#>');
insert into localised_strings(id, strings_KEY, strings) values(576, 'en', 'holonimia sytuacji towarzyszącej');
insert into localised_strings(id, strings_KEY, strings) values(577, 'en', 'hol_syt_tow');
insert into localised_strings(id, strings_KEY, strings) values(578, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(579, 'en', '<x#> jest holonimem sytuacji towarzyszącej dla <y#>');

# inchoatywność
insert into localised(id) values(580);
insert into localised(id) values(581);
insert into localised(id) values(582);
insert into localised(id) values(583);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(128, 580, 581, 582, 583, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(580, 'pl', 'inchoatywność');
insert into localised_strings(id, strings_KEY, strings) values(581, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(582, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(583, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(580, 'en', 'inchoatywność');
insert into localised_strings(id, strings_KEY, strings) values(581, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(582, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(583, 'en', '');

# inchoatywność DK-NDK
insert into localised(id) values(584);
insert into localised(id) values(585);
insert into localised(id) values(586);
insert into localised(id) values(587);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(129, 584, 585, 586, 587, 128, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(584, 'pl', 'inchoatywność DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(585, 'pl', 'inch_dk');
insert into localised_strings(id, strings_KEY, strings) values(586, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(587, 'pl', '<x#> wchodzi w relację inchoatywności DK-NDK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(584, 'en', 'inchoatywność DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(585, 'en', 'inch_dk');
insert into localised_strings(id, strings_KEY, strings) values(586, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(587, 'en', '<x#> wchodzi w relację inchoatywności DK-NDK z <y#>');

# inchoatywność NDK-NDK
insert into localised(id) values(588);
insert into localised(id) values(589);
insert into localised(id) values(590);
insert into localised(id) values(591);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(130, 588, 589, 590, 591, 128, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(588, 'pl', 'inchoatywność NDK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(589, 'pl', 'inch_ndk');
insert into localised_strings(id, strings_KEY, strings) values(590, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(591, 'pl', '<x#> wchodzi w relację inchoatywności NDK-NDK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(588, 'en', 'inchoatywność NDK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(589, 'en', 'inch_ndk');
insert into localised_strings(id, strings_KEY, strings) values(590, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(591, 'en', '<x#> wchodzi w relację inchoatywności NDK-NDK z <y#>');

# wielokrotność
insert into localised(id) values(592);
insert into localised(id) values(593);
insert into localised(id) values(594);
insert into localised(id) values(595);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(133, 592, 593, 594, 595, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(592, 'pl', 'wielokrotność');
insert into localised_strings(id, strings_KEY, strings) values(593, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(594, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(595, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(592, 'en', 'wielokrotność');
insert into localised_strings(id, strings_KEY, strings) values(593, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(594, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(595, 'en', '');

# iteratywność NDK-NDK
insert into localised(id) values(596);
insert into localised(id) values(597);
insert into localised(id) values(598);
insert into localised(id) values(599);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(134, 596, 597, 598, 599, 133, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(596, 'pl', 'iteratywność NDK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(597, 'pl', 'iter_NDK');
insert into localised_strings(id, strings_KEY, strings) values(598, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(599, 'pl', '<x#> wchodzi w relację iteratywności NDK-NDK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(596, 'en', 'iteratywność NDK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(597, 'en', 'iter_NDK');
insert into localised_strings(id, strings_KEY, strings) values(598, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(599, 'en', '<x#> wchodzi w relację iteratywności NDK-NDK z <y#>');

# dystrybutywność
insert into localised(id) values(600);
insert into localised(id) values(601);
insert into localised(id) values(602);
insert into localised(id) values(603);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(136, 600, 601, 602, 603, 133, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(600, 'pl', 'dystrybutywność');
insert into localised_strings(id, strings_KEY, strings) values(601, 'pl', 'dystr');
insert into localised_strings(id, strings_KEY, strings) values(602, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(603, 'pl', '<x#> wchodzi w relację dystrybutywności z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(600, 'en', 'dystrybutywność');
insert into localised_strings(id, strings_KEY, strings) values(601, 'en', 'dystr');
insert into localised_strings(id, strings_KEY, strings) values(602, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(603, 'en', '<x#> wchodzi w relację dystrybutywności z <y#>');

# presupozycja
insert into localised(id) values(604);
insert into localised(id) values(605);
insert into localised(id) values(606);
insert into localised(id) values(607);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(137, 604, 605, 606, 607, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(604, 'pl', 'presupozycja');
insert into localised_strings(id, strings_KEY, strings) values(605, 'pl', 'presup');
insert into localised_strings(id, strings_KEY, strings) values(606, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(607, 'pl', '<x#> wchodzi w relację presupozycji z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(604, 'en', 'presupozycja');
insert into localised_strings(id, strings_KEY, strings) values(605, 'en', 'presup');
insert into localised_strings(id, strings_KEY, strings) values(606, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(607, 'en', '<x#> wchodzi w relację presupozycji z <y#>');

# uprzedniość
insert into localised(id) values(608);
insert into localised(id) values(609);
insert into localised(id) values(610);
insert into localised(id) values(611);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(138, 608, 609, 610, 611, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(608, 'pl', 'uprzedniość');
insert into localised_strings(id, strings_KEY, strings) values(609, 'pl', 'uprzedn');
insert into localised_strings(id, strings_KEY, strings) values(610, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(611, 'pl', '<x#> wchodzi w relację uprzedniości z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(608, 'en', 'uprzedniość');
insert into localised_strings(id, strings_KEY, strings) values(609, 'en', 'uprzedn');
insert into localised_strings(id, strings_KEY, strings) values(610, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(611, 'en', '<x#> wchodzi w relację uprzedniości z <y#>');

# iteratywność NDK-DK
insert into localised(id) values(612);
insert into localised(id) values(613);
insert into localised(id) values(614);
insert into localised(id) values(615);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(140, 612, 613, 614, 615, 133, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(612, 'pl', 'iteratywność NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(613, 'pl', 'iter_DK');
insert into localised_strings(id, strings_KEY, strings) values(614, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(615, 'pl', '<x#> wchodzi w relację iteratywności NDK-DK z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(612, 'en', 'iteratywność NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(613, 'en', 'iter_DK');
insert into localised_strings(id, strings_KEY, strings) values(614, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(615, 'en', '<x#> wchodzi w relację iteratywności NDK-DK z <y#>');

# międzyjęzykowa synonimia międzyparadygmatyczna
insert into localised(id) values(616);
insert into localised(id) values(617);
insert into localised(id) values(618);
insert into localised(id) values(619);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(232, 616, 617, 618, 619, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(616, 'pl', 'międzyjęzykowa synonimia międzyparadygmatyczna');
insert into localised_strings(id, strings_KEY, strings) values(617, 'pl', 'syn_mpar');
insert into localised_strings(id, strings_KEY, strings) values(618, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(619, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(616, 'en', 'międzyjęzykowa synonimia międzyparadygmatyczna');
insert into localised_strings(id, strings_KEY, strings) values(617, 'en', 'syn_mpar');
insert into localised_strings(id, strings_KEY, strings) values(618, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(619, 'en', '');

# hiponimia_międzyjęzykowa
insert into localised(id) values(620);
insert into localised(id) values(621);
insert into localised(id) values(622);
insert into localised(id) values(623);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(197, 620, 621, 622, 623, NULL, 199, 1);
insert into localised_strings(id, strings_KEY, strings) values(620, 'pl', 'hiponimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(621, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(622, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(623, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(620, 'en', 'hiponimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(621, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(622, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(623, 'en', '');

# synonimia_międzyjęzykowa
insert into localised(id) values(624);
insert into localised(id) values(625);
insert into localised(id) values(626);
insert into localised(id) values(627);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(198, 624, 625, 626, 627, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(624, 'pl', 'synonimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(625, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(626, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(627, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(624, 'en', 'synonimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(625, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(626, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(627, 'en', '');

# hiperonimia_międzyjęzykowa
insert into localised(id) values(628);
insert into localised(id) values(629);
insert into localised(id) values(630);
insert into localised(id) values(631);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(199, 628, 629, 630, 631, NULL, 197, 1);
insert into localised_strings(id, strings_KEY, strings) values(628, 'pl', 'hiperonimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(629, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(630, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(631, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(628, 'en', 'hiperonimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(629, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(630, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(631, 'en', '');

# meronimia_międzyjęzykowa
insert into localised(id) values(632);
insert into localised(id) values(633);
insert into localised(id) values(634);
insert into localised(id) values(635);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(200, 632, 633, 634, 635, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(632, 'pl', 'meronimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(633, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(634, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(635, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(632, 'en', 'meronimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(633, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(634, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(635, 'en', '');

# mczęść_plWN-PWN
insert into localised(id) values(636);
insert into localised(id) values(637);
insert into localised(id) values(638);
insert into localised(id) values(639);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(201, 636, 637, 638, 639, 200, 217, 1);
insert into localised_strings(id, strings_KEY, strings) values(636, 'pl', 'mczęść_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(637, 'pl', 'mero:cz_pa');
insert into localised_strings(id, strings_KEY, strings) values(638, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(639, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(636, 'en', 'mczęść_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(637, 'en', 'mero:cz_pa');
insert into localised_strings(id, strings_KEY, strings) values(638, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(639, 'en', '');

# melement_plWN-PWN
insert into localised(id) values(640);
insert into localised(id) values(641);
insert into localised(id) values(642);
insert into localised(id) values(643);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(202, 640, 641, 642, 643, 200, 218, 1);
insert into localised_strings(id, strings_KEY, strings) values(640, 'pl', 'melement_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(641, 'pl', 'mero:el_pa');
insert into localised_strings(id, strings_KEY, strings) values(642, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(643, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(640, 'en', 'melement_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(641, 'en', 'mero:el_pa');
insert into localised_strings(id, strings_KEY, strings) values(642, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(643, 'en', '');

# mmateriał_plWN-PWN
insert into localised(id) values(644);
insert into localised(id) values(645);
insert into localised(id) values(646);
insert into localised(id) values(647);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(203, 644, 645, 646, 647, 200, 219, 1);
insert into localised_strings(id, strings_KEY, strings) values(644, 'pl', 'mmateriał_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(645, 'pl', 'mero:mat_pa');
insert into localised_strings(id, strings_KEY, strings) values(646, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(647, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(644, 'en', 'mmateriał_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(645, 'en', 'mero:mat_pa');
insert into localised_strings(id, strings_KEY, strings) values(646, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(647, 'en', '');

# holonimia_międzyjęzykowa
insert into localised(id) values(648);
insert into localised(id) values(649);
insert into localised(id) values(650);
insert into localised(id) values(651);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(204, 648, 649, 650, 651, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(648, 'pl', 'holonimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(649, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(650, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(651, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(648, 'en', 'holonimia_międzyjęzykowa');
insert into localised_strings(id, strings_KEY, strings) values(649, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(650, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(651, 'en', '');

# hczęść_plWN-PWN
insert into localised(id) values(652);
insert into localised(id) values(653);
insert into localised(id) values(654);
insert into localised(id) values(655);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(205, 652, 653, 654, 655, 204, 214, 1);
insert into localised_strings(id, strings_KEY, strings) values(652, 'pl', 'hczęść_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(653, 'pl', 'holo:cz_pa');
insert into localised_strings(id, strings_KEY, strings) values(654, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(655, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(652, 'en', 'hczęść_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(653, 'en', 'holo:cz_pa');
insert into localised_strings(id, strings_KEY, strings) values(654, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(655, 'en', '');

# helement_plWN-PWN
insert into localised(id) values(656);
insert into localised(id) values(657);
insert into localised(id) values(658);
insert into localised(id) values(659);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(206, 656, 657, 658, 659, 204, 215, 1);
insert into localised_strings(id, strings_KEY, strings) values(656, 'pl', 'helement_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(657, 'pl', 'holo:el_pa');
insert into localised_strings(id, strings_KEY, strings) values(658, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(659, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(656, 'en', 'helement_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(657, 'en', 'holo:el_pa');
insert into localised_strings(id, strings_KEY, strings) values(658, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(659, 'en', '');

# hmateriał_plWN-PWN
insert into localised(id) values(660);
insert into localised(id) values(661);
insert into localised(id) values(662);
insert into localised(id) values(663);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(207, 660, 661, 662, 663, 204, 216, 1);
insert into localised_strings(id, strings_KEY, strings) values(660, 'pl', 'hmateriał_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(661, 'pl', 'holo:mat_pa');
insert into localised_strings(id, strings_KEY, strings) values(662, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(663, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(660, 'en', 'hmateriał_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(661, 'en', 'holo:mat_pa');
insert into localised_strings(id, strings_KEY, strings) values(662, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(663, 'en', '');

# Syn_plWN-PWN
insert into localised(id) values(664);
insert into localised(id) values(665);
insert into localised(id) values(666);
insert into localised(id) values(667);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(208, 664, 665, 666, 667, 198, 209, 1);
insert into localised_strings(id, strings_KEY, strings) values(664, 'pl', 'Syn_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(665, 'pl', 'syn_pa');
insert into localised_strings(id, strings_KEY, strings) values(666, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(667, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(664, 'en', 'Syn_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(665, 'en', 'syn_pa');
insert into localised_strings(id, strings_KEY, strings) values(666, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(667, 'en', '');

# Syn_PWN-plWN
insert into localised(id) values(668);
insert into localised(id) values(669);
insert into localised(id) values(670);
insert into localised(id) values(671);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(209, 668, 669, 670, 671, 198, 208, 1);
insert into localised_strings(id, strings_KEY, strings) values(668, 'pl', 'Syn_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(669, 'pl', 'syn_ap');
insert into localised_strings(id, strings_KEY, strings) values(670, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(671, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(668, 'en', 'Syn_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(669, 'en', 'syn_ap');
insert into localised_strings(id, strings_KEY, strings) values(670, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(671, 'en', '');

# Hipo_plWN-PWN
insert into localised(id) values(672);
insert into localised(id) values(673);
insert into localised(id) values(674);
insert into localised(id) values(675);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(210, 672, 673, 674, 675, 197, 213, 1);
insert into localised_strings(id, strings_KEY, strings) values(672, 'pl', 'Hipo_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(673, 'pl', 'hipo_pa');
insert into localised_strings(id, strings_KEY, strings) values(674, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(675, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(672, 'en', 'Hipo_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(673, 'en', 'hipo_pa');
insert into localised_strings(id, strings_KEY, strings) values(674, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(675, 'en', '');

# Hipo_PWN-plWN
insert into localised(id) values(676);
insert into localised(id) values(677);
insert into localised(id) values(678);
insert into localised(id) values(679);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(211, 676, 677, 678, 679, 197, 212, 1);
insert into localised_strings(id, strings_KEY, strings) values(676, 'pl', 'Hipo_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(677, 'pl', 'hipo_ap');
insert into localised_strings(id, strings_KEY, strings) values(678, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(679, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(676, 'en', 'Hipo_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(677, 'en', 'hipo_ap');
insert into localised_strings(id, strings_KEY, strings) values(678, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(679, 'en', '');

# Hiper_plWN-PWN
insert into localised(id) values(680);
insert into localised(id) values(681);
insert into localised(id) values(682);
insert into localised(id) values(683);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(212, 680, 681, 682, 683, 199, 211, 1);
insert into localised_strings(id, strings_KEY, strings) values(680, 'pl', 'Hiper_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(681, 'pl', 'hiper_pa');
insert into localised_strings(id, strings_KEY, strings) values(682, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(683, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(680, 'en', 'Hiper_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(681, 'en', 'hiper_pa');
insert into localised_strings(id, strings_KEY, strings) values(682, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(683, 'en', '');

# Hiper_PWN-plWN
insert into localised(id) values(684);
insert into localised(id) values(685);
insert into localised(id) values(686);
insert into localised(id) values(687);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(213, 684, 685, 686, 687, 199, 210, 1);
insert into localised_strings(id, strings_KEY, strings) values(684, 'pl', 'Hiper_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(685, 'pl', 'hiper_ap');
insert into localised_strings(id, strings_KEY, strings) values(686, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(687, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(684, 'en', 'Hiper_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(685, 'en', 'hiper_ap');
insert into localised_strings(id, strings_KEY, strings) values(686, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(687, 'en', '');

# mczęść_PWN-plWN
insert into localised(id) values(688);
insert into localised(id) values(689);
insert into localised(id) values(690);
insert into localised(id) values(691);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(214, 688, 689, 690, 691, 200, 205, 1);
insert into localised_strings(id, strings_KEY, strings) values(688, 'pl', 'mczęść_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(689, 'pl', 'mero:cz_ap');
insert into localised_strings(id, strings_KEY, strings) values(690, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(691, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(688, 'en', 'mczęść_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(689, 'en', 'mero:cz_ap');
insert into localised_strings(id, strings_KEY, strings) values(690, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(691, 'en', '');

# melement_PWN-plWN
insert into localised(id) values(692);
insert into localised(id) values(693);
insert into localised(id) values(694);
insert into localised(id) values(695);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(215, 692, 693, 694, 695, 200, 206, 1);
insert into localised_strings(id, strings_KEY, strings) values(692, 'pl', 'melement_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(693, 'pl', 'mero:el_ap');
insert into localised_strings(id, strings_KEY, strings) values(694, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(695, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(692, 'en', 'melement_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(693, 'en', 'mero:el_ap');
insert into localised_strings(id, strings_KEY, strings) values(694, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(695, 'en', '');

# mmateriał_PWN-plWN
insert into localised(id) values(696);
insert into localised(id) values(697);
insert into localised(id) values(698);
insert into localised(id) values(699);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(216, 696, 697, 698, 699, 200, 207, 1);
insert into localised_strings(id, strings_KEY, strings) values(696, 'pl', 'mmateriał_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(697, 'pl', 'mero:mat_ap');
insert into localised_strings(id, strings_KEY, strings) values(698, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(699, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(696, 'en', 'mmateriał_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(697, 'en', 'mero:mat_ap');
insert into localised_strings(id, strings_KEY, strings) values(698, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(699, 'en', '');

# hczęść_PWN-plWN
insert into localised(id) values(700);
insert into localised(id) values(701);
insert into localised(id) values(702);
insert into localised(id) values(703);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(217, 700, 701, 702, 703, 204, 201, 1);
insert into localised_strings(id, strings_KEY, strings) values(700, 'pl', 'hczęść_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(701, 'pl', 'holo:cz_ap');
insert into localised_strings(id, strings_KEY, strings) values(702, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(703, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(700, 'en', 'hczęść_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(701, 'en', 'holo:cz_ap');
insert into localised_strings(id, strings_KEY, strings) values(702, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(703, 'en', '');

# helement_PWN-plWN
insert into localised(id) values(704);
insert into localised(id) values(705);
insert into localised(id) values(706);
insert into localised(id) values(707);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(218, 704, 705, 706, 707, 204, 202, 1);
insert into localised_strings(id, strings_KEY, strings) values(704, 'pl', 'helement_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(705, 'pl', 'holo:el_ap');
insert into localised_strings(id, strings_KEY, strings) values(706, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(707, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(704, 'en', 'helement_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(705, 'en', 'holo:el_ap');
insert into localised_strings(id, strings_KEY, strings) values(706, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(707, 'en', '');

# hmateriał_PWN-plWN
insert into localised(id) values(708);
insert into localised(id) values(709);
insert into localised(id) values(710);
insert into localised(id) values(711);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(219, 708, 709, 710, 711, 204, 203, 1);
insert into localised_strings(id, strings_KEY, strings) values(708, 'pl', 'hmateriał_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(709, 'pl', 'holo:mat_ap');
insert into localised_strings(id, strings_KEY, strings) values(710, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(711, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(708, 'en', 'hmateriał_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(709, 'en', 'holo:mat_ap');
insert into localised_strings(id, strings_KEY, strings) values(710, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(711, 'en', '');

# potencjalny odpowiednik
insert into localised(id) values(712);
insert into localised(id) values(713);
insert into localised(id) values(714);
insert into localised(id) values(715);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(220, 712, 713, 714, 715, NULL, 220, 1);
insert into localised_strings(id, strings_KEY, strings) values(712, 'pl', 'potencjalny odpowiednik');
insert into localised_strings(id, strings_KEY, strings) values(713, 'pl', 'pot_odp');
insert into localised_strings(id, strings_KEY, strings) values(714, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(715, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(712, 'en', 'potencjalny odpowiednik');
insert into localised_strings(id, strings_KEY, strings) values(713, 'en', 'pot_odp');
insert into localised_strings(id, strings_KEY, strings) values(714, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(715, 'en', '');

# pot_odp_plWN-PWN
insert into localised(id) values(716);
insert into localised(id) values(717);
insert into localised(id) values(718);
insert into localised(id) values(719);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(222, 716, 717, 718, 719, 220, 223, 1);
insert into localised_strings(id, strings_KEY, strings) values(716, 'pl', 'pot_odp_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(717, 'pl', 'po_pa');
insert into localised_strings(id, strings_KEY, strings) values(718, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(719, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(716, 'en', 'pot_odp_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(717, 'en', 'po_pa');
insert into localised_strings(id, strings_KEY, strings) values(718, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(719, 'en', '');

# pot_odp_PWN-plWN
insert into localised(id) values(720);
insert into localised(id) values(721);
insert into localised(id) values(722);
insert into localised(id) values(723);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(223, 720, 721, 722, 723, 220, 222, 1);
insert into localised_strings(id, strings_KEY, strings) values(720, 'pl', 'pot_odp_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(721, 'pl', 'po_ap');
insert into localised_strings(id, strings_KEY, strings) values(722, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(723, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(720, 'en', 'pot_odp_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(721, 'en', 'po_ap');
insert into localised_strings(id, strings_KEY, strings) values(722, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(723, 'en', '');

# międzyjęzykowa_synonimia_międzyrejestrowa
insert into localised(id) values(724);
insert into localised(id) values(725);
insert into localised(id) values(726);
insert into localised(id) values(727);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(227, 724, 725, 726, 727, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(724, 'pl', 'międzyjęzykowa_synonimia_międzyrejestrowa');
insert into localised_strings(id, strings_KEY, strings) values(725, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(726, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(727, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(724, 'en', 'międzyjęzykowa_synonimia_międzyrejestrowa');
insert into localised_strings(id, strings_KEY, strings) values(725, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(726, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(727, 'en', '');

# międzyjęzykowa_synonimia_międzyrejestrowa_plWN-PWN
insert into localised(id) values(728);
insert into localised(id) values(729);
insert into localised(id) values(730);
insert into localised(id) values(731);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(228, 728, 729, 730, 731, 227, 229, 1);
insert into localised_strings(id, strings_KEY, strings) values(728, 'pl', 'międzyjęzykowa_synonimia_międzyrejestrowa_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(729, 'pl', 'synmr_pa');
insert into localised_strings(id, strings_KEY, strings) values(730, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(731, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(728, 'en', 'międzyjęzykowa_synonimia_międzyrejestrowa_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(729, 'en', 'synmr_pa');
insert into localised_strings(id, strings_KEY, strings) values(730, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(731, 'en', '');

# międzyjęzykowa_synonimia_międzyrejestrowa_PWN-plWN
insert into localised(id) values(732);
insert into localised(id) values(733);
insert into localised(id) values(734);
insert into localised(id) values(735);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(229, 732, 733, 734, 734, 227, 228, 1);
insert into localised_strings(id, strings_KEY, strings) values(732, 'pl', 'międzyjęzykowa_synonimia_międzyrejestrowa_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(733, 'pl', 'synmr_ap');
insert into localised_strings(id, strings_KEY, strings) values(734, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(735, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(732, 'en', 'międzyjęzykowa_synonimia_międzyrejestrowa_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(733, 'en', 'synmr_ap');
insert into localised_strings(id, strings_KEY, strings) values(734, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(735, 'en', '');

# sumo instance
insert into localised(id) values(736);
insert into localised(id) values(737);
insert into localised(id) values(738);
insert into localised(id) values(739);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(230, 736, 737, 738, 739, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(736, 'pl', 'sumo instance');
insert into localised_strings(id, strings_KEY, strings) values(737, 'pl', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(738, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(739, 'pl', '<x#> jest rodzajem <y#> w ontologii SUMO');
insert into localised_strings(id, strings_KEY, strings) values(736, 'en', 'sumo instance');
insert into localised_strings(id, strings_KEY, strings) values(737, 'en', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(738, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(739, 'en', '<x#> jest rodzajem <y#> w ontologii SUMO');

# międzyjęzykowa_synonimia_międzyparadygmatyczna_made_of_plWN-PWN
insert into localised(id) values(744);
insert into localised(id) values(745);
insert into localised(id) values(746);
insert into localised(id) values(747);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(235, 744, 745, 746, 747, 232, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(744, 'pl', 'międzyjęzykowa_synonimia_międzyparadygmatyczna_made_of_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(745, 'pl', 'syn_mpar_mat_pa');
insert into localised_strings(id, strings_KEY, strings) values(746, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(747, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(744, 'en', 'międzyjęzykowa_synonimia_międzyparadygmatyczna_made_of_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(745, 'en', 'syn_mpar_mat_pa');
insert into localised_strings(id, strings_KEY, strings) values(746, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(747, 'en', '');

# międzyjęzykowa_synonimia_międzyparadygmatyczna_resembling_plWN-PWN
insert into localised(id) values(748);
insert into localised(id) values(749);
insert into localised(id) values(750);
insert into localised(id) values(751);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(238, 748, 749, 750, 751, 232, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(748, 'pl', 'międzyjęzykowa_synonimia_międzyparadygmatyczna_resembling_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(749, 'pl', 'syn_mpar_res_pa');
insert into localised_strings(id, strings_KEY, strings) values(750, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(751, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(748, 'en', 'międzyjęzykowa_synonimia_międzyparadygmatyczna_resembling_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(749, 'en', 'syn_mpar_res_pa');
insert into localised_strings(id, strings_KEY, strings) values(750, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(751, 'en', '');

# międzyjęzykowa_synonimia_międzyparadygmatyczna_related_to_plWN-PWN
insert into localised(id) values(752);
insert into localised(id) values(753);
insert into localised(id) values(754);
insert into localised(id) values(755);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(239, 752, 753, 754, 755, 232, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(752, 'pl', 'międzyjęzykowa_synonimia_międzyparadygmatyczna_related_to_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(753, 'pl', 'syn_mpar_rel_pa');
insert into localised_strings(id, strings_KEY, strings) values(754, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(755, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(752, 'en', 'międzyjęzykowa_synonimia_międzyparadygmatyczna_related_to_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(753, 'en', 'syn_mpar_rel_pa');
insert into localised_strings(id, strings_KEY, strings) values(754, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(755, 'en', '');

# equivalent
insert into localised(id) values(756);
insert into localised(id) values(757);
insert into localised(id) values(758);
insert into localised(id) values(759);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3000, 756, 757, 758, 759, NULL, NULL, 1);
insert into localised_strings(id, strings_KEY, strings) values(756, 'pl', 'equivalent');
insert into localised_strings(id, strings_KEY, strings) values(757, 'pl', 'equivalent');
insert into localised_strings(id, strings_KEY, strings) values(758, 'pl', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(759, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(756, 'en', 'equivalent');
insert into localised_strings(id, strings_KEY, strings) values(757, 'en', 'equivalent');
insert into localised_strings(id, strings_KEY, strings) values(758, 'en', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(759, 'en', '');

# subsumed
insert into localised(id) values(760);
insert into localised(id) values(761);
insert into localised(id) values(762);
insert into localised(id) values(763);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3001, 760, 761, 762, 763, NULL, NULL, 1);
insert into localised_strings(id, strings_KEY, strings) values(760, 'pl', 'subsumed');
insert into localised_strings(id, strings_KEY, strings) values(761, 'pl', 'subsumed');
insert into localised_strings(id, strings_KEY, strings) values(762, 'pl', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(763, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(760, 'en', 'subsumed');
insert into localised_strings(id, strings_KEY, strings) values(761, 'en', 'subsumed');
insert into localised_strings(id, strings_KEY, strings) values(762, 'en', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(763, 'en', '');

# instance_of
insert into localised(id) values(764);
insert into localised(id) values(765);
insert into localised(id) values(766);
insert into localised(id) values(767);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3002, 764, 765, 766, 767, NULL, NULL, 1);
insert into localised_strings(id, strings_KEY, strings) values(764, 'pl', 'instance of');
insert into localised_strings(id, strings_KEY, strings) values(765, 'pl', 'instance_of');
insert into localised_strings(id, strings_KEY, strings) values(766, 'pl', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(767, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(764, 'en', 'instance of');
insert into localised_strings(id, strings_KEY, strings) values(765, 'en', 'instance_of');
insert into localised_strings(id, strings_KEY, strings) values(766, 'en', 'sumo');
insert into localised_strings(id, strings_KEY, strings) values(767, 'en', '');

# Międzyjęzykowa synonimia częściowa
insert into localised(id) values(768);
insert into localised(id) values(769);
insert into localised(id) values(770);
insert into localised(id) values(771);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3004, 768, 769, 770, 771, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(768, 'pl', 'Międzyjęzykowa synonimia częściowa');
insert into localised_strings(id, strings_KEY, strings) values(769, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(770, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(771, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(768, 'en', 'Międzyjęzykowa synonimia częściowa');
insert into localised_strings(id, strings_KEY, strings) values(769, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(770, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(771, 'en', '');

# międzyjęzykowa_synonimia_częściowa_plWN-PWN
insert into localised(id) values(772);
insert into localised(id) values(773);
insert into localised(id) values(774);
insert into localised(id) values(775);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3005, 772, 773, 774, 775, 3004, 3006, 1);
insert into localised_strings(id, strings_KEY, strings) values(772, 'pl', 'międzyjęzykowa_synonimia_częściowa_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(773, 'pl', 'syncz_pa');
insert into localised_strings(id, strings_KEY, strings) values(774, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(775, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(772, 'en', 'międzyjęzykowa_synonimia_częściowa_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(773, 'en', 'syncz_pa');
insert into localised_strings(id, strings_KEY, strings) values(774, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(775, 'en', '');

# międzyjęzykowa_synonimia_częściowa_PWN-plWN
insert into localised(id) values(776);
insert into localised(id) values(777);
insert into localised(id) values(778);
insert into localised(id) values(779);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3006, 776, 777, 778, 779, 3004, 3005, 1);
insert into localised_strings(id, strings_KEY, strings) values(776, 'pl', 'międzyjęzykowa_synonimia_częściowa_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(777, 'pl', 'syncz_ap');
insert into localised_strings(id, strings_KEY, strings) values(778, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(779, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(776, 'en', 'międzyjęzykowa_synonimia_częściowa_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(777, 'en', 'syncz_ap');
insert into localised_strings(id, strings_KEY, strings) values(778, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(779, 'en', '');

# Międzyjęzykowy_typ
insert into localised(id) values(780);
insert into localised(id) values(781);
insert into localised(id) values(782);
insert into localised(id) values(783);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3011, 780, 781, 782, 783, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(780, 'pl', 'Międzyjęzykowy_typ');
insert into localised_strings(id, strings_KEY, strings) values(781, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(782, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(783, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(780, 'en', 'Międzyjęzykowy_typ');
insert into localised_strings(id, strings_KEY, strings) values(781, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(782, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(783, 'en', '');

# Międzyjęzykowy_egzemplarz
insert into localised(id) values(784);
insert into localised(id) values(785);
insert into localised(id) values(786);
insert into localised(id) values(787);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3012, 784, 785, 786, 787, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(784, 'pl', 'Międzyjęzykowy_egzemplarz');
insert into localised_strings(id, strings_KEY, strings) values(785, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(786, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(787, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(784, 'en', 'Międzyjęzykowy_egzemplarz');
insert into localised_strings(id, strings_KEY, strings) values(785, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(786, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(787, 'en', '');

# Typ_plWN-PWN
insert into localised(id) values(788);
insert into localised(id) values(789);
insert into localised(id) values(790);
insert into localised(id) values(791);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3013, 788, 789, 790, 791, 3011, 3016, 1);
insert into localised_strings(id, strings_KEY, strings) values(788, 'pl', 'Typ_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(789, 'pl', 'typ_pa');
insert into localised_strings(id, strings_KEY, strings) values(790, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(791, 'pl', 'typ');
insert into localised_strings(id, strings_KEY, strings) values(788, 'en', 'Typ_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(789, 'en', 'typ_pa');
insert into localised_strings(id, strings_KEY, strings) values(790, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(791, 'en', 'typ');

# Typ_PWN-plWN
insert into localised(id) values(792);
insert into localised(id) values(793);
insert into localised(id) values(794);
insert into localised(id) values(795);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3014, 792, 793, 794, 795, 3011, 3015, 1);
insert into localised_strings(id, strings_KEY, strings) values(792, 'pl', 'Typ_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(793, 'pl', 'typ_ap');
insert into localised_strings(id, strings_KEY, strings) values(794, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(795, 'pl', 'typ');
insert into localised_strings(id, strings_KEY, strings) values(792, 'en', 'Typ_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(793, 'en', 'typ_ap');
insert into localised_strings(id, strings_KEY, strings) values(794, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(795, 'en', 'typ');

# Egz_plWN-PWN
insert into localised(id) values(796);
insert into localised(id) values(797);
insert into localised(id) values(798);
insert into localised(id) values(799);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3015, 796, 797, 798, 799, 3012, 3014, 1);
insert into localised_strings(id, strings_KEY, strings) values(796, 'pl', 'Egz_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(797, 'pl', 'egz_pa');
insert into localised_strings(id, strings_KEY, strings) values(798, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(799, 'pl', 'egz');
insert into localised_strings(id, strings_KEY, strings) values(796, 'en', 'Egz_plWN-PWN');
insert into localised_strings(id, strings_KEY, strings) values(797, 'en', 'egz_pa');
insert into localised_strings(id, strings_KEY, strings) values(798, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(799, 'en', 'egz');

# Egz_PWN-plWN
insert into localised(id) values(800);
insert into localised(id) values(801);
insert into localised(id) values(802);
insert into localised(id) values(803);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3016, 800, 801, 802, 803, 3012, 3013, 1);
insert into localised_strings(id, strings_KEY, strings) values(800, 'pl', 'Egz_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(801, 'pl', 'egz_ap');
insert into localised_strings(id, strings_KEY, strings) values(802, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(803, 'pl', 'egz');
insert into localised_strings(id, strings_KEY, strings) values(800, 'en', 'Egz_PWN-plWN');
insert into localised_strings(id, strings_KEY, strings) values(801, 'en', 'egz_ap');
insert into localised_strings(id, strings_KEY, strings) values(802, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(803, 'en', 'egz');

# meronimia czasownikowa
insert into localised(id) values(804);
insert into localised(id) values(805);
insert into localised(id) values(806);
insert into localised(id) values(807);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3410, 804, 805, 806, 807, 112, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(804, 'pl', 'meronimia czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(805, 'pl', 'mero_cz');
insert into localised_strings(id, strings_KEY, strings) values(806, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(807, 'pl', '<#x> towarzyszy <#y>');
insert into localised_strings(id, strings_KEY, strings) values(804, 'en', 'meronimia czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(805, 'en', 'mero_cz');
insert into localised_strings(id, strings_KEY, strings) values(806, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(807, 'en', '<#x> towarzyszy <#y>');

# holonimia czasownikowa
insert into localised(id) values(808);
insert into localised(id) values(809);
insert into localised(id) values(810);
insert into localised(id) values(811);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3412, 808, 809, 810, 811, 115, 3410, 0);
insert into localised_strings(id, strings_KEY, strings) values(808, 'pl', 'holonimia czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(809, 'pl', 'holo_cz');
insert into localised_strings(id, strings_KEY, strings) values(810, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(811, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(808, 'en', 'holonimia czasownikowa');
insert into localised_strings(id, strings_KEY, strings) values(809, 'en', 'holo_cz');
insert into localised_strings(id, strings_KEY, strings) values(810, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(811, 'en', '');

# kauzacja
insert into localised(id) values(812);
insert into localised(id) values(813);
insert into localised(id) values(814);
insert into localised(id) values(815);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3413, 812, 813, 814, 815, 80, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(812, 'pl', 'kauzacja');
insert into localised_strings(id, strings_KEY, strings) values(813, 'pl', 'kauz');
insert into localised_strings(id, strings_KEY, strings) values(814, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(815, 'pl', '<x#> powoduje/spowodował <y#>');
insert into localised_strings(id, strings_KEY, strings) values(812, 'en', 'kauzacja');
insert into localised_strings(id, strings_KEY, strings) values(813, 'en', 'kauz');
insert into localised_strings(id, strings_KEY, strings) values(814, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(815, 'en', '<x#> powoduje/spowodował <y#>');

# procesywność
insert into localised(id) values(816);
insert into localised(id) values(817);
insert into localised(id) values(818);
insert into localised(id) values(819);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3414, 816, 817, 818, 819, 86, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(816, 'pl', 'procesywność');
insert into localised_strings(id, strings_KEY, strings) values(817, 'pl', 'proc');
insert into localised_strings(id, strings_KEY, strings) values(818, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(819, 'pl', '<x#> staje się/stał się <y#>');
insert into localised_strings(id, strings_KEY, strings) values(816, 'en', 'procesywność');
insert into localised_strings(id, strings_KEY, strings) values(817, 'en', 'proc');
insert into localised_strings(id, strings_KEY, strings) values(818, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(819, 'en', '<x#> staje się/stał się <y#>');

# stanowość
insert into localised(id) values(820);
insert into localised(id) values(821);
insert into localised(id) values(822);
insert into localised(id) values(823);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3415, 820, 821, 822, 823, 91, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(820, 'pl', 'stanowość');
insert into localised_strings(id, strings_KEY, strings) values(821, 'pl', 'st');
insert into localised_strings(id, strings_KEY, strings) values(822, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(823, 'pl', '<x#> jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(820, 'en', 'stanowość');
insert into localised_strings(id, strings_KEY, strings) values(821, 'en', 'st');
insert into localised_strings(id, strings_KEY, strings) values(822, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(823, 'en', '<x#> jest <y#>');

# iteratywność
insert into localised(id) values(824);
insert into localised(id) values(825);
insert into localised(id) values(826);
insert into localised(id) values(827);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3416, 824, 825, 826, 827, 133, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(824, 'pl', 'iteratywność');
insert into localised_strings(id, strings_KEY, strings) values(825, 'pl', 'iter');
insert into localised_strings(id, strings_KEY, strings) values(826, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(827, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(824, 'en', 'iteratywność');
insert into localised_strings(id, strings_KEY, strings) values(825, 'en', 'iter');
insert into localised_strings(id, strings_KEY, strings) values(826, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(827, 'en', '');

# inchoatywność
insert into localised(id) values(828);
insert into localised(id) values(829);
insert into localised(id) values(830);
insert into localised(id) values(831);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3417, 828, 829, 830, 831, 128, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(828, 'pl', 'inchoatywność');
insert into localised_strings(id, strings_KEY, strings) values(829, 'pl', 'inch');
insert into localised_strings(id, strings_KEY, strings) values(830, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(831, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(828, 'en', 'inchoatywność');
insert into localised_strings(id, strings_KEY, strings) values(829, 'en', 'inch');
insert into localised_strings(id, strings_KEY, strings) values(830, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(831, 'en', '');

# presupozycja_new
insert into localised(id) values(832);
insert into localised(id) values(833);
insert into localised(id) values(834);
insert into localised(id) values(835);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3418, 832, 833, 834, 835, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(832, 'pl', 'presupozycja_new');
insert into localised_strings(id, strings_KEY, strings) values(833, 'pl', 'PRES');
insert into localised_strings(id, strings_KEY, strings) values(834, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(835, 'pl', '<y#> presuponuje <x#>');
insert into localised_strings(id, strings_KEY, strings) values(832, 'en', 'presupozycja_new');
insert into localised_strings(id, strings_KEY, strings) values(833, 'en', 'PRES');
insert into localised_strings(id, strings_KEY, strings) values(834, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(835, 'en', '<y#> presuponuje <x#>');

# presupozycja z tożsamością podmiotu
insert into localised(id) values(836);
insert into localised(id) values(837);
insert into localised(id) values(838);
insert into localised(id) values(839);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3419, 836, 837, 838, 839, 3418, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(836, 'pl', 'presupozycja z tożsamością podmiotu');
insert into localised_strings(id, strings_KEY, strings) values(837, 'pl', 'pres+t');
insert into localised_strings(id, strings_KEY, strings) values(838, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(839, 'pl', '<y#> przesuponuje <x#>');
insert into localised_strings(id, strings_KEY, strings) values(836, 'en', 'presupozycja z tożsamością podmiotu');
insert into localised_strings(id, strings_KEY, strings) values(837, 'en', 'pres+t');
insert into localised_strings(id, strings_KEY, strings) values(838, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(839, 'en', '<y#> przesuponuje <x#>');

# presupozycja bez tożsamości podmiotów
insert into localised(id) values(840);
insert into localised(id) values(841);
insert into localised(id) values(842);
insert into localised(id) values(843);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3421, 840, 841, 842, 843, 3418, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(840, 'pl', 'presupozycja bez tożsamości podmiotów');
insert into localised_strings(id, strings_KEY, strings) values(841, 'pl', 'pres-t');
insert into localised_strings(id, strings_KEY, strings) values(842, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(843, 'pl', '<y#> przesuponuje <x#>');
insert into localised_strings(id, strings_KEY, strings) values(840, 'en', 'presupozycja bez tożsamości podmiotów');
insert into localised_strings(id, strings_KEY, strings) values(841, 'en', 'pres-t');
insert into localised_strings(id, strings_KEY, strings) values(842, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(843, 'en', '<y#> przesuponuje <x#>');

# uprzedniość_new
insert into localised(id) values(844);
insert into localised(id) values(845);
insert into localised(id) values(846);
insert into localised(id) values(847);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3422, 844, 845, 846, 847, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(844, 'pl', 'uprzedniość_new');
insert into localised_strings(id, strings_KEY, strings) values(845, 'pl', 'UPRZ');
insert into localised_strings(id, strings_KEY, strings) values(846, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(847, 'pl', '<y#> poprzedza <x#>');
insert into localised_strings(id, strings_KEY, strings) values(844, 'en', 'uprzedniość_new');
insert into localised_strings(id, strings_KEY, strings) values(845, 'en', 'UPRZ');
insert into localised_strings(id, strings_KEY, strings) values(846, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(847, 'en', '<y#> poprzedza <x#>');

# uprzedniość z tożsamością podmiotu
insert into localised(id) values(848);
insert into localised(id) values(849);
insert into localised(id) values(850);
insert into localised(id) values(851);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3423, 848, 849, 850, 851, 3422, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(848, 'pl', 'uprzedniość z tożsamością podmiotu');
insert into localised_strings(id, strings_KEY, strings) values(849, 'pl', 'uprz+t');
insert into localised_strings(id, strings_KEY, strings) values(850, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(851, 'pl', '<y#> poprzedza <x#>');
insert into localised_strings(id, strings_KEY, strings) values(848, 'en', 'uprzedniość z tożsamością podmiotu');
insert into localised_strings(id, strings_KEY, strings) values(849, 'en', 'uprz+t');
insert into localised_strings(id, strings_KEY, strings) values(850, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(851, 'en', '<y#> poprzedza <x#>');

# obiekt
insert into localised(id) values(852);
insert into localised(id) values(853);
insert into localised(id) values(854);
insert into localised(id) values(855);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3427, 852, 853, 854, 855, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(852, 'pl', 'obiekt');
insert into localised_strings(id, strings_KEY, strings) values(853, 'pl', 'ob');
insert into localised_strings(id, strings_KEY, strings) values(854, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(855, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(852, 'en', 'obiekt');
insert into localised_strings(id, strings_KEY, strings) values(853, 'en', 'ob');
insert into localised_strings(id, strings_KEY, strings) values(854, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(855, 'en', '');

# okoliczność
insert into localised(id) values(856);
insert into localised(id) values(857);
insert into localised(id) values(858);
insert into localised(id) values(859);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3428, 856, 857, 858, 859, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(856, 'pl', 'okoliczność');
insert into localised_strings(id, strings_KEY, strings) values(857, 'pl', 'oko');
insert into localised_strings(id, strings_KEY, strings) values(858, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(859, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(856, 'en', 'okoliczność');
insert into localised_strings(id, strings_KEY, strings) values(857, 'en', 'oko');
insert into localised_strings(id, strings_KEY, strings) values(858, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(859, 'en', '');

# sposób
insert into localised(id) values(860);
insert into localised(id) values(861);
insert into localised(id) values(862);
insert into localised(id) values(863);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3429, 860, 861, 862, 863, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(860, 'pl', 'sposób');
insert into localised_strings(id, strings_KEY, strings) values(861, 'pl', 'spos');
insert into localised_strings(id, strings_KEY, strings) values(862, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(863, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(860, 'en', 'sposób');
insert into localised_strings(id, strings_KEY, strings) values(861, 'en', 'spos');
insert into localised_strings(id, strings_KEY, strings) values(862, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(863, 'en', '');

# subiekt
insert into localised(id) values(864);
insert into localised(id) values(865);
insert into localised(id) values(866);
insert into localised(id) values(867);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3430, 864, 865, 866, 867, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(864, 'pl', 'subiekt');
insert into localised_strings(id, strings_KEY, strings) values(865, 'pl', 'sub');
insert into localised_strings(id, strings_KEY, strings) values(866, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(867, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(864, 'en', 'subiekt');
insert into localised_strings(id, strings_KEY, strings) values(865, 'en', 'sub');
insert into localised_strings(id, strings_KEY, strings) values(866, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(867, 'en', '');

# uprzedniość bez tożsamości podmiotów
insert into localised(id) values(868);
insert into localised(id) values(869);
insert into localised(id) values(870);
insert into localised(id) values(871);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3431, 868, 869, 870, 871, 3422, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(868, 'pl', 'uprzedniość bez tożsamości podmiotów');
insert into localised_strings(id, strings_KEY, strings) values(869, 'pl', 'uprz-t');
insert into localised_strings(id, strings_KEY, strings) values(870, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(871, 'pl', '<y#> poprzedza <x#>');
insert into localised_strings(id, strings_KEY, strings) values(868, 'en', 'uprzedniość bez tożsamości podmiotów');
insert into localised_strings(id, strings_KEY, strings) values(869, 'en', 'uprz-t');
insert into localised_strings(id, strings_KEY, strings) values(870, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(871, 'en', '<y#> poprzedza <x#>');

# Princeton relations
# Hypernym
insert into localised(id) values(872);
insert into localised(id) values(873);
insert into localised(id) values(874);
insert into localised(id) values(875);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(171, 872, 873, 874, 875, NULL, 173, 1);
insert into localised_strings(id, strings_KEY, strings) values(872, 'pl', 'Hypernym');
insert into localised_strings(id, strings_KEY, strings) values(873, 'pl', 'Hyper');
insert into localised_strings(id, strings_KEY, strings) values(874, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(875, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(872, 'en', 'Hypernym');
insert into localised_strings(id, strings_KEY, strings) values(873, 'en', 'Hyper');
insert into localised_strings(id, strings_KEY, strings) values(874, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(875, 'en', '');

# Instance_Hypernym
insert into localised(id) values(876);
insert into localised(id) values(877);
insert into localised(id) values(878);
insert into localised(id) values(879);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(172, 876, 877, 878, 879, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(876, 'pl', 'Instance_Hypernym');
insert into localised_strings(id, strings_KEY, strings) values(877, 'pl', 'InstHyper');
insert into localised_strings(id, strings_KEY, strings) values(878, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(879, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(876, 'en', 'Instance_Hypernym');
insert into localised_strings(id, strings_KEY, strings) values(877, 'en', 'InstHyper');
insert into localised_strings(id, strings_KEY, strings) values(878, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(879, 'en', '');

# Hyponym
insert into localised(id) values(880);
insert into localised(id) values(881);
insert into localised(id) values(882);
insert into localised(id) values(883);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(173, 880, 881, 882, 883, NULL, 171, 1);
insert into localised_strings(id, strings_KEY, strings) values(880, 'pl', 'Hyponym');
insert into localised_strings(id, strings_KEY, strings) values(881, 'pl', 'Hypo');
insert into localised_strings(id, strings_KEY, strings) values(882, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(883, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(880, 'en', 'Hyponym');
insert into localised_strings(id, strings_KEY, strings) values(881, 'en', 'Hypo');
insert into localised_strings(id, strings_KEY, strings) values(882, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(883, 'en', '');

# Instance_Hyponym
insert into localised(id) values(884);
insert into localised(id) values(885);
insert into localised(id) values(886);
insert into localised(id) values(887);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(174, 884, 885, 886, 887, NULL, 172, 1);
insert into localised_strings(id, strings_KEY, strings) values(884, 'pl', 'Instance_Hyponym');
insert into localised_strings(id, strings_KEY, strings) values(885, 'pl', 'InstHypo');
insert into localised_strings(id, strings_KEY, strings) values(886, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(887, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(884, 'en', 'Instance_Hyponym');
insert into localised_strings(id, strings_KEY, strings) values(885, 'en', 'InstHypo');
insert into localised_strings(id, strings_KEY, strings) values(886, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(887, 'en', '');

# Member_holonym
insert into localised(id) values(888);
insert into localised(id) values(889);
insert into localised(id) values(890);
insert into localised(id) values(891);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(175, 888, 889, 890, 891, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(888, 'pl', 'Member_holonym');
insert into localised_strings(id, strings_KEY, strings) values(889, 'pl', 'Holo:member');
insert into localised_strings(id, strings_KEY, strings) values(890, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(891, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(888, 'en', 'Member_holonym');
insert into localised_strings(id, strings_KEY, strings) values(889, 'en', 'Holo:member');
insert into localised_strings(id, strings_KEY, strings) values(890, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(891, 'en', '');

# Substance_holonym
insert into localised(id) values(892);
insert into localised(id) values(893);
insert into localised(id) values(894);
insert into localised(id) values(895);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(176, 892, 893, 894, 895, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(892, 'pl', 'Substance_holonym');
insert into localised_strings(id, strings_KEY, strings) values(893, 'pl', 'Holo:subst');
insert into localised_strings(id, strings_KEY, strings) values(894, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(895, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(892, 'en', 'Substance_holonym');
insert into localised_strings(id, strings_KEY, strings) values(893, 'en', 'Holo:subst');
insert into localised_strings(id, strings_KEY, strings) values(894, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(895, 'en', '');

# Part_holonym
insert into localised(id) values(896);
insert into localised(id) values(897);
insert into localised(id) values(898);
insert into localised(id) values(899);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(177, 896, 897, 898, 899, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(896, 'pl', 'Part_holonym');
insert into localised_strings(id, strings_KEY, strings) values(897, 'pl', 'Holo:part');
insert into localised_strings(id, strings_KEY, strings) values(898, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(899, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(896, 'en', 'Part_holonym');
insert into localised_strings(id, strings_KEY, strings) values(897, 'en', 'Holo:part');
insert into localised_strings(id, strings_KEY, strings) values(898, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(899, 'en', '');

# Member_meronym
insert into localised(id) values(900);
insert into localised(id) values(901);
insert into localised(id) values(902);
insert into localised(id) values(903);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(178, 900, 901, 902, 903, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(900, 'pl', 'Member_meronym');
insert into localised_strings(id, strings_KEY, strings) values(901, 'pl', 'Mero:member');
insert into localised_strings(id, strings_KEY, strings) values(902, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(903, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(900, 'en', 'Member_meronym');
insert into localised_strings(id, strings_KEY, strings) values(901, 'en', 'Mero:member');
insert into localised_strings(id, strings_KEY, strings) values(902, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(903, 'en', '');

# Substance_meronym
insert into localised(id) values(904);
insert into localised(id) values(905);
insert into localised(id) values(906);
insert into localised(id) values(907);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(179, 904, 905, 906, 907, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(904, 'pl', 'Substance_meronym');
insert into localised_strings(id, strings_KEY, strings) values(905, 'pl', 'Mero:subst');
insert into localised_strings(id, strings_KEY, strings) values(906, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(907, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(904, 'en', 'Substance_meronym');
insert into localised_strings(id, strings_KEY, strings) values(905, 'en', 'Mero:subst');
insert into localised_strings(id, strings_KEY, strings) values(906, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(907, 'en', '');

# Part_meronym
insert into localised(id) values(908);
insert into localised(id) values(909);
insert into localised(id) values(910);
insert into localised(id) values(911);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(180, 908, 909, 910, 911, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(908, 'pl', 'Part_meronym');
insert into localised_strings(id, strings_KEY, strings) values(909, 'pl', 'Mero:part');
insert into localised_strings(id, strings_KEY, strings) values(910, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(911, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(908, 'en', 'Part_meronym');
insert into localised_strings(id, strings_KEY, strings) values(909, 'en', 'Mero:part');
insert into localised_strings(id, strings_KEY, strings) values(910, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(911, 'en', '');

# Attribute
insert into localised(id) values(912);
insert into localised(id) values(913);
insert into localised(id) values(914);
insert into localised(id) values(915);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(181, 912, 913, 914, 915, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(912, 'pl', 'Attribute');
insert into localised_strings(id, strings_KEY, strings) values(913, 'pl', 'Attr');
insert into localised_strings(id, strings_KEY, strings) values(914, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(915, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(912, 'en', 'Attribute');
insert into localised_strings(id, strings_KEY, strings) values(913, 'en', 'Attr');
insert into localised_strings(id, strings_KEY, strings) values(914, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(915, 'en', '');

# Domain_of_synset_-_TOPIC
insert into localised(id) values(916);
insert into localised(id) values(917);
insert into localised(id) values(918);
insert into localised(id) values(919);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(183, 916, 917, 918, 919, NULL, 184, 1);
insert into localised_strings(id, strings_KEY, strings) values(916, 'pl', 'Domain_of_synset_-_TOPIC');
insert into localised_strings(id, strings_KEY, strings) values(917, 'pl', 'Domain');
insert into localised_strings(id, strings_KEY, strings) values(918, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(919, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(916, 'en', 'Domain_of_synset_-_TOPIC');
insert into localised_strings(id, strings_KEY, strings) values(917, 'en', 'Domain');
insert into localised_strings(id, strings_KEY, strings) values(918, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(919, 'en', '');

# Member_of_this_domain_-_TOPIC
insert into localised(id) values(920);
insert into localised(id) values(921);
insert into localised(id) values(922);
insert into localised(id) values(923);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(184, 920, 921, 922, 923, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(920, 'pl', 'Member_of_this_domain_-_TOPIC');
insert into localised_strings(id, strings_KEY, strings) values(921, 'pl', 'Topic');
insert into localised_strings(id, strings_KEY, strings) values(922, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(923, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(920, 'en', 'Member_of_this_domain_-_TOPIC');
insert into localised_strings(id, strings_KEY, strings) values(921, 'en', 'Topic');
insert into localised_strings(id, strings_KEY, strings) values(922, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(923, 'en', '');

# Domain_of_synset_-_REGION
insert into localised(id) values(924);
insert into localised(id) values(925);
insert into localised(id) values(926);
insert into localised(id) values(927);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(185, 924, 925, 926, 927, NULL, 186, 1);
insert into localised_strings(id, strings_KEY, strings) values(924, 'pl', 'Domain_of_synset_-_REGION');
insert into localised_strings(id, strings_KEY, strings) values(925, 'pl', 'Domain');
insert into localised_strings(id, strings_KEY, strings) values(926, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(927, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(924, 'en', 'Domain_of_synset_-_REGION');
insert into localised_strings(id, strings_KEY, strings) values(925, 'en', 'Domain');
insert into localised_strings(id, strings_KEY, strings) values(926, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(927, 'en', '');

# Member_of_this_domain_-_REGION
insert into localised(id) values(928);
insert into localised(id) values(929);
insert into localised(id) values(930);
insert into localised(id) values(931);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(186, 928, 929, 930, 931, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(928, 'pl', 'Member_of_this_domain_-_REGION');
insert into localised_strings(id, strings_KEY, strings) values(929, 'pl', 'Region');
insert into localised_strings(id, strings_KEY, strings) values(930, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(931, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(928, 'en', 'Member_of_this_domain_-_REGION');
insert into localised_strings(id, strings_KEY, strings) values(929, 'en', 'Region');
insert into localised_strings(id, strings_KEY, strings) values(930, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(931, 'en', '');

# Domain_of_synset_-_USAGE
insert into localised(id) values(932);
insert into localised(id) values(933);
insert into localised(id) values(934);
insert into localised(id) values(935);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(187, 932, 933, 934, 935, NULL, 188, 1);
insert into localised_strings(id, strings_KEY, strings) values(932, 'pl', 'Domain_of_synset_-_USAGE');
insert into localised_strings(id, strings_KEY, strings) values(933, 'pl', 'Domain');
insert into localised_strings(id, strings_KEY, strings) values(934, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(935, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(932, 'en', 'Domain_of_synset_-_USAGE');
insert into localised_strings(id, strings_KEY, strings) values(933, 'en', 'Domain');
insert into localised_strings(id, strings_KEY, strings) values(934, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(935, 'en', '');

# Member_of_this_domain_-_USAGE
insert into localised(id) values(936);
insert into localised(id) values(937);
insert into localised(id) values(938);
insert into localised(id) values(939);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(188, 936, 937, 938, 939, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(936, 'pl', 'Member_of_this_domain_-_USAGE');
insert into localised_strings(id, strings_KEY, strings) values(937, 'pl', 'Usage');
insert into localised_strings(id, strings_KEY, strings) values(938, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(939, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(936, 'en', 'Member_of_this_domain_-_USAGE');
insert into localised_strings(id, strings_KEY, strings) values(937, 'en', 'Usage');
insert into localised_strings(id, strings_KEY, strings) values(938, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(939, 'en', '');

# Entailment
insert into localised(id) values(940);
insert into localised(id) values(941);
insert into localised(id) values(942);
insert into localised(id) values(943);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(189, 940, 941, 942, 942, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(940, 'pl', 'Entailment');
insert into localised_strings(id, strings_KEY, strings) values(941, 'pl', 'Entailed');
insert into localised_strings(id, strings_KEY, strings) values(942, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(943, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(940, 'en', 'Entailment');
insert into localised_strings(id, strings_KEY, strings) values(941, 'en', 'Entailed');
insert into localised_strings(id, strings_KEY, strings) values(942, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(943, 'en', '');

# Cause
insert into localised(id) values(944);
insert into localised(id) values(945);
insert into localised(id) values(946);
insert into localised(id) values(947);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(190, 944, 945, 946, 947, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(944, 'pl', 'Cause');
insert into localised_strings(id, strings_KEY, strings) values(945, 'pl', 'Caused');
insert into localised_strings(id, strings_KEY, strings) values(946, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(947, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(944, 'en', 'Cause');
insert into localised_strings(id, strings_KEY, strings) values(945, 'en', 'Caused');
insert into localised_strings(id, strings_KEY, strings) values(946, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(947, 'en', '');

# Verb_Group
insert into localised(id) values(948);
insert into localised(id) values(949);
insert into localised(id) values(950);
insert into localised(id) values(951);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(192, 948, 949, 950, 951, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(948, 'pl', 'Verb_Group');
insert into localised_strings(id, strings_KEY, strings) values(949, 'pl', 'VG');
insert into localised_strings(id, strings_KEY, strings) values(950, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(951, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(948, 'en', 'Verb_Group');
insert into localised_strings(id, strings_KEY, strings) values(949, 'en', 'VG');
insert into localised_strings(id, strings_KEY, strings) values(950, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(951, 'en', '');

# Similar_to
insert into localised(id) values(952);
insert into localised(id) values(953);
insert into localised(id) values(954);
insert into localised(id) values(955);
insert into synset_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(193, 952, 953, 954, 955, NULL, 193, 1);
insert into localised_strings(id, strings_KEY, strings) values(952, 'pl', 'Similar_to');
insert into localised_strings(id, strings_KEY, strings) values(953, 'pl', 'Sim');
insert into localised_strings(id, strings_KEY, strings) values(954, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(955, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(952, 'en', 'Similar_to');
insert into localised_strings(id, strings_KEY, strings) values(953, 'en', 'Sim');
insert into localised_strings(id, strings_KEY, strings) values(954, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(955, 'en', '');

SET FOREIGN_KEY_CHECKS = 1;