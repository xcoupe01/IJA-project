Projekt IJA 2020 - simulace linek hromadné dopravy

Vypracovali:
 - Vojtěch Čoupek (xcoupe01) <xcoupe01@stud.fit.vutbr.cz> - Vedoucí
 - Tadeáš Jůza (xjuzat00) <xjuzat00@stud.fit.vutbr.cz>

** Ovládání a popis UI: ** 
Po spuštění aplikace se objeví úvodní obrazovka. Na pravé straně se nachází podklad s mapou a na levé straně se nachází menu. Na mapovém podkladu se dá pohybovat táhnutím myši a přibližovat či oddalovat pomocí kolečka myši. Mapový podklad obsahuje vykreslení aktuální načtené mapy a aktuální načtené hromadné dopravy (kocept mapy a hromadné dopravy je vysvětlen v zadání a nebude tu opakován). Na mapovém podkladu se dále dá klikat na zastávky (žluté čtverečky) a vozidla (kolečka v barvě linky). Po kliknutí se v panelu menu zobrazí itinerář vozidla nebo itinerář zastávky podle toho, na co bylo kliknuto. Černá kolečka na mapě předsavují definiční souřadnice segmentů ulice (po kliknutí na ně je do konzole vypsána jejich aktuální poloha). 
Nyní se přesuneme od mapy k popisu menu. V horní části menu jsou tři sekce. Po kliknutí na tlačítko nějaké sekce, menu se přepne na zvolenou volbu a zobrazí se volby korespondující s danou sekcí. 

Právě zvolená sekce "Main" slouží k nastavování simulace provozu v čase a spouštění běhu této simulace, prvky budou popsány od shora dolů:
 - text, který zobrazuje aktuální hodiny aplikace 
 - tlačítko "Run", které spouští běh simulace 
 - tlačítko "Stop" pozastavuje simulaci.
 - slider, který nastavuje rychlost animace, čím více vlevo, tím rychleji simualce poběží. 
 - tlačítko "Real time speed" nastaví animaci tak, aby čas aplikace běžel ve stejné rychlosti jako reálný čas.
 - čtveřice tlačítek pro ovládání skoku v čase. 
	- na prvním řádku se nastavuje čas, kam bude skok proveden
		- rozbalovací tlačítko pro nastavení hodin
		- rozbalovací tlačítko pro nastavení minut
	- na druhém řádku můžeme zvolit, kam má skok proběhnout 
		- tlačítko "To past" posune simulaci na zadaný čas do minulosti
		- tlačíko "To future" do budoucnosti. 
 - informační panel pro zobrazování itinerářů, který nemá interaktivní prvky.

Další sekce "Map" slouží k editaci mapy. Opět od shora se nejprve nachází možnost zvolení ulice pro další editaci. Pro zvolení existující ulice stačí rozkliknout a zvolit požadovanou ulici, pro založení nové je potřeba napsat její jméno. Zvolenou ulici lze editovat tlačítky: 
 - tlačítko "Add coordinate", po jehož zakliknutí a kliknutí do mapy se na místě kliku v mapě přidá nový definiční bod ulice
 - tlačítko "Remove last coord" odstraní poslední definiční bod ulice i s případnými zastávkami na trase (poslední dva body není možné odstranin, zanikla by tak ulice, pro tuto volbu použijte "Remove street")
 - tlačítko "Add stop", které po najetí na mapový podklad zobrazuje na zvolené ulici náhled přidávané zastávky a po kliknutí jí přidá se jménem zadaným pod tímto tlačítkem v textovém poli
 - tlačítko "Remove stop", po jehož zakliknutí a najetí do mapy se začne zobrazovat náhled, jaká zastávka má být odstraněna, po kliknutí do mapy je daná zastávka odstraněna
 - tlačítko "Remove street" odstraní z mapy právě zvolenou ulici
 - tlačítko "Toggle street visibility" přepíná zdali je ulice vykreslena či nikoliv (nemaže ji z mapy, pouze ji maže ze zobrazení / vkládá ji do zobrazení)
 - tlačítko "Toggle highlight" vykreslí zvýraznění ulice, koncový bod ulice je znázorněn kolečkem, znázornění je červené.
 - trojice prvků pro ovládání nastavení provozu
	- první řádek pro výběr a zvýraznění segmentu
		- rozbalovací tlačítko "Segment" je pro zvolení segmentu, kde budeme provoz nastavovat, je přítomná volba "Whole street", po jejímž zvolení se nastavuje provoz rovnoměrně na celé ulici.
		- tlačítko "Show segment highlight" vykreslí zvýraznění pouze pro zvolený segment
	- druhý řádek - slider nastavuje provoz, ten je znázorněn kategoriemi 1 - 5, při provozu 2 trvá vozidlu 2krát déle trasu projet než při provozu 1, při porovozu 3 to vozidlu trvá 3krát déle apod. 
 - tlačítko "Load map" vyvolá dialog pro zvolení souboru, ze kterého se načte nová mapa (stávající mapa je přemazána a ke smazána aktuální hromadná doprava, neboť na mapu nemusí sedět)
 - tlačítko "Save map" vyvolá dialog pro zvolení/vytvoření souboru, do kterého se aktuální mapa uloží (více o formátu ukládacích souborů níže)

