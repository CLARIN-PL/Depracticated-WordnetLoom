CREATE TABLE application_labels (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  label_key VARCHAR(70)  NOT NULL,
  value     VARCHAR(255) NOT NULL,
  language  NCHAR(2)     NOT NULL
);

INSERT INTO application_labels (`label_key`, `value`, `language`) VALUES ('label.kpwr', 'Przykład KPWr', 'pl'),
  ('label.error.occured', 'Wystąpił bład', 'pl'),
  ('label.from', 'Od', 'pl'),
  ('label.to', 'Do', 'pl'),
  ('label.lexicon', 'Leksykon', 'pl'),
  ('label.unknown', 'nieznana', 'pl'),
  ('label.relations', 'Relacje', 'pl'),
  ('label.search', 'Szukaj', 'pl'),
  ('label.searching.synsets', 'Szukanie synsetów', 'pl'),
  ('label.searching.units', 'Szukanie jednostek', 'pl'),
  ('label.refresh.list', 'Odświeżanie listy', 'pl'),
  ('label.limit.to', ' Ogranicz do %s elementów', 'pl'),
  ('label.value.all', 'Wszystkie', 'pl'),
  ('label.searching', 'Wyszukiwanie', 'pl'),
  ('label.delete', 'Usuń', 'pl'),
  ('label.save', 'Zapisz', 'pl'),
  ('label.cancel', 'Anuluj', 'pl'),
  ('label.confirm', 'Zatwierdź', 'pl'),
  ('label.expand', 'Rozwiń', 'pl'),
  ('label.open.in.new.tab', 'Otwórz w nowej zakładce', 'pl'),
  ('label.path.to.hiperonim', 'Droga do hiperonimu', 'pl'),
  ('label.create.relation.with', 'Utwórz relację z ...', 'pl'),
  ('label.set.status.validated', 'Ustaw status: sprawdzone', 'pl'),
  ('label.set.status.error', 'Ustaw status\: błędne', 'pl'),
  ('label.set.status.done', 'Ustaw status: przetworzone', 'pl'),
  ('label.set.status.todo', 'Ustaw status: nieprzetworzone', 'pl'),
  ('label.not.or.partial.processed', 'Nie lub częściowo przetworzone', 'pl'),
  ('label.artificial', 'Sztuczny', 'pl'),
  ('label.artificial.synset', 'Synset sztuczny', 'pl'),
  ('label.value.unknown', 'braknych', 'pl'),
  ('label.unit.properties', 'Właściwości jednostki', 'pl'),
  ('label.moving.units', 'Przenoszenie jedlabel.error.occured', 'pl'),
  ('label.creating.connection', 'Tworzenie powiązania', 'pl'),
  ('label.synset.relations', 'Relacje synsetów', 'pl'),
  ('label.visualization', 'Wizualizacja', 'pl'),
  ('label.save.candidates.graphs', 'Zapisz grafy kandydatów', 'pl'),
  ('label.other', 'Inne', 'pl'),
  ('label.units', 'Jednostki', 'pl'),
  ('label.unit.relations', 'Relacje jednostki', 'pl'),
  ('label.candidates', 'Kandydaci', 'pl'),
  ('label.synsets', 'Synsety', 'pl'),
  ('label.synset', 'Synset', 'pl'),
  ('label.synset.options', 'Opcje synsetu', 'pl'),
  ('label.synset.mark', 'Zaznacz synset', 'pl'),
  ('label.synset.unmark', 'Odznacz synset', 'pl'),
  ('label.synset.create.relation.with', 'Utwórz relację synsetu z ...', 'pl'),
  ('label.synset.merge.with', 'Połącz synset z ...', 'pl'),
  ('label.lexical.unit.options', 'Opcje jednostek leksykalnych', 'pl'),
  ('label.unit.create.relation.with', 'Utwórz relację jednostki ...', 'pl'),
  ('label.unit.add.to.clipboard', 'Dodaj jednostkę ... do schowka', 'pl'),
  ('label.follow.edge', 'Idź za krawędzią', 'pl'),
  ('label.remove.relation', 'Usuń relację', 'pl'),
  ('label.add.to.clipboard', 'Dodaj do schowka', 'pl'),
  ('label.grupping', 'Grupuj', 'pl'),
  ('label.examples', 'Przykłady', 'pl'),
  ('label.properties', ' Właściwości', 'pl'),
  ('label.clipboard', 'Schowek', 'pl'),
  ('label.preview', 'Podgląd', 'pl'),
  ('label.view', 'Widok', 'pl'),
  ('label.wordnet.visualization', 'Wizualizacja wordnetu', 'pl'),
  ('label.relations.configuration', 'Konfiguracja wyświetlanych relacji', 'pl'),
  ('label.recount', 'Przelicz', 'pl'),
  ('label.number.of.elements', 'Liczba elementów: %s', 'pl'),
  ('label.graphs.calculating', 'Przeliczenie grafów', 'pl'),
  ('label.window', 'Okno', 'pl'),
  ('label.settings', 'Ustawienia', 'pl'),
  ('label.default.settings', 'Przywróć domyślne ustawienia', 'pl'),
  ('label.show.tooltips', 'Pokaż dymki podpowiedzi', 'pl'),
  ('label.perspective', 'Perspektywa', 'pl'),
  ('label.help', 'Pomoc', 'pl'),
  ('label.edit', 'Edycja', 'pl'),
  ('label.file', 'Plik', 'pl'),
  ('label.about.app', 'O programie', 'pl'),
  ('label.exit', 'Wyjście', 'pl'),
  ('label.import', 'Import ...', 'pl'),
  ('label.export', 'Eksport ...', 'pl'),
  ('label.paste', 'Wklej', 'pl'),
  ('label.cut', 'Wytnij', 'pl'),
  ('label.copy', 'Kopiuj', 'pl'),
  ('label.memory.usage', 'Ogólne zużycie pamięci\: %s MB', 'pl'),
  ('label.clear.cache', 'Wyczyśćche', 'pl'),
  ('label.general.stats', 'Statystyki ogólne', 'pl'),
  ('label.domain.stats', 'Statystyki dziedzin', 'pl'),
  ('label.remove.user.data', 'Usuńne użytkownika', 'pl'),
  ('label.ok', 'Ok', 'pl'),
  ('label.all.rights.reserved', 'Wszystkie prawa zastrzeżone', 'pl'),
  ('label.about.description', '<html>Program jest narzędziem służącym doycji WordNet-u<br/> -- sieci relacji leksykalnych i semantycznych między<br>słowami i znaczeniami.</html>', 'pl'),
  ('label.delete.selected', 'Usuń wybraną', 'pl'),
  ('label.relation.info', '<html>Relacja %s pomiędzy synsetami<br/>1\: %s <br/>2\: %s</html>', 'pl'),
  ('label.select', 'Wybierz', 'pl'),
  ('label.switch', 'Zamień', 'pl'),
  ('label.parts.of.speech', 'Części mowy', 'pl'),
  ('label.relation.params', 'Parametry relacji', 'pl'),
  ('label.new', 'Nowa', 'pl'),
  ('label.add', 'Dodaj', 'pl'),
  ('label.unit.params', 'Parametry jednostki', 'pl'),
  ('label.system.unit', 'systemowa', 'pl'),
  ('label.user.data', 'Dane użytkownika', 'pl'),
  ('label.lexical.units', 'Jednostki leksykalne', 'pl'),
  ('label.hide.units.assigned.to.synsets', 'Ukryj jednostki przypisane do synsetów', 'pl'),
  ('label.auto', '(automatyczna)', 'pl'),
  ('label.edit.relation.types', 'Edycja typów relacji', 'pl'),
  ('label.no.value', 'brak wartości', 'pl'),
  ('label.auto.add.reverse', 'Automatycznie dodawaj odwrotną', 'pl'),
  ('label.inherited', '(dziedziczone)', 'pl'),
  ('label.revers.realtion', 'Relacja odwrotna', 'pl'),
  ('label.without.reverse', 'Bez odwrotnej', 'pl'),
  ('label.undefined', 'niezdefiniowane', 'pl'),
  ('label.subtype', 'podtyp', 'pl'),
  ('label.edit.test', 'Edycja testu', 'pl'),
  ('label.cannonical.form', 'forma kanoniczna', 'pl'),
  ('label.in.subtypes', ' (w podtypach)', 'pl'),
  ('label.relation', 'relacja', 'pl'),
  ('label.lexical.relations', 'relacja leksykalna', 'pl'),
  ('label.between.synsets.relation', 'relacja pomiędzy synsetami', 'pl'),
  ('label.synonymy.relations', 'relacja synonimii', 'pl'),
  ('label.language', 'Język', 'pl'),
  ('label.application.restart', 'Restart aplikacji', 'pl'),
  ('label.error', 'Wystąpił błąd', 'pl'),
  ('label.choose.lexicon.to.work', 'Leksykony z którymi pracujesz', 'pl'),
  ('label.loading', 'Wczytywanie ...', 'pl'),
  ('label.nd', 'n/d', 'pl'),
  ('label.normal', 'Normalny', 'pl'),
  ('label.synset.type', 'Rodzaj synsetu', 'pl'),
  ('label.download', 'Pobierz', 'pl'),
  ('label.download.lexicon', 'Pobierz leksykon', 'pl'),
  ('label.exports', 'Exportuj', 'pl'),
  ('label.active.task', 'Aktywne zadanie', 'pl'),
  ('label.export.to.uby', 'Export do UBY-LMF', 'pl'),
  ('label.import.from.uby', 'Import z UBY-LMF', 'pl'),
  ('label.import.file', 'Plik do importu', 'pl'),
  ('label.export.lexicon', 'Exportuj leksykon', 'pl'),
  ('label.related.lexicon.to.export', 'Wybierz powiązane leksykony do eksportu', 'pl'),
  ('label.verb', 'czasownik', 'pl'),
  ('label.adverb', 'przysłowek', 'pl'),
  ('label.adjective', 'przymiotnik', 'pl'),
  ('label.noun', 'rzeczownik', 'pl'),
  ('label.multilingual', 'Międzyjęzykowa', 'pl'),
  ('label.clear', 'Wyczyść', 'pl'),
  ('label.uby', 'Uby-lmf', 'pl'),
  ('label.not.chosen', 'Nie wybrana', 'pl'),
  ('label.edit.example', 'Edytuj przykład', 'pl'),
  ('label.new.example', 'Nowy przykład', 'pl'),
  ('label.change', 'Zmień', 'pl'),
  ('label.emotions', 'Emocje', 'pl'),
  ('label.multiligual.only.with.princenton', 'Relacje  międzyjęzykowe tylko z Princeton ID', 'pl'),
  ('label.other.qualifier', 'Inny kwalifikator:', 'pl'),
  ('label.import.log.colon', 'Import log:', 'pl'),
  ('label.filename.colon', 'Nazwa pliku:', 'pl'),
  ('label.export.log.colon', 'Export log:', 'pl'),
  ('label.value.count', 'Liczba: %s', 'pl'),
  ('label.desc.colon', 'Opis:', 'pl'),
  ('label.relation.types.colon', 'Typy relacji:', 'pl'),
  ('label.test.content.colon', 'Treść testu\:', 'pl'),
  ('label.shortcut.colon', 'Skrót:', 'pl'),
  ('label.display.form.colon', 'Forma wyświetlana\:', 'pl'),
  ('label.subtype.relation.name.colon', 'Nazwa podtypu relacji:', 'pl'),
  ('label.relation.domain.colon', 'Dziedzina relacji:', 'pl'),
  ('label.relation.name.colon', 'Nazwa relacji:', 'pl'),
  ('label.new.realtion.name.colon', 'Nazwa nowej relacji:', 'pl'),
  ('label.lastname.colon', 'Nazwisko:', 'pl'),
  ('label.firstname.colon', 'Imię\:', 'pl'),
  ('label.tests.colon', 'Testy:', 'pl'),
  ('label.intermediate.unit.colon', 'Jednostka pośrednia\:', 'pl'),
  ('label.relation.desc.colon', 'Opis relacji:', 'pl'),
  ('label.relation.type.colon', 'Typ relacji:', 'pl'),
  ('label.relation.subtype.colon', 'Pod typ relacji:', 'pl'),
  ('label.user.colon', 'Użytkownik\:', 'pl'),
  ('label.created.colon', 'Wykonanie:', 'pl'),
  ('label.version.colon', 'Wersja:', 'pl'),
  ('label.global.progress', 'Postępłkowity\:', 'pl'),
  ('label.lemma.colon', 'Lemat:', 'pl'),
  ('label.number.colon', 'Numer:', 'pl'),
  ('label.comment.colon', 'Komentarz:', 'pl'),
  ('label.tag.count.colon', 'Wystąpienia\:', 'pl'),
  ('label.search.colon', 'Wyszukaj:', 'pl'),
  ('label.synsets.colon', 'Synsety:', 'pl'),
  ('label.parts.of.speech.colon', 'Części mowy\:', 'pl'),
  ('label.domain.colon', 'Dziedzina:', 'pl'),
  ('label.relations.colon', 'Relacje:', 'pl'),
  ('label.relation.colon', 'Relacja:', 'pl'),
  ('label.correct.colon', 'Poprawny:', 'pl'),
  ('label.source.unit.colon', 'Jednostka źródłowy\:', 'pl'),
  ('label.target.unit.colon', 'Jednostka docelowy:', 'pl'),
  ('label.source.synset.colon', 'Synset źródłowy\:', 'pl'),
  ('label.target.synset.colon', 'Synset docelowy:', 'pl'),
  ('label.units.colon', 'Jednostki leksykalne:', 'pl'),
  ('label.definition.colon', 'Definicja:', 'pl'),
  ('label.lexical.units.in.synset.colon', 'Jednostki w synsecie:', 'pl'),
  ('label.candidates.colon', 'Kandydaci:', 'pl'),
  ('label.save.visualisation.to.file.colon', 'Zapisz graf do pliku:', 'pl'),
  ('label.package.number.colon', 'Numer paczki:', 'pl'),
  ('label.artificial.colon', 'Sztuczny:', 'pl'),
  ('label.status.colon', 'Status:', 'pl'),
  ('label.owner.colon', 'Właściciel\:', 'pl'),
  ('label.synset.comment.colon', 'Komentarz synsetu:', 'pl'),
  ('label.unit.comment.colon', 'Komentarz jednostki:', 'pl'),
  ('label.lexicon.colon', 'Leksykon:', 'pl'),
  ('label.register.colon', 'Rejestr:', 'pl'),
  ('label.link.colon', 'Link:', 'pl'),
  ('label.use.case.colon', 'Przykład użycia\:', 'pl'),
  ('label.yes', 'Tak', 'pl'),
  ('label.no', 'Nie', 'pl'),
  ('label.color.colon', 'Kolor:', 'pl'),
  ('label.direction.colon', 'Kierunek:', 'pl'),
  ('hint.remove.relation.between.units', 'Usuniecie relacji pomiędzy jednostkami (Delete)', 'pl'),
  ('hint.add.relation.between.units', 'Dodanie relacji pomiędzy jednostkami (Insert)', 'pl'),
  ('hint.remove.unit', 'Usunięcie jednostki (Delete)', 'pl'),
  ('hint.create.new.unit', 'Utworzenie nowej jednostki', 'pl'),
  ('hint.create.new.unit.and.synset', 'Utworzenie nowej jednostki i synsetu', 'pl'),
  ('hint.add.to.new.synset', 'Dodanie do nowego synsetu', 'pl'),
  ('hint.save.changes.in.lexical.units', 'Zapisanie zmian w jednostce leksykalnej', 'pl'),
  ('hint.save.changes.in.synset', 'Zapisanie zmian w synsecie', 'pl'),
  ('hint.switch.to.unit.perspective', 'Przejście do perspektywy jednostek (Ctrl+L)', 'pl'),
  ('hint.add.relation', 'Dodanie relacji (Ctrl+R)', 'pl'),
  ('hint.detach.unit.from.synset', 'Odłączenie jednostki z synsetu (Delete)', 'pl'),
  ('hint.add.units', 'Dodanie jednostek (Insert)', 'pl'),
  ('hint.move.unit.up', 'Przesunięcie jednostki w górę (Ctrl+Góra)', 'pl'),
  ('hint.move.unit.down', 'Przesunięcie jednostki w dół (Ctrl+Dół)', 'pl'),
  ('hint.create.synset.and.move.selected.units', 'Utworzenie nowego synsetu, przeniesienie do niego wybranych jednostek i utworzenie relacji z aktualnie wybranym (źródłowym) synstem (Ctrl+Shift+N)', 'pl'),
  ('hint.shows.content.of.selected.package.in.list', 'Wyświetla zawartość wybranej paczki na liście', 'pl'),
  ('hint.recalculates.graphs.for.selected.package', 'Przelicza grafy dla aktualnie wybranej paczki', 'pl'),
  ('hint.adds.content.of.selected.package.to.list', 'Dodaje zawartość paczki do listy', 'pl'),
  ('hint.save.changes', 'Zapamiętanie zmian', 'pl'),
  ('hint.remove.selected.test', 'Usunięcie zaznaczonego testu', 'pl'),
  ('hint.edit.selected.test', 'Edycja zaznaczone testu', 'pl'),
  ('hint.create.new.test', 'Utworzenie nowego testu', 'pl'),
  ('hint.choose.revers.relation', 'Wybór relacja odwrotnej', 'pl'),
  ('hint.move.test.up', 'Przeniesienie testu do góry', 'pl'),
  ('hint.move.test.down', 'Przeniesienie testu w dół', 'pl'),
  ('hint.move.relation.up', 'Przeniesienie relacji do góry', 'pl'),
  ('hint.move.relation.down', 'Przeniesienie relacji w dół', 'pl'),
  ('hint.choose.pos.for.relation', 'Wybór części mowy dla relacji', 'pl'),
  ('hint.remove.selected.relation.and.subrelation', 'Usunięcie zaznaczonej relacji oraz podrelacji', 'pl'),
  ('hint.create.new.reltaion.subtype', 'Utworzenie nowego podtypu relacji', 'pl'),
  ('hint.create.relation.type', 'Utworzenie nowego typu relacji', 'pl'),
  ('hint.choose.color.relation ', ' Wybór koloru relacji', 'pl'),
  ('question.message.do.you.want.to.recalculate.graphs', 'Process przeliczenia grafów trwa kilkanaście minut.\nCzy chcesz teraz przeliczyć grafy?', 'pl'),
  ('question.message.remove.unit', 'Czy na pewno chcesz usunąć zaznaczoną jednostkę?', 'pl'),
  ('question.message.remove.units', 'Czy na pewno chcesz usunąć zaznaczone jednostki?', 'pl'),
  ('question.message.unit.has.relations', 'Jednostka \'%1$s\' ma zdefiniowane relacje? Czy na pewno chcesz usunąć jednostkę i relacje z nią związane?', 'pl'),
  ('question.message.set.status', 'Czy na pewno chcesz zmienić status zaznaczonych jednostek?', 'pl'),
  ('question.message.delete.synset', 'Zaznaczone wszytkie jednostki w synsecie, spowoduje to usunięciełego synsetu.\nDla synsetu zdefiniowane są relacje, zostaną one także usunięce.\nCzy na pewno chcesz usunąć synset?', 'pl'),
  ('question.message.detach.lexical.units.form.synset', 'Czy na pewno chcesz odłączyć zaznaczone jednostki z synsetu?', 'pl'),
  ('question.message.create.connection.from.reverse.relation', 'Czy utworzyć powiązanie dla relacji odwrotnej (%s)?', 'pl'),
  ('question.message.remove.user.data', 'Dane o użytkowniku zostaną usunięte a program zostanie wyłączony.\nNależy go ponownie uruchomić.\nCzy na pewno chcesz usunąćne o użytkowniku?\n', 'pl'),
  ('question.message.sure.to.remove.element ', 'Czy na pewno chcesz usunąć zaznaczony element?', 'pl'),
  ('question.message.reassign.relation.to.subtype', 'Ponieważ relacja jest już wykorzystywana wconvertUTF82Char: error1 BA!zienych, dlatego wszystkie powiązania zostaną przepisane do potypu "forma kanoniczna".\nCzy na pewno chesz kontynować?', 'pl'),
  ('question.message.reassign.test.to.subtype', 'Testy zdefinowane dla relacji zostaną przeniesione do nowo utworzonego podtypu "forma kanoniczna".\nCzy na pewno chesz kontynować?', 'pl'),
  ('question.message.sure.to.remove.test', 'Czy na pewno chcesz usunać zaznaczony test?', 'pl'),
  ('question.message.sure.to.remove.relation', '<html>Czy na pewno chcesz usunąć relację\:<br>%s?</html>', 'pl'),
  ('question.message.sure.to.remove.reverse.relation', 'Czy usunąć także relację odwrotną %s', 'pl'),
  ('error.message.reation.cant.have.subtypes', 'Relacja tego typu nie może mieć podtypów.', 'pl'),
  ('error.message.cannot.move.all.units.from.synset', 'Nie można przenieść WSZYSTKICH jednostek z synsetu.', 'pl'),
  ('error.message.cannot.change.status', 'Nie można było zmienić statusu dla jednostki %s,\ngdyż istnieją błędy w relacjach.', 'pl'),
  ('error.incorrect.domain ', 'Uwaga\! Jednostka leksykalna ma ustawioną dziędzinę,\nktóra NIE należy do dziedzin związanych z ustawioną częścią mowy\!\n\nDziedzina\: %s\nCzęść mowy\: %s', 'pl'),
  ('error.no.status.change.because.of.relations.in.unit', 'Status nie został zmieniony, ponieważ istnieją błędne relacje powiązane z tą jednostką.', 'pl'),
  ('error.no.status.change.because.of.relations.in.synset', 'Status nie został zmieniony, ponieważ istnieją błędne relacje powiązane z tym synsetem.', 'pl'),
  ('error.wrong.number.format', 'Niepoprawny format liczbowy', 'pl'),
  ('error.cannot.change.status.reserved.for.admin', 'Wybrany status jest zarezerowany dla korektora iconvertUTF82Char: error1 AD!ministratora', 'pl'),
  ('error.server.connection', 'Błąd połączenia z serwerem, spróbuj ponownie.', 'pl'),
  ('error.server.connection.auth', 'Błąd połączenia z serwerem lub niepoprawnene logowania, spróbuj ponownie.', 'pl'),
  ('error.message.invalid.char.in.lexicon', 'Błąd odczytu pliku konfiguracyjnego. W kluczu Lexicon występuje niedozwolony znak.', 'pl'),
  ('error.message.wrong.link', 'Przepraszamy, wystąpił bład podczas próby otwarcia linku w przegładarce.', 'pl'),
  ('error.select.domain', 'Dziedzina nie została wybrana', 'pl'),
  ('error.select.pos', 'Część mowy nie została wybrana', 'pl'),
  ('error.select.lexicon', 'Leksykon nie został wybrany', 'pl'),
  ('error.select.lemma', 'Pole lematu nie może być puste', 'pl'),
  ('success.message.relation.added', 'Dodano relację.', 'pl'),
  ('success.message.package.recalcualtion.complete', 'Paczka %d została przeliczona.', 'pl'),
  ('success.message.selected.relation.deleted', 'Wskazana relacja została usunięta', 'pl'),
  ('success.message.selected.relation.with.reversed.deleted', 'Wskazana relacja wraz z relacją odwrotną zostały usuniętkote', 'pl'),
  ('success.message.cache.cleaned', 'Pamiećche została wyczyszczona.', 'pl'),
  ('info.message.add.user.data', 'Przed pierwszym użyciem programu należy wprowadzićne użytkownika', 'pl'),
  ('info.message.unit.already.assigned.to.synset', 'Wybrana jednostka (%s) jest już przypisana do jednego z synsetów', 'pl'),
  ('info.message.restart.application', 'Zmian język wymaga restartu aplikacji. Czy zrestarowac teraz?', 'pl'),
  ('failure.message.unable.to.add.relation', 'Nie udało się dodać relacji.', 'pl'),
  ('failure.message.relation.exists', 'Taka relacja już istnieje.', 'pl'),
  ('failure.message.source.synset.same.as.target', 'Nie można utworzyć relacji, synset źródłowy i docelowy są jednakowe.', 'pl'),
  ('failure.message.source.unit.same.as.target', 'Nie można utworzyć relacji, jednostka źródłowa i docelowa są jednakowe.', 'pl'),
  ('failure.message.unit.exists', 'WconvertUTF82Char: error1 BA!zie istnieje już jednostka lub jednostki o podanym lemacie i dziedzinie.\n\nJednostki o takim samym lemacie i dziedzinie muszą różnić się komentarzem.', 'pl'),
  ('label.kpwr', 'KPWr Example', 'en'),
  ('label.from', 'From', 'en'),
  ('label.to', 'To', 'en'),
  ('label.lexicon', 'Lexicons', 'en'),
  ('label.unknown', 'unknown', 'en'),
  ('label.relations', 'Relations', 'en'),
  ('label.search', 'Search', 'en'),
  ('label.searching.synsets', 'Synsets searching', 'en'),
  ('label.searching.units', 'Units searching', 'en'),
  ('label.refresh.list', 'Refresh list', 'en'),
  ('label.value.count', 'Items: %s', 'en'),
  ('label.limit.to', ' Limit to %s elements', 'en'),
  ('label.value.all', 'All', 'en'),
  ('label.searching', 'Searching', 'en'),
  ('label.delete', 'Delete', 'en'),
  ('label.save', 'Save', 'en'),
  ('label.cancel', 'Cancel', 'en'),
  ('label.confirm', 'Confirm', 'en'),
  ('label.expand', 'Expand', 'en'),
  ('label.open.in.new.tab', 'Open in new Tab', 'en'),
  ('label.path.to.hiperonim', 'Path to hiperonim', 'en'),
  ('label.create.relation.with', 'Create relation with ...', 'en'),
  ('label.set.status.validated', 'Set status: checked', 'en'),
  ('label.set.status.error', 'Set status: mistake', 'en'),
  ('label.set.status.done', 'Set status: done', 'en'),
  ('label.set.status.todo', 'Set status: TODO', 'en'),
  ('label.not.or.partial.processed', 'Partial or not processed', 'en'),
  ('label.artificial', 'Artificial', 'en'),
  ('label.artificial.synset', 'Artificial synset', 'en'),
  ('label.value.unknown', 'n/d', 'en'),
  ('label.unit.properties', 'Unit properties', 'en'),
  ('label.moving.units', 'Moving units', 'en'),
  ('label.creating.connection', 'Creating relations', 'en'),
  ('label.synset.relations', 'Synset relations', 'en'),
  ('label.visualization', 'Visualization', 'en'),
  ('label.save.candidates.graphs', 'Save candidates visualisation', 'en'),
  ('label.other', 'Other', 'en'),
  ('label.units', 'Lexical units', 'en'),
  ('label.unit.relations', 'Lexical unit relations', 'en'),
  ('label.candidates', 'Candidates', 'en'),
  ('label.synsets', 'Synsets', 'en'),
  ('label.synset', 'Synset', 'en'),
  ('label.synset.options', 'Synset options', 'en'),
  ('label.synset.mark', 'Mark synset', 'en'),
  ('label.synset.unmark', 'UnMark synset', 'en'),
  ('label.synset.create.relation.with', 'Create synset relation with ...', 'en'),
  ('label.synset.merge.with', 'Merge synset with ...', 'en'),
  ('label.lexical.unit.options', 'Lexical units options', 'en'),
  ('label.unit.create.relation.with', 'Create unit relation with ...', 'en'),
  ('label.unit.add.to.clipboard', 'Add unit ... to clipboard', 'en'),
  ('label.follow.edge', 'Follow the edge', 'en'),
  ('label.remove.relation', 'Remove relation', 'en'),
  ('label.add.to.clipboard', 'Add to clipboard', 'en'),
  ('label.grupping', 'Group', 'en'),
  ('label.examples', 'Examples', 'en'),
  ('label.properties', ' Properties', 'en'),
  ('label.clipboard', 'Clipboard', 'en'),
  ('label.preview', 'Preview', 'en'),
  ('label.view', 'View', 'en'),
  ('label.wordnet.visualization', 'Wordnet Visualization', 'en'),
  ('label.relations.configuration', 'Relations configuration', 'en'),
  ('label.recount', 'Recalculate', 'en'),
  ('label.number.of.elements', 'Number of elements: %s', 'en'),
  ('label.graphs.calculating', 'Recalculating graphs', 'en'),
  ('label.window', 'Window', 'en'),
  ('label.settings', 'Settings', 'en'),
  ('label.default.settings', 'Restore default settings', 'en'),
  ('label.show.tooltips', 'Show tooltips', 'en'),
  ('label.perspective', 'Perspective', 'en'),
  ('label.help', 'Help', 'en'),
  ('label.edit', 'Edit', 'en'),
  ('label.file', 'File', 'en'),
  ('label.about.app', 'About', 'en'),
  ('label.exit', 'Exit', 'en'),
  ('label.import', 'Import ...', 'en'),
  ('label.export', 'Export ...', 'en'),
  ('label.paste', 'Paste', 'en'),
  ('label.cut', 'Cut', 'en'),
  ('label.copy', 'Copy', 'en'),
  ('label.memory.usage', 'General memory usage: %s MB', 'en'),
  ('label.clear.cache', 'Clear cache', 'en'),
  ('label.general.stats', 'General statistics', 'en'),
  ('label.domain.stats', 'Domain statistics', 'en'),
  ('label.remove.user.data', 'Remove user data', 'en'),
  ('label.ok', 'Ok', 'en'),
  ('label.all.rights.reserved', 'All rights reserved', 'en'),
  ('label.about.description', '<html>The program is a tool to create sematical relations.</html>', 'en'),
  ('label.delete.selected', 'Delete selected', 'en'),
  ('label.relation.info', '<html>Relation %s between synsets<br/>1: %s <br/>2: %s</html>', 'en'),
  ('label.select', 'Select', 'en'),
  ('label.switch', 'Switch', 'en'),
  ('label.relation.params', 'Relation parameters', 'en'),
  ('label.new', 'New', 'en'),
  ('label.parts.of.speech', 'Parts of speech', 'en'),
  ('label.add', 'Add', 'en'),
  ('label.unit.params', 'Unit parameters', 'en'),
  ('label.system.unit', 'system', 'en'),
  ('label.user.data', 'User data', 'en'),
  ('label.lexical.units', 'Lexical units', 'en'),
  ('label.hide.units.assigned.to.synsets', 'Hide units assigned to synsets', 'en'),
  ('label.language', 'Language', 'en'),
  ('label.auto', '(automatic)', 'en'),
  ('label.edit.relation.types', 'Edit relations types', 'en'),
  ('label.no.value', 'no value', 'en'),
  ('label.auto.add.reverse', 'Automatic add reverse relation', 'en'),
  ('label.inherited', '(inherited)', 'en'),
  ('label.revers.realtion', 'Reverse relation', 'en'),
  ('label.without.reverse', 'Without reverse relation', 'en'),
  ('label.undefined', 'undefined', 'en'),
  ('label.subtype', 'subtype', 'en'),
  ('label.edit.test', 'Edit test', 'en'),
  ('label.cannonical.form', 'canonical form', 'en'),
  ('label.in.subtypes', ' (in subtypes)', 'en'),
  ('label.relation', 'relation', 'en'),
  ('label.lexical.relations', 'lexical realtion', 'en'),
  ('label.between.synsets.relation', 'relation between synsets', 'en'),
  ('label.synonymy.relations', 'synonymy relation', 'en'),
  ('label.loading', 'Loading ...', 'en'),
  ('label.application.restart', 'Application restart', 'en'),
  ('label.error', 'Error occurred', 'en'),
  ('label.choose.lexicon.to.work', 'Lexicons to work with', 'en'),
  ('label.nd', 'n/d', 'en'),
  ('label.error.occured', 'A problem occured', 'en'),
  ('label.normal', 'Normal', 'en'),
  ('label.synset.type', 'Synset type', 'en'),
  ('label.download', 'Download', 'en'),
  ('label.download.lexicon', 'Download lexicon', 'en'),
  ('label.exports', 'Export', 'en'),
  ('label.export.lexicon', 'Export lexicon', 'en'),
  ('label.active.task', 'Active task', 'en'),
  ('label.export.to.uby', 'Export to UBY-LMF', 'en'),
  ('label.import.from.uby', 'Import from UBY-LMF', 'en'),
  ('label.import.file', 'File to import', 'en'),
  ('label.related.lexicon.to.export', 'Choose related lexicons to export', 'en'),
  ('label.verb', 'verb', 'en'),
  ('label.adverb', 'adverb', 'en'),
  ('label.adjective', 'adjective', 'en'),
  ('label.noun', 'noun', 'en'),
  ('label.multilingual', 'Multilugual', 'en'),
  ('label.clear', 'Clear', 'en'),
  ('label.uby', 'Uby-lmf', 'en'),
  ('label.not.chosen', 'Not selected', 'en'),
  ('label.emotions', 'Emotions', 'en'),
  ('label.yes', 'Yes', 'en'),
  ('label.no', 'No', 'en'),
  ('label.multiligual.only.with.princenton', 'Multiligual relations only with Princeton ID', 'en'),
  ('label.change', 'Change', 'en'),
  ('label.edit.example', 'Edit example', 'en'),
  ('label.new.example', 'New example', 'en'),
  ('label.other.qualifier', 'Other qualifier:', 'en'),
  ('label.import.log.colon', 'Import log:', 'en'),
  ('label.filename.colon', 'Filename:', 'en'),
  ('label.export.log.colon', 'Export log:', 'en'),
  ('label.desc.colon', 'Description:', 'en'),
  ('label.relation.types.colon', 'Relation type:', 'en'),
  ('label.test.content.colon', 'Test content:', 'en'),
  ('label.shortcut.colon', 'Abbreviation:', 'en'),
  ('label.display.form.colon', 'Displayed form:', 'en'),
  ('label.subtype.relation.name.colon', 'Relation subtype name:', 'en'),
  ('label.relation.domain.colon', 'Relation domain:', 'en'),
  ('label.relation.name.colon', 'Relation name:', 'en'),
  ('label.new.realtion.name.colon', 'New relation name:', 'en'),
  ('label.firstname.colon', 'Firstname:', 'en'),
  ('label.lastname.colon', 'Lastname:', 'en'),
  ('label.tests.colon', 'Tests:', 'en'),
  ('label.intermediate.unit.colon', 'Intermediate unit:', 'en'),
  ('label.relation.desc.colon', 'Relation description:', 'en'),
  ('label.relation.type.colon', 'Relation type:', 'en'),
  ('label.relation.subtype.colon', 'Relation subtype:', 'en'),
  ('label.user.colon', 'User:', 'en'),
  ('label.created.colon', 'Created by:', 'en'),
  ('label.version.colon', 'Version:', 'en'),
  ('label.global.progress', 'Total progress:', 'en'),
  ('label.lemma.colon', 'Lemma:', 'en'),
  ('label.number.colon', 'Number:', 'en'),
  ('label.comment.colon', 'Comment:', 'en'),
  ('label.tag.count.colon', 'Tag count:', 'en'),
  ('label.search.colon', 'Search:', 'en'),
  ('label.synsets.colon', 'Synsets:', 'en'),
  ('label.parts.of.speech.colon', 'Parts of speech:', 'en'),
  ('label.domain.colon', 'Domain:', 'en'),
  ('label.relations.colon', 'Relations:', 'en'),
  ('label.relation.colon', 'Relation:', 'en'),
  ('label.correct.colon', 'Valid:', 'en'),
  ('label.source.unit.colon', 'Source unit:', 'en'),
  ('label.target.unit.colon', 'Target unit:', 'en'),
  ('label.source.synset.colon', 'Source synset:', 'en'),
  ('label.target.synset.colon', 'Target synset:', 'en'),
  ('label.lexicon.colon', 'Lexicon:', 'en'),
  ('label.units.colon', 'Lexical units:', 'en'),
  ('label.definition.colon', 'Definition:', 'en'),
  ('label.lexical.units.in.synset.colon', 'Lexical units in synset:', 'en'),
  ('label.candidates.colon', 'Candidates:', 'en'),
  ('label.save.visualisation.to.file.colon', 'Save visualisation to file:', 'en'),
  ('label.package.number.colon', 'Number of packages:', 'en'),
  ('label.artificial.colon', 'Artificial:', 'en'),
  ('label.status.colon', 'Status:', 'en'),
  ('label.owner.colon', 'Owner:', 'en'),
  ('label.synset.comment.colon', 'Synset comment:', 'en'),
  ('label.unit.comment.colon', 'Unit comment:', 'en'),
  ('label.register.colon', 'Register:', 'en'),
  ('label.link.colon', 'Link:', 'en'),
  ('label.use.case.colon', 'Example:', 'en'),
  ('label.color.colon', 'Color:', 'en'),
  ('label.direction.colon', 'Direction:', 'en'),
  ('hint.remove.relation.between.units', 'Remove relation between lexical units (Delete)', 'en'),
  ('hint.add.relation.between.units', 'Add realtion between lexical units (Insert)', 'en'),
  ('hint.save.changes', 'Save changes', 'en'),
  ('hint.remove.selected.test', 'Remove selected test', 'en'),
  ('hint.edit.selected.test', 'Edit selected test', 'en'),
  ('hint.create.new.test', 'Create new test', 'en'),
  ('hint.choose.revers.relation', 'Choose reverse relation', 'en'),
  ('hint.move.test.up', 'Move test up', 'en'),
  ('hint.move.test.down', 'Move test down', 'en'),
  ('hint.move.relation.up', 'Move relation up', 'en'),
  ('hint.move.relation.down', 'Move relation down', 'en'),
  ('hint.choose.pos.for.relation', 'Choose part of speech for relation', 'en'),
  ('hint.remove.selected.relation.and.subrelation', 'Delete selected relation and subrelations', 'en'),
  ('hint.create.new.reltaion.subtype', 'Create new relation subtype', 'en'),
  ('hint.create.relation.type', 'Create new relation type', 'en'),
  ('hint.remove.unit', 'Remove unit (Delete)', 'en'),
  ('hint.create.new.unit', 'Create new unit (Insert)', 'en'),
  ('hint.save.changes.in.lexical.units', 'Save changes in lexical unit', 'en'),
  ('hint.save.changes.in.synset', 'Save changes in synset', 'en'),
  ('hint.add.to.new.synset', 'Add new synset', 'en'),
  ('hint.add.relation', 'Add relation (Ctrl+R)', 'en'),
  ('hint.add.units', 'Add units (Insert)', 'en'),
  ('hint.switch.to.unit.perspective', 'Go to lexical units perspective (Ctrl+L)', 'en'),
  ('hint.detach.unit.from.synset', 'Remove unit form selected synset (Delete)', 'en'),
  ('hint.move.unit.up', 'Move units up (Ctrl+up)', 'en'),
  ('hint.move.unit.down', 'Move units down (Ctrl+down)', 'en'),
  ('hint.create.synset.and.move.selected.units', 'Create new synset, move selected units to created synset and create relation with selected (source) synset (Ctrl+Shift+N)', 'en'),
  ('hint.create.new.unit.and.synset', 'Create new unit and synset', 'en'),
  ('hint.shows.content.of.selected.package.in.list', 'Replace list of words with given package', 'en'),
  ('hint.recalculates.graphs.for.selected.package', 'Recalculate selected package', 'en'),
  ('hint.adds.content.of.selected.package.to.list', 'Add words from selected package to the list', 'en'),
  ('hint.choose.color.relation', 'Choose relation color', 'en'),
  ('question.message.sure.to.remove.element', 'Are you sure to remove selected element', 'en'),
  ('question.message.reassign.relation.to.subtype', 'Relation already exists in database. All connections will be rewrite  to subtype \'canonical form\'.\n Do you want to continue?', 'en'),
  ('question.message.reassign.test.to.subtype', 'Tests defined for this relation will be rewrite to new sub type \'canonical form\'.\n Do you want to continue?', 'en'),
  ('question.message.sure.to.remove.test', 'Are you sure to remove selected test?', 'en'),
  ('error.message.reation.cant.have.subtypes', 'Relation of this kind can\'t have subtypes', 'en'),
  ('question.message.detach.lexical.units.form.synset', 'Do you really want to remove lexical unit form selected synset?', 'en'),
  ('question.message.do.you.want.to.recalculate.graphs', 'Graph recalculation can take several minutes. Would you like to proceed?\n\n Number of packages in the queue\: %d', 'en'),
  ('question.message.set.status', 'Do you want to change status of selected units?', 'en'),
  ('question.message.create.connection.from.reverse.relation', 'Do you want to create connection for the reverse relation (%s)?', 'en'),
  ('question.message.remove.unit', 'Do you want to remove selected unit?', 'en'),
  ('question.message.remove.units', 'Do you want to remove selected units?', 'en'),
  ('question.message.remove.user.data', 'Information about user will be removed and the program will be terminated.\nYou should run the program once again.\nDo you want to remove user informations?\n', 'en'),
  ('question.message.unit.has.relations', 'Unit\'%1$s\' has declared relations.Do you want remove connected units and relations?', 'en'),
  ('question.message.delete.synset', 'All units in synset selected, removal will cause deletion of synset and all relations. Are sure to remove synset?', 'en'),
  ('question.message.sure.to.remove.relation', '<html>Are you sure to remove relation:<br>%s?</html>', 'en'),
  ('question.message.sure.to.remove.reverse.relation', 'Do you want remove reverse relation: %s ?', 'en'),
  ('error.wrong.number.format', 'Incorrect number format', 'en'),
  ('error.message.cannot.move.all.units.from.synset', 'You cannot move ALL units from synset.', 'en'),
  ('error.message.cannot.change.status', 'You cannot change status of the synset %s,\nbecause of the invalid relations.', 'en'),
  ('error.incorrect.domain ', 'Attention\! Lexical unit has assigned domain,\n that doesn\'t belong to domain connected to the part of speech\!\n\nDomain\: %s\nPart of speech\: %s', 'en'),
  ('error.cannot.change.status.reserved.for.admin', 'The status you selected is reserved for coordinator and administrator', 'en'),
  ('error.no.status.change.because.of.relations.in.unit', 'You cannot change status of the unit %s,\n because invalid relations exist.', 'en'),
  ('error.no.status.change.because.of.relations.in.synset', 'You cannot change status of the synset %s,\n because invalid relations exist.', 'en'),
  ('error.server.connection', 'Unable connect to server, please try again later.', 'en'),
  ('error.server.connection.auth', 'Unable connect to server or incorrect authorization data, please try again.', 'en'),
  ('error.message.invalid.char.in.lexicon', 'Unable to read configuration file. Disallowed character in Lexicon key.', 'en'),
  ('error.message.wrong.link', 'Sorry, a problem occurred while trying to open this link in your system\'s standard browser.', 'en'),
  ('error.select.domain', 'Domain not selected', 'en'),
  ('error.select.pos', 'Part of speech not selected', 'en'),
  ('error.select.lexicon', 'Lexicon not not selected', 'en'),
  ('error.select.lemma', 'Lemma cannot by empty', 'en'),
  ('success.message.relation.added', 'Oh, joy! Relation added.', 'en'),
  ('success.message.selected.relation.deleted', 'Selected relation has been removed', 'en'),
  ('success.message.selected.relation.with.reversed.deleted', 'Selected relation with reversed relation has been removed', 'en'),
  ('success.message.package.recalcualtion.complete', 'Recalculation of package no %d is done.', 'en'),
  ('success.message.cache.cleaned', 'Cache was cleared.', 'en'),
  ('info.message.add.user.data', 'This is first time when you run this application. Please provide us with your\'s data', 'en'),
  ('info.message.unit.already.assigned.to.synset', 'Selected unit (%s) already assigned to one of synsets', 'en'),
  ('info.message.restart.application', 'To change language settings You have to restart application.', 'en'),
  ('failure.message.unable.to.add.relation', 'Could not add the relation', 'en'),
  ('failure.message.relation.exists', 'Relation already exists.', 'en'),
  ('failure.message.source.synset.same.as.target', 'Unable to create relation, source and target synset are same.', 'en'),
  ('failure.message.source.unit.same.as.target', 'Unable to create relation, source and target unit are same.', 'en'),
  ('failure.message.unit.exists',
   'Unit or units of given lemma and domain already exists. Units of same lemma and domain must have different comment.',
   'en');