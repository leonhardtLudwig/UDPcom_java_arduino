# Leonardo Luigi Pepe
###### 4IB ITIS C. Zuccante AS 2018/2019

# Comunicazione tra client e server mediante protocollo UDP

- La seguente documentazione descrive l'invio delle richieste da parte di un client java/arduino e la gestione delle richieste da parte di un server java

- Ogni client simula una stazione meteo che ottiene informazioni e le salva su di un server.

- Arduino monta uno shield Ethernet e dentro il codice del client si configura indirizzo mac e ipv4 della scheda d'interfaccia di rete

- Il client in java ha anche la possibilità di richiedere al server i dati fino a quel momento salvati

- I client prima di inviare informazioni o effettuare richieste devono configurarsi tramite data, fornita dal server. I client non partiranno fintanto che non si otterrà una configurazione opportuna

- Il server rimane in ascolto e fornisce 2 tipi di servizio: `GETDATE`fornisce al client la data corrente e `GETFILE` fornisce al client il file dove sono salvate tutte le informazioni ottenute dal client (richieste escluse)

- Sono stati utilizzati gli IDE `IntelliJIdea Ultimate` per la scrittura del server e client in java (scelta esclusivamente abitudinaria, ogni ide valeva l'altro), `Arduino IDE` per la scrittura del client arduino

## Vantaggo protocollo UDP
- Essendo un protocollo "connectionless" (quindi non necessita che ci sia una connessione stabilita tra un host e l'altro) è molto più veloce perché non prevede l'invio di messaggi ACK che verificano la ricezione del messaggio da parte del destinatario. Quando un messaggio viene inviato l'host non si preoccupa di sapere se è arrivato a destinazione.

## Svantaggio protocollo UDP
- Il fatto che non ci siano messaggi ACK tra un host e un altro è un arma a doppio taglio perché in caso un informazione non arrivi a destinazione, l'host di origine non potrà mai sapere se sia il caso di rinviare il messaggio o meno. Altro svantaggio è il riordinamento dei pacchetti, il protocollo non garantisce tale caratteristica fornita invece dal `TCP`

# Scelte e considerazioni personali
- Il formato delle informazioni inviate dai client verso il server è `Date: GG/MM/AAAA_hh:mm:ss Lux: LLL`
- Il server invia la data secondo il formato descritto dalle varie classi appartenenti a `java.util` che definiscono un insieme di regole e metodi per lavorare sulle date
- Il client arduino invece di restituire valori ottenuti tramite il fotoresistore restituisce un valore di luce compreso tra 0 e 999 (come il client in java)
- Il client java quando richiede il file, successivamente alla richiesta e alla ricezione avviene il salvataggio del file `.txt` in locale e viene anche stampato su terminale
- Il 3 codici nel complesso funzionano perfettamente senza nessun tipo di problema. Unica aggiunta che avrei preferito fare era l'implementazione di una GUI tramite `javaFX` sfruttando il `scene builder` implementato in `IntelliJIdea Ultimate`