Poslední sekce "Lines" slouží pro nastavení, editaci a ukládání hromadné dopravy na mapě.
 - tlačítko "Highlight public transport" zvýrazní celý systém hromadné dopravy, tedy všechny trasy s korespondující barvou tras
 - rozbalovací tlačítko výběru linek funguje na podobné bázi jako výběr ulic, nejde ale editovat, linky lze poze vybírat
 - tlačítko "Add line route point" po zakliknutí a přejetí na mapu se zobrazí náhled přidávaného bodu (označují se pouze body, které je možné přidat). Po kliknutí do mapy se vyznačený bod do linky přidá
 - tlačítko "Remove last route point" odstraní poslední bod trasy zvolené linky (poslední bod linky smazán není, zanikla by tak linka, pro tuto volbu použijte "Remove line")
 - dvojice tlačítek pro přidávání vozidel
 	- tlačítko "Add vehicle" po zakliknutí a najetí na mapu zobrazuje náhled, kam bude vozidlo přidáno, po kliknutí je vozidlo na tuto pozici přidáno, směr přidaného vozidla určuje následující tlačítko
	- tlačítko "Forward" určuje jakým směrem se na trase vydá nově přidané vozidlo. Má dvě pozice: "Forward" a "Backward", což učuje zdali vozidlo z pohledu linky pojede dopředu či dozadu
 - tlačítko "Remove vehicle" po zakliknutí a najetí na mapu zobrazuje náhled, které vozidlo bude odstraněno, po kliknutí je toto vozidlo odstraněno
 - tlačítko "Highlight route" zvýrazní právě vybranou trasu na mapě
 - tlačítko "Delete line" vymaže právě zvolenou linku
 - tlačítko "Add new line number [X]" přidá linku s číslem X s barvou zvolenou nížě
 - tlačítko pro výběr barvy volí barvu pro další přidanou linku
 - plocha spojů zobrazuje naplánované spoje linky po kliknutí na "Connection [Y]" se zobrazí podrobnosti spoje Y a zvolí se spoj Y.
 - trojice prvků pro přidání spoje 
	- rozbalovací tlačítko hodin volí v kolik hodin spoj vyrazí
	- rozbalovací tlačítko minut volí v kolik minut spoj vyrazí
	- tlačítko "Forward" volí, kterým směrem spoj pojede (podobně jako u přidávání vozidla), zárověň při směru dopředu startuje spoj na začátku linky a končí na konce, u směru dozadu naopak
 - tlačítko "Add connection" přidá spoj s právě nastavenými hodnotami z trojice prvků pro přidání spoje
 - tlačítko "Remove connection" odstraní zvolený spoj Y
 - tlačítko "Generate time table" vygeneruje jízdní řád pro linku, kde jsou brány vpotaz pouze spoje (ne samotná vozidla). Vygenerovaný jízndí řád je uložen ve složce data/generatedTimeTables/ a je typu png
 - tlačítko "Load public transport" vyvolá dialog pro zvolení souboru, ze kterého proběhne načtení hromadné dopravy (stávající hromadná doprava je přemazána)
 - tlačítko "Save public transport" vyvolá dialog ro zvolení/vytvoření souboru, do kterého se aktuální hromadná doprava uloží (více o formátu ukládacích souborů níže)

