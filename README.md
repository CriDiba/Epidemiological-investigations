# Epidemiological-investigations
Project for Software Engineering course, University of Verona

## Description

Si vuole progettare un sistema software per la gestione delle informazioni socio-sanitarie di monitoraggio della popolazione italiana per la prevenzione di contagi e pandemie.

Il sistema memorizza la suddivisione in regioni, province e comuni del territorio italiano. Per ogni regione il sistema registra: il nome univoco, il comune capoluogo, la superficie in chilometri quadrati; per ogni provincia sono registrate le stesse informazioni delle regioni. Per ogni regione sono inoltre memorizzate le province che ne fanno parte. Per ogni comune il sistema registra: il codice ISTAT univoco, il nome, la data d’istituzione, la superficie in chilometri, il tipo di territorio (montano, collinare, pianeggiante) e l’indicazione del fatto che il comune si affacci sul mare. Per ogni comune si registra la provincia di appartenenza.

Il personale dell’ente incaricato del monitoraggio, dopo autenticazione, può inserire le suddette informazioni.

Il sistema supporta il monitoraggio di potenziali contagi ed epidemie e permette di memorizzare per ogni comune, una volta alla settimana, il numero di persone rispettivamente ricoverate in terapia intensiva e in cura presso il medico di base per

- episodi influenzali (con la segnalazione di eventuali) complicazioni respiratorie
- polmonite
- meningite
- epatiti
- morbillo
- tubercolosi
- gastroenteriti

I differenti dati epidemiologici sono inseriti da personale a contratto, dopo attenta elaborazione dei dati provenienti da ospedali e medici di base. Ogni persona assunta a contratto ha l’autorizzazione ad inserire i dati di un numero predefinito di comuni. Il personale dell’ente inserisce, per ogni persona a contratto, i comuni di cui è responsabile.

Infine, per ogni provincia si registrano annualmente: il numero di decessi causati da incidenti stradali, malattie tumorali, malattie cardiovascolari, e malattie contagiose (quelle considerate nel monitoraggio comune per comune). Tali dati sono registrati da apposito personale dell’ente. Il sistema fornisce i dati relativi alle varie cause di morte, aggregati anche per regione e complessivi a livello nazionale. Tali dati sono consultabili da tutti gli utenti autorizzati del sistema informatico.

Il sistema software è in grado di fornire i dati epidemiologici per ogni anno di rilevazione. Il personale dell’ente con il ruolo di ricercatore analista ha a disposizione la possibilità di analizzare anno per anno i dati acquisiti, avere rappresentazioni grafiche, aggregare e disaggregare i dati in modo da poter osservare le varie informazioni (storicizzate) a livello di comune, provincia, regione e nazione. In particolare deve essere possibile confrontare i dati sui decessi per malattie contagiose, rispetto ai casi segnalati per queste malattie, provincia per provincia. I ricercatori analisti non possono modificare alcun dato, ma hanno la possibilità di esportare in i dati memorizzati nel sistema in un formato leggibile da altri sw di analisi (excel, xml, txt, csv).
