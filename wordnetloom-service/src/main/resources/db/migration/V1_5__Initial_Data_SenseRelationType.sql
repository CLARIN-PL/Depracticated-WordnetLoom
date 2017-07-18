# anto
insert into localised(id) values(109);
insert into localised(id) values(110);
insert into localised(id) values(111);
insert into localised(id) values(112);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(12, 109, 110, 111, 112, NULL, 12, 1);
insert into localised_strings(id, strings_KEY, strings) values(109, 'pl', 'antonimia');
insert into localised_strings(id, strings_KEY, strings) values(110, 'pl', 'anto');
insert into localised_strings(id, strings_KEY, strings) values(111, 'pl', 'Relacja antonimii wskazuje każdą znaczeniową przeciwstawność między jednostkami leksykalnymi, wyjąwszy konwersję. Do antonimów zaliczamy antonimy komplementarne jak kręgowiec -- bezkręgowiec, antonimy właściwe, np. dzień -- noc  oraz antonimy kierunkowe, np. północ -- południe i antonimy kulturowe, np. anioł -- diabeł. Antonimia jest relacją zachodzącą między jednostkami o tej samej klasie gramatycznej.');
insert into localised_strings(id, strings_KEY, strings) values(112, 'pl', '<x#> jest antonimem <y#>');
insert into localised_strings(id, strings_KEY, strings) values(109, 'en', 'antonimia');
insert into localised_strings(id, strings_KEY, strings) values(110, 'en', 'anto');
insert into localised_strings(id, strings_KEY, strings) values(111, 'en', 'Relacja antonimii wskazuje każdą znaczeniową przeciwstawność między jednostkami leksykalnymi, wyjąwszy konwersję. Do antonimów zaliczamy antonimy komplementarne jak kręgowiec -- bezkręgowiec, antonimy właściwe, np. dzień -- noc  oraz antonimy kierunkowe, np. północ -- południe i antonimy kulturowe, np. anioł -- diabeł. Antonimia jest relacją zachodzącą między jednostkami o tej samej klasie gramatycznej.');
insert into localised_strings(id, strings_KEY, strings) values(112, 'en', '<x#> jest antonimem <y#>');

# konw
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', 'konwersja');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', 'konw');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', 'Konwersja to relacja odwrotności łącząca jednostki leksykalne. Relacja ta przedstawia to samo zjawisko z dwóch różnych stron. Konwersja jest częsta między leksemami należącymi do słownictwa opisującego role społeczne, stopnie pokrewieństwa, stosunki czasowe i przestrzenne. Mówiąc ściślej, konwersami są takie predykaty o co najmniej dwóch walencjach semantycznych, które mają ten sam zbiór walencji, ale różny układ aktantów dla tych samych walencji.');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '<x#> i <y#> to konwersy');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', 'konwersja');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', 'konw');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', 'Konwersja to relacja odwrotności łącząca jednostki leksykalne. Relacja ta przedstawia to samo zjawisko z dwóch różnych stron. Konwersja jest częsta między leksemami należącymi do słownictwa opisującego role społeczne, stopnie pokrewieństwa, stosunki czasowe i przestrzenne. Mówiąc ściślej, konwersami są takie predykaty o co najmniej dwóch walencjach semantycznych, które mają ten sam zbiór walencji, ale różny układ aktantów dla tych samych walencji.');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '<x#> i <y#> to konwersy');

# fuzz
insert into localised(id) values(117);
insert into localised(id) values(118);
insert into localised(id) values(119);
insert into localised(id) values(120);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(19, 117, 118, 119, 120, NULL, 19, 1);
insert into localised_strings(id, strings_KEY, strings) values(117, 'pl', 'fuzzynimia');
insert into localised_strings(id, strings_KEY, strings) values(118, 'pl', 'fuzz');
insert into localised_strings(id, strings_KEY, strings) values(119, 'pl', 'Korzystamy z relacji fuzzynimii w przypadku, gdy bez niej jednostka leksykalna miałaby pozostać poza siecią z braku zdefiniowania odpowiedniej relacji. Fuzzynimia pojawi się więc, gdy wszystkie pozostałe testy zawiodą, a wciąż można będzie powiedzieć, że \"X łączy z Y jakaś silna relacja\". Fuzzynimia w Słowosieci łączy jednostki tej samej klasy gramatycznej.', 'czasownik,rzeczownik,przysłówek,przymiotnik');
insert into localised_strings(id, strings_KEY, strings) values(120, 'pl', '<x#> i <y#> to fuzzynimy');
insert into localised_strings(id, strings_KEY, strings) values(117, 'en', 'fuzzynimia');
insert into localised_strings(id, strings_KEY, strings) values(118, 'en', 'fuzz');
insert into localised_strings(id, strings_KEY, strings) values(119, 'en', 'Korzystamy z relacji fuzzynimii w przypadku, gdy bez niej jednostka leksykalna miałaby pozostać poza siecią z braku zdefiniowania odpowiedniej relacji. Fuzzynimia pojawi się więc, gdy wszystkie pozostałe testy zawiodą, a wciąż można będzie powiedzieć, że \"X łączy z Y jakaś silna relacja\". Fuzzynimia w Słowosieci łączy jednostki tej samej klasy gramatycznej.', 'czasownik,rzeczownik,przysłówek,przymiotnik');
insert into localised_strings(id, strings_KEY, strings) values(120, 'en', '<x#> i <y#> to fuzzynimy');