** Popis formátu ukládacích souborů: **
Program využívá dva typy ukládacích souborů, jeden pro mapu (doporučená koncovka ".map") a pro hromadnou dopravu (doporučená koncovka ".line"). Oba formáty jsou strukturovány do "ukládacích příkazů", které definují, co má aplikace načíst. Níže je popis těchto příkazů.
 - formát mapy "file.map":
	- příkaz "STREET" slouží k založení ulice
		- použití: STREET [název_ulice] [seznam bodů definujících ulici ve formátu "[X,Y]"], název ulice musí být jednoslovný (nesmí obsahovat mezery), ulici je třeba založit s alespoň dvěma body
		- příklad: STREET ulice1 [159,62] [159,242] [459,242] [459,142] [239,142]
	- příkaz "TRAFFIC" slouží pro určení provozu na dříve definované ulici, volání před definováním jakékoliv ulice zůsobí chybu
		- použití: TRAFFIC [úrovně 1 - 5 pro provoz podle počtu segmentů ulice]
		- příklad: TRAFFIC 1 2 3 4
	- příkaz "STOP" slouží pro přidání zastávky na dříve definovanou ulici, volání před definováním jakékoliv ulice způsobí chybu, název zastávky musí být jednoslovný (nesmí obsahovat mezery), je kontrolováno, zdali je zastávka opravdu na ulici
		- použití: STOP [jméno zastávky] [souřadnice ve tvaru "[X,Y]"]
		- příklad: STOP stop_u1_1 [159,142]
 - formát hromadné dopravy "file.line":
	- příkaz "LINE" slouží pro definování linky
		- použití: LINE [číslo linky] [barva linky] [definiční body linky ve formátu "[jméno ulice, bod ulice ]"] (mezi body patří jak definiční souřadnice, tak zastávky, jsou číslovány od nuly od začátku do konce ulice)
		- příklad: LINE 1 0x0000ffff [ulice1,1] [ulice1,2] [ulice1,3] [ulice1,4] [ulice4,5] [ulice3,4] [ulice3,3] [ulice3,2] [ulice3,1]
	- příkaz "TIME" slouží pro nastavení času aplikace, počítá se poslední výskyt
		- použití: TIME [hodiny]:[minuty]:[vteřiny] (hodiny minuty a vteřiny se zadávají nejmenším možným počtem číslic, přebytečné nuly se nepíší)
		- příklad: TIME 12:5:0
	- příkaz "VEHICLE" slouží pro vložení pendlujícího vozidla bez času odjezdu na poslední definovanou linku, volání před definováním jakékoliv linky způsobí chybu
		- použití: VEHICLE [bod na lince] [forward/backward] [počet kroků od bodu] (bodem na lince jsou míněny definiční body linky)
		- příklad: VEHICLE 5 backward 0
	- příkaz "SCHEDULE" slouží pro vložení časového spoje na poslední definovanou linku, volání před definováním jakékoliv linky způsobí chybu
		- použití: SCHEDULE [čas odjezdu ve stejném formátu jako u TIME] [forward/backward]
		- příklad: SCHEDULE 12:7:0 forward

** Doporučení **
doporučujeme spouštět aplikaci z terminálu a terminál míti otevřený vedle aplikace, při neúspěšném načtení případně nějakých dalších chybách aplikace informuje do terminálu, není to však ale nutné


