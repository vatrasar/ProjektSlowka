# ProjektSlowka

## Wstęp
Obecnie jest to mój największy projekt. Początkowo miała to być prosta aplikacja na zaliczenie przedmiotu na studiach.
Okazała się ona dla mnie na tyle pomocna w nauce że używałem ją przez następne pół roku, stale przy tym rozwijając.

## Wykorzystane technologie
* Java 1.8
* spring boot
* spring mvc
* spring security
* JPA
* Maven
* HTML
* CSS
* javaScript
* jQuery
* Thymeleaf



## Zastosowanie
Program służy do organizacji i przeprowadzania powtórzeń materiału którego chcemy się nauczyć (np. słówek). Pozwala
dodać powtórzenie oraz pytania (i odpowiedzi do nich) które mają się w nim znaleźć. Następnie
program ustala którego dnia ma zostać przeprowadzone powtórzenie. W czasie
przeprowadzania powtórzenia program wyświetla użytkownikowi pytanie i
pozwala wpisać odpowiedz. Następnie użytkownik decyduje czy jego
odpowiedz była poprawna czy nie. Jeśli któraś odpowiedz była niepoprawna
program utworzy nowe powtórzenie z błędami które będzie przewidziane na
następny dzień. Powtórzenie z pozostałymi pytaniami jest wtedy przeniesione
na kolejny poziom. Poziomy to:1, 3, 10, 30, 90, 180, 360 dni. Działa to tak że np. pytanie na które dobrze odpowiedzieliśmy po 
3 dniach następnym razem 
zobaczymy po 10 dniach.


Program pozwala dodatkowo na:
### Do pytań i odpowiedzi można dodawać multimedia: filmy, zdjęcia oraz dźwięki 

### Tworzenie pustych powtórzeń
Czyli powtórzenia które nie ma żadnych pytań. Takie powtórzenia mogą pomóc w planowaniu powtórzeń do których nie potrzeba pytań 
(np. układów tanecznych)
### Tworzenie notatek
Notatka może znaleźć się w dowolnym powtórzeniu. Nie posiada ona pytania lecz samą odpowiedz.
### Edycje powtórzeń 
* zmianę wszystkie właściwości powtórzenia (takie jak nazwa, data notatek itp)
* dodawanie lub usuwanie pytań
* modyfikowanie treści pytań

### Wyszukiwanie pytań i powtórzeń
Można wyszukać pytania i powtórzenia. Za kryteria można brać nazwę powtórzenia, jego datę oraz tagi jakie są do niego przypisane. 

### Archiwizacje powtórzeń
 Po archiwizacji powtórzenie nie jest uwzględniane w harmonogramie a jednocześnie możemy je nadal wyszukać. 
 
 ### Sprawdzanie planu powtórzeń
 Plan pokazuje ile będziemy mieli powtórzeń w ciągu 7 najbliższych dni.

### Nauke 
Opcja nauki rożni się od powtarzania tym, że podczas powtarzania nasze odpowiedzi mają konsekwencje. Jeżeli w danym pytaniu naciśniemy 'Umiem' to 'poziom' tego pytania zostanie zwiększony. Natomiast jeżeli zaznaczymy opcje 'nie umiem' to poziom pytania zostanie zresetowany i będziemy musieli je znowu powtarzać już następnego dnia.

W wypadku nauki nie ma żadnych konsekwencji. Możemy  wielokrotnie przerabiać to samo powtórzenie tego samego dnia.
### Zaznaczenie problematycznych pytań
Przydaje się gdy uczyliśmy się 100 pytań, a 10 z nich sprawiło nam problemy. Dzięki zaznaczaniu problemów możemy później łatwo do nich wrócić i dokładniej przerobić.

### Operowanie na zbiorach powtórzeniach
* scalanie wielu powtórzeń w jedno
* ćwiczenie wielu powtórzeń razem (ale bez scalania)
* ćwiczenie problematycznych pytań ze wszystkich wyszukanych powtórzeń razem

## Uwaga
W projekcie występują zarówno polskie jak i angielskie nazwy zmiennych/plików. Wynika to z faktu że początkowo miał to być  mały projekt studencki. Z tego względu na początku używałem polskich nazw, teraz staram się używać wyłącznie angielskich. 