# rola
insert into localised(id) values(121);
insert into localised(id) values(122);
insert into localised(id) values(123);
insert into localised(id) values(124);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(32, 121, 122, 123, 124, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(121, 'pl', 'rola');
insert into localised_strings(id, strings_KEY, strings) values(122, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(123, 'pl', 'Relacją \"Roli\" łączymy rzeczownik odczasownikowy z jego podstawą słowotwórczą. Warto zwrócić uwagę na ten fakt, że \"Rola\" i bliźniacza relacja \"Zawierania roli\" nie są relacjami odwrotnymi. Związane to jest z kierunkiem derywacji. Zakłada się, że relacja \"Roli\" zachodzi między rzeczownikiem a czasownikiem, gdy czasownik odnosi się do sytuacji, w której aktant wyrażony przez rzeczownik pełni pewną rolę, semantycznie charakteryzując ten czasownik.');
insert into localised_strings(id, strings_KEY, strings) values(124, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(121, 'en', 'rola');
insert into localised_strings(id, strings_KEY, strings) values(122, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(123, 'en', 'Relacją \"Roli\" łączymy rzeczownik odczasownikowy z jego podstawą słowotwórczą. Warto zwrócić uwagę na ten fakt, że \"Rola\" i bliźniacza relacja \"Zawierania roli\" nie są relacjami odwrotnymi. Związane to jest z kierunkiem derywacji. Zakłada się, że relacja \"Roli\" zachodzi między rzeczownikiem a czasownikiem, gdy czasownik odnosi się do sytuacji, w której aktant wyrażony przez rzeczownik pełni pewną rolę, semantycznie charakteryzując ten czasownik.');
insert into localised_strings(id, strings_KEY, strings) values(124, 'en', '');

# zawieranie roli
insert into localised(id) values(124);
insert into localised(id) values(125);
insert into localised(id) values(126);
insert into localised(id) values(127);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(33, 124, 125, 126, 127, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(124, 'pl', 'zawieranie roli');
insert into localised_strings(id, strings_KEY, strings) values(125, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(126, 'pl', 'Relacją \"Zawierania roli\" łączymy czasownik odrzeczownikowy z jego podstawą słowotwórczą. Warto zwrócić uwagę na ten fakt, że \"Rola\" i bliźniacza relacja \"Zawierania roli\" nie są relacjami odwrotnymi. Związane to jest z kierunkiem derywacji. Zakłada się, że relacja \"Zawierania roli\" zachodzi między czasownikiem a rzeczownikiem, gdy czasownik odnosi się do sytuacji, w której aktant wyrażony przez rzeczownik pełni pewną rolę, semantycznie charakteryzując ten czasownik.');
insert into localised_strings(id, strings_KEY, strings) values(127, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(124, 'en', 'zawieranie roli');
insert into localised_strings(id, strings_KEY, strings) values(125, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(126, 'en', 'Relacją \"Zawierania roli\" łączymy czasownik odrzeczownikowy z jego podstawą słowotwórczą. Warto zwrócić uwagę na ten fakt, że \"Rola\" i bliźniacza relacja \"Zawierania roli\" nie są relacjami odwrotnymi. Związane to jest z kierunkiem derywacji. Zakłada się, że relacja \"Zawierania roli\" zachodzi między czasownikiem a rzeczownikiem, gdy czasownik odnosi się do sytuacji, w której aktant wyrażony przez rzeczownik pełni pewną rolę, semantycznie charakteryzując ten czasownik.');
insert into localised_strings(id, strings_KEY, strings) values(127, 'en', '');

# agens|subiekt
insert into localised(id) values(128);
insert into localised(id) values(129);
insert into localised(id) values(130);
insert into localised(id) values(131);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(34, 128, 129, 130, 131, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(128, 'pl', 'agens|subiekt');
insert into localised_strings(id, strings_KEY, strings) values(129, 'pl', 'rol:ag');
insert into localised_strings(id, strings_KEY, strings) values(130, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(131, 'pl', '<x#> jest agensem dla czynności wyrażanej przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(128, 'en', 'agens|subiekt');
insert into localised_strings(id, strings_KEY, strings) values(129, 'en', 'rol:ag');
insert into localised_strings(id, strings_KEY, strings) values(130, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(131, 'en', '<x#> jest agensem dla czynności wyrażanej przez <y#>');


# pacjens|obiekt
insert into localised(id) values(132);
insert into localised(id) values(133);
insert into localised(id) values(134);
insert into localised(id) values(135);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(35, 132, 133, 134, 135, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(132, 'pl', 'pacjens|obiekt');
insert into localised_strings(id, strings_KEY, strings) values(133, 'pl', 'rol:pacj');
insert into localised_strings(id, strings_KEY, strings) values(134, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(135, 'pl', '<x#> jest pacjensem dla czynności wyrażanej przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(132, 'en', 'pacjens|obiekt');
insert into localised_strings(id, strings_KEY, strings) values(133, 'en', 'rol:pacj');
insert into localised_strings(id, strings_KEY, strings) values(134, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(135, 'en', '<x#> jest pacjensem dla czynności wyrażanej przez <y#>');

# narzędzie
insert into localised(id) values(136);
insert into localised(id) values(137);
insert into localised(id) values(138);
insert into localised(id) values(139);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(36, 136, 137, 138, 139, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(136, 'pl', 'narzędzie');
insert into localised_strings(id, strings_KEY, strings) values(137, 'pl', 'rol:narz');
insert into localised_strings(id, strings_KEY, strings) values(138, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(139, 'pl', '<x#> jest narzędziem dla czynności wyrażanej przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(136, 'en', 'narzędzie');
insert into localised_strings(id, strings_KEY, strings) values(137, 'en', 'rol:narz');
insert into localised_strings(id, strings_KEY, strings) values(138, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(139, 'en', '<x#> jest narzędziem dla czynności wyrażanej przez <y#>');

# miejsce
insert into localised(id) values(140);
insert into localised(id) values(141);
insert into localised(id) values(142);
insert into localised(id) values(143);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(37, 140, 141, 142, 143, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(140, 'pl', 'miejsce');
insert into localised_strings(id, strings_KEY, strings) values(141, 'pl', 'rol:msc');
insert into localised_strings(id, strings_KEY, strings) values(142, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(143, 'pl', '<x#> jest miejscem dla czynności wyrażanej przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(140, 'en', 'miejsce');
insert into localised_strings(id, strings_KEY, strings) values(141, 'en', 'rol:msc');
insert into localised_strings(id, strings_KEY, strings) values(142, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(143, 'en', '<x#> jest miejscem dla czynności wyrażanej przez <y#>');

# wytwór|rezultat
insert into localised(id) values(144);
insert into localised(id) values(145);
insert into localised(id) values(146);
insert into localised(id) values(147);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(38, 144, 145, 146, 147, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(144, 'pl', 'wytwór|rezultat');
insert into localised_strings(id, strings_KEY, strings) values(145, 'pl', 'rol:wtw&rez');
insert into localised_strings(id, strings_KEY, strings) values(146, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(147, 'pl', '<x#> jest wytworem|rezultatem dla czynności wyrażanej przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(144, 'en', 'wytwór|rezultat');
insert into localised_strings(id, strings_KEY, strings) values(145, 'en', 'rol:wtw&rez');
insert into localised_strings(id, strings_KEY, strings) values(146, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(147, 'en', '<x#> jest wytworem|rezultatem dla czynności wyrażanej przez <y#>');

# czas
insert into localised(id) values(148);
insert into localised(id) values(149);
insert into localised(id) values(150);
insert into localised(id) values(151);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(39, 148, 149, 150, 151, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(148, 'pl', 'czas');
insert into localised_strings(id, strings_KEY, strings) values(149, 'pl', 'rol:czas');
insert into localised_strings(id, strings_KEY, strings) values(150, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(151, 'pl', '<x#> wyraża czas czynności wyrażanej przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(148, 'en', 'time');
insert into localised_strings(id, strings_KEY, strings) values(149, 'en', 'rol:czas');
insert into localised_strings(id, strings_KEY, strings) values(150, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(151, 'en', '<x#> wyraża czas czynności wyrażanej przez <y#>');

# podtyp nieokreślony
insert into localised(id) values(152);
insert into localised(id) values(153);
insert into localised(id) values(154);
insert into localised(id) values(155);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(40, 152, 153, 154, 155, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(152, 'pl', 'podtyp nieokreślony');
insert into localised_strings(id, strings_KEY, strings) values(153, 'pl', 'rol:?');
insert into localised_strings(id, strings_KEY, strings) values(154, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(155, 'pl', '<x#> jest w relacji rola - podtyp nieokreślony do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(152, 'en', 'podtyp nieokreślony');
insert into localised_strings(id, strings_KEY, strings) values(153, 'en', 'rol:?');
insert into localised_strings(id, strings_KEY, strings) values(154, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(155, 'en', '<x#> jest w relacji rola - podtyp nieokreślony do <y#>');

# zawieranie agensa|subiektu
insert into localised(id) values(156);
insert into localised(id) values(157);
insert into localised(id) values(158);
insert into localised(id) values(159);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(41, 156, 157, 158, 159, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(156, 'pl', 'zawieranie agensa|subiektu');
insert into localised_strings(id, strings_KEY, strings) values(157, 'pl', 'zrol:ag');
insert into localised_strings(id, strings_KEY, strings) values(158, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(159, 'pl', '<x#> to sytuacja, której agensem jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(156, 'en', 'zawieranie agensa|subiektu');
insert into localised_strings(id, strings_KEY, strings) values(157, 'en', 'zrol:ag');
insert into localised_strings(id, strings_KEY, strings) values(158, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(159, 'en', '<x#> to sytuacja, której agensem jest <y#>');

# zawieranie pacjensa|obiektu
insert into localised(id) values(160);
insert into localised(id) values(161);
insert into localised(id) values(162);
insert into localised(id) values(163);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(42, 160, 161, 162, 163, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(160, 'pl', 'zawieranie pacjensa|obiektu');
insert into localised_strings(id, strings_KEY, strings) values(161, 'pl', 'zrol:pacj');
insert into localised_strings(id, strings_KEY, strings) values(162, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(163, 'pl', '<x#> to sytuacja, której pacjensem jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(160, 'en', 'zawieranie pacjensa|obiektu');
insert into localised_strings(id, strings_KEY, strings) values(161, 'en', 'zrol:pacj');
insert into localised_strings(id, strings_KEY, strings) values(162, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(163, 'en', '<x#> to sytuacja, której pacjensem jest <y#>');

# zawieranie czasu
insert into localised(id) values(164);
insert into localised(id) values(165);
insert into localised(id) values(166);
insert into localised(id) values(167);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(43, 164, 165, 166, 167, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(164, 'pl', 'zawieranie czasu');
insert into localised_strings(id, strings_KEY, strings) values(165, 'pl', 'zrol:czas');
insert into localised_strings(id, strings_KEY, strings) values(166, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(167, 'pl', '<x#> to sytuacja, której czasem jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(164, 'en', 'zawieranie czasu');
insert into localised_strings(id, strings_KEY, strings) values(165, 'en', 'zrol:czas');
insert into localised_strings(id, strings_KEY, strings) values(166, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(167, 'en', '<x#> to sytuacja, której czasem jest <y#>');

# zawieranie miejsca
insert into localised(id) values(168);
insert into localised(id) values(169);
insert into localised(id) values(170);
insert into localised(id) values(171);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(44, 168, 169, 170, 171, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(168, 'pl', 'zawieranie miejsca');
insert into localised_strings(id, strings_KEY, strings) values(169, 'pl', 'zrol:msc');
insert into localised_strings(id, strings_KEY, strings) values(170, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(171, 'pl', '<x#> to sytuacja, której miejscem jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(168, 'en', 'zawieranie miejsca');
insert into localised_strings(id, strings_KEY, strings) values(169, 'en', 'zrol:msc');
insert into localised_strings(id, strings_KEY, strings) values(170, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(171, 'en', '<x#> to sytuacja, której miejscem jest <y#>');

# zawieranie narzędzia
insert into localised(id) values(172);
insert into localised(id) values(173);
insert into localised(id) values(174);
insert into localised(id) values(175);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(45, 172, 173, 174, 175, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(172, 'pl', 'zawieranie narzędzia');
insert into localised_strings(id, strings_KEY, strings) values(173, 'pl', 'zrol:narz');
insert into localised_strings(id, strings_KEY, strings) values(174, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(175, 'pl', '<x#> to sytuacja, której narzędziem jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(172, 'en', 'zawieranie narzędzia');
insert into localised_strings(id, strings_KEY, strings) values(173, 'en', 'zrol:narz');
insert into localised_strings(id, strings_KEY, strings) values(174, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(175, 'en', '<x#> to sytuacja, której narzędziem jest <y#>');

# zawieranie wytworu | rezultatu
insert into localised(id) values(176);
insert into localised(id) values(177);
insert into localised(id) values(178);
insert into localised(id) values(179);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(46, 176, 177, 178, 179, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(176, 'pl', 'zawieranie wytworu | rezultatu');
insert into localised_strings(id, strings_KEY, strings) values(177, 'pl', 'zrol:wtw&rez');
insert into localised_strings(id, strings_KEY, strings) values(178, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(179, 'pl', '<x#> to sytuacja, której wytworem|rezultatem jest <y#>');
insert into localised_strings(id, strings_KEY, strings) values(176, 'en', 'zawieranie wytworu | rezultatu');
insert into localised_strings(id, strings_KEY, strings) values(177, 'en', 'zrol:wtw&rez');
insert into localised_strings(id, strings_KEY, strings) values(178, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(179, 'en', '<x#> to sytuacja, której wytworem|rezultatem jest <y#>');

# podtyp nieokreślony
insert into localised(id) values(180);
insert into localised(id) values(181);
insert into localised(id) values(182);
insert into localised(id) values(183);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(47, 180, 181, 182, 183, 33, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(180, 'pl', 'podtyp nieokreślony');
insert into localised_strings(id, strings_KEY, strings) values(181, 'pl', 'zrol:?');
insert into localised_strings(id, strings_KEY, strings) values(182, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(183, 'pl', '<x#> to sytuacja, która jest związana relacją zawieranie roli - podtyp nieokreślony z <y#>');
insert into localised_strings(id, strings_KEY, strings) values(180, 'en', 'podtyp nieokreślony');
insert into localised_strings(id, strings_KEY, strings) values(181, 'en', 'zrol:?');
insert into localised_strings(id, strings_KEY, strings) values(182, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(183, 'en', '<x#> to sytuacja, która jest związana relacją zawieranie roli - podtyp nieokreślony z <y#>');

# agens przy niewyrażonym predykacie
insert into localised(id) values(184);
insert into localised(id) values(185);
insert into localised(id) values(186);
insert into localised(id) values(187);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(48, 184, 185, 186, 187, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(184, 'pl', 'agens przy niewyrażonym predykacie');
insert into localised_strings(id, strings_KEY, strings) values(185, 'pl', 'rol:agNP');
insert into localised_strings(id, strings_KEY, strings) values(186, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(187, 'pl', '<x#> jest agensem przy niewyrażonym predykacie derywowanym od rzeczownika <y#>');
insert into localised_strings(id, strings_KEY, strings) values(184, 'en', 'agens przy niewyrażonym predykacie');
insert into localised_strings(id, strings_KEY, strings) values(185, 'en', 'rol:agNP');
insert into localised_strings(id, strings_KEY, strings) values(186, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(187, 'en', '<x#> jest agensem przy niewyrażonym predykacie derywowanym od rzeczownika <y#>');

# miejsce przy niewyrażonym predykaci
insert into localised(id) values(188);
insert into localised(id) values(189);
insert into localised(id) values(190);
insert into localised(id) values(191);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(49, 188, 189, 190, 191, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(188, 'pl', 'miejsce przy niewyrażonym predykacie');
insert into localised_strings(id, strings_KEY, strings) values(189, 'pl', 'rol:mscNP');
insert into localised_strings(id, strings_KEY, strings) values(190, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(191, 'pl', '<x#> jest miejscem przy niewyrażonym predykacie derywowanym od rzeczownika <y#>');
insert into localised_strings(id, strings_KEY, strings) values(188, 'en', 'miejsce przy niewyrażonym predykacie');
insert into localised_strings(id, strings_KEY, strings) values(189, 'en', 'rol:mscNP');
insert into localised_strings(id, strings_KEY, strings) values(190, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(191, 'en', '<x#> jest miejscem przy niewyrażonym predykacie derywowanym od rzeczownika <y#>');

# wytwór | rezultat przy niewyrażonym predykacie
insert into localised(id) values(192);
insert into localised(id) values(193);
insert into localised(id) values(194);
insert into localised(id) values(195);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(50, 192, 193, 194, 195, 32, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(192, 'pl', 'wytwór | rezultat przy niewyrażonym predykacie');
insert into localised_strings(id, strings_KEY, strings) values(193, 'pl', 'rol:wtw&rezNP');
insert into localised_strings(id, strings_KEY, strings) values(194, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(195, 'pl', '<x#> jest rezultatem|wytworem przy niewyrażonym predykacie derywowanym od rzeczownika <y#>');
insert into localised_strings(id, strings_KEY, strings) values(192, 'en', 'rol:wtw&rezNP');
insert into localised_strings(id, strings_KEY, strings) values(193, 'en', 'wytwór | rezultat przy niewyrażonym predykacie');
insert into localised_strings(id, strings_KEY, strings) values(194, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(195, 'en', '<x#> jest rezultatem|wytworem przy niewyrażonym predykacie derywowanym od rzeczownika <y#>');

# nosiciel_stanu|cechy
insert into localised(id) values(196);
insert into localised(id) values(197);
insert into localised(id) values(198);
insert into localised(id) values(199);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(51, 196, 197, 198, 199, NULL, 52, 1);
insert into localised_strings(id, strings_KEY, strings) values(196, 'pl', 'nosiciel_stanu|cechy');
insert into localised_strings(id, strings_KEY, strings) values(197, 'pl', 'nos:s&c');
insert into localised_strings(id, strings_KEY, strings) values(198, 'pl', 'Relacja służy połączeniu rzeczowników opisujących kogoś lub coś, będących w określonym stanie, przejawiających jakiś stan lub charakterystycznych ze względu na jakąś cechę, z przymiotnikami, które wyrażają stan lub cechę. Rzeczownik jest derywowany od przymiotnika.');
insert into localised_strings(id, strings_KEY, strings) values(199, 'pl', '<x#> jest nosicielem stanu|cechy <y#>');
insert into localised_strings(id, strings_KEY, strings) values(196, 'en', 'nosiciel_stanu|cechy');
insert into localised_strings(id, strings_KEY, strings) values(197, 'en', 'nos:s&c');
insert into localised_strings(id, strings_KEY, strings) values(198, 'en', 'Relacja służy połączeniu rzeczowników opisujących kogoś lub coś, będących w określonym stanie, przejawiających jakiś stan lub charakterystycznych ze względu na jakąś cechę, z przymiotnikami, które wyrażają stan lub cechę. Rzeczownik jest derywowany od przymiotnika.');
insert into localised_strings(id, strings_KEY, strings) values(199, 'en', '<x#> jest nosicielem stanu|cechy <y#>');

# stan|cecha
insert into localised(id) values(200);
insert into localised(id) values(201);
insert into localised(id) values(202);
insert into localised(id) values(203);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(52, 200, 201, 202, 203, NULL, 51, 1);
insert into localised_strings(id, strings_KEY, strings) values(200, 'pl', 'stan|cecha');
insert into localised_strings(id, strings_KEY, strings) values(201, 'pl', 'st&cech');
insert into localised_strings(id, strings_KEY, strings) values(202, 'pl', 'Relacja służy połączeniu rzeczowników opisujących kogoś lub coś, będących w określonym stanie, przejawiających jakiś stan lub charakterystycznych ze względu na jakąś cechę, z przymiotnikami, które wyrażają stan lub cechę. Przymiotnik jest bazą derywacyjną dla rzeczownika.');
insert into localised_strings(id, strings_KEY, strings) values(203, 'pl', '<x#> jest stanem|cechą <y#>');
insert into localised_strings(id, strings_KEY, strings) values(200, 'en', 'stan|cecha');
insert into localised_strings(id, strings_KEY, strings) values(201, 'en', 'st&cech');
insert into localised_strings(id, strings_KEY, strings) values(202, 'en', 'Relacja służy połączeniu rzeczowników opisujących kogoś lub coś, będących w określonym stanie, przejawiających jakiś stan lub charakterystycznych ze względu na jakąś cechę, z przymiotnikami, które wyrażają stan lub cechę. Przymiotnik jest bazą derywacyjną dla rzeczownika.');
insert into localised_strings(id, strings_KEY, strings) values(203, 'en', '<x#> jest stanem|cechą <y#>');

# żeńskość
insert into localised(id) values(204);
insert into localised(id) values(205);
insert into localised(id) values(206);
insert into localised(id) values(207);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(53, 204, 205, 206, 207, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(204, 'pl', 'żeńskość');
insert into localised_strings(id, strings_KEY, strings) values(205, 'pl', 'żeń');
insert into localised_strings(id, strings_KEY, strings) values(206, 'pl', 'Dla nazw żeńskich wyprowadzanych z rzeczowników męskich wprowadzamy relację żeńskości. Relacja ta łączyć będzie jednostki leksykalne.\nPary rzeczowników niezwiązanych morfologicznie, a wyrażające różnicę płci, na przykład \"klacz\" i \"ogier\", łączymy relacją konwersji.');
insert into localised_strings(id, strings_KEY, strings) values(207, 'pl', '<x#> jest nazwą żeńską derywowaną od <y#>');
insert into localised_strings(id, strings_KEY, strings) values(204, 'en', 'żeńskość');
insert into localised_strings(id, strings_KEY, strings) values(205, 'en', 'żeń');
insert into localised_strings(id, strings_KEY, strings) values(206, 'en', 'Dla nazw żeńskich wyprowadzanych z rzeczowników męskich wprowadzamy relację żeńskości. Relacja ta łączyć będzie jednostki leksykalne.\nPary rzeczowników niezwiązanych morfologicznie, a wyrażające różnicę płci, na przykład \"klacz\" i \"ogier\", łączymy relacją konwersji.');
insert into localised_strings(id, strings_KEY, strings) values(207, 'en', '<x#> jest nazwą żeńską derywowaną od <y#>');

# nacechowanie
insert into localised(id) values(208);
insert into localised(id) values(209);
insert into localised(id) values(210);
insert into localised(id) values(211);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(54, 208, 209, 210, 211, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(208, 'pl', 'nacechowanie');
insert into localised_strings(id, strings_KEY, strings) values(209, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(210, 'pl', 'Relacja nacechowania łączy jednostki leksykalne, dla których istnieje związek derywacyjny o charakterze stylistycznym. Należy zwracać szczególną uwagę na to, czy wyrazy łączy związek derywacyjny. Może się zdarzyć, że wyraz o kształcie derywatu jest już zleksykalizowany (i np. synonimiczny lub bliskoznaczny do innego wyrazu).');
insert into localised_strings(id, strings_KEY, strings) values(211, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(208, 'en', 'nacechowanie');
insert into localised_strings(id, strings_KEY, strings) values(209, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(210, 'en', 'Relacja nacechowania łączy jednostki leksykalne, dla których istnieje związek derywacyjny o charakterze stylistycznym. Należy zwracać szczególną uwagę na to, czy wyrazy łączy związek derywacyjny. Może się zdarzyć, że wyraz o kształcie derywatu jest już zleksykalizowany (i np. synonimiczny lub bliskoznaczny do innego wyrazu).');
insert into localised_strings(id, strings_KEY, strings) values(211, 'en', '');

# istota młoda
insert into localised(id) values(212);
insert into localised(id) values(213);
insert into localised(id) values(214);
insert into localised(id) values(215);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(55, 212, 213, 214, 215, 54, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(212, 'pl', 'istota młoda');
insert into localised_strings(id, strings_KEY, strings) values(213, 'pl', 'nac:ist_mł');
insert into localised_strings(id, strings_KEY, strings) values(214, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(215, 'pl', '<x#> to nazwa istoty młodej derywowana od <y#>');
insert into localised_strings(id, strings_KEY, strings) values(212, 'en', 'istota młoda');
insert into localised_strings(id, strings_KEY, strings) values(213, 'en', 'nac:ist_mł');
insert into localised_strings(id, strings_KEY, strings) values(214, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(215, 'en', '<x#> to nazwa istoty młodej derywowana od <y#>');

# deminutywność
insert into localised(id) values(216);
insert into localised(id) values(217);
insert into localised(id) values(218);
insert into localised(id) values(219);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(56, 216, 217, 218, 219, 54, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(216, 'pl', 'deminutywność');
insert into localised_strings(id, strings_KEY, strings) values(217, 'pl', 'nac:dem');
insert into localised_strings(id, strings_KEY, strings) values(218, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(219, 'pl', '<x#> jest deminutywne względem <y#>');
insert into localised_strings(id, strings_KEY, strings) values(216, 'en', 'deminutywność');
insert into localised_strings(id, strings_KEY, strings) values(217, 'en', 'nac:dem');
insert into localised_strings(id, strings_KEY, strings) values(218, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(219, 'en', '<x#> jest deminutywne względem <y#>');

# ekspresywność | augmentatywność
insert into localised(id) values(220);
insert into localised(id) values(221);
insert into localised(id) values(222);
insert into localised(id) values(223);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(57, 220, 221, 222, 223, 54, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(220, 'pl', 'ekspresywność | augmentatywność');
insert into localised_strings(id, strings_KEY, strings) values(221, 'pl', 'nac:eks&aug');
insert into localised_strings(id, strings_KEY, strings) values(222, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(223, 'pl', '<x#>\ to nazwa ekspresywna/augmentatywna derywowana od <y#>');
insert into localised_strings(id, strings_KEY, strings) values(220, 'en', 'ekspresywność | augmentatywność');
insert into localised_strings(id, strings_KEY, strings) values(221, 'en', 'nac:eks&aug');
insert into localised_strings(id, strings_KEY, strings) values(222, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(223, 'en', '<x#> to nazwa ekspresywna/augmentatywna derywowana od <y#>');

# derywacyjność
insert into localised(id) values(224);
insert into localised(id) values(225);
insert into localised(id) values(226);
insert into localised(id) values(227);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(59, 224, 225, 226, 227, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', 'derywacyjność');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', 'der');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', 'Derywaty synchroniczne reprezentujące procesy mniej regularne łączymy z podstawami słowotwórczymi relacją derywacyjności.');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '<x#> jest derywatem od <y#>');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', 'derywacyjność');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', 'der');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', 'Derywaty synchroniczne reprezentujące procesy mniej regularne łączymy z podstawami słowotwórczymi relacją derywacyjności.');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '<x#> jest derywatem od <y#>');

# synonimia międzyparadygmatyczna
insert into localised(id) values(228);
insert into localised(id) values(229);
insert into localised(id) values(230);
insert into localised(id) values(231);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(61, 228, 229, 230, 231, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(228, 'pl', 'synonimia międzyparadygmatyczna');
insert into localised_strings(id, strings_KEY, strings) values(229, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(230, 'pl', 'O synonimii międzyparadygmatycznej mówimy, gdy to samo znaczenie reprezentują co najmniej dwa leksemy należące do różnych klas gramatycznych. Są to tzw. derywaty transpozycyjne.');
insert into localised_strings(id, strings_KEY, strings) values(231, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(228, 'en', 'synonimia międzyparadygmatyczna');
insert into localised_strings(id, strings_KEY, strings) values(229, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(230, 'en', 'O synonimii międzyparadygmatycznej mówimy, gdy to samo znaczenie reprezentują co najmniej dwa leksemy należące do różnych klas gramatycznych. Są to tzw. derywaty transpozycyjne.');
insert into localised_strings(id, strings_KEY, strings) values(231, 'en', '');

# synonimia międzyparadygmatyczna N-V
insert into localised(id) values(232);
insert into localised(id) values(233);
insert into localised(id) values(234);
insert into localised(id) values(235);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(62, 232, 233, 234, 235, 61, 141, 1);
insert into localised_strings(id, strings_KEY, strings) values(232, 'pl', 'synonimia międzyparadygmatyczna N-V');
insert into localised_strings(id, strings_KEY, strings) values(233, 'pl', 'syn:mpar_NV');
insert into localised_strings(id, strings_KEY, strings) values(234, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(235, 'pl', '<x#> i <y#> to synonimy międzyparadygmatyczne');
insert into localised_strings(id, strings_KEY, strings) values(232, 'en', 'synonimia międzyparadygmatyczna N-V');
insert into localised_strings(id, strings_KEY, strings) values(233, 'en', 'syn:mpar_NV');
insert into localised_strings(id, strings_KEY, strings) values(234, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(235, 'en', '<x#> i <y#> to synonimy międzyparadygmatyczne');

# synonimia międzyparadygmatyczna N-ADJ
insert into localised(id) values(236);
insert into localised(id) values(237);
insert into localised(id) values(238);
insert into localised(id) values(239);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(63, 236, 237, 238, 239, 61, 142, 1);
insert into localised_strings(id, strings_KEY, strings) values(236, 'pl', 'synonimia międzyparadygmatyczna N-ADJ');
insert into localised_strings(id, strings_KEY, strings) values(237, 'pl', 'syn:mpar_NAdj');
insert into localised_strings(id, strings_KEY, strings) values(238, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(239, 'pl', '<x#> i <y#> to synonimy międzyparadygmatyczne');
insert into localised_strings(id, strings_KEY, strings) values(236, 'en', 'synonimia międzyparadygmatyczna N-ADJ');
insert into localised_strings(id, strings_KEY, strings) values(237, 'en', 'syn:mpar_NAdj');
insert into localised_strings(id, strings_KEY, strings) values(238, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(239, 'en', '<x#> i <y#> to synonimy międzyparadygmatyczne');

# synonimia międzyparadygmatyczna V-N
insert into localised(id) values(240);
insert into localised(id) values(241);
insert into localised(id) values(242);
insert into localised(id) values(243);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(141, 240, 241, 242, 243, 61, 62, 1);
insert into localised_strings(id, strings_KEY, strings) values(240, 'pl', 'synonimia międzyparadygmatyczna V-N');
insert into localised_strings(id, strings_KEY, strings) values(241, 'pl', 'syn:mpar_VN');
insert into localised_strings(id, strings_KEY, strings) values(242, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(243, 'pl', '<x#> i <y#> to synonimy międzyparadygmatyczne');
insert into localised_strings(id, strings_KEY, strings) values(240, 'en', 'synonimia międzyparadygmatyczna V-N');
insert into localised_strings(id, strings_KEY, strings) values(241, 'en', 'syn:mpar_VN');
insert into localised_strings(id, strings_KEY, strings) values(242, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(243, 'en', '<x#> i <y#> to synonimy międzyparadygmatyczne');

# synonimia międzyparadygmatyczna ADJ-N
insert into localised(id) values(244);
insert into localised(id) values(245);
insert into localised(id) values(246);
insert into localised(id) values(247);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(142, 244, 245, 246, 247, 61, 63, 1);
insert into localised_strings(id, strings_KEY, strings) values(244, 'pl', 'synonimia międzyparadygmatyczna ADJ-N');
insert into localised_strings(id, strings_KEY, strings) values(245, 'pl', 'syn:mpar_AdjN');
insert into localised_strings(id, strings_KEY, strings) values(246, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(247, 'pl', '<x#> i <y#> to synonimy międzyparadygmatyczne');
insert into localised_strings(id, strings_KEY, strings) values(244, 'en', 'synonimia międzyparadygmatyczna ADJ-N');
insert into localised_strings(id, strings_KEY, strings) values(245, 'en', 'syn:mpar_AdjN');
insert into localised_strings(id, strings_KEY, strings) values(246, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(247, 'en', '<x#> i <y#> to synonimy międzyparadygmatyczne');

# synonimia międzyparadygmatyczna dla relacyjnych
insert into localised(id) values(248);
insert into localised(id) values(249);
insert into localised(id) values(250);
insert into localised(id) values(251);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(169, 248, 249, 250, 251, 61, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(248, 'pl', 'synonimia międzyparadygmatyczna dla relacyjnych');
insert into localised_strings(id, strings_KEY, strings) values(249, 'pl', 'syn.mpar.rel.');
insert into localised_strings(id, strings_KEY, strings) values(250, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(251, 'pl', '<x#> jest synonimem mpar. do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(248, 'en', 'synonimia międzyparadygmatyczna dla relacyjnych');
insert into localised_strings(id, strings_KEY, strings) values(249, 'en', 'syn.mpar.rel.');
insert into localised_strings(id, strings_KEY, strings) values(250, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(251, 'en', '<x#> jest synonimem mpar. do <y#>');

# synonimia międzyparadygmatyczna ADV-ADJ
insert into localised(id) values(252);
insert into localised(id) values(253);
insert into localised(id) values(254);
insert into localised(id) values(255);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(244, 252, 253, 254, 255, 61, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(252, 'pl', 'synonimia międzyparadygmatyczna ADV-ADJ');
insert into localised_strings(id, strings_KEY, strings) values(253, 'pl', 'syn:mpar_AdvAdj');
insert into localised_strings(id, strings_KEY, strings) values(254, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(255, 'pl', '<x#> i <y#> są synonimami międzyparadygmatycznymi');
insert into localised_strings(id, strings_KEY, strings) values(252, 'en', 'synonimia międzyparadygmatyczna ADV-ADJ');
insert into localised_strings(id, strings_KEY, strings) values(253, 'en', 'syn:mpar_AdvAdj');
insert into localised_strings(id, strings_KEY, strings) values(254, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(255, 'en', '<x#> i <y#> są synonimami międzyparadygmatycznymi');

# aspektowość
insert into localised(id) values(256);
insert into localised(id) values(257);
insert into localised(id) values(258);
insert into localised(id) values(259);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(73, 256, 257, 258, 259, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(256, 'pl', 'aspektowość');
insert into localised_strings(id, strings_KEY, strings) values(257, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(258, 'pl', 'Jest to relacja leksykalna łącząca czyste pary aspektowe, typu \"pisać-napisać\" i \"przepisać-przepisywać\"; łączy czasowniki powiązane zależnościami leksykalnymi. Test wtórnej imperfektywizacji nie jest spełniony dla takich czasowników');
insert into localised_strings(id, strings_KEY, strings) values(259, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(256, 'en', 'aspektowość');
insert into localised_strings(id, strings_KEY, strings) values(257, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(258, 'en', 'Jest to relacja leksykalna łącząca czyste pary aspektowe, typu \"pisać-napisać\" i \"przepisać-przepisywać\"; łączy czasowniki powiązane zależnościami leksykalnymi. Test wtórnej imperfektywizacji nie jest spełniony dla takich czasowników');
insert into localised_strings(id, strings_KEY, strings) values(259, 'en', '');

# aspektowość czysta DK-NDK
insert into localised(id) values(260);
insert into localised(id) values(261);
insert into localised(id) values(262);
insert into localised(id) values(263);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(74, 260, 261, 262, 263, 73, 75, 1);
insert into localised_strings(id, strings_KEY, strings) values(260, 'pl', 'aspektowość czysta DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(261, 'pl', 'asp_cz_DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(262, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(263, 'pl', '<x#> i <y#> tworzą czystą parę aspektową');
insert into localised_strings(id, strings_KEY, strings) values(260, 'en', 'aspektowość czysta DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(261, 'en', 'asp_cz_DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(262, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(263, 'en', '<x#> i <y#> tworzą czystą parę aspektową');

# aspektowość czysta NDK-DK
insert into localised(id) values(264);
insert into localised(id) values(265);
insert into localised(id) values(266);
insert into localised(id) values(267);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(75, 264, 265, 266, 267, 73, 74, 1);
insert into localised_strings(id, strings_KEY, strings) values(264, 'pl', 'aspektowość czysta NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(265, 'pl', 'asp_cz_NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(266, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(267, 'pl', '<x#> i <y#> tworzą parę aspektową');
insert into localised_strings(id, strings_KEY, strings) values(264, 'en', 'aspektowość czysta NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(265, 'en', 'asp_cz_NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(266, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(267, 'en', '<x#> i <y#> tworzą parę aspektową');

# aspektowość wtórna DK-NDK
insert into localised(id) values(268);
insert into localised(id) values(269);
insert into localised(id) values(270);
insert into localised(id) values(271);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(110, 268, 269, 270, 271, 73, 111, 1);
insert into localised_strings(id, strings_KEY, strings) values(268, 'pl', 'aspektowość wtórna DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(269, 'pl', 'asp_wt_DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(270, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(271, 'pl', '<x#> i <y#> tworzą wtórną parę aspektową');
insert into localised_strings(id, strings_KEY, strings) values(268, 'en', 'aspektowość wtórna DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(269, 'en', 'asp_wt_DK-NDK');
insert into localised_strings(id, strings_KEY, strings) values(270, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(271, 'en', '<x#> i <y#> tworzą wtórną parę aspektową');

# aspektowość wtórna NDK-DK
insert into localised(id) values(272);
insert into localised(id) values(273);
insert into localised(id) values(274);
insert into localised(id) values(275);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(111, 272, 273, 274, 275, 73, 110, 1);
insert into localised_strings(id, strings_KEY, strings) values(272, 'pl', 'aspektowość wtórna NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(273, 'pl', 'asp_wt_NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(274, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(275, 'pl', '<x#> i <y#> tworzą wtórną parę aspektową');
insert into localised_strings(id, strings_KEY, strings) values(272, 'en', 'aspektowość wtórna NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(273, 'en', 'asp_wt_NDK-DK');
insert into localised_strings(id, strings_KEY, strings) values(274, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(275, 'en', '<x#> i <y#> tworzą wtórną parę aspektową');

# aspektowość czysta
insert into localised(id) values(276);
insert into localised(id) values(277);
insert into localised(id) values(278);
insert into localised(id) values(279);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3425, 276, 277, 278, 279, 73, 3425, 1);
insert into localised_strings(id, strings_KEY, strings) values(276, 'pl', 'aspektowość czysta');
insert into localised_strings(id, strings_KEY, strings) values(277, 'pl', 'ASPcz');
insert into localised_strings(id, strings_KEY, strings) values(278, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(279, 'pl', '<x#> i <y#> tworzą parę aspektową czystą');
insert into localised_strings(id, strings_KEY, strings) values(276, 'en', 'aspektowość czysta');
insert into localised_strings(id, strings_KEY, strings) values(277, 'en', 'ASPcz');
insert into localised_strings(id, strings_KEY, strings) values(278, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(279, 'en', '<x#> i <y#> tworzą parę aspektową czystą');

# aspektowość wtórna
insert into localised(id) values(280);
insert into localised(id) values(281);
insert into localised(id) values(282);
insert into localised(id) values(283);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(3426, 280, 281, 282, 283, 73, 3426, 1);
insert into localised_strings(id, strings_KEY, strings) values(280, 'pl', 'aspektowość wtórna');
insert into localised_strings(id, strings_KEY, strings) values(281, 'pl', 'ASPwt');
insert into localised_strings(id, strings_KEY, strings) values(282, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(283, 'pl', '<x#> i <y#> tworzą parę aspektową wtórną');
insert into localised_strings(id, strings_KEY, strings) values(280, 'en', 'aspektowość wtórna');
insert into localised_strings(id, strings_KEY, strings) values(281, 'en', 'ASPwt');
insert into localised_strings(id, strings_KEY, strings) values(282, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(283, 'en', '<x#> i <y#> tworzą parę aspektową wtórną');

# antonimia komplementarna
insert into localised(id) values(284);
insert into localised(id) values(285);
insert into localised(id) values(286);
insert into localised(id) values(287);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(101, 284, 285, 286, 287, 12, 101, 1);
insert into localised_strings(id, strings_KEY, strings) values(284, 'pl', 'antonimia komplementarna');
insert into localised_strings(id, strings_KEY, strings) values(285, 'pl', 'ant_kom');
insert into localised_strings(id, strings_KEY, strings) values(286, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(287, 'pl', '<x#> i <y#> to antonimy komplementarne');
insert into localised_strings(id, strings_KEY, strings) values(284, 'en', 'antonimia komplementarna');
insert into localised_strings(id, strings_KEY, strings) values(285, 'en', 'ant_kom');
insert into localised_strings(id, strings_KEY, strings) values(286, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(287, 'en', '<x#> i <y#> to antonimy komplementarne');

# forma kanoniczna
insert into localised(id) values(288);
insert into localised(id) values(289);
insert into localised(id) values(290);
insert into localised(id) values(291);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(102, 288, 289, 290, 291, 12, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(288, 'pl', 'forma kanoniczna');
insert into localised_strings(id, strings_KEY, strings) values(289, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(290, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(291, 'pl', '<x#> i <y#> to antonimy');
insert into localised_strings(id, strings_KEY, strings) values(288, 'en', 'forma kanoniczna');
insert into localised_strings(id, strings_KEY, strings) values(289, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(290, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(291, 'en', '<x#> i <y#> to antonimy');

# antonimia właściwa
insert into localised(id) values(292);
insert into localised(id) values(293);
insert into localised(id) values(294);
insert into localised(id) values(295);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(104, 292, 293, 294, 295, 12, 104, 1);
insert into localised_strings(id, strings_KEY, strings) values(292, 'pl', 'antonimia właściwa');
insert into localised_strings(id, strings_KEY, strings) values(293, 'pl', 'ant_wł');
insert into localised_strings(id, strings_KEY, strings) values(294, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(295, 'pl', '<x#> i <y#> to antonimy właściwe');
insert into localised_strings(id, strings_KEY, strings) values(292, 'en', 'antonimia właściwa');
insert into localised_strings(id, strings_KEY, strings) values(293, 'en', 'ant_wł');
insert into localised_strings(id, strings_KEY, strings) values(294, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(295, 'en', '<x#> i <y#> to antonimy właściwe');

# podobieństwo
insert into localised(id) values(296);
insert into localised(id) values(297);
insert into localised(id) values(298);
insert into localised(id) values(299);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(148, 296, 297, 298, 299, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(296, 'pl', 'podobieństwo');
insert into localised_strings(id, strings_KEY, strings) values(297, 'pl', 'sim');
insert into localised_strings(id, strings_KEY, strings) values(298, 'pl', 'Relacją tą łączymy przymiotnik odrzeczownikowy z jego bazą słowotwórczą, jeśli przymiotnik jest jakościowy i przypomina byt wyrażony rzeczownikiem pod jakimś względem.', 'rzeczownik,przymiotnik');
insert into localised_strings(id, strings_KEY, strings) values(299, 'pl', '<x#> przypomina <y#>');
insert into localised_strings(id, strings_KEY, strings) values(296, 'en', 'podobieństwo');
insert into localised_strings(id, strings_KEY, strings) values(297, 'en', 'sim');
insert into localised_strings(id, strings_KEY, strings) values(298, 'en', 'Relacją tą łączymy przymiotnik odrzeczownikowy z jego bazą słowotwórczą, jeśli przymiotnik jest jakościowy i przypomina byt wyrażony rzeczownikiem pod jakimś względem.', 'rzeczownik,przymiotnik');
insert into localised_strings(id, strings_KEY, strings) values(299, 'en', '<x#> przypomina <y#>');

# stopniowanie
insert into localised(id) values(300);
insert into localised(id) values(301);
insert into localised(id) values(302);
insert into localised(id) values(303);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(150, 300, 301, 302, 303, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(300, 'pl', 'stopniowanie');
insert into localised_strings(id, strings_KEY, strings) values(301, 'pl', 'stopn');
insert into localised_strings(id, strings_KEY, strings) values(302, 'pl', 'Syntetyczne formy stopnia wyższego i najwyższego uznajemy za osobne jednostki leksykalne');
insert into localised_strings(id, strings_KEY, strings) values(303, 'pl', '<x#> jest w stopniu wyższym lub najwyższym');
insert into localised_strings(id, strings_KEY, strings) values(300, 'en', 'stopniowanie');
insert into localised_strings(id, strings_KEY, strings) values(301, 'en', 'stopn');
insert into localised_strings(id, strings_KEY, strings) values(302, 'en', 'Syntetyczne formy stopnia wyższego i najwyższego uznajemy za osobne jednostki leksykalne');
insert into localised_strings(id, strings_KEY, strings) values(303, 'en', '<x#> jest w stopniu wyższym lub najwyższym');

# stopień wyższy
insert into localised(id) values(304);
insert into localised(id) values(305);
insert into localised(id) values(306);
insert into localised(id) values(307);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(151, 304, 305, 306, 307, 150, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(304, 'pl', 'stopień wyższy');
insert into localised_strings(id, strings_KEY, strings) values(305, 'pl', 'comp');
insert into localised_strings(id, strings_KEY, strings) values(306, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(307, 'pl', '<x#> jest formą stopnia wyższego od <y#>');
insert into localised_strings(id, strings_KEY, strings) values(304, 'en', 'stopień wyższy');
insert into localised_strings(id, strings_KEY, strings) values(305, 'en', 'comp');
insert into localised_strings(id, strings_KEY, strings) values(306, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(307, 'en', '<x#> jest formą stopnia wyższego od <y#>');

# stopień najwyższy
insert into localised(id) values(308);
insert into localised(id) values(309);
insert into localised(id) values(310);
insert into localised(id) values(311);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(152, 308, 309, 310, 311, 150, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(308, 'pl', 'stopień najwyższy');
insert into localised_strings(id, strings_KEY, strings) values(309, 'pl', 'sup');
insert into localised_strings(id, strings_KEY, strings) values(320, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(311, 'pl', '<x#> jest formą stopnia najwyższego, derywowaną od <y#>');
insert into localised_strings(id, strings_KEY, strings) values(308, 'en', 'stopień najwyższy');
insert into localised_strings(id, strings_KEY, strings) values(309, 'en', 'sup');
insert into localised_strings(id, strings_KEY, strings) values(310, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(311, 'en', '<x#> jest formą stopnia najwyższego, derywowaną od <y#>');

# charakteryzowanie
insert into localised(id) values(312);
insert into localised(id) values(313);
insert into localised(id) values(314);
insert into localised(id) values(315);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(149, 312, 313, 314, 315, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(312, 'pl', 'charakteryzowanie');
insert into localised_strings(id, strings_KEY, strings) values(313, 'pl', 'cha');
insert into localised_strings(id, strings_KEY, strings) values(314, 'pl', 'Relacją tą łączymy te odrzeczownikowe przymiotniki jakościowe z ich podstawami słowotórczymi.');
insert into localised_strings(id, strings_KEY, strings) values(315, 'pl', '<x#> jest charakteryzowane przez <y#>');
insert into localised_strings(id, strings_KEY, strings) values(312, 'en', 'charakteryzowanie');
insert into localised_strings(id, strings_KEY, strings) values(313, 'en', 'cha');
insert into localised_strings(id, strings_KEY, strings) values(314, 'en', 'Relacją tą łączymy te odrzeczownikowe przymiotniki jakościowe z ich podstawami słowotórczymi.');
insert into localised_strings(id, strings_KEY, strings) values(315, 'en', '<x#> jest charakteryzowane przez <y#>');

# rola Adj-V
insert into localised(id) values(316);
insert into localised(id) values(317);
insert into localised(id) values(318);
insert into localised(id) values(319);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(153, 316, 317, 318, 319, NULL, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(316, 'pl', 'rola Adj-V');
insert into localised_strings(id, strings_KEY, strings) values(317, 'pl', 'rola_Adj-V');
insert into localised_strings(id, strings_KEY, strings) values(318, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(319, 'pl', '<x#> jest w relacji roli do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(316, 'en', 'rola Adj-V');
insert into localised_strings(id, strings_KEY, strings) values(317, 'en', 'rola_Adj-V');
insert into localised_strings(id, strings_KEY, strings) values(318, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(319, 'en', '<x#> jest w relacji roli do <y#>');

# subiekt
insert into localised(id) values(320);
insert into localised(id) values(321);
insert into localised(id) values(322);
insert into localised(id) values(323);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(154, 320, 321, 322, 323, 153, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(320, 'pl', 'subiekt');
insert into localised_strings(id, strings_KEY, strings) values(321, 'pl', 'sub');
insert into localised_strings(id, strings_KEY, strings) values(322, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(323, 'pl', '<x#> jest w relacji agensa do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(320, 'en', 'subiekt');
insert into localised_strings(id, strings_KEY, strings) values(321, 'en', 'sub');
insert into localised_strings(id, strings_KEY, strings) values(322, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(323, 'en', '<x#> jest w relacji agensa do <y#>');

# obiekt
insert into localised(id) values(324);
insert into localised(id) values(325);
insert into localised(id) values(326);
insert into localised(id) values(327);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(155, 324, 325, 326, 327, 153, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(324, 'pl', 'obiekt');
insert into localised_strings(id, strings_KEY, strings) values(325, 'pl', 'ob');
insert into localised_strings(id, strings_KEY, strings) values(326, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(327, 'pl', '<x#> jest w relacji pacjensa do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(324, 'en', 'obiekt');
insert into localised_strings(id, strings_KEY, strings) values(325, 'en', 'ob');
insert into localised_strings(id, strings_KEY, strings) values(326, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(327, 'en', '<x#> jest w relacji pacjensa do <y#>');
  
# instrument
insert into localised(id) values(328);
insert into localised(id) values(329);
insert into localised(id) values(330);
insert into localised(id) values(331);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(156, 328, 329, 330, 331, 153, NULL, 0);
insert into localised_strings(id, strings_KEY, strings) values(328, 'pl', 'instrument');
insert into localised_strings(id, strings_KEY, strings) values(329, 'pl', 'instr');
insert into localised_strings(id, strings_KEY, strings) values(330, 'pl', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(331, 'pl', '<x#> jest w relacji instrumentu do <y#>');
insert into localised_strings(id, strings_KEY, strings) values(328, 'en', 'instrument');
insert into localised_strings(id, strings_KEY, strings) values(329, 'en', 'instr');
insert into localised_strings(id, strings_KEY, strings) values(330, 'en', '(dziedziczone)');
insert into localised_strings(id, strings_KEY, strings) values(331, 'en', '<x#> jest w relacji instrumentu do <y#>');

'157', '0', '153', NULL, 'miejsce', '(dziedziczone)', '(dziedziczone)', '0', '<x#> jest w relacji miejsca do <y#>', 'loc', 'p_ravm', '5'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'158', '0', '153', NULL, 'czas', '(dziedziczone)', '(dziedziczone)', '0', '<x#> jest w relacji czasu do <y#>', 'time', 'p_ravczas', '6'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'160', '0', '153', NULL, 'rezultat', '(dziedziczone)', '(dziedziczone)', '0', '<x#> jest w relacji rezultatu do <y#>', 'result', 'p_ravres', '7'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'161', '0', '153', NULL, 'kauzacja', '(dziedziczone)', '(dziedziczone)', '0', '<x#> jest w relacji przyczyny do <y#>', 'caus', 'p_ravcau', '8'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'162', '0', NULL, NULL, 'predyspozycyjność', '', 'nieznana,czasownik,przymiotnik', '0', '', '', 'p_pred', '46'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'163', '0', '162', NULL, 'potencjalność', '(dziedziczone)', '(dziedziczone)', '0', '<x#> wchodzi w relację potencjalności z <y#>', 'pot', 'p_predpo', '2'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'164', '0', '162', NULL, 'habitualność', '(dziedziczone)', '(dziedziczone)', '0', '<x#> wchodzi w relację habitualności z <y#>', 'hab', 'p_predhab', '3'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'165', '0', '162', NULL, 'kwantytatywność', '(dziedziczone)', '(dziedziczone)', '0', '<x#> wchodzi w relację kwantytatywności z <y#>', 'kwant', 'p_predkwa', '4'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'166', '0', '162', NULL, 'ocena', '(dziedziczone)', '(dziedziczone)', '0', '<x#> wchodzi w relację oceny z <y#>', 'ocen', 'p_predoce', '5'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'168', '0', NULL, NULL, 'nacechowanie-intensywność Adj-Adj', '', 'przymiotnik', '0', '<x#> jest to intensivum od <y#>', 'intens', 'p_diminaa', '47'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'170', '0', NULL, NULL, 'Antonym', 'Opis', 'rzeczownik pwn', '0', '', 'skrot', '!', '61'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'182', '0', NULL, NULL, 'Derivationally related form', 'Opis', 'czasownik pwn,rzeczownik pwn', '0', '', 'skrot', '+', '74'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'191', '0', NULL, NULL, 'Also see', 'Opis', 'czasownik pwn', '0', '', 'See', '^', '84'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'194', '0', NULL, NULL, 'Participle of verb', 'Opis', 'czasownik pwn', '0', '', 'PP', '<', '87'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');

'195', '0', NULL, NULL, 'Pertainym (pertains to noun)', 'Opis', 'rzeczownik pwn', '0', '', 'Pert', '\\', '88'
#
insert into localised(id) values(113);
insert into localised(id) values(114);
insert into localised(id) values(115);
insert into localised(id) values(116);
insert into sense_relation_type (id, name_id, short_display_text_id, description_id, display_text_id, parent_relation_type_id, reverse_relation_type_id, auto_reverse)
                          values(13, 113, 114, 115, 116, NULL, 13, 1);
insert into localised_strings(id, strings_KEY, strings) values(113, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'pl', '');
insert into localised_strings(id, strings_KEY, strings) values(113, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(114, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(115, 'en', '');
insert into localised_strings(id, strings_KEY, strings) values(116, 'en', '');
